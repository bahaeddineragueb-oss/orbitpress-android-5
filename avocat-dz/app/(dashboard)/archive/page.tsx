"use client";
import { useI18n } from "@/components/LanguageProvider";
import { mockCases } from "@/lib/data";
import { Archive } from "lucide-react";
export default function ArchivePage(){
  const { t } = useI18n();

  const archived = mockCases.filter(c=>c.status==="مؤرشف" || c.status==="مغلق");
  return (
    <div className="space-y-6">
      <h1 className="font-display font-extrabold text-2xl flex items-center gap-2"><Archive className="text-[#0e7490]"/> {t("nav.archive")}</h1>
      <p className="text-sm text-slate-500">الملفات المغلقة والمؤرشفة — لا يمكن حذفها إلا بصلاحية المدير</p>
      <div className="rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] overflow-hidden">
        {archived.length? archived.map(c=> <div key={c.id} className="p-4 border-b dark:border-[#1e2e50]"><div className="font-bold">{c.fileNumber} — {c.title}</div><div className="text-sm text-slate-500">{c.court} • {c.status}</div></div>) : <div className="p-12 text-center text-slate-500">الأرشيف فارغ حالياً — سيظهر هنا الملفات المغلقة</div>}
      </div>
    </div>
  );
}
