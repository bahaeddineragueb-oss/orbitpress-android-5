"use client";
import { useState } from "react";
import { legalTemplates } from "@/lib/data";
import { LegalTemplate } from "@/lib/types";
import { FileText, Sparkles, Download, Copy, Wand2 } from "lucide-react";

export default function TemplatesPage(){
  const [selected, setSelected] = useState<LegalTemplate>(legalTemplates[0]);
  const [values, setValues] = useState<Record<string,string>>({});
  const [output, setOutput] = useState("");

  const generate = () => {
    let txt = selected.content;
    selected.fields.forEach(f=>{
      txt = txt.replaceAll(`{{${f.key}}}`, values[f.key] || `___${f.label}___`);
    });
    setOutput(txt);
  };

  const copy = () => {
    navigator.clipboard.writeText(output);
    alert("تم النسخ");
  };

  return (
    <div className="space-y-6">
      <div>
        <h1 className="font-display font-extrabold text-2xl flex items-center gap-2"><FileText className="text-[#0e7490]"/> النماذج القانونية الجاهزة</h1>
        <p className="text-sm text-slate-500">مكتبة عرائض ومذكرات قابلة للتعبئة التلقائية — أدخل البيانات ويقوم التطبيق بملء النموذج</p>
      </div>

      <div className="grid lg:grid-cols-3 gap-6">
        <div className="space-y-3">
          {legalTemplates.map(t=>(
            <button key={t.id} onClick={()=>{setSelected(t); setOutput(""); setValues({});}} className={`w-full text-right p-4 rounded-2xl border text-sm ${selected.id===t.id?"bg-[#0e7490] text-white border-[#0e7490] shadow":"bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] hover:shadow-card"}`}>
              <div className="font-bold">{t.title}</div>
              <div className={`text-xs mt-1 ${selected.id===t.id?"text-white/80":"text-slate-500"}`}>{t.category} • {t.description}</div>
            </button>
          ))}
          <div className="rounded-xl border border-dashed dark:border-white/20 p-4 text-center text-sm text-slate-500">
            + يمكنك إضافة قوالبك الخاصة (Word/PDF) وربط الحقول تلقائياً
          </div>
        </div>

        <div className="lg:col-span-2 space-y-4">
          <div className="rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] p-5">
            <h3 className="font-bold flex items-center gap-2"><Wand2 size={18} className="text-violet-600"/> تعبئة النموذج: {selected.title}</h3>
            <div className="grid md:grid-cols-2 gap-4 mt-4">
              {selected.fields.map(f=>(
                <div key={f.key}>
                  <label className="text-xs font-bold text-slate-600 dark:text-slate-400">{f.label}</label>
                  <input placeholder={f.placeholder} value={values[f.key]||""} onChange={e=>setValues({...values, [f.key]: e.target.value})} className="mt-1 w-full px-4 py-3 rounded-xl border dark:bg-[#070e1f] dark:border-[#1e2e50]" />
                </div>
              ))}
            </div>
            <button onClick={generate} className="mt-4 w-full py-3 rounded-xl bg-gradient-to-br from-violet-600 to-indigo-600 text-white font-bold inline-flex items-center justify-center gap-2"><Sparkles size={18}/> توليد النموذج</button>
          </div>

          <div className="rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] p-5">
            <div className="flex items-center justify-between">
              <h3 className="font-bold">المعاينة والطباعة</h3>
              <div className="flex gap-2">
                <button onClick={copy} disabled={!output} className="px-4 py-2 rounded-xl border dark:border-white/10 inline-flex items-center gap-2 text-sm font-bold disabled:opacity-50"><Copy size={16}/> نسخ</button>
                <button onClick={()=>window.print()} disabled={!output} className="px-4 py-2 rounded-xl bg-[#0e7490] text-white inline-flex items-center gap-2 text-sm font-bold disabled:opacity-50"><Download size={16}/> طباعة/PDF</button>
              </div>
            </div>
            <div className="mt-4 min-h-[280px] p-6 rounded-xl bg-slate-50 dark:bg-[#070e1f] border dark:border-[#1e2e50] whitespace-pre-wrap leading-relaxed font-mono text-sm">
              {output || "— سيظهر النموذج المعبأ هنا بعد الضغط على توليد —\n\nمثال: اختر عريضة افتتاح دعوى مدنية، أدخل اسم الموكل والمحكمة والوقائع، ثم اضغط توليد لتحصل على نموذج جاهز للطباعة."}
            </div>
            <div className="mt-3 text-xs text-slate-500">💡 يمكن ربط النماذج ببيانات الملف تلقائياً: عند فتح ملف، اضغط "تعبئة من الملف" ليملأ الحقول من بيانات العميل والقضية فوراً.</div>
          </div>
        </div>
      </div>
    </div>
  );
}
