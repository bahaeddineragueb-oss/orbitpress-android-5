import { NextResponse } from 'next/server';
import { mockDeadlines } from '@/lib/data';
export const dynamic = 'force-static';
export async function GET() {
  try {
    const { prisma } = await import('@/lib/prisma');
    const d = await prisma.deadline.findMany({ orderBy: { dueDate: 'asc' } });
    if (d.length) return NextResponse.json(d);
  } catch {}
  return NextResponse.json(mockDeadlines);
}
export async function POST(req: Request) {
  const body = await req.json();
  try {
    const { prisma } = await import('@/lib/prisma');
    const c = await prisma.deadline.create({ data: { caseId: body.caseId, title: body.title, dueDate: new Date(body.dueDate), type: body.type || 'أخرى', priority: body.priority || 'عادي', done: false }});
    return NextResponse.json(c, { status: 201 });
  } catch { return NextResponse.json({ id: 'tmp_' + Date.now(), ...body }, { status: 201 }); }
}
