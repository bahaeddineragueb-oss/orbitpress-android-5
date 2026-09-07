import { NextResponse } from 'next/server';
import data from '@/lib/courts-data.json';
import { DB_INFO } from '@/lib/courts';

export const dynamic = 'force-static';
export const revalidate = false;

export async function GET(request: Request) {
  const { searchParams } = new URL(request.url);
  const q = searchParams.get('q')?.toLowerCase() || '';
  const code = searchParams.get('code');

  if (code) {
    const w = (data as any[]).find((x: any) => x.code === code);
    if (!w) return NextResponse.json({ error: 'Not found' }, { status: 404 });
    return NextResponse.json(w);
  }

  let filtered: any[] = data as any[];
  if (q) {
    filtered = filtered.filter((w: any) =>
      w.wilaya.includes(q) ||
      w.council.includes(q) ||
      w.tribunals.some((t: any) => t.name.includes(q)) ||
      (w.adminCourt && w.adminCourt.includes(q))
    );
  }

  return NextResponse.json({
    info: DB_INFO,
    count: filtered.length,
    data: filtered,
  });
}
