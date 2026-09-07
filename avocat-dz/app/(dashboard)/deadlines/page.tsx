"use client";
import { useI18n } from "@/components/LanguageProvider";
import { useState, useMemo } from "react";
import { mockDeadlines, mockCases } from "@/lib/data";
import { Deadline } from "@/lib/types";
import { formatDateShort, daysUntil, uid } from "@/lib/utils";
import { AlarmClock, Plus, Check, AlertTriangle, Clock, Search } from "lucide-react";

export default function DeadlinesPage() {
  const { t } = useI18n();

  const [deadlines, setDeadlines] = useState<Deadline[]>(mockDeadlines);
  const [filter, setFilter] = useState<"الكل" | "عاجل" | "هام" | "عادي">("الكل");
  const [q, setQ] = useState("");
  const [showForm, setShowForm] = useState(false);
  const [form, setForm] = useState<Partial<Deadline>>({ title: "", type: "أخرى", priority: "عادي", dueDate: new Date().toISOString().slice(0,10) });

  const filtered = useMemo(() => deadlines.filter(d => {
    const c = mockCases.find(x=>x.id===d.caseId);
    const matchQ = [d.title, c?.title, c?.fileNumber].join(" ").toLowerCase().includes(q.toLowerCase());
    const matchP = filter==="الكل" || d.priority===filter;
    return matchQ && matchP;
  }).sort((a,b)=> new Date(a.dueDate).getTime() - new Date(b.dueDate).getTime()), [deadlines, q, filter]);

  const toggleDone = (id:string) => setDeadlines(deadlines.map(d=> d.id===id ? {...d, done:!d.done}:d));
  const add = () => {
    if (!form.title || !form.caseId) return alert("العنوان والقضية مطلوبان");
    setDeadlines([{ id: uid(), caseId: form.caseId!, title: form.title!, type: form.type as any, priority: form.priority as any, dueDate: new Date(form.dueDate!).toISOString(), done:false }, ...deadlines]);
    setShowForm(false);
  };

  return (
    <div className="space-y-6">
      <div className="rounded-2xl bg-gradient-to-br from-red-500 to-orange-500 text-white p-6">
        <h1 className="font-display font-extrabold text-2xl flex items-center gap-2"><AlarmClock /> {t("nav.deadlines")} 🚨</h1>
        <p className="text-white/90 mt-1">تنبيهات ذكية للآجال القانونية — بقي 10 أيام على الاستئناف، 3 أيام على الجلسة...</p>
        <div className="mt-4 grid grid-cols-3 gap-3 text-center">
          <div className="rounded-xl bg-white/15 border border-white/20 p-3"><div className="text-2xl font-extrabold">{deadlines.filter(d=>!d.done).length}</div><div className="text-xs">آجال نشطة</div></div>
          <div className="rounded-xl bg-white/15 border border-white/20 p-3"><div className="text-2xl font-extrabold">{deadlines.filter(d=>!d.done && daysUntil(d.dueDate)<=3).length}</div><div className="text-xs">عاجلة ≤3 أيام</div></div>
          <div className="rounded-xl bg-white/15 border border-white/20 p-3"><div className="text-2xl font-extrabold">{deadlines.filter(d=>d.done).length}</div><div className="text-xs">منجزة</div></div>
        </div>
      </div>

      <div className="flex flex-col md:flex-row gap-3">
        <div className="relative flex-1">
          <Search size={18} className="absolute right-3 top-1/2 -translate-y-1/2 text-slate-400" />
          <input value={q} onChange={e=>setQ(e.target.value)} placeholder="بحث: عنوان الأجل، قضية..." className="w-full pr-10 pl-4 py-3 rounded-xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50]" />
        </div>
        <div className="flex gap-2">
          {["الكل","عاجل","هام","عادي"].map(f=>(
            <button key={f} onClick={()=>setFilter(f as any)} className={`px-4 py-2 rounded-xl border font-bold ${filter===f?"bg-[#0e7490] text-white border-[#0e7490]":"bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50]"}`}>{f}</button>
          ))}
          <button onClick={()=>setShowForm(!showForm)} className="px-5 py-2.5 rounded-xl bg-[#0e7490] text-white font-bold inline-flex items-center gap-2"><Plus size={18}/> أجل جديد</button>
        </div>
      </div>

      {showForm && (
        <div className="rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] p-5">
          <div className="grid md:grid-cols-3 gap-4">
            <input placeholder="عنوان الأجل *" value={form.title||""} onChange={e=>setForm({...form, title:e.target.value})} className="px-4 py-3 rounded-xl border dark:bg-[#070e1f] dark:border-[#1e2e50]" />
            <select value={form.caseId||""} onChange={e=>setForm({...form, caseId:e.target.value})} className="px-4 py-3 rounded-xl border dark:bg-[#070e1f] dark:border-[#1e2e50]">
              <option value="">— القضية —</option>
              {mockCases.map(c=><option key={c.id} value={c.id}>{c.fileNumber} — {c.title}</option>)}
            </select>
            <input type="date" value={form.dueDate||""} onChange={e=>setForm({...form, dueDate:e.target.value})} className="px-4 py-3 rounded-xl border dark:bg-[#070e1f] dark:border-[#1e2e50]" />
            <select value={form.type} onChange={e=>setForm({...form, type:e.target.value as any})} className="px-4 py-3 rounded-xl border dark:bg-[#070e1f] dark:border-[#1e2e50]"><option>استئناف</option><option>طعن</option><option>تبليغ</option><option>خبرة</option><option>دفع</option><option>أخرى</option></select>
            <select value={form.priority} onChange={e=>setForm({...form, priority:e.target.value as any})} className="px-4 py-3 rounded-xl border dark:bg-[#070e1f] dark:border-[#1e2e50]"><option>عادي</option><option>هام</option><option>عاجل</option></select>
            <div className="flex gap-3">
              <button onClick={add} className="flex-1 py-3 rounded-xl bg-[#0e7490] text-white font-bold">حفظ</button>
              <button onClick={()=>setShowForm(false)} className="flex-1 py-3 rounded-xl border dark:border-[#1e2e50]">إلغاء</button>
            </div>
          </div>
        </div>
      )}

      <div className="grid md:grid-cols-2 gap-4">
        {filtered.map(d=>{
          const c = mockCases.find(x=>x.id===d.caseId);
          const left = daysUntil(d.dueDate);
          const urgent = left <= 3 && !d.done;
          return (
            <div key={d.id} className={`rounded-2xl border p-5 ${d.done ? "bg-slate-50 dark:bg-white/5 opacity-60" : urgent ? "bg-red-50 dark:bg-red-500/10 border-red-200 dark:border-red-500/20" : "bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50]"}`}>
              <div className="flex items-start justify-between gap-3">
                <div className="flex-1">
                  <div className="flex items-center gap-2">
                    <span className={`text-xs px-2 py-1 rounded-full font-bold ${d.priority==="عاجل"?"bg-red-500 text-white":d.priority==="هام"?"bg-amber-500 text-white":"bg-slate-200 dark:bg-white/10"}`}>{d.priority}</span>
                    <span className="text-xs px-2 py-1 rounded-full bg-slate-100 dark:bg-white/10">{d.type}</span>
                    {urgent && <span className="text-xs px-2 py-1 rounded-full bg-red-500 text-white animate-pulse flex items-center gap-1"><AlertTriangle size={12}/> عاجل جداً</span>}
                  </div>
                  <div className={`font-bold mt-2 ${d.done?"line-through":""}`}>{d.title}</div>
                  <div className="text-sm text-slate-500 mt-1">{c?.fileNumber} • {c?.title}</div>
                  <div className="text-sm mt-2 inline-flex items-center gap-1"><Clock size={14}/> {formatDateShort(d.dueDate)} • <span className={left<=3?"text-red-600 font-bold": left<=10?"text-amber-600 font-bold":""}>{left>0?`بقي ${left} أيام`:`متأخر ${Math.abs(left)} أيام`}</span></div>
                </div>
                <button onClick={()=>toggleDone(d.id)} className={`w-10 h-10 rounded-xl grid place-items-center border shrink-0 ${d.done?"bg-emerald-500 text-white border-emerald-500":"bg-white dark:bg-[#070e1f] hover:bg-slate-50"}`}>
                  <Check size={18} />
                </button>
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
}
