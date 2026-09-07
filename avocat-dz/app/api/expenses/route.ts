import { NextResponse } from 'next/server';
import { mockExpenses } from '@/lib/data';
export const dynamic = 'force-static';
export async function GET() {
  try {
    const { prisma } = await import('@/lib/prisma');
    const e = await prisma.expense.findMany({ orderBy: { date: 'desc' } });
    if (e.length) return NextResponse.json(e);
  } catch {}
  return NextResponse.json(mockExpenses);
}
export async function POST(req: Request) {
  const body = await req.json();
  try {
    const { prisma } = await import('@/lib/prisma');
    const c = await prisma.expense.create({ data: { caseId: body.caseId, title: body.title, amount: body.amount, date: new Date(body.date), category: body.category || 'أخرى' }});
    return NextResponse.json(c, { status: 201 });
  } catch { return NextResponse.json({ id: 'tmp_' + Date.now(), ...body }, { status: 201 }); }
}
