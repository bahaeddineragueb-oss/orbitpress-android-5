// lib/courts.ts — قاعدة بيانات المحاكم الحقيقية (وزارة العدل)
// المصدر: data/courts.json (58 ولاية، 256 محكمة) + الهيئات العليا + prisma/maktabi.db (SQLite)
// المحامي يختار فقط — لا كتابة يدوية + المحكمة العليا + مجلس الدولة + كل الأقسام
// التمييز: في المحكمة توجد غُرف (الغرفة المدنية...) وفي المجلس توجد أقسام (القسم المدني... + غرفة الاتهام)

import data from './courts-data.json';

export type Tribunal = { name: string; isBranch: boolean; chambers: string[]; sections?: string[] };
export type WilayaCourts = {
  code: string;
  wilaya: string;
  wilayaAr: string;
  council: string;
  councilDivisions?: string[];
  tribunals: Tribunal[];
  adminCourt: string | null;
  isNew: boolean;
};

// الغرف في المحكمة الابتدائية (Tribunal) — 8 غرف
export const TRIBUNAL_CHAMBERS = [
  "المدني",
  "العقاري",
  "التجاري",
  "الجزائي",
  "شؤون الأسرة",
  "الاجتماعي",
  "الاستعجالي",
  "البحري",
] as const;

// الأقسام/الغرف في المجلس القضائي (Conseil) — 13 قسم/غرفة + غرفة الاتهام
export const COUNCIL_DIVISIONS = [
  "المدني",
  "العقاري",
  "التجاري",
  "الجزائي",
  "الجنح",
  "المخالفات",
  "الأحداث",
  "شؤون الأسرة",
  "الاجتماعي",
  "الاستعجالي",
  "البحري",
  "الإداري",
  "غرفة الاتهام",
] as const;

// للتوافق — كل الأقسام
export const ALL_SECTIONS = [...COUNCIL_DIVISIONS] as const;

// الهيئات القضائية العليا — خارج الولايات (وطنية)
export const NATIONAL_COURTS: WilayaCourts = {
  code: "00",
  wilaya: "الهيئات العليا",
  wilayaAr: "الهيئات القضائية العليا",
  council: "الهيئات العليا",
  councilDivisions: [...COUNCIL_DIVISIONS],
  tribunals: [
    { name: "المحكمة العليا", isBranch: false, chambers: [...COUNCIL_DIVISIONS] },
    { name: "مجلس الدولة", isBranch: false, chambers: [...COUNCIL_DIVISIONS] },
  ],
  adminCourt: null,
  isNew: false,
};

// المحاكم التجارية المتخصصة — 5 محاكم وطنية (مرسوم 22-148 المؤرخ 28 مارس 2022)
// المصدر: وزارة العدل — المحاكم التجارية المتخصصة بالجزائر/وهران/عنابة/قسنطينة/ورقلة
export const SPECIALIZED_COMMERCIAL_COURTS: WilayaCourts = {
  code: "99",
  wilaya: "المحاكم التجارية المتخصصة",
  wilayaAr: "المحاكم التجارية المتخصصة",
  council: "المحاكم التجارية المتخصصة",
  councilDivisions: ["التجاري", "البحري", "المدني", "الاستعجالي"],
  tribunals: [
    { name: "المحكمة التجارية المتخصصة بالجزائر", isBranch: false, chambers: ["التجاري", "البحري", "المدني", "الاستعجالي"] },
    { name: "المحكمة التجارية المتخصصة بوهران", isBranch: false, chambers: ["التجاري", "البحري", "المدني", "الاستعجالي"] },
    { name: "المحكمة التجارية المتخصصة بعنابة", isBranch: false, chambers: ["التجاري", "البحري", "المدني", "الاستعجالي"] },
    { name: "المحكمة التجارية المتخصصة بقسنطينة", isBranch: false, chambers: ["التجاري", "البحري", "المدني", "الاستعجالي"] },
    { name: "المحكمة التجارية المتخصصة بورقلة", isBranch: false, chambers: ["التجاري", "البحري", "المدني", "الاستعجالي"] },
  ],
  adminCourt: null,
  isNew: false,
};

const rawData: WilayaCourts[] = data as any;
// دمج الولايات 58 + الهيئات العليا (المحكمة العليا + مجلس الدولة) + التجارية المتخصصة = 60 جهة
export const courtsData: WilayaCourts[] = [...rawData, NATIONAL_COURTS, SPECIALIZED_COMMERCIAL_COURTS];

// Helpers
export function getAllWilayas(): WilayaCourts[] {
  return courtsData.sort((a,b) => a.code.localeCompare(b.code));
}

export function getWilayaByCode(code: string): WilayaCourts | undefined {
  return courtsData.find(w => w.code === code);
}

export function getCouncilByWilaya(wilayaName: string): WilayaCourts | undefined {
  return courtsData.find(w => w.wilaya === wilayaName);
}

export function getTribunalsByWilaya(code: string): Tribunal[] {
  const w = getWilayaByCode(code);
  return w ? w.tribunals : [];
}

export function getAllTribunals(): { wilaya: string; code: string; council: string; tribunal: string; isBranch: boolean }[] {
  const out: any[] = [];
  for (const w of courtsData) {
    for (const t of w.tribunals) {
      out.push({ wilaya: w.wilaya, code: w.code, council: w.council, tribunal: t.name, isBranch: t.isBranch });
    }
  }
  return out;
}

export function searchCourts(query: string) {
  const q = query.toLowerCase().trim();
  if (!q) return courtsData;
  return courtsData.filter(w =>
    w.wilaya.includes(q) ||
    w.council.includes(q) ||
    w.tribunals.some(t => t.name.includes(q)) ||
    (w.adminCourt && w.adminCourt.includes(q))
  );
}

// For DB file info
export const DB_INFO = {
  source: "وزارة العدل الجزائرية — https://www.mjustice.gov.dz/ar/المحاكم-و-المجالس/ + الهيئات العليا + المحاكم التجارية المتخصصة (مرسوم 22-148)",
  date: "2026-09-07",
  wilayas: 58,
  councils: 58,
  tribunals: 256,
  adminCourts: 58,
  supremeCourt: 1, // المحكمة العليا
  councilOfState: 1, // مجلس الدولة
  nationalBodies: 2,
  specializedCommercial: 5, // المحاكم التجارية المتخصصة
  totalCourts: 58 + 256 + 58 + 2 + 5, // مجالس + محاكم + إدارية + عليا + تجارية متخصصة = 379
  fileJson: "data/courts.json (111KB)",
  fileDb: "prisma/maktabi.db (164KB SQLite)",
  isNewCount: 10,
  sections: ALL_SECTIONS.length, // 13 قسم
  sectionsList: ALL_SECTIONS.join(" • "),
  chambers: TRIBUNAL_CHAMBERS.length, // 8 غرف
  chambersList: TRIBUNAL_CHAMBERS.join(" • "),
  divisions: COUNCIL_DIVISIONS.length, // 13
  divisionsList: COUNCIL_DIVISIONS.join(" • "),
};
