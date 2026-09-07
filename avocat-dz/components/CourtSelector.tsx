"use client";
import { useMemo, useState, useEffect } from "react";
import { courtsData } from "@/lib/courts";

type Props = {
  value?: { wilayaCode?: string; council?: string; tribunal?: string; section?: string };
  onChange: (v: { wilayaCode: string; wilaya: string; council: string; tribunal: string; section: string; adminCourt?: string }) => void;
  compact?: boolean;
};

// كل الأقسام في النظام القضائي الجزائري — مدني عقاري تجاري وكل الفروع
const SECTIONS = ["المدني","العقاري","التجاري","الجزائي","الجنح","المخالفات","الأحداث","شؤون الأسرة","الأسرة","الاجتماعي","الاستعجالي","البحري","الإداري"];

export function CourtSelector({ value, onChange, compact }: Props) {
  const [wilayaCode, setWilayaCode] = useState(value?.wilayaCode || "16");
  const wilaya = useMemo(() => courtsData.find(w => w.code === wilayaCode), [wilayaCode]);
  const [tribunal, setTribunal] = useState(value?.tribunal || wilaya?.tribunals[0]?.name || "");
  const [section, setSection] = useState(value?.section || "المدني");

  // Reset tribunal when wilaya changes
  useEffect(() => {
    if (wilaya) {
      const opts = wilayaCode === "00" ? wilaya.tribunals : [...wilaya.tribunals, ...(wilaya.adminCourt ? [{ name: wilaya.adminCourt, isBranch: false } as any] : [])];
      const first = opts[0]?.name || "";
      const belongs = opts.some((t: any) => t.name === tribunal);
      const next = belongs ? tribunal : first;
      setTribunal(next);
      onChange({
        wilayaCode: wilaya.code,
        wilaya: wilaya.wilaya,
        council: wilaya.council,
        tribunal: next,
        section,
        adminCourt: wilaya.adminCourt || undefined,
      });
    }
  }, [wilayaCode]);

  useEffect(() => {
    if (wilaya && tribunal && section) {
      onChange({
        wilayaCode: wilaya.code,
        wilaya: wilaya.wilaya,
        council: wilaya.council,
        tribunal,
        section,
        adminCourt: wilaya.adminCourt || undefined,
      });
    }
  }, [tribunal, section]);

  // For national bodies (00), show special label
  const isNational = wilayaCode === "00";

  if (!wilaya) return null;

  // Build tribunal options: normal tribunals + المحكمة الإدارية as selectable + Supreme/Council for national
  const tribunalOptions = isNational
    ? wilaya.tribunals
    : [...wilaya.tribunals, ...(wilaya.adminCourt ? [{ name: wilaya.adminCourt, isBranch: false, sections: SECTIONS as unknown as string[] }] : [])];

  return (
    <div className={`grid gap-3 ${compact ? "grid-cols-1" : "grid-cols-1 md:grid-cols-2"}`}>
      {/* Wilaya */}
      <div>
        <label className="text-xs font-bold text-slate-600 dark:text-slate-400">
          {isNational ? "الهيئة القضائية العليا" : "الولاية — المجلس القضائي"}
        </label>
        <select
          value={wilayaCode}
          onChange={e => setWilayaCode(e.target.value)}
          className="mt-1 w-full px-3 py-3 rounded-xl border bg-white dark:bg-[#070e1f] dark:border-[#1e2e50] text-sm font-bold"
        >
          {courtsData.sort((a,b)=>a.code.localeCompare(b.code)).map(w => (
            <option key={w.code} value={w.code}>
              {w.code === "00" ? "00 — الهيئات العليا — المحكمة العليا / مجلس الدولة" : `${w.code} — ${w.wilayaAr} — ${w.council} ${w.isNew ? " (جديدة)" : ""}`}
            </option>
          ))}
        </select>
        <div className="text-[11px] text-slate-500 mt-1">
          {isNational ? "المحكمة العليا ومجلس الدولة — الهيئتان القضائيتان العليتان في الجزائر" : `${wilaya.council} • ${wilaya.adminCourt || "بدون محكمة إدارية"}`}
        </div>
      </div>

      {/* Tribunal */}
      <div>
        <label className="text-xs font-bold text-slate-600 dark:text-slate-400">
          {isNational ? "الهيئة" : "المحكمة / الفرع / المحكمة الإدارية"}
        </label>
        <select
          value={tribunal}
          onChange={e => setTribunal(e.target.value)}
          className="mt-1 w-full px-3 py-3 rounded-xl border bg-white dark:bg-[#070e1f] dark:border-[#1e2e50] text-sm"
        >
          {tribunalOptions.map(t => {
            const isAdmin = !isNational && t.name === wilaya.adminCourt;
            const isSupreme = t.name === "المحكمة العليا";
            const isCouncilState = t.name === "مجلس الدولة";
            return (
              <option key={t.name} value={t.name}>
                {isSupreme ? "⚖️ المحكمة العليا" : isCouncilState ? "🏛️ مجلس الدولة" : isAdmin ? `⚖️ ${t.name} — إدارية` : `${t.name} ${t.isBranch ? "— فرع" : ""}`}
              </option>
            );
          })}
        </select>
        <div className="text-[11px] text-slate-500 mt-1">
          {isNational ? "هيئتان عليتان" : `${wilaya.tribunals.length} محاكم/فروع + ${wilaya.adminCourt ? "1 محكمة إدارية" : "0 إدارية"} = ${tribunalOptions.length} جهات`}
        </div>
      </div>

      {/* Section */}
      <div className={compact ? "" : "md:col-span-2"}>
        <label className="text-xs font-bold text-slate-600 dark:text-slate-400">القسم / الغرفة</label>
        <select
          value={section}
          onChange={e => setSection(e.target.value)}
          className="mt-1 w-full px-3 py-3 rounded-xl border bg-white dark:bg-[#070e1f] dark:border-[#1e2e50] text-sm"
        >
          {SECTIONS.map(s => <option key={s}>{s}</option>)}
        </select>
      </div>

      {/* Preview */}
      <div className={`p-3 rounded-xl bg-[#0e7490]/5 border border-[#0e7490]/20 text-xs leading-relaxed ${compact ? "" : "md:col-span-2"}`}>
        <b>الاختيار:</b> {isNational ? "الهيئات العليا" : wilaya.wilayaAr} → {wilaya.council} → {tribunal} → قسم {section}
        {!isNational && wilaya.adminCourt && tribunal !== wilaya.adminCourt && <span className="block text-[11px] text-slate-600 dark:text-slate-400">المحكمة الإدارية: {wilaya.adminCourt} (يمكن اختيارها مباشرة من القائمة أعلاه)</span>}
        {tribunal === "المحكمة العليا" && <span className="block text-[11px] text-amber-700 font-bold">⚖️ المحكمة العليا — أعلى هيئة قضائية (نقض الأحكام)</span>}
        {tribunal === "مجلس الدولة" && <span className="block text-[11px] text-violet-700 font-bold">🏛️ مجلس الدولة — القضاء الإداري الأعلى</span>}
        {tribunal === wilaya.adminCourt && <span className="block text-[11px] text-violet-700 font-bold">⚖️ محكمة إدارية — منازعات الإدارة</span>}
        <span className="block text-[11px] text-emerald-700 font-bold">✓ من قاعدة بيانات وزارة العدل (58 ولاية + الهيئات العليا) — {SECTIONS.length} قسم</span>
      </div>
    </div>
  );
}
