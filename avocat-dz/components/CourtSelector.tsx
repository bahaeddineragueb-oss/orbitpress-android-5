"use client";
import { useMemo, useState, useEffect } from "react";
import { courtsData } from "@/lib/courts";

type Props = {
  value?: { wilayaCode?: string; council?: string; tribunal?: string; section?: string };
  onChange: (v: { wilayaCode: string; wilaya: string; council: string; tribunal: string; section: string; adminCourt?: string }) => void;
  compact?: boolean;
};

const SECTIONS = ["المدني","الجزائي","الأسرة","التجاري","العقاري","الاجتماعي","الاستعجالي","البحري","شؤون الأسرة","الإداري"];

export function CourtSelector({ value, onChange, compact }: Props) {
  const [wilayaCode, setWilayaCode] = useState(value?.wilayaCode || "16");
  const wilaya = useMemo(() => courtsData.find(w => w.code === wilayaCode), [wilayaCode]);
  const [tribunal, setTribunal] = useState(value?.tribunal || wilaya?.tribunals[0]?.name || "");
  const [section, setSection] = useState(value?.section || "المدني");

  // Reset tribunal when wilaya changes
  useEffect(() => {
    if (wilaya) {
      const first = wilaya.tribunals[0]?.name || "";
      // keep existing if it belongs to new wilaya, otherwise reset
      const belongs = wilaya.tribunals.some(t => t.name === tribunal);
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

  if (!wilaya) return null;

  return (
    <div className={`grid gap-3 ${compact ? "grid-cols-1" : "grid-cols-1 md:grid-cols-2"}`}>
      {/* Wilaya */}
      <div>
        <label className="text-xs font-bold text-slate-600 dark:text-slate-400">الولاية — المجلس القضائي</label>
        <select
          value={wilayaCode}
          onChange={e => setWilayaCode(e.target.value)}
          className="mt-1 w-full px-3 py-3 rounded-xl border bg-white dark:bg-[#070e1f] dark:border-[#1e2e50] text-sm font-bold"
        >
          {courtsData.sort((a,b)=>a.code.localeCompare(b.code)).map(w => (
            <option key={w.code} value={w.code}>
              {w.code} — {w.wilayaAr} — {w.council} {w.isNew ? " (جديدة)" : ""}
            </option>
          ))}
        </select>
        <div className="text-[11px] text-slate-500 mt-1">{wilaya.council} • {wilaya.adminCourt || "بدون محكمة إدارية"}</div>
      </div>

      {/* Tribunal */}
      <div>
        <label className="text-xs font-bold text-slate-600 dark:text-slate-400">المحكمة / الفرع</label>
        <select
          value={tribunal}
          onChange={e => setTribunal(e.target.value)}
          className="mt-1 w-full px-3 py-3 rounded-xl border bg-white dark:bg-[#070e1f] dark:border-[#1e2e50] text-sm"
        >
          {wilaya.tribunals.map(t => (
            <option key={t.name} value={t.name}>
              {t.name} {t.isBranch ? "— فرع" : ""}
            </option>
          ))}
        </select>
        <div className="text-[11px] text-slate-500 mt-1">{wilaya.tribunals.length} محاكم/فروع في هذه الولاية</div>
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
        <b>الاختيار:</b> {wilaya.wilayaAr} → {wilaya.council} → {tribunal} → قسم {section}
        {wilaya.adminCourt && <span className="block text-[11px] text-slate-600 dark:text-slate-400">المحكمة الإدارية: {wilaya.adminCourt}</span>}
        <span className="text-[11px] text-emerald-700 font-bold">✓ من قاعدة بيانات وزارة العدل (58 ولاية)</span>
      </div>
    </div>
  );
}
