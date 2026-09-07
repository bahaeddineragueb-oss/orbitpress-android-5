import { NextResponse } from 'next/server';
import { mockHearings } from '@/lib/data';

export const dynamic = 'force-static';

export async function GET(request: Request) {
  const { searchParams } = new URL(request.url);
  const caseId = searchParams.get('caseId');

  try {
    const { prisma } = await import('@/lib/prisma');
    const where: any = {};
    if (caseId) where.caseId = caseId;
    const hearings = await prisma.hearing.findMany({ where, orderBy: { date: 'asc' } });
    if (hearings.length > 0) return NextResponse.json(hearings);
  } catch (e) {
    console.warn('Prisma hearings fallback', (e as Error).message?.slice(0,60));
  }

  let filtered: any[] = mockHearings as any[];
  if (caseId) filtered = filtered.filter(h => h.caseId === caseId);
  return NextResponse.json(filtered);
}

export async function POST(request: Request) {
  try {
    const body = await request.json();
    if (!body.caseId || !body.date) return NextResponse.json({ error: 'القضية والتاريخ مطلوبان' }, { status: 400 });

    try {
      const { prisma } = await import('@/lib/prisma');
      const created = await prisma.hearing.create({
        data: {
          caseId: body.caseId,
          date: new Date(body.date),
          court: body.court,
          room: body.room,
          judge: body.judge,
          reason: body.reason,
          decision: body.decision,
          nextDate: body.nextDate ? new Date(body.nextDate) : undefined,
          status: body.status || 'قادمة',
          wilayaCode: body.wilayaCode,
          councilName: body.council,
          section: body.section,
        }
      });
      return NextResponse.json(created, { status: 201 });
    } catch (e) {
      return NextResponse.json({ id: 'tmp_' + Date.now(), ...body }, { status: 201 });
    }
  } catch {
    return NextResponse.json({ error: 'Invalid JSON' }, { status: 400 });
  }
}
