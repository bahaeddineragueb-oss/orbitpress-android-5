"use client";
import { useI18n } from "@/components/LanguageProvider";
import { useState, useMemo } from "react";
import { mockClients, mockCases } from "@/lib/data";
import { Client } from "@/lib/types";
import { formatDateShort, uid } from "@/lib/utils";
import { Search, Plus, Phone, Mail, MapPin, Trash2, Pencil, Users, FileText } from "lucide-react";

export default function ClientsPage() {
  const { t } = useI18n();

  const [clients, setClients] = useState<Client[]>(mockClients);
  const [q, setQ] = useState("");
  const [showForm, setShowForm] = useState(false);
  const [form, setForm] = useState<Partial<Client>>({ fullName: "", phone: "", email: "", address: "", profession: "" });
  const [selected, setSelected] = useState<string | null>(null);

  const filtered = useMemo(() => clients.filter(c => [c.fullName, c.phone, c.email, c.address].join(" ").toLowerCase().includes(q.toLowerCase())), [clients, q]);

  const addClient = () => {
    if (!form.fullName || !form.phone) return alert("الاسم والهاتف مطلوبان");
    setClients([{ id: uid(), fullName: form.fullName!, phone: form.phone!, email: form.email, address: form.address, profession: form.profession, createdAt: new Date().toISOString(), notes: "" }, ...clients]);
    setForm({ fullName: "", phone: "", email: "", address: "", profession: "" });
    setShowForm(false);
  };

  const remove = (id: string) => {
    if (!confirm("حذف العميل؟ سيبقى أرشيف قضاياه.")) return;
    setClients(clients.filter(c => c.id !== id));
  };

  return (
    <div className="space-y-6">
      <div className="flex flex-col lg:flex-row lg:items-center justify-between gap-4">
        <div>
          <h1 className="font-display font-extrabold text-2xl flex items-center gap-2"><Users className="text-[#0e7490]" /> {t("nav.clients")}</h1>
          <p className="text-sm text-slate-500">ملف كامل لكل عميل مع كل قضاياه، سجل التواصل والوثائق</p>
        </div>
        <button onClick={() => setShowForm(!showForm)} className="px-5 py-3 rounded-xl bg-[#0e7490] text-white font-bold inline-flex items-center gap-2"><Plus size={18} /> عميل جديد</button>
      </div>

      {showForm && (
        <div className="rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] p-5">
          <h3 className="font-bold mb-4">إضافة عميل جديد</h3>
          <div className="grid md:grid-cols-2 gap-4">
            <input placeholder="الاسم الكامل *" value={form.fullName || ""} onChange={e => setForm({ ...form, fullName: e.target.value })} className="px-4 py-3 rounded-xl border dark:bg-[#070e1f] dark:border-[#1e2e50]" />
            <input placeholder="الهاتف *" value={form.phone || ""} onChange={e => setForm({ ...form, phone: e.target.value })} className="px-4 py-3 rounded-xl border dark:bg-[#070e1f] dark:border-[#1e2e50]" />
            <input placeholder="البريد (اختياري)" value={form.email || ""} onChange={e => setForm({ ...form, email: e.target.value })} className="px-4 py-3 rounded-xl border dark:bg-[#070e1f] dark:border-[#1e2e50]" />
            <input placeholder="العنوان" value={form.address || ""} onChange={e => setForm({ ...form, address: e.target.value })} className="px-4 py-3 rounded-xl border dark:bg-[#070e1f] dark:border-[#1e2e50]" />
            <input placeholder="المهنة" value={form.profession || ""} onChange={e => setForm({ ...form, profession: e.target.value })} className="px-4 py-3 rounded-xl border dark:bg-[#070e1f] dark:border-[#1e2e50]" />
            <div className="flex gap-3">
              <button onClick={addClient} className="flex-1 py-3 rounded-xl bg-[#0e7490] text-white font-bold">حفظ</button>
              <button onClick={() => setShowForm(false)} className="flex-1 py-3 rounded-xl border dark:border-[#1e2e50]">إلغاء</button>
            </div>
          </div>
        </div>
      )}

      <div className="relative max-w-xl">
        <Search size={18} className="absolute right-3 top-1/2 -translate-y-1/2 text-slate-400" />
        <input value={q} onChange={e => setQ(e.target.value)} placeholder="بحث: الاسم، الهاتف، العنوان..." className="w-full pr-10 pl-4 py-3 rounded-xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50]" />
      </div>

      <div className="grid md:grid-cols-2 xl:grid-cols-3 gap-4">
        {filtered.map(c => {
          const cases = mockCases.filter(k => k.clientId === c.id);
          const expanded = selected === c.id;
          return (
            <div key={c.id} className="rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] p-5 hover:shadow-card transition">
              <div className="flex items-start justify-between">
                <div className="flex gap-3">
                  <img src={`https://i.pravatar.cc/100?u=${c.id}`} alt={c.fullName} className="w-12 h-12 rounded-xl object-cover" />
                  <div>
                    <div className="font-bold">{c.fullName}</div>
                    <div className="text-xs text-slate-500 flex items-center gap-1"><Phone size={12} /> {c.phone}</div>
                    {c.email && <div className="text-xs text-slate-500 flex items-center gap-1"><Mail size={12} /> {c.email}</div>}
                  </div>
                </div>
                <div className="flex gap-1">
                  <button onClick={() => setSelected(expanded ? null : c.id)} className="w-8 h-8 grid place-items-center rounded-lg hover:bg-slate-100 dark:hover:bg-white/10"><Pencil size={14} /></button>
                  <button onClick={() => remove(c.id)} className="w-8 h-8 grid place-items-center rounded-lg hover:bg-red-50 text-red-600"><Trash2 size={14} /></button>
                </div>
              </div>

              <div className="mt-3 flex flex-wrap gap-2 text-xs">
                {c.profession && <span className="px-2 py-1 rounded-full bg-slate-100 dark:bg-white/10">{c.profession}</span>}
                {c.address && <span className="inline-flex items-center gap-1 px-2 py-1 rounded-full bg-slate-100 dark:bg-white/10"><MapPin size={12} /> {c.address}</span>}
                <span className="px-2 py-1 rounded-full bg-[#0e7490]/10 text-[#0e7490] font-bold">{cases.length} قضايا</span>
              </div>

              <div className="mt-4 flex items-center justify-between text-xs text-slate-500">
                <span>منذ {formatDateShort(c.createdAt)}</span>
                <button onClick={() => setSelected(expanded ? null : c.id)} className="font-bold text-[#0e7490]">{expanded ? "إخفاء" : "عرض القضايا"}</button>
              </div>

              {expanded && (
                <div className="mt-4 space-y-2 border-t dark:border-[#1e2e50] pt-4">
                  {cases.length ? cases.map(k => (
                    <div key={k.id} className="p-3 rounded-xl bg-slate-50 dark:bg-white/5 border dark:border-white/10">
                      <div className="font-bold text-sm">{k.title}</div>
                      <div className="text-xs text-slate-500">{k.fileNumber} • {k.court} • {k.status}</div>
                    </div>
                  )) : <div className="text-sm text-slate-500 text-center py-4">لا توجد قضايا لهذا العميل بعد</div>}
                  <div className="flex gap-2 text-xs">
                    <span className="inline-flex items-center gap-1"><FileText size={12} /> سجل التواصل</span>
                    <span>•</span>
                    <span>ملاحظات خاصة</span>
                  </div>
                </div>
              )}
            </div>
          );
        })}
      </div>

      {filtered.length === 0 && <div className="text-center py-12 text-slate-500">لا يوجد عملاء بهذا البحث</div>}
    </div>
  );
}
