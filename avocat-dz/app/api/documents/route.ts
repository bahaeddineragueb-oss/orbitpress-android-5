import { NextResponse } from 'next/server';
import { mockDocuments } from '@/lib/data';
export const dynamic = 'force-static';
export async function GET(req: Request) {
  const { searchParams } = new URL(req.url);
  const caseId = searchParams.get('caseId');
  try {
    const { prisma } = await import('@/lib/prisma');
    const where: any = {};
    if (caseId) where.caseId = caseId;
    const docs = await prisma.document.findMany({ where, orderBy: { date: 'desc' } });
    if (docs.length) return NextResponse.json(docs);
  } catch {}
  let f: any[] = mockDocuments as any[];
  if (caseId) f = f.filter(d => d.caseId === caseId);
  return NextResponse.json(f);
}
export async function POST(req: Request) {
  const body = await req.json();
  try {
    const { prisma } = await import('@/lib/prisma');
    const c = await prisma.document.create({ data: { caseId: body.caseId, title: body.title, category: body.category || 'أخرى', date: body.date ? new Date(body.date) : new Date(), fileName: body.fileName, uploadedBy: body.uploadedBy || 'أ. بهاء الدين' }});
    return NextResponse.json(c, { status: 201 });
  } catch { return NextResponse.json({ id: 'tmp_' + Date.now(), ...body }, { status: 201 }); }
}
