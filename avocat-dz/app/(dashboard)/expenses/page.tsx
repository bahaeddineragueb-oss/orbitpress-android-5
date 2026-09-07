"use client";
import { mockExpenses, mockCases } from "@/lib/data";
import { formatDZD, formatDateShort } from "@/lib/utils";
import { Receipt } from "lucide-react";
export default function ExpensesPage(){
  const total = mockExpenses.reduce((s,e)=>s+e.amount,0);
  return (
    <div className="space-y-6">
      <div>
        <h1 className="font-display font-extrabold text-2xl flex items-center gap-2"><Receipt className="text-[#0e7490]"/> المصاريف</h1>
        <p className="text-sm text-slate-500">تبليغ، محضر قضائي، تنقل، طوابع، نسخ، خبرة، بريد...</p>
      </div>
      <div className="rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] p-5">
        <div className="flex items-center justify-between"><span className="font-bold">إجمالي المصاريف</span><span className="text-2xl font-extrabold">{formatDZD(total)}</span></div>
        <div className="mt-4 space-y-2">
          {mockExpenses.map(e=>{
            const c = e.caseId? mockCases.find(x=>x.id===e.caseId): null;
            return <div key={e.id} className="flex items-center justify-between p-3 rounded-xl bg-slate-50 dark:bg-white/5 border dark:border-white/10"><div><div className="font-bold text-sm">{e.title}</div><div className="text-xs text-slate-500">{e.category} {c?`• ${c.fileNumber}`:""} • {formatDateShort(e.date)}</div></div><div className="font-bold">{formatDZD(e.amount)}</div></div>
          })}
        </div>
      </div>
    </div>
  );
}
