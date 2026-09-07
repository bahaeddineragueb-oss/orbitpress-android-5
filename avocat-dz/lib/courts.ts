// lib/courts.ts — قاعدة بيانات المحاكم الحقيقية (وزارة العدل)
// المصدر: data/courts.json (58 ولاية، 256 محكمة) + prisma/maktabi.db (SQLite)
// المحامي يختار فقط — لا كتابة يدوية

import data from './courts-data.json';

export type Tribunal = { name: string; isBranch: boolean; sections: string[] };
export type WilayaCourts = {
  code: string;
  wilaya: string;
  wilayaAr: string;
  council: string;
  tribunals: Tribunal[];
  adminCourt: string | null;
  isNew: boolean;
};

export const courtsData: WilayaCourts[] = data as any;

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
  source: "وزارة العدل الجزائرية — https://www.mjustice.gov.dz/ar/المحاكم-و-المجالس/",
  date: "2026-09-07",
  wilayas: 58,
  councils: 58,
  tribunals: 256,
  adminCourts: 58,
  fileJson: "data/courts.json (111KB)",
  fileDb: "prisma/maktabi.db (164KB SQLite)",
  isNewCount: 10,
};
