"use client";
import { useEffect, useState } from "react";
import { Monitor, Download, HardDrive, ShieldCheck } from "lucide-react";

export function PCBanner() {
  const [isPC, setIsPC] = useState(false);
  const [ver, setVer] = useState("");
  useEffect(() => {
    const e = (window as any).electronAPI;
    if (e?.isElectron) {
      setIsPC(true);
      e.getVersion().then(setVer);
    }
  }, []);
  if (!isPC) return (
    <div className="rounded-xl border-2 border-dashed border-[#0e7490]/30 bg-[#0e7490]/5 dark:bg-[#0e7490]/10 p-4 flex flex-col md:flex-row items-center justify-between gap-3">
      <div className="flex items-center gap-3">
        <div className="w-10 h-10 rounded-xl bg-[#0e7490] text-white grid place-items-center"><Monitor size={20} /></div>
        <div>
          <div className="font-bold text-sm">تستخدم النسخة الويب — حوّلها إلى logiciel PC</div>
          <div className="text-xs text-slate-600 dark:text-slate-400">حمّل الـ .exe وشغّله على ويندوز بدون انترنت — بياناتك تبقى على جهازك</div>
        </div>
      </div>
      <a href="#download-pc" className="px-5 py-2.5 rounded-xl bg-[#0e7490] text-white font-bold inline-flex items-center gap-2"><Download size={16} /> تحميل للويندوز</a>
    </div>
  );
  return (
    <div className="rounded-xl border bg-emerald-50 dark:bg-emerald-500/10 border-emerald-200 dark:border-emerald-500/20 p-3 flex items-center justify-between">
      <div className="flex items-center gap-2 text-sm"><ShieldCheck className="text-emerald-600" size={18} /> أنت تستخدم <b>Logiciel PC</b> — يعمل Offline بالكامل <span className="text-xs px-2 py-0.5 rounded-full bg-emerald-600 text-white">v{ver || '1.0.0'} • Windows</span></div>
      <span className="hidden md:inline-flex items-center gap-1 text-xs text-emerald-700"><HardDrive size={14} /> البيانات محفوظة على جهازك</span>
    </div>
  );
}
