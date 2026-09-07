"use client";
import { useState, useMemo } from "react";
import { mockCases, mockClients, mockHearings, mockDocuments, mockFees } from "@/lib/data";
import { CourtCase, CaseStatus } from "@/lib/types";
import { formatDateShort, formatDZD, uid } from "@/lib/utils";
import { Scale, Search, Plus, MapPin, Calendar, User, Clock, FileText, Gavel, Filter, TrendingUp } from "lucide-react";

const statuses: CaseStatus[] = ["جديد", "قيد المتابعة", "مؤجل", "محكوم", "استئناف", "طعن", "مغلق", "مؤرشف"];

export default function CasesPage() {
  const [cases, setCases] = useState<CourtCase[]>(mockCases);
  const [q, setQ] = useState("");
  const [statusFilter, setStatusFilter] = useState<string>("الكل");
  const [selected, setSelected] = useState<string | null>(null);
  const [showForm, setShowForm] = useState(false);
  const [form, setForm] = useState<Partial<CourtCase>>({ title: "", court: "محكمة الرويبة", category: "مدني", status: "جديد", section: "مدني", clientRole: "مدعي", council: "مجلس قضاء الجزائر", opponent: "" });

  const filtered = useMemo(() => cases.filter(c => {
    const matchQ = [c.title, c.fileNumber, c.caseNumber, c.court, c.opponent].join(" ").toLowerCase().includes(q.toLowerCase());
    const matchStatus = statusFilter === "الكل" || c.status === statusFilter;
    return matchQ && matchStatus;
  }), [cases, q, statusFilter]);

  const addCase = () => {
    if (!form.title || !form.fileNumber) return alert("عنوان القضية ورقم الملف مطلوبان");
    const newCase: CourtCase = {
      id: uid(),
      fileNumber: form.fileNumber!,
      caseNumber: form.caseNumber || form.fileNumber!,
      clientId: mockClients[0].id,
      title: form.title!,
      court: form.court!,
      council: form.council!,
      section: form.section!,
      category: form.category as any,
      clientRole: form.clientRole as any,
      opponent: form.opponent || "غير محدد",
      status: (form.status as CaseStatus) || "جديد",
      assignedLawyer: "أ. بهاء الدين",
      openDate: new Date().toISOString(),
    };
    setCases([newCase, ...cases]);
    setShowForm(false);
    setForm({ title: "", court: "محكمة الرويبة", category: "مدني", status: "جديد", section: "مدني", clientRole: "مدعي", council: "مجلس قضاء الجزائر", opponent: "" });
  };

  const selectedCase = cases.find(c => c.id === selected);

  return (
    <div className="space-y-6">
      <div className="flex flex-col lg:flex-row lg:items-center justify-between gap-4">
        <div>
          <h1 className="font-display font-extrabold text-2xl flex items-center gap-2"><Scale className="text-[#0e7490]" /> القضايا والملفات</h1>
          <p className="text-sm text-slate-500">كل ملف يعرض Timeline كامل من الافتتاح إلى الحكم والأرشيف</p>
        </div>
        <button onClick={() => setShowForm(!showForm)} className="px-5 py-3 rounded-xl bg-[#0e7490] text-white font-bold inline-flex items-center gap-2"><Plus size={18} /> ملف جديد</button>
      </div>

      {showForm && (
        <div className="rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] p-5">
          <h3 className="font-bold mb-4">فتح ملف جديد</h3>
          <div className="grid md:grid-cols-3 gap-4">
            <input placeholder="رقم الملف * (مثال: 2026/500)" value={form.fileNumber || ""} onChange={e => setForm({ ...form, fileNumber: e.target.value })} className="px-4 py-3 rounded-xl border dark:bg-[#070e1f] dark:border-[#1e2e50]" />
            <input placeholder="رقم القضية" value={form.caseNumber || ""} onChange={e => setForm({ ...form, caseNumber: e.target.value })} className="px-4 py-3 rounded-xl border dark:bg-[#070e1f] dark:border-[#1e2e50]" />
            <input placeholder="عنوان القضية *" value={form.title || ""} onChange={e => setForm({ ...form, title: e.target.value })} className="px-4 py-3 rounded-xl border dark:bg-[#070e1f] dark:border-[#1e2e50] md:col-span-1" />
            <input placeholder="المحكمة" value={form.court || ""} onChange={e => setForm({ ...form, court: e.target.value })} className="px-4 py-3 rounded-xl border dark:bg-[#070e1f] dark:border-[#1e2e50]" />
            <input placeholder="القسم" value={form.section || ""} onChange={e => setForm({ ...form, section: e.target.value })} className="px-4 py-3 rounded-xl border dark:bg-[#070e1f] dark:border-[#1e2e50]" />
            <input placeholder="الخصم" value={form.opponent || ""} onChange={e => setForm({ ...form, opponent: e.target.value })} className="px-4 py-3 rounded-xl border dark:bg-[#070e1f] dark:border-[#1e2e50]" />
            <select value={form.category} onChange={e => setForm({ ...form, category: e.target.value as any })} className="px-4 py-3 rounded-xl border dark:bg-[#070e1f] dark:border-[#1e2e50]">
              <option>مدني</option><option>جزائي</option><option>إداري</option><option>تجاري</option><option>أسرة</option><option>عقاري</option><option>اجتماعي</option>
            </select>
            <select value={form.status} onChange={e => setForm({ ...form, status: e.target.value as any })} className="px-4 py-3 rounded-xl border dark:bg-[#070e1f] dark:border-[#1e2e50]">
              {statuses.map(s => <option key={s}>{s}</option>)}
            </select>
            <div className="flex gap-3 md:col-span-3">
              <button onClick={addCase} className="flex-1 py-3 rounded-xl bg-[#0e7490] text-white font-bold">حفظ الملف</button>
              <button onClick={() => setShowForm(false)} className="flex-1 py-3 rounded-xl border dark:border-[#1e2e50]">إلغاء</button>
            </div>
          </div>
        </div>
      )}

      <div className="flex flex-col md:flex-row gap-3">
        <div className="relative flex-1">
          <Search size={18} className="absolute right-3 top-1/2 -translate-y-1/2 text-slate-400" />
          <input value={q} onChange={e => setQ(e.target.value)} placeholder="بحث: رقم الملف، عنوان، محكمة، خصم..." className="w-full pr-10 pl-4 py-3 rounded-xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50]" />
        </div>
        <div className="flex gap-2 overflow-auto">
          <button onClick={() => setStatusFilter("الكل")} className={`px-4 py-2 rounded-xl border font-bold whitespace-nowrap ${statusFilter === "الكل" ? "bg-[#0e7490] text-white border-[#0e7490]" : "bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50]"}`}>الكل</button>
          {statuses.map(s => (
            <button key={s} onClick={() => setStatusFilter(s)} className={`px-4 py-2 rounded-xl border font-bold whitespace-nowrap ${statusFilter === s ? "bg-[#0e7490] text-white border-[#0e7490]" : "bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50]"}`}>{s}</button>
          ))}
        </div>
      </div>

      <div className="grid lg:grid-cols-3 gap-6">
        <div className="lg:col-span-2 space-y-3">
          {filtered.map(c => {
            const client = mockClients.find(x => x.id === c.clientId);
            const fee = mockFees.find(f => f.caseId === c.id);
            const active = selected === c.id;
            return (
              <div key={c.id} onClick={() => setSelected(c.id)} className={`rounded-2xl border p-5 cursor-pointer transition ${active ? "bg-[#0e7490]/5 border-[#0e7490] shadow" : "bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] hover:shadow-card"}`}>
                <div className="flex items-start justify-between gap-4">
                  <div className="flex-1 min-w-0">
                    <div className="flex items-center gap-2 flex-wrap">
                      <span className="font-mono text-xs px-2 py-1 rounded-full bg-slate-900 text-white dark:bg-white dark:text-slate-900">{c.fileNumber}</span>
                      <span className="text-xs text-slate-500">{c.caseNumber}</span>
                      <span className={`text-xs px-2 py-1 rounded-full font-bold border ${c.status === "قيد المتابعة" ? "bg-blue-50 text-blue-700 border-blue-200" : c.status === "مؤجل" ? "bg-amber-50 text-amber-700 border-amber-200" : c.status === "محكوم" ? "bg-emerald-50 text-emerald-700 border-emerald-200" : "bg-slate-100 dark:bg-white/10"}`}>{c.status}</span>
                    </div>
                    <div className="font-bold mt-2 leading-tight">{c.title}</div>
                    <div className="text-sm text-slate-500 mt-1 flex flex-wrap gap-3">
                      <span className="inline-flex items-center gap-1"><MapPin size={14} /> {c.court} • {c.section}</span>
                      <span className="inline-flex items-center gap-1"><User size={14} /> {client?.fullName} ({c.clientRole})</span>
                      <span className="inline-flex items-center gap-1"><Gavel size={14} /> ضد {c.opponent}</span>
                    </div>
                    <div className="mt-3 flex flex-wrap gap-2 text-xs">
                      <span className="px-2 py-1 rounded-full bg-slate-100 dark:bg-white/10">{c.category}</span>
                      <span className="px-2 py-1 rounded-full bg-slate-100 dark:bg-white/10 inline-flex items-center gap-1"><Calendar size={12} /> {formatDateShort(c.openDate)}</span>
                      {fee && <span className={`px-2 py-1 rounded-full font-bold ${fee.paid < fee.total ? "bg-amber-100 text-amber-800" : "bg-emerald-100 text-emerald-800"}`}>{formatDZD(fee.total - fee.paid)} متبقي</span>}
                    </div>
                  </div>
                  <div className="hidden md:block text-xs text-slate-400 whitespace-nowrap">{c.assignedLawyer}</div>
                </div>
              </div>
            );
          })}
          {filtered.length === 0 && <div className="text-center py-12 text-slate-500">لا توجد ملفات</div>}
        </div>

        {/* Detail / Timeline */}
        <div className="lg:col-span-1">
          <div className="sticky top-[80px] rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] overflow-hidden">
            {!selectedCase ? (
              <div className="p-8 text-center">
                <div className="w-16 h-16 rounded-2xl bg-slate-100 dark:bg-white/5 grid place-items-center mx-auto"><FileText size={28} className="text-slate-400" /></div>
                <div className="font-bold mt-4">اختر ملفاً لعرض Timeline</div>
                <div className="text-sm text-slate-500 mt-1">سترى كل تاريخ القضية من فتح الملف إلى آخر إجراء، مع الجلسات والوثائق والأتعاب</div>
              </div>
            ) : (
              <div>
                <div className="p-5 border-b dark:border-[#1e2e50] bg-gradient-to-br from-[#0e7490] to-[#063544] text-white">
                  <div className="text-xs opacity-80">{selectedCase.fileNumber} • {selectedCase.caseNumber}</div>
                  <div className="font-bold leading-tight mt-1">{selectedCase.title}</div>
                  <div className="text-xs opacity-80 mt-1">{selectedCase.court} • {selectedCase.section} • {selectedCase.category}</div>
                </div>
                <div className="p-5 space-y-4">
                  <div className="grid grid-cols-2 gap-3 text-xs">
                    <div className="p-3 rounded-xl bg-slate-50 dark:bg-white/5 border dark:border-white/10"><div className="text-slate-500">الموكل</div><div className="font-bold">{mockClients.find(c=>c.id===selectedCase.clientId)?.fullName} ({selectedCase.clientRole})</div></div>
                    <div className="p-3 rounded-xl bg-slate-50 dark:bg-white/5 border dark:border-white/10"><div className="text-slate-500">الخصم</div><div className="font-bold">{selectedCase.opponent}</div></div>
                  </div>

                  <h4 className="font-bold flex items-center gap-2"><TrendingUp size={16} className="text-[#0e7490]" /> Timeline القضية</h4>
                  <div className="relative pl-4">
                    <div className="absolute right-[9px] top-0 bottom-0 w-0.5 bg-slate-200 dark:bg-white/10" />
                    <div className="space-y-4">
                      <div className="relative flex gap-3">
                        <span className="w-5 h-5 rounded-full bg-[#0e7490] border-4 border-white dark:border-[#0f1b33] shrink-0 mt-1" />
                        <div className="flex-1 p-3 rounded-xl border dark:border-white/10 bg-slate-50 dark:bg-white/5">
                          <div className="text-xs text-slate-500">{formatDateShort(selectedCase.openDate)}</div>
                          <div className="font-bold text-sm">فتح الملف</div>
                          <div className="text-xs text-slate-600 dark:text-slate-400">{selectedCase.notes || "تم فتح الملف وإيداع الوثائق الأولية"}</div>
                        </div>
                      </div>
                      {mockHearings.filter(h=>h.caseId===selectedCase.id).map(h=>(
                        <div key={h.id} className="relative flex gap-3">
                          <span className={`w-5 h-5 rounded-full border-4 border-white dark:border-[#0f1b33] shrink-0 mt-1 ${h.status==="قادمة"?"bg-amber-500":h.status==="تمت"?"bg-emerald-500":"bg-slate-400"}`} />
                          <div className="flex-1 p-3 rounded-xl border dark:border-white/10 bg-white dark:bg-[#070e1f]">
                            <div className="text-xs text-slate-500">{formatDateShort(h.date)} • {h.court} {h.room?`• ${h.room}`:""}</div>
                            <div className="font-bold text-sm">جلسة — {h.status}</div>
                            {h.decision && <div className="text-xs mt-1">القرار: {h.decision}</div>}
                            {h.nextDate && <div className="text-xs text-[#0e7490]">القادمة: {formatDateShort(h.nextDate)}</div>}
                          </div>
                        </div>
                      ))}
                      {mockDocuments.filter(d=>d.caseId===selectedCase.id).map(d=>(
                        <div key={d.id} className="relative flex gap-3">
                          <span className="w-5 h-5 rounded-full bg-violet-500 border-4 border-white dark:border-[#0f1b33] shrink-0 mt-1" />
                          <div className="flex-1 p-3 rounded-xl border dark:border-white/10 bg-violet-50/50 dark:bg-violet-500/10">
                            <div className="text-xs text-slate-500">{formatDateShort(d.date)} • {d.category}</div>
                            <div className="font-bold text-sm">{d.title}</div>
                            <div className="text-xs">{d.fileName} • {d.uploadedBy}</div>
                          </div>
                        </div>
                      ))}
                      <div className="relative flex gap-3">
                        <span className="w-5 h-5 rounded-full bg-slate-800 dark:bg-white border-4 border-white dark:border-[#0f1b33] shrink-0 mt-1" />
                        <div className="flex-1 p-3 rounded-xl border-2 border-dashed dark:border-white/20">
                          <div className="font-bold text-sm">استراتيجية الدفاع</div>
                          <div className="text-xs text-slate-600 dark:text-slate-400 mt-1">{selectedCase.strategy || "—"}</div>
                        </div>
                      </div>
                    </div>
                  </div>

                  <div className="flex gap-2 pt-2">
                    <button className="flex-1 py-2.5 rounded-xl bg-[#0e7490] text-white font-bold text-sm">إضافة إجراء</button>
                    <button className="flex-1 py-2.5 rounded-xl border dark:border-[#1e2e50] font-bold text-sm">طباعة الملخص</button>
                  </div>
                </div>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
