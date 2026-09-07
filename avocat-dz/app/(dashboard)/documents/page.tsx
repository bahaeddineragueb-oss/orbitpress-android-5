"use client";
import { useState, useMemo } from "react";
import { mockCases, mockDocuments } from "@/lib/data";
import { Document, DocCategory } from "@/lib/types";
import { formatDateShort, uid } from "@/lib/utils";
import { Files, Search, Upload, Eye, Download, Trash2, FileText, Filter } from "lucide-react";

const cats: DocCategory[] = ["عريضة افتتاح","مذكرة","محضر","حكم","قرار","استدعاء","وكالة","مراسلة","أخرى"];

export default function DocumentsPage(){
  const [docs, setDocs] = useState<Document[]>(mockDocuments);
  const [q,setQ]=useState("");
  const [cat,setCat]=useState<string>("الكل");
  const [showForm,setShowForm]=useState(false);
  const [form,setForm]=useState<Partial<Document>>({ title:"", category:"أخرى", caseId: mockCases[0].id });

  const filtered = useMemo(()=> docs.filter(d=>{
    const c = mockCases.find(x=>x.id===d.caseId);
    const matchQ = [d.title, d.category, d.fileName, c?.title, c?.fileNumber].join(" ").toLowerCase().includes(q.toLowerCase());
    const matchCat = cat==="الكل" || d.category===cat;
    return matchQ && matchCat;
  }), [docs,q,cat]);

  const addDoc=()=>{
    if (!form.title || !form.caseId) return alert("العنوان والقضية مطلوبان");
    const nd: Document = { id: uid(), caseId: form.caseId!, title: form.title!, category: form.category as DocCategory, date: new Date().toISOString().slice(0,10), fileName: form.fileName || "document.pdf", uploadedBy: "أ. بهاء الدين" };
    setDocs([nd, ...docs]);
    setShowForm(false);
    setForm({ title:"", category:"أخرى", caseId: mockCases[0].id });
  };

  return (
    <div className="space-y-6">
      <div className="flex flex-col lg:flex-row lg:items-center justify-between gap-4">
        <div>
          <h1 className="font-display font-extrabold text-2xl flex items-center gap-2"><Files className="text-[#0e7490]" /> الوثائق</h1>
          <p className="text-sm text-slate-500">مجلد إلكتروني لكل ملف — تصنيف، بحث، معاينة PDF، وتنزيل</p>
        </div>
        <button onClick={()=>setShowForm(!showForm)} className="px-5 py-3 rounded-xl bg-[#0e7490] text-white font-bold inline-flex items-center gap-2"><Upload size={18}/> رفع وثيقة</button>
      </div>

      {showForm && (
        <div className="rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] p-5">
          <div className="grid md:grid-cols-3 gap-4">
            <input placeholder="عنوان الوثيقة *" value={form.title||""} onChange={e=>setForm({...form,title:e.target.value})} className="px-4 py-3 rounded-xl border dark:bg-[#070e1f] dark:border-[#1e2e50]" />
            <select value={form.caseId} onChange={e=>setForm({...form,caseId:e.target.value})} className="px-4 py-3 rounded-xl border dark:bg-[#070e1f] dark:border-[#1e2e50]">
              {mockCases.map(c=><option key={c.id} value={c.id}>{c.fileNumber} — {c.title}</option>)}
            </select>
            <select value={form.category} onChange={e=>setForm({...form,category:e.target.value as any})} className="px-4 py-3 rounded-xl border dark:bg-[#070e1f] dark:border-[#1e2e50]">
              {cats.map(c=><option key={c}>{c}</option>)}
            </select>
            <input placeholder="اسم الملف (مثال: hukm.pdf)" value={form.fileName||""} onChange={e=>setForm({...form,fileName:e.target.value})} className="px-4 py-3 rounded-xl border dark:bg-[#070e1f] dark:border-[#1e2e50]" />
            <div className="md:col-span-2 flex gap-3">
              <button onClick={addDoc} className="flex-1 py-3 rounded-xl bg-[#0e7490] text-white font-bold">حفظ</button>
              <button onClick={()=>setShowForm(false)} className="flex-1 py-3 rounded-xl border dark:border-[#1e2e50]">إلغاء</button>
            </div>
          </div>
        </div>
      )}

      <div className="flex flex-col md:flex-row gap-3">
        <div className="relative flex-1">
          <Search size={18} className="absolute right-3 top-1/2 -translate-y-1/2 text-slate-400" />
          <input value={q} onChange={e=>setQ(e.target.value)} placeholder="بحث: عنوان، تصنيف، رقم ملف..." className="w-full pr-10 pl-4 py-3 rounded-xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50]" />
        </div>
        <div className="flex gap-2 overflow-auto">
          <button onClick={()=>setCat("الكل")} className={`px-4 py-2 rounded-xl border font-bold whitespace-nowrap ${cat==="الكل"?"bg-[#0e7490] text-white border-[#0e7490]":"bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50]"}`}>الكل</button>
          {cats.map(c=> <button key={c} onClick={()=>setCat(c)} className={`px-4 py-2 rounded-xl border font-bold whitespace-nowrap ${cat===c?"bg-[#0e7490] text-white border-[#0e7490]":"bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50]"}`}>{c}</button>)}
        </div>
      </div>

      <div className="rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] overflow-hidden">
        <div className="overflow-auto">
          <table className="w-full text-sm">
            <thead className="bg-slate-50 dark:bg-white/5 text-slate-500">
              <tr><th className="p-3 text-right">الوثيقة</th><th className="p-3 text-right">الملف</th><th className="p-3 text-right">التصنيف</th><th className="p-3 text-right">التاريخ</th><th className="p-3 text-right">الرفع</th><th className="p-3"></th></tr>
            </thead>
            <tbody className="divide-y dark:divide-[#1e2e50]">
              {filtered.map(d=>{
                const c = mockCases.find(x=>x.id===d.caseId);
                return (
                  <tr key={d.id} className="hover:bg-slate-50 dark:hover:bg-white/[0.03]">
                    <td className="p-3"><div className="font-bold flex items-center gap-2"><FileText size={16} className="text-[#0e7490]"/>{d.title}</div><div className="text-xs text-slate-500">{d.fileName}</div></td>
                    <td className="p-3"><div className="font-bold">{c?.fileNumber}</div><div className="text-xs text-slate-500 truncate max-w-[220px]">{c?.title}</div></td>
                    <td className="p-3"><span className="text-xs px-2 py-1 rounded-full bg-slate-100 dark:bg-white/10 font-bold">{d.category}</span></td>
                    <td className="p-3">{formatDateShort(d.date)}</td>
                    <td className="p-3 text-xs">{d.uploadedBy}</td>
                    <td className="p-3"><div className="flex gap-1">
                      <button className="w-8 h-8 grid place-items-center rounded-lg hover:bg-slate-100 dark:hover:bg-white/10"><Eye size={16}/></button>
                      <button className="w-8 h-8 grid place-items-center rounded-lg hover:bg-slate-100 dark:hover:bg-white/10"><Download size={16}/></button>
                      <button onClick={()=>setDocs(docs.filter(x=>x.id!==d.id))} className="w-8 h-8 grid place-items-center rounded-lg hover:bg-red-50 text-red-600"><Trash2 size={16}/></button>
                    </div></td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
        {filtered.length===0 && <div className="p-12 text-center text-slate-500">لا توجد وثائق</div>}
      </div>
    </div>
  );
}
