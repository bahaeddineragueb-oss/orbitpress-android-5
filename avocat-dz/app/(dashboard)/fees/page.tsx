"use client";
import { useI18n } from "@/components/LanguageProvider";
import { useState, useMemo } from "react";
import { mockCases, mockClients, mockFees, mockExpenses } from "@/lib/data";
import { formatDZD, formatDateShort, uid } from "@/lib/utils";
import { Wallet, Receipt, Plus, TrendingUp, AlertTriangle, Search } from "lucide-react";

export default function FeesPage(){
  const { t } = useI18n();

  const [fees, setFees] = useState(mockFees);
  const [expenses, setExpenses] = useState(mockExpenses);
  const [q,setQ]=useState("");
  const [showFeeForm, setShowFeeForm]=useState(false);
  const [form,setForm]=useState<any>({ caseId: mockCases[0].id, total: 100000, paid: 0 });

  const filteredFees = useMemo(()=> fees.filter(f=>{
    const c = mockCases.find(x=>x.id===f.caseId);
    const cl = mockClients.find(x=>x.id===f.clientId);
    return [c?.title, c?.fileNumber, cl?.fullName].join(" ").toLowerCase().includes(q.toLowerCase());
  }), [fees,q]);

  const total = fees.reduce((s,f)=>s+f.total,0);
  const paid = fees.reduce((s,f)=>s+f.paid,0);
  const remaining = total - paid;
  const expTotal = expenses.reduce((s,e)=>s+e.amount,0);
  const net = paid - expTotal;

  const addFee=()=>{
    if (!form.caseId) return;
    const c = mockCases.find(x=>x.id===form.caseId);
    setFees([{ id: uid(), caseId: form.caseId, clientId: c!.clientId, total: Number(form.total), paid: Number(form.paid), dueDate: form.dueDate, method: form.method }, ...fees]);
    setShowFeeForm(false);
  };

  const pay = (id:string, amount:number)=>{
    setFees(fees.map(f=> f.id===id ? {...f, paid: Math.min(f.total, f.paid+amount)}:f));
  };

  return (
    <div className="space-y-6">
      <div>
        <h1 className="font-display font-extrabold text-2xl flex items-center gap-2"><Wallet className="text-[#0e7490]" /> {t("nav.fees")} والمصاريف</h1>
        <p className="text-sm text-slate-500">تتبع الأتعاب المتفق عليها، المدفوع، المتبقي، الأقساط، والمصاريف — صافي المداخيل</p>
      </div>

      <div className="grid md:grid-cols-4 gap-4">
        <div className="rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] p-5">
          <div className="text-sm text-slate-500">إجمالي الأتعاب</div><div className="text-2xl font-extrabold">{formatDZD(total)}</div>
          <div className="text-xs text-slate-500 mt-1">{fees.length} ملفات</div>
        </div>
        <div className="rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] p-5">
          <div className="text-sm text-slate-500">المحصّل</div><div className="text-2xl font-extrabold text-emerald-600">{formatDZD(paid)}</div>
          <div className="text-xs text-emerald-600 mt-1">{((paid/total)*100).toFixed(0)}% من الإجمالي</div>
        </div>
        <div className="rounded-2xl border bg-amber-50 dark:bg-amber-500/10 border-amber-200 dark:border-amber-500/20 p-5">
          <div className="text-sm text-amber-700">المتبقي ⚠️</div><div className="text-2xl font-extrabold text-amber-700">{formatDZD(remaining)}</div>
          <div className="text-xs text-amber-700 mt-1">{fees.filter(f=>f.paid<f.total).length} عملاء لديهم مستحقات</div>
        </div>
        <div className="rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] p-5">
          <div className="text-sm text-slate-500">صافي المداخيل</div><div className={`text-2xl font-extrabold ${net>=0?"text-emerald-600":"text-red-600"}`}>{formatDZD(net)}</div>
          <div className="text-xs text-slate-500 mt-1">بعد المصاريف {formatDZD(expTotal)}</div>
        </div>
      </div>

      <div className="flex flex-col md:flex-row gap-3">
        <div className="relative flex-1">
          <Search size={18} className="absolute right-3 top-1/2 -translate-y-1/2 text-slate-400" />
          <input value={q} onChange={e=>setQ(e.target.value)} placeholder="بحث: عميل، ملف..." className="w-full pr-10 pl-4 py-3 rounded-xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50]" />
        </div>
        <button onClick={()=>setShowFeeForm(!showFeeForm)} className="px-5 py-3 rounded-xl bg-[#0e7490] text-white font-bold inline-flex items-center gap-2"><Plus size={18}/> أتعاب جديدة</button>
      </div>

      {showFeeForm && (
        <div className="rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] p-5">
          <div className="grid md:grid-cols-4 gap-4">
            <select value={form.caseId} onChange={e=>setForm({...form, caseId:e.target.value})} className="px-4 py-3 rounded-xl border dark:bg-[#070e1f] dark:border-[#1e2e50]">
              {mockCases.map(c=><option key={c.id} value={c.id}>{c.fileNumber} — {c.title}</option>)}
            </select>
            <input type="number" placeholder="الإجمالي" value={form.total} onChange={e=>setForm({...form, total:e.target.value})} className="px-4 py-3 rounded-xl border dark:bg-[#070e1f] dark:border-[#1e2e50]" />
            <input type="number" placeholder="المدفوع" value={form.paid} onChange={e=>setForm({...form, paid:e.target.value})} className="px-4 py-3 rounded-xl border dark:bg-[#070e1f] dark:border-[#1e2e50]" />
            <input type="date" value={form.dueDate||""} onChange={e=>setForm({...form, dueDate:e.target.value})} className="px-4 py-3 rounded-xl border dark:bg-[#070e1f] dark:border-[#1e2e50]" />
            <div className="md:col-span-4 flex gap-3">
              <button onClick={addFee} className="flex-1 py-3 rounded-xl bg-[#0e7490] text-white font-bold">حفظ</button>
              <button onClick={()=>setShowFeeForm(false)} className="flex-1 py-3 rounded-xl border dark:border-[#1e2e50]">إلغاء</button>
            </div>
          </div>
        </div>
      )}

      <div className="grid lg:grid-cols-3 gap-6">
        <div className="lg:col-span-2 rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] overflow-hidden">
          <div className="p-4 border-b dark:border-[#1e2e50] font-bold flex items-center justify-between">
            <span className="flex items-center gap-2"><Wallet size={18} className="text-[#0e7490]"/> جدول الأتعاب</span>
            <span className="text-xs text-slate-500">{filteredFees.length} سجلات</span>
          </div>
          <div className="overflow-auto">
            <table className="w-full text-sm">
              <thead className="bg-slate-50 dark:bg-white/5 text-slate-500">
                <tr><th className="p-3 text-right">الملف/العميل</th><th className="p-3 text-right">الإجمالي</th><th className="p-3 text-right">المدفوع</th><th className="p-3 text-right">المتبقي</th><th className="p-3"></th></tr>
              </thead>
              <tbody className="divide-y dark:divide-[#1e2e50]">
                {filteredFees.map(f=>{
                  const c = mockCases.find(x=>x.id===f.caseId);
                  const cl = mockClients.find(x=>x.id===f.clientId);
                  const perc = Math.round((f.paid/f.total)*100);
                  return (
                    <tr key={f.id} className="hover:bg-slate-50 dark:hover:bg-white/[0.03]">
                      <td className="p-3"><div className="font-bold">{c?.fileNumber} — {cl?.fullName}</div><div className="text-xs text-slate-500 truncate max-w-[240px]">{c?.title}</div></td>
                      <td className="p-3 font-bold">{formatDZD(f.total)}</td>
                      <td className="p-3 text-emerald-600 font-bold">{formatDZD(f.paid)} <div className="w-20 h-1.5 bg-slate-200 dark:bg-white/10 rounded-full mt-1"><div className="h-1.5 bg-emerald-500 rounded-full" style={{width:`${perc}%`}} /></div></td>
                      <td className={`p-3 font-bold ${f.paid<f.total?"text-amber-600":"text-emerald-600"}`}>{formatDZD(f.total-f.paid)}</td>
                      <td className="p-3"><div className="flex gap-1">
                        <button onClick={()=>pay(f.id, 20000)} className="text-xs px-3 py-1 rounded-full bg-[#0e7490] text-white font-bold">+20,000</button>
                        <button onClick={()=>pay(f.id, f.total-f.paid)} className="text-xs px-3 py-1 rounded-full border dark:border-white/10">تسديد كامل</button>
                      </div></td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        </div>

        <div className="space-y-4">
          <div className="rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] p-5">
            <h3 className="font-bold flex items-center gap-2"><Receipt size={18} className="text-amber-600"/> المصاريف</h3>
            <div className="mt-4 space-y-2">
              {expenses.map(e=>{
                const c = e.caseId ? mockCases.find(x=>x.id===e.caseId) : null;
                return (
                  <div key={e.id} className="flex items-center justify-between p-3 rounded-xl bg-slate-50 dark:bg-white/5 border dark:border-white/10">
                    <div><div className="font-bold text-sm">{e.title}</div><div className="text-xs text-slate-500">{e.category} {c?`• ${c.fileNumber}`:""} • {formatDateShort(e.date)}</div></div>
                    <div className="font-bold">{formatDZD(e.amount)}</div>
                  </div>
                );
              })}
            </div>
            <div className="mt-4 p-3 rounded-xl bg-amber-50 dark:bg-amber-500/10 border border-amber-200 dark:border-amber-500/20 flex items-center justify-between">
              <span className="font-bold">المجموع</span><span className="font-extrabold">{formatDZD(expTotal)}</span>
            </div>
          </div>

          <div className="rounded-xl border border-amber-200 bg-amber-50 dark:bg-amber-500/10 dark:border-amber-500/20 p-4 flex gap-3">
            <AlertTriangle className="text-amber-600 shrink-0" size={20} />
            <div className="text-sm">لديك <b>4 عملاء</b> بمستحقات متأخرة. استخدم تذكير واتساب/مكالمة من صفحة العميل.</div>
          </div>
        </div>
      </div>
    </div>
  );
}
