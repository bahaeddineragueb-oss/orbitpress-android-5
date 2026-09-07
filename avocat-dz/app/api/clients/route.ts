import { NextResponse } from 'next/server';
import { mockClients } from '@/lib/data';

export const dynamic = 'force-static';

// GET /api/clients — قائمة العملاء من قاعدة حقيقية (PostgreSQL) مع fallback إلى mock
export async function GET() {
  // Try Prisma (PostgreSQL) if available
  try {
    // Dynamically import to avoid build failure if @prisma/client not generated
    const { prisma } = await import('@/lib/prisma');
    const clients = await prisma.client.findMany({ orderBy: { createdAt: 'desc' } });
    if (clients.length > 0) return NextResponse.json(clients);
  } catch (e) {
    // Fallback: will use mock below
    console.warn('Prisma clients failed, fallback to mock', (e as Error).message?.slice(0,100));
  }

  // Fallback: try SQLite file directly via JSON? For now return mock + courts data
  // In PC mode, Electron can read prisma/maktabi.db directly
  return NextResponse.json(mockClients);
}

export async function POST(request: Request) {
  try {
    const body = await request.json();
    // Validate
    if (!body.fullName || !body.phone) return NextResponse.json({ error: 'الاسم والهاتف مطلوبان' }, { status: 400 });

    try {
      const { prisma } = await import('@/lib/prisma');
      const created = await prisma.client.create({ data: {
        fullName: body.fullName,
        phone: body.phone,
        email: body.email,
        address: body.address,
        profession: body.profession,
        nationalId: body.nationalId,
        notes: body.notes,
      }});
      return NextResponse.json(created, { status: 201 });
    } catch (e) {
      // Fallback: simulate creation (PC offline will save to JSON/Electron)
      return NextResponse.json({ id: 'tmp_' + Date.now(), ...body, createdAt: new Date().toISOString() }, { status: 201 });
    }
  } catch (e) {
    return NextResponse.json({ error: 'Invalid JSON' }, { status: 400 });
  }
}
