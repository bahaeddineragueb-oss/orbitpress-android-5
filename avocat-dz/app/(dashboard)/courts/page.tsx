"use client";
import { courtsDB } from "@/lib/data";
import { useState, useMemo } from "react";
import { Building2, Search, MapPin, Phone, Filter } from "lucide-react";

export default function CourtsPage(){
  const [q,setQ]=useState("");
  const [wilaya,setWilaya]=useState("الكل");
  const wilayas = ["الكل", ...Array.from(new Set(courtsDB.map(c=>c.wilaya)))];
  const filtered = useMemo(()=> courtsDB.filter(c=> {
    const matchQ = [c.court, c.council, c.section, c.wilaya].join(" ").toLowerCase().includes(q.toLowerCase());
    const matchW = wilaya==="الكل" || c.wilaya===wilaya;
    return matchQ && matchW;
  }), [q,wilaya]);

  return (
    <div className="space-y-6">
      <div>
        <h1 className="font-display font-extrabold text-2xl flex items-center gap-2"><Building2 className="text-[#0e7490]"/> قاعدة بيانات المحاكم</h1>
        <p className="text-sm text-slate-500">اختر الولاية → المحكمة → القسم — عناوين، هواتف، وأوقات العمل (قابلة للتوسعة)</p>
      </div>

      <div className="flex flex-col md:flex-row gap-3">
        <div className="relative flex-1">
          <Search size={18} className="absolute right-3 top-1/2 -translate-y-1/2 text-slate-400" />
          <input value={q} onChange={e=>setQ(e.target.value)} placeholder="بحث: محكمة الرويبة، الحراش..." className="w-full pr-10 pl-4 py-3 rounded-xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50]" />
        </div>
        <select value={wilaya} onChange={e=>setWilaya(e.target.value)} className="px-4 py-3 rounded-xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50]">
          {wilayas.map(w=> <option key={w}>{w}</option>)}
        </select>
      </div>

      <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-4">
        {filtered.map((c,i)=>(
          <div key={i} className="rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] p-5 hover:shadow-card transition">
            <div className="flex items-center gap-3">
              <div className="w-10 h-10 rounded-xl bg-[#0e7490]/10 text-[#0e7490] grid place-items-center"><Building2 size={18}/></div>
              <div>
                <div className="font-bold">{c.court}</div>
                <div className="text-xs text-slate-500">{c.council} • {c.wilaya}</div>
              </div>
            </div>
            <div className="mt-3">
              <span className="text-xs px-2 py-1 rounded-full bg-slate-900 text-white dark:bg-white dark:text-slate-900">{c.section}</span>
            </div>
            <div className="mt-4 space-y-1 text-sm text-slate-600 dark:text-slate-400">
              <div className="flex items-center gap-2"><MapPin size={14}/> {c.address}</div>
              <div className="flex items-center gap-2"><Phone size={14}/> {c.phone}</div>
            </div>
            <div className="mt-4 text-xs text-slate-500">الملفات المرتبطة: {Math.floor(Math.random()*5)} ملفات • <span className="text-[#0e7490] font-bold">عرض</span></div>
          </div>
        ))}
      </div>
      {filtered.length===0 && <div className="p-12 text-center text-slate-500">لا توجد نتائج</div>}

      <div className="rounded-2xl border-2 border-dashed dark:border-white/20 p-6 text-center">
        <div className="font-bold">هل تريد إضافة كل محاكم ومجالس الجزائر (58 ولاية)؟</div>
        <div className="text-sm text-slate-500 mt-1">لدينا ملف JSON جاهز بكل المحاكم والمجالس والأقسام — يمكن استيراده بضغطة واحدة</div>
        <button className="mt-3 px-5 py-2.5 rounded-xl bg-[#0e7490] text-white font-bold">استيراد قاعدة المحاكم الكاملة</button>
      </div>
    </div>
  );
}
