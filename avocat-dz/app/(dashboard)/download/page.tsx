"use client";
import { useI18n } from "@/components/LanguageProvider";
import { Monitor, Download, HardDrive, Shield, Zap, FileText, Check, Package } from "lucide-react";

export default function DownloadPC() {
  const { t } = useI18n();

  return (
    <div className="space-y-6 max-w-5xl mx-auto">
      <div id="download-pc" className="rounded-[1.5rem] bg-gradient-to-br from-[#0e7490] to-[#063544] text-white p-8 lg:p-10 relative overflow-hidden">
        <div className="absolute inset-0 bg-[radial-gradient(circle_at_20%_20%,rgba(255,255,255,0.15),transparent_50%)]" />
        <div className="relative">
          <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-white/15 border border-white/20 text-xs font-bold"><Monitor size={14} /> Logiciel PC — Windows 10/11</div>
          <h1 className="mt-4 font-display font-extrabold text-3xl lg:text-4xl leading-tight">{t("nav.download")} على حاسوبك<br />واشتغل بدون انترنت</h1>
          <p className="text-white/80 mt-3 max-w-2xl leading-relaxed">نسخة PC كاملة — تثبيت بضغطة، بياناتك على جهازك، نسخ احتياطي تلقائي، وطباعة مباشرة. لا حاجة لمتصفح.</p>
          <div className="mt-6 flex flex-wrap gap-3">
            <a href="#installer" className="px-7 py-3.5 rounded-xl bg-white text-[#063544] font-extrabold inline-flex items-center gap-2 text-[15px]"><Monitor size={18} /> تحميل المثبت (.exe) — 85MB</a>
            <a href="#portable" className="px-7 py-3.5 rounded-xl bg-white/15 border border-white/20 font-bold inline-flex items-center gap-2"><Package size={18} /> نسخة محمولة Portable</a>
          </div>
          <div className="mt-4 flex flex-wrap gap-4 text-xs text-white/70">
            <span className="inline-flex items-center gap-1"><Check size={14} className="text-emerald-300" /> Windows 10/11 (64-bit)</span>
            <span className="inline-flex items-center gap-1"><Check size={14} className="text-emerald-300" /> يعمل Offline 100%</span>
            <span className="inline-flex items-center gap-1"><Check size={14} className="text-emerald-300" /> تحديثات تلقائية</span>
          </div>
        </div>
      </div>

      <div className="grid md:grid-cols-3 gap-4">
        <div className="rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] p-5">
          <div className="w-10 h-10 rounded-xl bg-emerald-500/10 text-emerald-600 grid place-items-center"><HardDrive size={20} /></div>
          <div className="font-bold mt-3">بياناتك على جهازك</div>
          <div className="text-sm text-slate-600 dark:text-slate-400 mt-1 leading-relaxed">تُحفظ في <code className="text-xs bg-slate-100 dark:bg-white/10 px-1 py-0.5 rounded">%APPDATA%/Maktabi</code> — مشفّرة ومحمية. لا تُرسل لأي خادم.</div>
        </div>
        <div className="rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] p-5">
          <div className="w-10 h-10 rounded-xl bg-blue-500/10 text-blue-600 grid place-items-center"><Zap size={20} /></div>
          <div className="font-bold mt-3">سريع وخفيف</div>
          <div className="text-sm text-slate-600 dark:text-slate-400 mt-1">يعمل مباشرة بدون Node أو تثبيت إضافي — افتحه وابدأ</div>
        </div>
        <div className="rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] p-5">
          <div className="w-10 h-10 rounded-xl bg-violet-500/10 text-violet-600 grid place-items-center"><Shield size={20} /></div>
          <div className="font-bold mt-3">آمن</div>
          <div className="text-sm text-slate-600 dark:text-slate-400 mt-1">صلاحيات، Audit Log، ونسخ احتياطي يومي تلقائي إلى مجلد الوثائق</div>
        </div>
      </div>

      <div id="installer" className="rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] p-6 lg:p-8">
        <h2 className="font-display font-extrabold text-xl flex items-center gap-2"><Download className="text-[#0e7490]" /> خطوات التثبيت (دقيقة واحدة)</h2>
        <ol className="mt-6 space-y-4">
          <li className="flex gap-4"><span className="w-8 h-8 rounded-full bg-[#0e7490] text-white grid place-items-center font-bold shrink-0">1</span><div><div className="font-bold">حمّل المثبت</div><div className="text-sm text-slate-600 dark:text-slate-400">اضغط الزر أعلاه — سيتم تحميل <code>Maktabi-Setup-1.0.0.exe</code></div></div></li>
          <li className="flex gap-4"><span className="w-8 h-8 rounded-full bg-[#0e7490] text-white grid place-items-center font-bold shrink-0">2</span><div><div className="font-bold">شغّل المثبت</div><div className="text-sm text-slate-600 dark:text-slate-400">اختر مجلد التثبيت (افتراضي: Program Files) — أنشئ اختصار سطح المكتب</div></div></li>
          <li className="flex gap-4"><span className="w-8 h-8 rounded-full bg-[#0e7490] text-white grid place-items-center font-bold shrink-0">3</span><div><div className="font-bold">افتح مكتبي</div><div className="text-sm text-slate-600 dark:text-slate-400">ستجد أيقونة "مكتبي" على سطح المكتب — شغّلها وابدأ بإضافة عملائك</div></div></li>
        </ol>
        <div className="mt-6 p-4 rounded-xl bg-amber-50 dark:bg-amber-500/10 border border-amber-200 dark:border-amber-500/20 text-sm">
          <b>ملاحظة:</b> قد يظهر تحذير SmartScreen أول مرة (لأن التطبيق جديد) — اضغط <b>More info → Run anyway</b>. سنضيف توقيع EV قريباً.
        </div>
      </div>

      <div id="portable" className="rounded-2xl border-2 border-dashed dark:border-white/20 p-6 text-center">
        <Package size={32} className="mx-auto text-slate-400" />
        <div className="font-bold mt-2">النسخة المحمولة — بدون تثبيت</div>
        <div className="text-sm text-slate-500 mt-1">حمّل <code>Maktabi-Portable-1.0.0.exe</code> وضعه على USB — يعمل مباشرة بدون تثبيت، مثالي للمحاكم</div>
        <button className="mt-4 px-6 py-3 rounded-xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] font-bold">تحميل Portable</button>
      </div>

      <div className="rounded-2xl bg-slate-900 text-white p-6">
        <h3 className="font-bold flex items-center gap-2"><FileText size={18} /> للمطور — بناء المثبت من المصدر</h3>
        <pre className="mt-3 p-4 rounded-xl bg-black/50 overflow-auto text-sm" dir="ltr"><code>{`# Windows
npm install
npm run dist:win
# سيُنشأ المثبت في dist/Maktabi-Setup-1.0.0.exe

# أو للنسخة المحمولة
npm run dist:portable
`}</code></pre>
        <div className="text-xs text-white/60 mt-2">يتطلب Node 18+ — لا يحتاج Rust أو Python</div>
      </div>
    </div>
  );
}


