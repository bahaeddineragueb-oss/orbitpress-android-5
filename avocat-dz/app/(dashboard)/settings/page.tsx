"use client";
import { Settings, Shield, Database, Users, Lock } from "lucide-react";
export default function SettingsPage(){
  return (
    <div className="space-y-6">
      <h1 className="font-display font-extrabold text-2xl flex items-center gap-2"><Settings className="text-[#0e7490]"/> الإعدادات والأمان</h1>
      <div className="grid md:grid-cols-2 gap-4">
        <div className="rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] p-5">
          <h3 className="font-bold flex items-center gap-2"><Shield size={18}/> الأمان والخصوصية</h3>
          <ul className="mt-3 space-y-2 text-sm list-disc pr-5 text-slate-600 dark:text-slate-400">
            <li>تسجيل دخول آمن + بصمة/Face ID</li>
            <li>تشفير EncryptedStorage للبيانات الحساسة</li>
            <li>صلاحيات: مدير/محامي/متربص/سكرتير</li>
            <li>Audit Log — من فتح/عدّل/حذف</li>
            <li>نسخ احتياطي تلقائي يومي</li>
          </ul>
        </div>
        <div className="rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] p-5">
          <h3 className="font-bold flex items-center gap-2"><Users size={18}/> إدارة فريق المكتب</h3>
          <div className="mt-3 space-y-2 text-sm">
            <div className="p-3 rounded-xl bg-slate-50 dark:bg-white/5 border dark:border-white/10 flex justify-between"><span>مدير المكتب</span><span className="text-xs px-2 py-1 rounded-full bg-[#0e7490] text-white">كل الصلاحيات</span></div>
            <div className="p-3 rounded-xl bg-slate-50 dark:bg-white/5 border dark:border-white/10 flex justify-between"><span>محامي</span><span className="text-xs px-2 py-1 rounded-full bg-slate-200 dark:bg-white/10">ملفاته فقط</span></div>
            <div className="p-3 rounded-xl bg-slate-50 dark:bg-white/5 border dark:border-white/10 flex justify-between"><span>سكرتير</span><span className="text-xs px-2 py-1 rounded-full bg-slate-200 dark:bg-white/10">جلسات ووثائق فقط</span></div>
          </div>
        </div>
        <div className="rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] p-5">
          <h3 className="font-bold flex items-center gap-2"><Database size={18}/> النسخ الاحتياطي</h3>
          <div className="mt-3 text-sm text-slate-600 dark:text-slate-400">آخر نسخة: اليوم 03:00 صباحاً — Google Drive / Local</div>
          <button className="mt-3 w-full py-2.5 rounded-xl bg-[#0e7490] text-white font-bold">إنشاء نسخة الآن</button>
        </div>
        <div className="rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] p-5">
          <h3 className="font-bold flex items-center gap-2"><Lock size={18}/> التشفير</h3>
          <div className="text-sm text-slate-500 mt-2">جميع المفاتيح محفوظة بـ EncryptedSharedPreferences ولا تُرسل لخادم خارجي إلا برضا المستخدم.</div>
        </div>
      </div>
    </div>
  );
}
