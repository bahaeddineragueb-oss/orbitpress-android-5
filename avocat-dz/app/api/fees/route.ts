import { NextResponse } from 'next/server';
import { mockFees } from '@/lib/data';
export const dynamic = 'force-static';
export async function GET() {
  try {
    const { prisma } = await import('@/lib/prisma');
    const f = await prisma.fee.findMany({ include: { client: true, kase: true } });
    if (f.length) return NextResponse.json(f);
  } catch {}
  return NextResponse.json(mockFees);
}
export async function POST(req: Request) {
  const body = await req.json();
  try {
    const { prisma } = await import('@/lib/prisma');
    const c = await prisma.fee.create({ data: { caseId: body.caseId, clientId: body.clientId, total: body.total, paid: body.paid || 0, dueDate: body.dueDate ? new Date(body.dueDate) : undefined }});
    return NextResponse.json(c, { status: 201 });
  } catch { return NextResponse.json({ id: 'tmp_' + Date.now(), ...body }, { status: 201 }); }
}
