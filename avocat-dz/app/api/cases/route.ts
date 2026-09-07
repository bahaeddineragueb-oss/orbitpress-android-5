import { NextResponse } from 'next/server';
import { mockCases } from '@/lib/data';

export const dynamic = 'force-static';

export async function GET(request: Request) {
  const { searchParams } = new URL(request.url);
  const status = searchParams.get('status');
  const q = searchParams.get('q')?.toLowerCase() || '';

  // Try Prisma
  try {
    const { prisma } = await import('@/lib/prisma');
    const where: any = {};
    if (status && status !== 'الكل') where.status = status;
    const cases = await prisma.courtCase.findMany({
      where,
      include: { client: true },
      orderBy: { createdAt: 'desc' },
    });
    if (cases.length > 0) {
      // Filter by q if provided (PostgreSQL ilike would be better, but we filter in JS for simplicity)
      let filtered = cases;
      if (q) {
        filtered = cases.filter((c: any) =>
          [c.title, c.fileNumber, c.caseNumber, c.tribunalName, c.opponent].join(' ').toLowerCase().includes(q)
        );
      }
      return NextResponse.json(filtered);
    }
  } catch (e) {
    console.warn('Prisma cases failed, fallback', (e as Error).message?.slice(0,80));
  }

  // Fallback to mock
  let filtered = mockCases as any[];
  if (status && status !== 'الكل') filtered = filtered.filter(c => c.status === status);
  if (q) filtered = filtered.filter(c => [c.title, c.fileNumber, c.caseNumber, c.court, c.opponent].join(' ').toLowerCase().includes(q));
  return NextResponse.json(filtered);
}

export async function POST(request: Request) {
  try {
    const body = await request.json();
    if (!body.title || !body.fileNumber) return NextResponse.json({ error: 'العنوان ورقم الملف مطلوبان' }, { status: 400 });

    // Try Prisma
    try {
      const { prisma } = await import('@/lib/prisma');
      // Find a client (first mock client id may not exist in DB, so find or create)
      let clientId = body.clientId;
      if (!clientId) {
        const firstClient = await prisma.client.findFirst();
        clientId = firstClient?.id;
        if (!clientId) return NextResponse.json({ error: 'لا يوجد عملاء في DB — أنشئ عميلاً أولاً' }, { status: 400 });
      }
      const created = await prisma.courtCase.create({
        data: {
          fileNumber: body.fileNumber,
          caseNumber: body.caseNumber || body.fileNumber,
          clientId,
          title: body.title,
          wilayaCode: body.wilayaCode,
          councilName: body.council,
          tribunalName: body.court || body.tribunal,
          section: body.section,
          category: body.category || 'مدني',
          clientRole: body.clientRole || 'مدعي',
          opponent: body.opponent || 'غير محدد',
          opponentLawyer: body.opponentLawyer,
          status: body.status || 'جديد',
          assignedLawyer: body.assignedLawyer || 'أ. بهاء الدين',
          openDate: body.openDate ? new Date(body.openDate) : new Date(),
          notes: body.notes,
          strategy: body.strategy,
        }
      });
      return NextResponse.json(created, { status: 201 });
    } catch (e: any) {
      // Fallback: simulate
      if (e.code === 'P2002') return NextResponse.json({ error: 'رقم الملف موجود مسبقاً' }, { status: 409 });
      return NextResponse.json({ id: 'tmp_' + Date.now(), ...body, createdAt: new Date().toISOString() }, { status: 201 });
    }
  } catch {
    return NextResponse.json({ error: 'Invalid JSON' }, { status: 400 });
  }
}
