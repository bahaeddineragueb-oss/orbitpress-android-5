import { PrismaClient } from '@prisma/client';
import * as fs from 'fs';
import * as path from 'path';

const prisma = new PrismaClient();

async function main() {
  console.log('🌱 Seeding Maktabi DB — وزارة العدل (58 ولاية)');

  // 1) Sections
  const sections = ["المدني","الجزائي","الأسرة","التجاري","العقاري","الاجتماعي","الاستعجالي","البحري","شؤون الأسرة"];
  for (const name of sections) {
    await prisma.courtSection.upsert({ where: { name }, update: {}, create: { name } });
  }
  console.log(`✓ Sections: ${sections.length}`);

  // 2) Courts from JSON (وزارة العدل)
  const courtsPath = path.join(__dirname, '../data/courts.json');
  if (!fs.existsSync(courtsPath)) {
    // fallback to lib
    const alt = path.join(__dirname, '../lib/courts-data.json');
    if (fs.existsSync(alt)) fs.copyFileSync(alt, courtsPath);
  }
  const courtsData: any[] = JSON.parse(fs.readFileSync(courtsPath, 'utf-8'));
  console.log(`📖 Loaded ${courtsData.length} wilayas from data/courts.json`);

  for (const w of courtsData) {
    const wilaya = await prisma.wilaya.upsert({
      where: { code: w.code },
      update: { name: w.wilaya, nameAr: w.wilayaAr, isNew: w.isNew },
      create: { code: w.code, name: w.wilaya, nameAr: w.wilayaAr, isNew: w.isNew },
    });

    const council = await prisma.council.upsert({
      where: { wilayaCode: w.code },
      update: { name: w.council },
      create: { wilayaCode: w.code, name: w.council },
    });

    if (w.adminCourt) {
      await prisma.adminCourt.upsert({
        where: { wilayaCode: w.code },
        update: { name: w.adminCourt, councilId: council.id },
        create: { wilayaCode: w.code, councilId: council.id, name: w.adminCourt },
      });
    }

    for (const t of w.tribunals) {
      await prisma.tribunal.upsert({
        where: { councilId_name: { councilId: council.id, name: t.name } as any },
        update: { isBranch: t.isBranch, wilayaCode: w.code },
        create: { councilId: council.id, wilayaCode: w.code, name: t.name, isBranch: t.isBranch },
      });
    }
  }

  const wilayas = await prisma.wilaya.count();
  const councils = await prisma.council.count();
  const tribunals = await prisma.tribunal.count();
  const admins = await prisma.adminCourt.count();
  console.log(`✓ Wilayas: ${wilayas}, Councils: ${councils}, Tribunals: ${tribunals}, AdminCourts: ${admins}`);

  // 3) Demo clients/cases (اختياري)
  // We keep mock data in app/lib/data.ts for now, but can seed minimal
  const existingClients = await prisma.client.count();
  if (existingClients === 0) {
    const c1 = await prisma.client.create({ data: { fullName: "محمد راقب", phone: "0550 12 34 56", address: "الرويبة - الجزائر", profession: "تاجر" } });
    await prisma.courtCase.create({
      data: {
        fileNumber: "2026/184",
        caseNumber: "1234/2026",
        clientId: c1.id,
        title: "نزاع عقاري - قطعة أرض الرويبة",
        tribunalName: "محكمة الرويبة",
        councilName: "مجلس قضاء الجزائر",
        wilayaCode: "16",
        section: "العقاري",
        category: "عقاري",
        clientRole: "مدعي",
        opponent: "سعيد مراد",
        status: "قيد المتابعة",
        assignedLawyer: "أ. بهاء الدين",
        openDate: new Date("2026-01-15"),
      }
    });
    console.log("✓ Demo client/case seeded");
  }

  console.log("✅ Seed complete — قاعدة بيانات حقيقية جاهزة");
}

main()
  .catch((e) => { console.error(e); process.exit(1); })
  .finally(async () => await prisma.$disconnect());
