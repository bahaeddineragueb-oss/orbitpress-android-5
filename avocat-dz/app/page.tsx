import Link from "next/link";
import { Gavel, Shield, Calendar, Files, Wallet, Sparkles, ArrowLeft, Check, Scale, Users, Building2, Bot, MonitorDown, HardDrive } from "lucide-react";

export default function Landing() {
  return (
    <div className="min-h-dvh">
      {/* Header */}
      <header className="sticky top-0 z-20 backdrop-blur bg-white/80 dark:bg-[#070e1f]/80 border-b dark:border-[#1e2e50]">
        <div className="max-w-7xl mx-auto px-6 py-4 flex items-center justify-between">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-xl bg-gradient-to-br from-[#0e7490] to-[#063544] grid place-items-center text-white"><Gavel size={20} /></div>
            <div>
              <div className="font-display font-extrabold">مكتبي — Avocat DZ Pro</div>
              <div className="text-xs text-slate-500">تسيير مكاتب المحاماة الجزائرية</div>
            </div>
          </div>
          <div className="flex items-center gap-3">
            <Link href="/dashboard" className="px-5 py-2.5 rounded-xl bg-[#0e7490] text-white font-bold hover:bg-[#0c6580] transition inline-flex items-center gap-2">دخول لوحة التحكم <ArrowLeft size={18} /></Link>
          </div>
        </div>
      </header>

      {/* Hero */}
      <section className="max-w-7xl mx-auto px-6 py-12 lg:py-20">
        <div className="grid lg:grid-cols-2 gap-10 items-center">
          <div>
            <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-amber-100 text-amber-800 text-xs font-bold border border-amber-200"><Sparkles size={14} /> صُمم للمحامي الجزائري</div>
            <h1 className="mt-4 font-display font-extrabold text-4xl lg:text-5xl leading-tight">
              ليس مجرد دفتر إلكتروني،<br />
              <span className="text-[#0e7490]">بل دورة حياة الملف كاملة</span>
            </h1>
            <p className="mt-4 text-slate-600 dark:text-slate-300 leading-relaxed">
              العميل → القضية → الإجراءات والجلسات → الوثائق → الأتعاب → المصاريف → الآجال → الأرشيف. كل شيء في صفحة واحدة، مع تنبيهات للآجال، وتقويم للجلسات، ومكتبة نماذج جاهزة قابلة للتعبئة التلقائية.
            </p>
            <div className="mt-6 flex flex-wrap gap-3">
              <Link href="/dashboard" className="px-6 py-3 rounded-xl bg-[#0e7490] text-white font-bold">جرّب لوحة التحكم الآن</Link>
              <Link href="/download" className="px-6 py-3 rounded-xl border-2 border-[#0e7490] bg-white dark:bg-[#0f1b33] text-[#0e7490] font-extrabold inline-flex items-center gap-2"><MonitorDown size={18} /> تحميل للـ PC (Windows .exe)</Link>
            </div>
            <div className="mt-4 p-3 rounded-xl bg-gradient-to-br from-slate-900 to-[#0e7490] text-white flex items-center justify-between">
              <div className="flex items-center gap-2 text-sm"><HardDrive size={16} /> <b>Logiciel PC</b> — يعمل بدون انترنت، بياناتك على جهازك</div>
              <Link href="/download" className="text-xs px-3 py-1 rounded-full bg-white text-slate-900 font-bold">التحميل مجاني →</Link>
            </div>
            <div className="mt-4 flex items-center gap-6 text-sm text-slate-600 dark:text-slate-400">
              <span className="inline-flex items-center gap-2"><Check size={16} className="text-emerald-600" /> logiciel PC + PWA</span>
              <span className="inline-flex items-center gap-2"><Check size={16} className="text-emerald-600" /> RTL كامل</span>
              <span className="inline-flex items-center gap-2"><Check size={16} className="text-emerald-600" /> آمن ومشفّر</span>
            </div>
          </div>

          <div className="relative">
            <div className="absolute -inset-4 bg-gradient-to-br from-cyan-100 to-blue-100 dark:from-cyan-900/20 dark:to-blue-900/20 rounded-[2rem] blur-2xl" />
            <div className="relative rounded-[1.5rem] border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] shadow-soft overflow-hidden">
              <div className="h-10 border-b dark:border-[#1e2e50] flex items-center gap-2 px-4">
                <span className="w-3 h-3 rounded-full bg-red-400" /><span className="w-3 h-3 rounded-full bg-amber-400" /><span className="w-3 h-3 rounded-full bg-emerald-400" />
                <span className="mr-auto text-xs text-slate-500">dashboard — مكتبي</span>
              </div>
              <img src="https://images.unsplash.com/photo-1589829085413-56de8ae18c73?w=1200&q=80&auto=format&fit=crop" alt="law" className="w-full h-[280px] object-cover" />
              <div className="p-4 grid grid-cols-3 gap-3">
                <div className="rounded-xl bg-slate-50 dark:bg-white/5 p-3 border dark:border-white/10"><div className="text-xs text-slate-500">جلسات اليوم</div><div className="font-extrabold text-lg">3</div></div>
                <div className="rounded-xl bg-amber-50 dark:bg-amber-500/10 p-3 border border-amber-100 dark:border-amber-500/20"><div className="text-xs text-amber-700">آجال قريبة</div><div className="font-extrabold text-lg">2</div></div>
                <div className="rounded-xl bg-emerald-50 dark:bg-emerald-500/10 p-3 border border-emerald-100 dark:border-emerald-500/20"><div className="text-xs text-emerald-700">أتعاب مستحقة</div><div className="font-extrabold text-lg">70,000 دج</div></div>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Features */}
      <section id="features" className="max-w-7xl mx-auto px-6 pb-16">
        <div className="grid md:grid-cols-3 lg:grid-cols-4 gap-4">
          {[
            { icon: Users, title: "إدارة العملاء", desc: "ملف كامل، سجل تواصل، وثائق، وكل قضاياه في صفحة واحدة" },
            { icon: Scale, title: "القضايا والملفات", desc: "Timeline كامل من فتح الملف إلى الحكم والأرشيف" },
            { icon: Calendar, title: "الجلسات والآجال", desc: "تقويم يومي/أسبوعي/شهري + تنبيهات آجال قانونية" },
            { icon: Files, title: "الوثائق", desc: "مجلد إلكتروني لكل ملف مع تصنيف وبحث ومعاينة PDF" },
            { icon: FileTextIcon, title: "نماذج قانونية", desc: "مكتبة عرائض ومذكرات قابلة للتعبئة التلقائية" },
            { icon: Wallet, title: "الأتعاب والمصاريف", desc: "تتبع المدفوعات، الأقساط، وصافي مداخيل المكتب" },
            { icon: Building2, title: "قاعدة المحاكم", desc: "المحاكم والمجالس والأقسام لكل ولاية" },
            { icon: Bot, title: "مساعد ذكي", desc: "تلخيص ملف، استخراج آجال، وصياغة مسودة مذكرة" },
          ].map((f) => (
            <div key={f.title} className="rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] p-5">
              <div className="w-10 h-10 rounded-xl bg-[#0e7490]/10 text-[#0e7490] grid place-items-center"><f.icon size={20} /></div>
              <div className="mt-3 font-bold">{f.title}</div>
              <div className="text-sm text-slate-600 dark:text-slate-400 leading-relaxed">{f.desc}</div>
            </div>
          ))}
        </div>

        <div className="mt-8 rounded-2xl border bg-gradient-to-br from-[#0e7490] to-[#063544] text-white p-6 lg:p-8 flex flex-col lg:flex-row items-center justify-between gap-4">
          <div>
            <div className="font-display font-extrabold text-xl">جاهز لتنظيم مكتبك في دقائق؟</div>
            <div className="text-white/80 text-sm mt-1">حمّل الـ logiciel PC وشغّله بدون انترنت — أو جرّبه على الويب الآن.</div>
          </div>
          <div className="flex gap-3">
            <Link href="/download" className="px-6 py-3 rounded-xl bg-white text-[#063544] font-extrabold inline-flex items-center gap-2"><MonitorDown size={18} /> تحميل للـ PC</Link>
            <Link href="/dashboard" className="px-6 py-3 rounded-xl bg-white/15 border border-white/20 font-bold">جرّب الويب</Link>
          </div>
        </div>
      </section>

      <footer className="border-t dark:border-[#1e2e50] py-6 text-center text-sm text-slate-500">© 2026 مكتبي — Avocat DZ Pro • صُنع بـ ❤️ في الجزائر</footer>
    </div>
  );
}

function FileTextIcon(props: any) { return <Files {...props} /> }
