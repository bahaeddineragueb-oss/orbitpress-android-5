"use client";
import { StatCard } from "@/components/StatCard";
import { PCBanner } from "@/components/PCBanner";
import { mockCases, mockClients, mockDeadlines, mockDocuments, mockFees, mockHearings, mockExpenses } from "@/lib/data";
import { formatDZD, formatDateShort, daysUntil } from "@/lib/utils";
import Link from "next/link";
import { Scale, Users, CalendarDays, AlarmClock, Wallet, Receipt, ArrowLeft, Clock, MapPin, FileText, TrendingUp, AlertTriangle } from "lucide-react";
import { useMemo } from "react";
import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer, PieChart, Pie, Cell } from "recharts";

export default function Dashboard() {
  const stats = useMemo(() => {
    const active = mockCases.filter(c => ["جديد","قيد المتابعة","مؤجل","استئناف"].includes(c.status)).length;
    const closed = mockCases.filter(c => c.status === "مغلق" || c.status === "محكوم").length;
    const totalFees = mockFees.reduce((s,f)=>s+f.total,0);
    const paidFees = mockFees.reduce((s,f)=>s+f.paid,0);
    const remaining = totalFees - paidFees;
    const expenses = mockExpenses.reduce((s,e)=>s+e.amount,0);
    const todaySessions = mockHearings.filter(h=> new Date(h.date).toDateString() === new Date().toDateString()).length;
    // For demo show 3
    return { active, closed, totalFees, paidFees, remaining, expenses, todaySessions: 3, deadlines: mockDeadlines.filter(d=>!d.done && daysUntil(d.dueDate) <= 10).length };
  }, []);

  const byCategory = [
    { name: "عقاري", value: 1 },
    { name: "أسرة", value: 1 },
    { name: "جزائي", value: 1 },
    { name: "تجاري", value: 1 },
    { name: "مدني", value: 1 },
  ];
  const monthly = [
    { name: "جانفي", income: 80000, expense: 15000 },
    { name: "فيفري", income: 120000, expense: 22000 },
    { name: "مارس", income: 95000, expense: 18000 },
    { name: "أفريل", income: 150000, expense: 31000 },
    { name: "ماي", income: 70000, expense: 12000 },
    { name: "جوان", income: 180000, expense: 28000 },
  ];
  const COLORS = ["#0e7490","#f59e0b","#10b981","#ef4444","#8b5cf6"];

  return (
    <div className="space-y-6">
      <PCBanner />
      {/* Welcome */}
      <div className="rounded-2xl bg-gradient-to-br from-[#0e7490] to-[#063544] text-white p-6 lg:p-8 relative overflow-hidden">
        <div className="absolute inset-0 bg-[radial-gradient(circle_at_30%_20%,rgba(255,255,255,0.15),transparent_50%)]" />
        <div className="relative flex flex-col lg:flex-row lg:items-center justify-between gap-6">
          <div>
            <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-white/15 border border-white/20 text-xs">مرحباً أستاذ بهاء الدين 👋 • {new Date().toLocaleDateString("ar-DZ", { weekday: "long", day: "numeric", month: "long", year: "numeric" })}</div>
            <h1 className="mt-3 font-display font-extrabold text-2xl lg:text-3xl">لوحة تحكم مكتبك في لمحة</h1>
            <p className="text-white/80 mt-2 max-w-2xl">تابع جلسات اليوم، الآجال الحرجة، الأتعاب المستحقة، وكل ملفاتك من مكان واحد.</p>
          </div>
          <div className="flex gap-3 shrink-0">
            <Link href="/cases" className="px-5 py-3 rounded-xl bg-white text-[#063544] font-bold">+ ملف جديد</Link>
            <Link href="/sessions" className="px-5 py-3 rounded-xl bg-white/15 border border-white/20 font-bold">+ جلسة</Link>
          </div>
        </div>
      </div>

      {/* KPIs */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
        <StatCard title="جلسات اليوم" value={stats.todaySessions} sub="في 3 محاكم مختلفة" icon={CalendarDays} color="cyan" trend="🔴 1 جلسة صباحاً 09:00" />
        <StatCard title="آجال قريبة (10 أيام)" value={stats.deadlines} sub="استئناف + مذكرات" icon={AlarmClock} color="rose" trend="⚠️ 1 عاجل جداً" />
        <StatCard title="ملفات نشطة" value={stats.active} sub={`من أصل ${mockCases.length} ملفات`} icon={Scale} color="violet" trend={`📁 ${stats.closed} مغلق/محكوم`} />
        <StatCard title="أتعاب غير محصلة" value={formatDZD(stats.remaining)} sub={`${mockClients.length} عملاء`} icon={Wallet} color="amber" trend={`💰 المحصل: ${formatDZD(stats.paidFees)}`} />
      </div>

      <div className="grid lg:grid-cols-3 gap-6">
        {/* Left 2 cols */}
        <div className="lg:col-span-2 space-y-6">
          {/* Today's sessions */}
          <div className="rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] overflow-hidden">
            <div className="p-5 border-b dark:border-[#1e2e50] flex items-center justify-between">
              <h3 className="font-bold flex items-center gap-2"><CalendarDays size={18} className="text-[#0e7490]" /> جلسات اليوم والقادمة</h3>
              <Link href="/sessions" className="text-sm text-[#0e7490] font-bold inline-flex items-center gap-1">عرض التقويم <ArrowLeft size={16} /></Link>
            </div>
            <div className="divide-y dark:divide-[#1e2e50]">
              {mockHearings.slice(0,4).map(h => {
                const c = mockCases.find(x=>x.id===h.caseId);
                const isToday = new Date(h.date).toDateString() === new Date().toDateString();
                return (
                  <div key={h.id} className="p-4 flex items-center gap-4 hover:bg-slate-50 dark:hover:bg-white/[0.03]">
                    <div className={`w-14 h-14 rounded-xl grid place-items-center text-white font-bold shrink-0 ${isToday ? "bg-red-500" : "bg-[#0e7490]"}`}>
                      <div className="text-center leading-none">
                        <div className="text-[11px]">{new Date(h.date).toLocaleDateString("ar-DZ", {month:"short"})}</div>
                        <div className="text-lg">{new Date(h.date).getDate()}</div>
                      </div>
                    </div>
                    <div className="flex-1 min-w-0">
                      <div className="font-bold truncate">{c?.title}</div>
                      <div className="text-sm text-slate-500 flex flex-wrap gap-3 mt-1">
                        <span className="inline-flex items-center gap-1"><MapPin size={14} /> {h.court} {h.room ? `• ${h.room}` : ""}</span>
                        <span className="inline-flex items-center gap-1"><Clock size={14} /> {new Date(h.date).toLocaleTimeString("ar-DZ", {hour:"2-digit", minute:"2-digit"})}</span>
                        <span className="hidden md:inline">• {c?.fileNumber}</span>
                      </div>
                    </div>
                    <span className={`hidden md:inline-flex text-xs px-3 py-1 rounded-full font-bold border ${h.status==="قادمة" ? "bg-emerald-50 text-emerald-700 border-emerald-200 dark:bg-emerald-500/10 dark:text-emerald-300" : "bg-amber-50 text-amber-700 border-amber-200"}`}>{h.status}</span>
                  </div>
                );
              })}
            </div>
          </div>

          {/* Revenue chart */}
          <div className="rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] p-5">
            <h3 className="font-bold flex items-center gap-2"><TrendingUp size={18} className="text-emerald-600" /> الإيرادات والمصاريف (6 أشهر)</h3>
            <div className="h-[260px] mt-4" dir="ltr">
              <ResponsiveContainer width="100%" height="100%">
                <BarChart data={monthly}>
                  <XAxis dataKey="name" tick={{fontSize:12}} />
                  <YAxis tick={{fontSize:12}} />
                  <Tooltip />
                  <Bar dataKey="income" name="الإيرادات" fill="#0e7490" radius={[8,8,0,0]} />
                  <Bar dataKey="expense" name="المصاريف" fill="#f59e0b" radius={[8,8,0,0]} />
                </BarChart>
              </ResponsiveContainer>
            </div>
            <div className="mt-3 grid grid-cols-3 gap-3 text-center">
              <div className="rounded-xl bg-slate-50 dark:bg-white/5 p-3 border dark:border-white/10"><div className="text-xs text-slate-500">الإيرادات</div><div className="font-extrabold">{formatDZD(695000)}</div></div>
              <div className="rounded-xl bg-slate-50 dark:bg-white/5 p-3 border dark:border-white/10"><div className="text-xs text-slate-500">المصاريف</div><div className="font-extrabold">{formatDZD(126000)}</div></div>
              <div className="rounded-xl bg-emerald-50 dark:bg-emerald-500/10 p-3 border border-emerald-100 dark:border-emerald-500/20"><div className="text-xs text-emerald-700">الصافي</div><div className="font-extrabold text-emerald-700">{formatDZD(569000)}</div></div>
            </div>
          </div>
        </div>

        {/* Right col */}
        <div className="space-y-6">
          {/* Deadlines */}
          <div className="rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] p-5">
            <h3 className="font-bold flex items-center gap-2"><AlarmClock size={18} className="text-red-500" /> آجال حرجة</h3>
            <div className="mt-4 space-y-3">
              {mockDeadlines.slice(0,4).map(d => {
                const c = mockCases.find(x=>x.id===d.caseId);
                const left = daysUntil(d.dueDate);
                return (
                  <div key={d.id} className="p-3 rounded-xl border dark:border-[#1e2e50] bg-slate-50/60 dark:bg-white/[0.03]">
                    <div className="flex items-center justify-between">
                      <span className={`text-[11px] px-2 py-1 rounded-full font-bold ${d.priority==="عاجل" ? "bg-red-500 text-white" : d.priority==="هام" ? "bg-amber-500 text-white" : "bg-slate-200 dark:bg-white/10"}`}>{d.priority}</span>
                      <span className={`text-xs font-bold ${left <= 3 ? "text-red-600" : left <= 10 ? "text-amber-600" : "text-slate-500"}`}>{left>0?`بقي ${left} أيام`:`متأخر ${Math.abs(left)} أيام`}</span>
                    </div>
                    <div className="font-bold text-sm mt-2 leading-tight">{d.title}</div>
                    <div className="text-xs text-slate-500 mt-1">{c?.fileNumber} • {c?.title?.slice(0,32)}...</div>
                    <div className="text-xs mt-1 flex items-center gap-1"><Clock size={12} /> {formatDateShort(d.dueDate)} • {d.type}</div>
                  </div>
                );
              })}
              <Link href="/deadlines" className="block text-center text-sm font-bold text-[#0e7490] py-2">عرض كل الآجال →</Link>
            </div>
          </div>

          {/* By category */}
          <div className="rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] p-5">
            <h3 className="font-bold">القضايا حسب النوع</h3>
            <div className="h-[200px] mt-2" dir="ltr">
              <ResponsiveContainer width="100%" height="100%">
                <PieChart>
                  <Pie data={byCategory} dataKey="value" nameKey="name" innerRadius={55} outerRadius={80} paddingAngle={3}>
                    {byCategory.map((_, i) => <Cell key={i} fill={COLORS[i % COLORS.length]} />)}
                  </Pie>
                  <Tooltip />
                </PieChart>
              </ResponsiveContainer>
            </div>
            <div className="flex flex-wrap gap-2 justify-center">
              {byCategory.map((b,i)=><span key={b.name} className="text-xs px-2 py-1 rounded-full border dark:border-white/10" style={{borderColor: COLORS[i]}}>{b.name}</span>)}
            </div>
          </div>

          {/* Quick actions */}
          <div className="rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] p-5">
            <h3 className="font-bold">إجراءات سريعة</h3>
            <div className="grid grid-cols-2 gap-3 mt-4">
              <Link href="/clients" className="p-4 rounded-xl bg-slate-50 dark:bg-white/5 border dark:border-white/10 hover:bg-slate-100 dark:hover:bg-white/10 text-center"><Users size={20} className="mx-auto" /><div className="text-sm font-bold mt-1">عميل جديد</div></Link>
              <Link href="/cases" className="p-4 rounded-xl bg-slate-50 dark:bg-white/5 border dark:border-white/10 hover:bg-slate-100 dark:hover:bg-white/10 text-center"><Scale size={20} className="mx-auto" /><div className="text-sm font-bold mt-1">قضية جديدة</div></Link>
              <Link href="/documents" className="p-4 rounded-xl bg-slate-50 dark:bg-white/5 border dark:border-white/10 hover:bg-slate-100 dark:hover:bg-white/10 text-center"><FileText size={20} className="mx-auto" /><div className="text-sm font-bold mt-1">رفع وثيقة</div></Link>
              <Link href="/templates" className="p-4 rounded-xl bg-slate-50 dark:bg-white/5 border dark:border-white/10 hover:bg-slate-100 dark:hover:bg-white/10 text-center"><FileText size={20} className="mx-auto" /><div className="text-sm font-bold mt-1">نموذج جاهز</div></Link>
            </div>
          </div>
        </div>
      </div>

      {/* Bottom: cases table preview */}
      <div className="rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] overflow-hidden">
        <div className="p-5 border-b dark:border-[#1e2e50] flex items-center justify-between">
          <h3 className="font-bold">أحدث الملفات</h3>
          <Link href="/cases" className="text-sm font-bold text-[#0e7490]">عرض كل الملفات <ArrowLeft size={14} className="inline" /></Link>
        </div>
        <div className="overflow-auto">
          <table className="w-full text-sm">
            <thead className="bg-slate-50 dark:bg-white/5 text-slate-500">
              <tr>
                <th className="p-3 text-right">رقم الملف</th>
                <th className="p-3 text-right">الموضوع</th>
                <th className="p-3 text-right">المحكمة</th>
                <th className="p-3 text-right">الحالة</th>
                <th className="p-3 text-right">الأتعاب</th>
              </tr>
            </thead>
            <tbody className="divide-y dark:divide-[#1e2e50]">
              {mockCases.slice(0,5).map(c=>{
                const fee = mockFees.find(f=>f.caseId===c.id);
                return (
                  <tr key={c.id} className="hover:bg-slate-50 dark:hover:bg-white/[0.03]">
                    <td className="p-3 font-mono font-bold">{c.fileNumber}</td>
                    <td className="p-3"><div className="font-bold">{c.title}</div><div className="text-xs text-slate-500">{c.caseNumber} • {c.category}</div></td>
                    <td className="p-3">{c.court}</td>
                    <td className="p-3"><span className={`text-xs px-2 py-1 rounded-full font-bold border ${c.status==="قيد المتابعة" ? "bg-blue-50 text-blue-700 border-blue-200 dark:bg-blue-500/10 dark:text-blue-300" : c.status==="مؤجل" ? "bg-amber-50 text-amber-700 border-amber-200" : c.status==="محكوم" ? "bg-emerald-50 text-emerald-700 border-emerald-200" : "bg-slate-100 dark:bg-white/10"}`}>{c.status}</span></td>
                    <td className="p-3">{fee ? <span className={fee.paid<fee.total ? "text-amber-600 font-bold":"text-emerald-600 font-bold"}>{formatDZD(fee.total - fee.paid)} متبقي</span> : "—"}</td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
      </div>

      <div className="rounded-xl border border-amber-200 bg-amber-50 dark:bg-amber-500/10 dark:border-amber-500/20 p-4 flex gap-3">
        <AlertTriangle className="text-amber-600 shrink-0" size={20} />
        <div className="text-sm leading-relaxed">
          <span className="font-bold">تنبيه:</span> لديك <span className="font-bold">4 عملاء</span> لديهم مستحقات، و <span className="font-bold">5 وثائق</span> تحتاج مراجعة. والمساعد الذكي يمكنه تلخيص أي ملف في ثوانٍ — جرّبه من <Link href="/assistant" className="underline font-bold">هنا</Link>.
        </div>
      </div>
    </div>
  );
}
