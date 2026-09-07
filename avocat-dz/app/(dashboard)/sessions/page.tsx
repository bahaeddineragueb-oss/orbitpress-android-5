"use client";
import { useState, useMemo } from "react";
import { mockCases, mockHearings } from "@/lib/data";
import { Hearing } from "@/lib/types";
import { formatDateShort, uid } from "@/lib/utils";
import { CalendarDays, Clock, MapPin, Plus, Search, Filter } from "lucide-react";

export default function SessionsPage() {
  const [hearings, setHearings] = useState<Hearing[]>(mockHearings);
  const [q, setQ] = useState("");
  const [view, setView] = useState<"list" | "calendar">("list");
  const [showForm, setShowForm] = useState(false);
  const [form, setForm] = useState<Partial<Hearing>>({ court: "محكمة الرويبة", date: new Date().toISOString().slice(0,16), status: "قادمة" });

  const filtered = useMemo(() => hearings.filter(h => {
    const c = mockCases.find(x=>x.id===h.caseId);
    return [h.court, h.room, c?.title, c?.fileNumber].join(" ").toLowerCase().includes(q.toLowerCase());
  }), [hearings, q]);

  const addHearing = () => {
    if (!form.caseId) return alert("اختر القضية");
    const nh: Hearing = { id: uid(), caseId: form.caseId!, date: new Date(form.date!).toISOString(), court: form.court!, room: form.room, judge: form.judge, status: (form.status as any) || "قادمة" };
    setHearings([nh, ...hearings]);
    setShowForm(false);
  };

  // Calendar helpers - group by date
  const byDate = useMemo(() => {
    const map: Record<string, Hearing[]> = {};
    filtered.forEach(h => {
      const key = new Date(h.date).toISOString().slice(0,10);
      (map[key] ||= []).push(h);
    });
    return map;
  }, [filtered]);

  const dates = Object.keys(byDate).sort();

  return (
    <div className="space-y-6">
      <div className="flex flex-col lg:flex-row lg:items-center justify-between gap-4">
        <div>
          <h1 className="font-display font-extrabold text-2xl flex items-center gap-2"><CalendarDays className="text-[#0e7490]" /> الجلسات والمواعيد</h1>
          <p className="text-sm text-slate-500">تقويم يومي/أسبوعي/شهري — جلسات اليوم والقادمة مع القاعة والقاضي</p>
        </div>
        <div className="flex gap-2">
          <div className="flex rounded-xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] overflow-hidden p-1">
            <button onClick={() => setView("list")} className={`px-4 py-1.5 rounded-lg text-sm font-bold ${view==="list" ? "bg-[#0e7490] text-white" : ""}`}>قائمة</button>
            <button onClick={() => setView("calendar")} className={`px-4 py-1.5 rounded-lg text-sm font-bold ${view==="calendar" ? "bg-[#0e7490] text-white" : ""}`}>تقويم</button>
          </div>
          <button onClick={() => setShowForm(!showForm)} className="px-5 py-2.5 rounded-xl bg-[#0e7490] text-white font-bold inline-flex items-center gap-2"><Plus size={18} /> جلسة جديدة</button>
        </div>
      </div>

      {showForm && (
        <div className="rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] p-5">
          <h3 className="font-bold mb-4">إضافة جلسة</h3>
          <div className="grid md:grid-cols-3 gap-4">
            <select value={form.caseId || ""} onChange={e=>setForm({...form, caseId:e.target.value})} className="px-4 py-3 rounded-xl border dark:bg-[#070e1f] dark:border-[#1e2e50]">
              <option value="">— اختر القضية —</option>
              {mockCases.map(c=><option key={c.id} value={c.id}>{c.fileNumber} — {c.title}</option>)}
            </select>
            <input type="datetime-local" value={form.date || ""} onChange={e=>setForm({...form, date:e.target.value})} className="px-4 py-3 rounded-xl border dark:bg-[#070e1f] dark:border-[#1e2e50]" />
            <input placeholder="المحكمة" value={form.court || ""} onChange={e=>setForm({...form, court:e.target.value})} className="px-4 py-3 rounded-xl border dark:bg-[#070e1f] dark:border-[#1e2e50]" />
            <input placeholder="القاعة (مثال: قاعة 03)" value={form.room || ""} onChange={e=>setForm({...form, room:e.target.value})} className="px-4 py-3 rounded-xl border dark:bg-[#070e1f] dark:border-[#1e2e50]" />
            <input placeholder="القاضي/الغرفة" value={form.judge || ""} onChange={e=>setForm({...form, judge:e.target.value})} className="px-4 py-3 rounded-xl border dark:bg-[#070e1f] dark:border-[#1e2e50]" />
            <select value={form.status} onChange={e=>setForm({...form, status:e.target.value as any})} className="px-4 py-3 rounded-xl border dark:bg-[#070e1f] dark:border-[#1e2e50]">
              <option>قادمة</option><option>تمت</option><option>مؤجلة</option><option>ملغاة</option>
            </select>
            <div className="md:col-span-3 flex gap-3">
              <button onClick={addHearing} className="flex-1 py-3 rounded-xl bg-[#0e7490] text-white font-bold">حفظ</button>
              <button onClick={()=>setShowForm(false)} className="flex-1 py-3 rounded-xl border dark:border-[#1e2e50]">إلغاء</button>
            </div>
          </div>
        </div>
      )}

      <div className="relative max-w-xl">
        <Search size={18} className="absolute right-3 top-1/2 -translate-y-1/2 text-slate-400" />
        <input value={q} onChange={e=>setQ(e.target.value)} placeholder="بحث: محكمة، قاعة، قضية..." className="w-full pr-10 pl-4 py-3 rounded-xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50]" />
      </div>

      {view==="list" ? (
        <div className="rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] overflow-hidden divide-y dark:divide-[#1e2e50]">
          {filtered.map(h=>{
            const c = mockCases.find(x=>x.id===h.caseId);
            const isToday = new Date(h.date).toDateString() === new Date().toDateString();
            return (
              <div key={h.id} className={`p-5 flex flex-col md:flex-row md:items-center gap-4 ${isToday ? "bg-red-50/60 dark:bg-red-500/5" : ""}`}>
                <div className={`w-full md:w-20 h-20 rounded-xl grid place-items-center text-white font-bold shrink-0 ${isToday?"bg-red-500":"bg-[#0e7490]"}`}>
                  <div className="text-center leading-none">
                    <div className="text-xs">{new Date(h.date).toLocaleDateString("ar-DZ", {weekday:"short"})}</div>
                    <div className="text-2xl">{new Date(h.date).getDate()}</div>
                    <div className="text-[11px]">{new Date(h.date).toLocaleDateString("ar-DZ", {month:"short"})}</div>
                  </div>
                </div>
                <div className="flex-1 min-w-0">
                  <div className="font-bold">{c?.title}</div>
                  <div className="text-sm text-slate-500">{c?.fileNumber} • {c?.caseNumber}</div>
                  <div className="mt-2 flex flex-wrap gap-3 text-sm">
                    <span className="inline-flex items-center gap-1"><MapPin size={14}/> {h.court} {h.room?`• ${h.room}`:""}</span>
                    <span className="inline-flex items-center gap-1"><Clock size={14}/> {new Date(h.date).toLocaleString("ar-DZ", {hour:"2-digit", minute:"2-digit"})} </span>
                    {h.judge && <span>• {h.judge}</span>}
                  </div>
                  {h.decision && <div className="mt-2 text-sm p-2 rounded-lg bg-amber-50 dark:bg-amber-500/10 border border-amber-100 dark:border-amber-500/20">القرار: {h.decision}</div>}
                </div>
                <span className={`self-start md:self-center text-xs px-3 py-1 rounded-full font-bold border ${h.status==="قادمة"?"bg-emerald-50 text-emerald-700 border-emerald-200":h.status==="مؤجلة"?"bg-amber-50 text-amber-700 border-amber-200":"bg-slate-100 dark:bg-white/10"}`}>{h.status}</span>
              </div>
            );
          })}
          {filtered.length===0 && <div className="p-12 text-center text-slate-500">لا توجد جلسات</div>}
        </div>
      ) : (
        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-4">
          {dates.map(d=>{
            const list = byDate[d];
            return (
              <div key={d} className="rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] p-4">
                <div className="font-bold flex items-center justify-between">
                  <span>{formatDateShort(d)}</span>
                  <span className="text-xs px-2 py-1 rounded-full bg-[#0e7490] text-white">{list.length} جلسات</span>
                </div>
                <div className="mt-3 space-y-2">
                  {list.map(h=>{
                    const c = mockCases.find(x=>x.id===h.caseId);
                    return (
                      <div key={h.id} className="p-3 rounded-xl bg-slate-50 dark:bg-white/5 border dark:border-white/10">
                        <div className="font-bold text-sm truncate">{c?.title}</div>
                        <div className="text-xs text-slate-500">{h.court} {h.room?`• ${h.room}`:""} • {new Date(h.date).toLocaleTimeString("ar-DZ",{hour:"2-digit",minute:"2-digit"})}</div>
                      </div>
                    );
                  })}
                </div>
              </div>
            );
          })}
          {dates.length===0 && <div className="col-span-full p-12 text-center text-slate-500">لا توجد جلسات</div>}
        </div>
      )}
    </div>
  );
}
