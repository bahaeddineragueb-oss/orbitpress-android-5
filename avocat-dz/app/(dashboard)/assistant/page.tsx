"use client";
import { useState } from "react";
import { mockCases, mockDocuments } from "@/lib/data";
import { Bot, Sparkles, FileText, Clock, Users, AlertTriangle } from "lucide-react";

export default function AssistantPage(){
  const [caseId,setCaseId]=useState(mockCases[0].id);
  const [action,setAction]=useState<"summary"|"deadlines"|"memo">("summary");
  const [output,setOutput]=useState("");
  const [loading,setLoading]=useState(false);

  const c = mockCases.find(x=>x.id===caseId)!;

  const run = ()=>{
    setLoading(true);
    setTimeout(()=>{
      if (action==="summary") setOutput(`📌 ملخص الملف ${c.fileNumber} — ${c.title}

• الأطراف: الموكل ${c.clientRole} ضد ${c.opponent} (${c.opponent? "محاميه "+c.opponent : ""})
• المحكمة: ${c.court} — ${c.section} — ${c.council}
• نوع القضية: ${c.category} — الحالة: ${c.status}
• تاريخ الفتح: ${new Date(c.openDate).toLocaleDateString("ar-DZ")}
• الاستراتيجية: ${c.strategy || "إثبات الحيازة بالشهود والخبرة"}
• الوثائق: ${mockDocuments.filter(d=>d.caseId===c.id).map(d=>d.title).join("، ") || "لا توجد وثائق بعد"}
• آخر إجراء: جلسة مؤجلة لسماع الشهود — القادمة بعد 1 يوم في ${c.court}
• التنبيه: يوجد أجل استئناف بعد 10 أيام — يوصى بالتحضير المسبق

⚠️ هذا تلخيص آلي للمعاينة فقط ويحتاج مراجعة المحامي قبل الاعتماد.`);
      if (action==="deadlines") setOutput(`⏰ الآجال المستخرجة من وثائق الملف ${c.fileNumber}:

• 10 أيام — أجل استئناف الحكم الجزائي (عاجل)
• 3 أيام — إيداع مذكرة جوابية
• 15 يوم — تبليغ عريضة الافتتاح
• 45 يوم — أجل الطعن بالنقض

تم الاستخراج تلقائياً من النصوص — يرجى تأكيد التواريخ من الأحكام الأصلية.`);
      if (action==="memo") setOutput(`📝 مسودة مذكرة جوابية — ${c.title}

إلى السيد رئيس ${c.court}
لفائدة: الموكل (${c.clientRole}) ضد ${c.opponent}
الموضوع: مذكرة جوابية

حيث أن الوقائع تتمثل في ${c.notes || "نزاع حول قطعة أرض..."}
وحيث أن موكلنا يتمسك بـ ${c.strategy || "..."}

لهذه الأسباب نلتمس من المحكمة:
1. رفض طلبات الخصم
2. الحكم لصالح موكلنا مع تحميل المصاريف

(مسودة أولية — راجعها وعدّلها قبل الإيداع)`);
      setLoading(false);
    }, 900);
  };

  return (
    <div className="space-y-6">
      <div className="rounded-2xl bg-gradient-to-br from-violet-600 to-indigo-700 text-white p-6">
        <h1 className="font-display font-extrabold text-2xl flex items-center gap-2"><Bot/> المساعد الذكي — للمحامي</h1>
        <p className="text-white/80 mt-1">يلخص الملف، يستخرج الآجال والتواريخ من الوثيقة، وينشئ مسودة مذكرة — مع تنبيه واضح أن المخرجات تحتاج مراجعة المحامي</p>
      </div>

      <div className="grid lg:grid-cols-3 gap-6">
        <div className="rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] p-5 space-y-4">
          <div>
            <label className="text-xs font-bold">اختر الملف</label>
            <select value={caseId} onChange={e=>setCaseId(e.target.value)} className="mt-1 w-full px-4 py-3 rounded-xl border dark:bg-[#070e1f] dark:border-[#1e2e50]">
              {mockCases.map(x=> <option key={x.id} value={x.id}>{x.fileNumber} — {x.title}</option>)}
            </select>
          </div>
          <div>
            <label className="text-xs font-bold">الإجراء</label>
            <div className="mt-2 grid gap-2">
              <button onClick={()=>setAction("summary")} className={`p-3 rounded-xl border text-right ${action==="summary"?"bg-violet-600 text-white border-violet-600":"bg-white dark:bg-[#070e1f] dark:border-[#1e2e50]"}`}>
                <div className="font-bold flex items-center gap-2"><FileText size={16}/> تلخيص الملف</div><div className="text-xs opacity-80">أطراف، وقائع، طلبات، دفوع، آخر إجراء</div>
              </button>
              <button onClick={()=>setAction("deadlines")} className={`p-3 rounded-xl border text-right ${action==="deadlines"?"bg-violet-600 text-white border-violet-600":"bg-white dark:bg-[#070e1f] dark:border-[#1e2e50]"}`}>
                <div className="font-bold flex items-center gap-2"><Clock size={16}/> استخراج الآجال</div><div className="text-xs opacity-80">تواريخ وآجال من الوثيقة</div>
              </button>
              <button onClick={()=>setAction("memo")} className={`p-3 rounded-xl border text-right ${action==="memo"?"bg-violet-600 text-white border-violet-600":"bg-white dark:bg-[#070e1f] dark:border-[#1e2e50]"}`}>
                <div className="font-bold flex items-center gap-2"><Users size={16}/> إنشاء مسودة مذكرة</div><div className="text-xs opacity-80">بناءً على معلومات الملف</div>
              </button>
            </div>
          </div>
          <button onClick={run} disabled={loading} className="w-full py-3 rounded-xl bg-[#0e7490] text-white font-bold inline-flex items-center justify-center gap-2 disabled:opacity-50">
            {loading ? "جاري المعالجة..." : <><Sparkles size={18}/> تنفيذ</>}
          </button>
          <div className="text-xs text-slate-500 flex gap-2"><AlertTriangle size={14} className="shrink-0"/> المخرجات للمساعدة فقط وليست استشارة قانونية — راجعها بنفسك.</div>
        </div>

        <div className="lg:col-span-2 rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] p-5">
          <h3 className="font-bold">النتيجة</h3>
          <div className="mt-4 min-h-[380px] p-5 rounded-xl bg-slate-50 dark:bg-[#070e1f] border dark:border-[#1e2e50] whitespace-pre-wrap leading-relaxed text-sm">
            {output || "— اختر ملفاً وإجراءً ثم اضغط تنفيذ —\n\nمثال: تلخيص ملف سيعطيك: الأطراف، الوقائع، الطلبات، الدفوع، الوثائق، آخر إجراء، والجلسة القادمة في ثوانٍ."}
          </div>
          <div className="mt-3 flex gap-2">
            <button onClick={()=> output && navigator.clipboard.writeText(output)} className="px-4 py-2 rounded-xl border dark:border-white/10 text-sm font-bold">نسخ</button>
            <button onClick={()=> output && window.print()} className="px-4 py-2 rounded-xl bg-slate-900 text-white dark:bg-white dark:text-slate-900 text-sm font-bold">طباعة</button>
          </div>
        </div>
      </div>
    </div>
  );
}
