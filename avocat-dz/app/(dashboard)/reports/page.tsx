"use client";
import { useI18n } from "@/components/LanguageProvider";
import { mockCases, mockFees, mockExpenses } from "@/lib/data";
import { formatDZD } from "@/lib/utils";
import { BarChart3 } from "lucide-react";
import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer } from "recharts";
export default function Reports(){
  const { t } = useI18n();

  const data = [
    { name:"مدني", count: mockCases.filter(c=>c.category==="مدني").length },
    { name:"جزائي", count: mockCases.filter(c=>c.category==="جزائي").length },
    { name:"عقاري", count: mockCases.filter(c=>c.category==="عقاري").length },
    { name:"أسرة", count: mockCases.filter(c=>c.category==="أسرة").length },
    { name:"تجاري", count: mockCases.filter(c=>c.category==="تجاري").length },
  ];
  const total = mockFees.reduce((s,f)=>s+f.total,0);
  const paid = mockFees.reduce((s,f)=>s+f.paid,0);
  return (
    <div className="space-y-6">
      <h1 className="font-display font-extrabold text-2xl flex items-center gap-2"><BarChart3 className="text-[#0e7490]"/> {t("nav.reports")}</h1>
      <div className="grid md:grid-cols-3 gap-4">
        <div className="rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] p-5"><div className="text-sm text-slate-500">الملفات النشطة</div><div className="text-2xl font-extrabold">{mockCases.filter(c=>["جديد","قيد المتابعة","مؤجل"].includes(c.status)).length}</div></div>
        <div className="rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] p-5"><div className="text-sm text-slate-500">الإيرادات المحصلة</div><div className="text-2xl font-extrabold text-emerald-600">{formatDZD(paid)}</div></div>
        <div className="rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] p-5"><div className="text-sm text-slate-500">المصاريف</div><div className="text-2xl font-extrabold">{formatDZD(mockExpenses.reduce((s,e)=>s+e.amount,0))}</div></div>
      </div>
      <div className="rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] p-5">
        <h3 className="font-bold">القضايا حسب النوع</h3>
        <div className="h-[280px] mt-4" dir="ltr"><ResponsiveContainer width="100%" height="100%"><BarChart data={data}><XAxis dataKey="name"/><YAxis/><Tooltip/><Bar dataKey="count" fill="#0e7490" radius={[8,8,0,0]}/></BarChart></ResponsiveContainer></div>
      </div>
    </div>
  );
}
