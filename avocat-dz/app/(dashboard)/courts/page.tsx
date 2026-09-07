"use client";
import { useState, useMemo } from "react";
import { courtsData, DB_INFO, getAllTribunals } from "@/lib/courts";
import { Building2, Search, MapPin, Phone, Database, Check, Shield, ExternalLink, Landmark } from "lucide-react";

export default function CourtsPage(){
  const [q,setQ]=useState("");
  const [code,setCode]=useState("16"); // الجزائر default
  const selected = useMemo(()=> courtsData.find(w=>w.code===code), [code]);
  const filtered = useMemo(()=> {
    if (!q) return courtsData;
    return courtsData.filter(w => 
      w.wilaya.includes(q) ||
      w.council.includes(q) ||
      w.tribunals.some(t=>t.name.includes(q)) ||
      (w.adminCourt && w.adminCourt.includes(q))
    );
  }, [q]);

  const allTribunals = getAllTribunals();

  return (
    <div className="space-y-6">
      {/* Header with DB info */}
      <div className="rounded-2xl bg-gradient-to-br from-[#0e7490] to-[#063544] text-white p-6 relative overflow-hidden">
        <div className="absolute inset-0 bg-[radial-gradient(circle_at_20%_20%,rgba(255,255,255,0.12),transparent_50%)]" />
        <div className="relative">
          <h1 className="font-display font-extrabold text-2xl flex items-center gap-2"><Building2 /> قاعدة بيانات المحاكم — حقيقية 100%</h1>
          <p className="text-white/80 mt-1 text-sm leading-relaxed">من <b>وزارة العدل الجزائرية</b> مباشرة — <a href="https://www.mjustice.gov.dz/ar/المحاكم-و-المجالس/" target="_blank" className="underline inline-flex items-center gap-1">mjustice.gov.dz <ExternalLink size={12}/></a> — المحامي يختار فقط، لا كتابة</p>
          <div className="mt-4 grid grid-cols-2 md:grid-cols-4 gap-3">
            <div className="rounded-xl bg-white/10 border border-white/20 p-3 text-center"><div className="text-2xl font-extrabold">58</div><div className="text-xs">ولاية</div><div className="text-[11px] opacity-70">48 + 10 جديدة</div></div>
            <div className="rounded-xl bg-white/10 border border-white/20 p-3 text-center"><div className="text-2xl font-extrabold">58</div><div className="text-xs">مجلس قضائي</div><div className="text-[11px] opacity-70">مجلس لكل ولاية</div></div>
            <div className="rounded-xl bg-white/10 border border-white/20 p-3 text-center"><div className="text-2xl font-extrabold">256</div><div className="text-xs">محكمة/فرع</div><div className="text-[11px] opacity-70">من الموقع الرسمي</div></div>
            <div className="rounded-xl bg-white/10 border border-white/20 p-3 text-center"><div className="text-2xl font-extrabold">58</div><div className="text-xs">محكمة إدارية</div><div className="text-[11px] opacity-70">لكل ولاية</div></div>
          </div>
          <div className="mt-3 text-[11px] text-white/60 flex flex-wrap gap-3">
            <span>📁 data/courts.json (111KB)</span><span>•</span><span>🗄️ prisma/maktabi.db (164KB SQLite)</span><span>•</span><span>🔄 تحديث: {DB_INFO.date}</span>
          </div>
        </div>
      </div>

      {/* Selector + Search */}
      <div className="grid lg:grid-cols-3 gap-4">
        <div className="lg:col-span-1 rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] p-5">
          <h3 className="font-bold flex items-center gap-2"><Landmark size={18} className="text-[#0e7490]"/> اختر الولاية</h3>
          <div className="mt-3 relative">
            <Search size={16} className="absolute right-3 top-1/2 -translate-y-1/2 text-slate-400" />
            <input value={q} onChange={e=>setQ(e.target.value)} placeholder="بحث: الرويبة، وهران، تيميمون..." className="w-full pr-9 pl-3 py-2.5 rounded-xl border bg-slate-50 dark:bg-[#070e1f] dark:border-[#1e2e50] text-sm" />
          </div>
          <div className="mt-3 max-h-[420px] overflow-auto space-y-1 pr-1">
            {(q ? filtered : courtsData).map(w => (
              <button
                key={w.code}
                onClick={()=>setCode(w.code)}
                className={`w-full text-right p-3 rounded-xl border text-sm flex items-center justify-between ${code===w.code ? "bg-[#0e7490] text-white border-[#0e7490]" : "bg-white dark:bg-[#070e1f] dark:border-[#1e2e50] hover:bg-slate-50 dark:hover:bg-white/5"}`}
              >
                <div>
                  <div className="font-bold">{w.code} — {w.wilayaAr}</div>
                  <div className={`text-xs ${code===w.code ? "text-white/80":"text-slate-500"}`}>{w.council} • {w.tribunals.length} محاكم</div>
                </div>
                {w.isNew && <span className={`text-[10px] px-2 py-0.5 rounded-full font-bold ${code===w.code ? "bg-white text-[#0e7490]":"bg-amber-500 text-white"}`}>جديدة</span>}
              </button>
            ))}
          </div>
        </div>

        <div className="lg:col-span-2 space-y-4">
          {selected && (
            <>
              <div className="rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] p-6">
                <div className="flex items-start justify-between gap-4">
                  <div>
                    <div className="inline-flex items-center gap-2 text-xs px-2 py-1 rounded-full bg-slate-900 text-white dark:bg-white dark:text-slate-900">{selected.code} — {selected.wilayaAr}</div>
                    <h2 className="font-display font-extrabold text-xl mt-2">{selected.council}</h2>
                    <div className="text-sm text-slate-600 dark:text-slate-400 mt-1">{selected.adminCourt || "بدون محكمة إدارية مسجلة"} • {selected.tribunals.length} محاكم/فروع</div>
                  </div>
                  <div className={`w-12 h-12 rounded-xl grid place-items-center ${selected.isNew ? "bg-amber-500 text-white":"bg-[#0e7490] text-white"}`}><Building2 /></div>
                </div>

                <div className="mt-6">
                  <h3 className="font-bold text-sm">المحاكم التابعة ({selected.tribunals.length})</h3>
                  <div className="mt-3 grid md:grid-cols-2 gap-2">
                    {selected.tribunals.map(t => (
                      <div key={t.name} className={`p-3 rounded-xl border flex items-center justify-between ${t.isBranch ? "bg-amber-50 dark:bg-amber-500/10 border-amber-200 dark:border-amber-500/20":"bg-slate-50 dark:bg-white/5 dark:border-white/10"}`}>
                        <div>
                          <div className="font-bold text-sm">{t.name}</div>
                          <div className="text-xs text-slate-500">{t.isBranch ? "فرع محكمة" : "محكمة ابتدائية"} • {t.sections.length} أقسام</div>
                        </div>
                        <span className={`text-[11px] px-2 py-1 rounded-full font-bold ${t.isBranch ? "bg-amber-500 text-white":"bg-[#0e7490] text-white"}`}>{t.isBranch ? "فرع":"محكمة"}</span>
                      </div>
                    ))}
                  </div>
                </div>

                {selected.adminCourt && (
                  <div className="mt-4 p-3 rounded-xl bg-violet-50 dark:bg-violet-500/10 border border-violet-200 dark:border-violet-500/20 flex items-center gap-3">
                    <div className="w-10 h-10 rounded-xl bg-violet-600 text-white grid place-items-center"><Shield size={18}/></div>
                    <div>
                      <div className="font-bold text-sm">{selected.adminCourt}</div>
                      <div className="text-xs text-violet-700 dark:text-violet-300">المحكمة الإدارية — مختصة بالقضاء الإداري</div>
                    </div>
                  </div>
                )}

                <div className="mt-4 p-3 rounded-xl bg-[#0e7490]/5 border border-[#0e7490]/20">
                  <div className="font-bold text-sm flex items-center gap-2"><Check size={16} className="text-emerald-600"/> الأقسام المتاحة في هذه المحاكم</div>
                  <div className="flex flex-wrap gap-1.5 mt-2">
                    {selected.tribunals[0]?.sections.map(s => <span key={s} className="text-xs px-2 py-1 rounded-full bg-white dark:bg-[#070e1f] border dark:border-[#1e2e50]">{s}</span>)}
                  </div>
                  <div className="text-[11px] text-slate-500 mt-2">المحامي يختار القسم عند إنشاء القضية — نفس الأقسام في كل المحاكم الابتدائية</div>
                </div>
              </div>

              <div className="rounded-xl border-2 border-dashed dark:border-white/20 p-4 text-center">
                <div className="text-sm font-bold">هذه البيانات من قاعدة حقيقية — هل تريد إضافتها لقضية؟</div>
                <div className="text-xs text-slate-500 mt-1">عند إنشاء قضية جديدة، ستظهر لك نفس القوائم: ولاية → مجلس → محكمة → قسم (اختيار فقط)</div>
                <a href="/cases" className="mt-3 inline-flex px-5 py-2.5 rounded-xl bg-[#0e7490] text-white font-bold">+ إنشاء قضية بهذه المحكمة</a>
              </div>
            </>
          )}
        </div>
      </div>

      {/* Full list search results */}
      {q && (
        <div className="rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] p-5">
          <h3 className="font-bold">نتائج البحث عن "{q}" — {filtered.length} ولايات</h3>
          <div className="mt-4 grid md:grid-cols-2 lg:grid-cols-3 gap-3">
            {filtered.map(w => (
              <div key={w.code} className="p-4 rounded-xl border dark:border-white/10 bg-slate-50 dark:bg-white/5">
                <div className="font-bold">{w.code} — {w.wilayaAr}</div>
                <div className="text-xs text-slate-500">{w.council}</div>
                <div className="text-xs mt-1">{w.tribunals.map(t=>t.name).join(" • ").slice(0,80)}...</div>
              </div>
            ))}
          </div>
        </div>
      )}

      {/* DB file info */}
      <div className="rounded-2xl border bg-slate-900 text-white p-6">
        <h3 className="font-bold flex items-center gap-2"><Database size={18}/> قاعدة بيانات حقيقية — ملفات</h3>
        <div className="mt-3 grid md:grid-cols-2 gap-4 text-sm">
          <div className="p-4 rounded-xl bg-white/5 border border-white/10">
            <div className="font-bold">data/courts.json</div>
            <div className="text-white/60 mt-1">111KB • JSON • للويب (تحميل فوري بدون DB)</div>
            <div className="text-xs mt-2">يحتوي 58 ولاية + 256 محكمة + الأقسام • يُستخدم في الواجهة مباشرة</div>
            <a href="/api/courts" target="_blank" className="mt-2 inline-flex text-xs px-3 py-1 rounded-full bg-white text-slate-900 font-bold">جرّب API /api/courts</a>
          </div>
          <div className="p-4 rounded-xl bg-white/5 border border-white/10">
            <div className="font-bold">prisma/maktabi.db</div>
            <div className="text-white/60 mt-1">164KB • SQLite • للـ PC (Electron)</div>
            <div className="text-xs mt-2">4 جداول: wilayas, councils, tribunals, admin_courts + 5 جداول مكتب • يُفتح بـ DB Browser</div>
            <div className="text-xs mt-2 font-mono">sqlite3 prisma/maktabi.db "SELECT * FROM tribunals WHERE wilayaCode='16'"</div>
          </div>
        </div>
        <div className="mt-4 text-xs text-white/50">المصدر: وزارة العدل + مرسوم 21-117 (10 ولايات جديدة) • يمكنك تحديث `data/courts.json` وإعادة تشغيل `python3 prisma/seed.py` أو فتح `prisma/maktabi.db` مباشرة</div>
      </div>
    </div>
  );
}
