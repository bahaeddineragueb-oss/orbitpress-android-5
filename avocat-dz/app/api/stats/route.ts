import { NextResponse } from 'next/server';
export const dynamic = 'force-static';
export async function GET() {
  try {
    const { prisma } = await import('@/lib/prisma');
    const [clients, cases, hearings, fees, expenses, wilayas, tribunals] = await Promise.all([
      prisma.client.count(),
      prisma.courtCase.count(),
      prisma.hearing.count(),
      prisma.fee.aggregate({ _sum: { total: true, paid: true } }),
      prisma.expense.aggregate({ _sum: { amount: true } }),
      prisma.wilaya.count(),
      prisma.tribunal.count(),
    ]);
    return NextResponse.json({
      source: 'PostgreSQL (Prisma)',
      clients, cases, hearings,
      totalFees: fees._sum.total || 0,
      paidFees: fees._sum.paid || 0,
      expenses: expenses._sum.amount || 0,
      wilayas, tribunals,
    });
  } catch (e: any) {
    // Fallback to mock counts + courts json
    const { courtsData } = await import('@/lib/courts');
    const { mockClients, mockCases } = await import('@/lib/data');
    return NextResponse.json({
      source: 'Mock + JSON (fallback — PostgreSQL غير متصل)',
      clients: mockClients.length,
      cases: mockCases.length,
      hearings: 4,
      totalFees: 550000,
      paidFees: 210000,
      expenses: 8800,
      wilayas: courtsData.length,
      tribunals: courtsData.reduce((s,w)=>s+w.tribunals.length,0),
      error: e.message?.slice(0,120),
      hint: 'شغّل docker compose up -d ثم npx prisma db push',
    });
  }
}
