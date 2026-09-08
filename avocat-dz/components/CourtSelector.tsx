"use client";
import { useMemo, useState, useEffect } from "react";
import { courtsData, TRIBUNAL_CHAMBERS, COUNCIL_DIVISIONS } from "@/lib/courts";

type Props = {
  value?: { wilayaCode?: string; council?: string; tribunal?: string; section?: string };
  onChange: (v: { wilayaCode: string; wilaya: string; council: string; tribunal: string; section: string; adminCourt?: string }) => void;
  compact?: boolean;
};

// الغرف في المحكمة والأقسام في المجلس — حسب النظام الجزائري
const TRIBUNAL_SECTIONS = TRIBUNAL_CHAMBERS as unknown as string[];
const COUNCIL_SECTIONS = COUNCIL_DIVISIONS as unknown as string[];

export function CourtSelector({ value, onChange, compact }: Props) {
  const [wilayaCode, setWilayaCode] = useState(value?.wilayaCode || "16");
  const wilaya = useMemo(() => courtsData.find(w => w.code === wilayaCode), [wilayaCode]);
  const [tribunal, setTribunal] = useState(value?.tribunal || wilaya?.tribunals[0]?.name || "");
  const [section, setSection] = useState(value?.section || "المدني");

  const isNational = wilayaCode === "00";
  const isSpecialized = wilayaCode === "99";

  // Reset tribunal when wilaya changes
  useEffect(() => {
    if (wilaya) {
      const opts = (isNational || isSpecialized) ? wilaya.tribunals : [...wilaya.tribunals, ...(wilaya.adminCourt ? [{ name: wilaya.adminCourt, isBranch: false } as any] : [])];
      const first = opts[0]?.name || "";
      const belongs = opts.some((t: any) => t.name === tribunal);
      const next = belongs ? tribunal : first;
      setTribunal(next);
      // reset section to first available for specialized
      const tribObj = (wilaya.tribunals as any).find((t: any) => t.name === next);
      const isTrib = !isNational && !isSpecialized && wilaya.tribunals.some((t:any)=>t.name===next);
      const availableSections: string[] = (tribObj?.chambers || tribObj?.sections) || (isTrib ? TRIBUNAL_SECTIONS : COUNCIL_SECTIONS);
      const nextSection = availableSections.includes(section) ? section : availableSections[0];
      setSection(nextSection);
      onChange({
        wilayaCode: wilaya.code,
        wilaya: wilaya.wilaya,
        council: wilaya.council,
        tribunal: next,
        section: nextSection,
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

  if (!wilaya) return null;

  // Build tribunal options: normal tribunals + المحكمة الإدارية as selectable + Supreme/Council + Specialized
  const tribunalOptions = (isNational || isSpecialized)
    ? wilaya.tribunals
    : [...wilaya.tribunals, ...(wilaya.adminCourt ? [{ name: wilaya.adminCourt, isBranch: false, chambers: COUNCIL_SECTIONS }] : [])];

  // Sections per tribunal: غرف المحكمة vs أقسام المجلس
  const currentTrib = (wilaya.tribunals as any).find((t: any) => t.name === tribunal);
  const isCurrentTribunal = !isNational && !isSpecialized && wilaya.tribunals.some((t:any)=>t.name===tribunal);
  const sectionOptions: string[] = (currentTrib?.chambers || currentTrib?.sections) ? (currentTrib.chambers || currentTrib.sections) : (isCurrentTribunal ? [...TRIBUNAL_SECTIONS] : [...COUNCIL_SECTIONS]);

  return (
    <div className={`grid gap-3 ${compact ? "grid-cols-1" : "grid-cols-1 md:grid-cols-2"}`}>
      {/* Wilaya */}
      <div>
        <label className="text-xs font-bold text-slate-600 dark:text-slate-400">
          {isNational ? "الهيئة القضائية العليا" : isSpecialized ? "المحاكم التجارية المتخصصة" : "الولاية — المجلس القضائي"}
        </label>
        <select
          value={wilayaCode}
          onChange={e => setWilayaCode(e.target.value)}
          className="mt-1 w-full px-3 py-3 rounded-xl border bg-white dark:bg-[#070e1f] dark:border-[#1e2e50] text-sm font-bold"
        >
          {courtsData.sort((a,b)=>a.code.localeCompare(b.code)).map(w => (
            <option key={w.code} value={w.code}>
              {w.code === "00" ? "00 — الهيئات العليا — المحكمة العليا / مجلس الدولة" : w.code === "99" ? "99 — المحاكم التجارية المتخصصة (5 محاكم — مرسوم 22-148)" : `${w.code} — ${w.wilayaAr} — ${w.council} ${w.isNew ? " (جديدة)" : ""}`}
            </option>
          ))}
        </select>
        <div className="text-[11px] text-slate-500 mt-1">
          {isNational ? "المحكمة العليا ومجلس الدولة — الهيئتان القضائيتان العليتان في الجزائر" : isSpecialized ? "5 محاكم تجارية متخصصة — الجزائر / وهران / عنابة / قسنطينة / ورقلة (مرسوم تنفيذي 22-148)" : `${wilaya.council} • ${wilaya.adminCourt || "بدون محكمة إدارية"}`}
        </div>
      </div>

      {/* Tribunal */}
      <div>
        <label className="text-xs font-bold text-slate-600 dark:text-slate-400">
          {isNational ? "الهيئة" : isSpecialized ? "المحكمة التجارية المتخصصة" : "المحكمة / الفرع / المحكمة الإدارية"}
        </label>
        <select
          value={tribunal}
          onChange={e => setTribunal(e.target.value)}
          className="mt-1 w-full px-3 py-3 rounded-xl border bg-white dark:bg-[#070e1f] dark:border-[#1e2e50] text-sm"
        >
          {tribunalOptions.map(t => {
            const isAdmin = !isNational && !isSpecialized && t.name === wilaya.adminCourt;
            const isSupreme = t.name === "المحكمة العليا";
            const isCouncilState = t.name === "مجلس الدولة";
            const isComm = t.name.startsWith("المحكمة التجارية المتخصصة");
            return (
              <option key={t.name} value={t.name}>
                {isSupreme ? "⚖️ المحكمة العليا" : isCouncilState ? "🏛️ مجلس الدولة" : isComm ? `💼 ${t.name}` : isAdmin ? `⚖️ ${t.name} — إدارية` : `${t.name} ${t.isBranch ? "— فرع" : ""}`}
              </option>
            );
          })}
        </select>
        <div className="text-[11px] text-slate-500 mt-1">
          {isNational ? "هيئتان عليتان" : isSpecialized ? "5 محاكم تجارية متخصصة وطنية" : `${wilaya.tribunals.length} محاكم/فروع + ${wilaya.adminCourt ? "1 محكمة إدارية" : "0 إدارية"} = ${tribunalOptions.length} جهات`}
        </div>
      </div>

      {/* Section — غرفة في المحكمة / قسم في المجلس */}
      <div className={compact ? "" : "md:col-span-2"}>
        <label className="text-xs font-bold text-slate-600 dark:text-slate-400">{isCurrentTribunal ? "الغرفة — في المحكمة" : "القسم — في المجلس"}</label>
        <select
          value={section}
          onChange={e => setSection(e.target.value)}
          className="mt-1 w-full px-3 py-3 rounded-xl border bg-white dark:bg-[#070e1f] dark:border-[#1e2e50] text-sm"
        >
          {sectionOptions.map(s => <option key={s}>{s}</option>)}
        </select>
        {isSpecialized && <div className="text-[11px] text-amber-600 mt-1">💼 الأقسام المتاحة في المحاكم التجارية المتخصصة: {sectionOptions.join(" • ")}</div>}
      </div>

      {/* Preview */}
      <div className={`p-3 rounded-xl bg-[#0e7490]/5 border border-[#0e7490]/20 text-xs leading-relaxed ${compact ? "" : "md:col-span-2"}`}>
        <b>الاختيار:</b> {isNational ? "الهيئات العليا" : isSpecialized ? "المحاكم التجارية المتخصصة" : wilaya.wilayaAr} → {wilaya.council} → {tribunal} → {isCurrentTribunal ? "غرفة" : "قسم"} {section}
        {!isNational && !isSpecialized && wilaya.adminCourt && tribunal !== wilaya.adminCourt && <span className="block text-[11px] text-slate-600 dark:text-slate-400">المحكمة الإدارية: {wilaya.adminCourt} (يمكن اختيارها مباشرة من القائمة أعلاه)</span>}
        {tribunal === "المحكمة العليا" && <span className="block text-[11px] text-amber-700 font-bold">⚖️ المحكمة العليا — أعلى هيئة قضائية (نقض الأحكام)</span>}
        {tribunal === "مجلس الدولة" && <span className="block text-[11px] text-violet-700 font-bold">🏛️ مجلس الدولة — القضاء الإداري الأعلى</span>}
        {tribunal === wilaya.adminCourt && <span className="block text-[11px] text-violet-700 font-bold">⚖️ محكمة إدارية — منازعات الإدارة</span>}
        {isSpecialized && <span className="block text-[11px] text-emerald-700 font-bold">💼 محكمة تجارية متخصصة — منازعات تجارية وبحرية (مرسوم 22-148)</span>}
        <span className="block text-[11px] text-emerald-700 font-bold">✓ من قاعدة بيانات وزارة العدل (58 ولاية + الهيئات العليا + التجارية المتخصصة) — {sectionOptions.length} {isCurrentTribunal ? "غرف" : "أقسام"}</span>
      </div>
    </div>
  );
}
