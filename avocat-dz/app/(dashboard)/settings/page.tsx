"use client";
import { useState, useEffect } from "react";
import { Settings, Shield, Database, Users, Lock, Palette, Type } from "lucide-react";
import { THEMES, PRIMARY_COLORS, FONT_SIZES, applyTheme, loadTheme } from "@/lib/theme";
import type { Theme, PrimaryColor, FontSize } from "@/lib/theme";

export default function SettingsPage(){
  const [theme, setTheme] = useState<Theme>("light");
  const [primary, setPrimary] = useState<PrimaryColor>("azraq");
  const [fontSize, setFontSize] = useState<FontSize>(16);

  useEffect(() => {
    const loaded = loadTheme();
    setTheme(loaded.theme);
    setPrimary(loaded.primary);
    setFontSize(loaded.fontSize);
  }, []);

  const handleTheme = (t: Theme) => {
    setTheme(t);
    applyTheme(t, primary, fontSize);
  };
  const handlePrimary = (p: PrimaryColor) => {
    setPrimary(p);
    applyTheme(theme, p, fontSize);
  };
  const handleFont = (f: FontSize) => {
    setFontSize(f);
    applyTheme(theme, primary, f);
  };

  return (
    <div className="space-y-6">
      <h1 className="font-display font-extrabold text-2xl flex items-center gap-2"><Settings className="text-[var(--primary,#0e7490)]"/> الإعدادات والأمان</h1>

      {/* تخصيص — الثيم والخط */}
      <div className="grid md:grid-cols-2 gap-4">
        <div className="rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] p-5">
          <h3 className="font-bold flex items-center gap-2"><Palette size={18}/> المظهر — الثيم</h3>
          <p className="text-xs text-slate-500 mt-1">اختر الفاتح أو الداكن أو حسب النظام — يُحفظ تلقائياً</p>
          <div className="mt-3 grid grid-cols-3 gap-2">
            {(Object.keys(THEMES) as Theme[]).map(t => (
              <button
                key={t}
                onClick={() => handleTheme(t)}
                className={`py-3 rounded-xl border font-bold text-sm transition ${theme === t ? "bg-[var(--primary,#0e7490)] text-white border-[var(--primary,#0e7490)]" : "bg-white dark:bg-white/5 border-slate-200 dark:border-white/10 hover:border-[var(--primary,#0e7490)]"}`}
              >
                {THEMES[t]}
              </button>
            ))}
          </div>
          <div className="mt-4">
            <h4 className="font-bold text-sm">اللون الأساسي</h4>
            <div className="mt-2 grid grid-cols-2 gap-2">
              {(Object.entries(PRIMARY_COLORS) as [PrimaryColor, typeof PRIMARY_COLORS[PrimaryColor]][]).map(([k, v]) => (
                <button
                  key={k}
                  onClick={() => handlePrimary(k)}
                  className={`py-2.5 rounded-xl border font-bold text-sm flex items-center justify-center gap-2 ${primary === k ? "text-white border-transparent" : "bg-white dark:bg-white/5 border-slate-200 dark:border-white/10"}`}
                  style={primary === k ? { background: v.hex } : {}}
                >
                  <span className="w-3 h-3 rounded-full border border-white/30" style={{ background: v.hex }} /> {v.label}
                </button>
              ))}
            </div>
          </div>
        </div>

        <div className="rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] p-5">
          <h3 className="font-bold flex items-center gap-2"><Type size={18}/> حجم الخط — تكبير / تصغير</h3>
          <p className="text-xs text-slate-500 mt-1">اسحب أو اختر — يُطبّق فوراً على كل البرنامج ويُحفظ</p>
          <div className="mt-4">
            <input
              type="range"
              min={14}
              max={20}
              step={1}
              value={fontSize}
              onChange={e => handleFont(Number(e.target.value) as FontSize)}
              className="w-full accent-[var(--primary,#0e7490)]"
            />
            <div className="flex justify-between text-[11px] text-slate-400 mt-1">
              <span>14 صغير</span><span>16 افتراضي</span><span>20 كبير جداً</span>
            </div>
          </div>
          <div className="mt-3 grid grid-cols-4 md:grid-cols-7 gap-1.5">
            {FONT_SIZES.map(f => (
              <button
                key={f.value}
                onClick={() => handleFont(f.value)}
                className={`py-2 rounded-xl border text-xs font-bold ${fontSize === f.value ? "bg-[var(--primary,#0e7490)] text-white border-[var(--primary,#0e7490)]" : "bg-white dark:bg-white/5 border-slate-200 dark:border-white/10"}`}
              >
                {f.value}px<br/><span className="text-[10px] opacity-70">{f.label}</span>
              </button>
            ))}
          </div>
          <div className="mt-4 p-3 rounded-xl bg-slate-50 dark:bg-white/5 border dark:border-white/10" style={{ fontSize: `${fontSize}px` }}>
            <div className="font-bold">معاينة — هذا حجم الخط الحالي ({fontSize}px)</div>
            <div className="text-slate-600 dark:text-slate-400 mt-1 leading-relaxed">بسم الله الرحمن الرحيم — نظام مكتبي لتسيير مكاتب المحاماة في الجزائر. يمكنك تكبير النص لراحة القراءة أو تصغيره لعرض محتوى أكثر.</div>
          </div>
          <div className="mt-3 text-[11px] text-emerald-600 font-bold">✓ يُحفظ في المتصفح ويُطبّق عند كل فتح</div>
        </div>
      </div>

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
          <button className="mt-3 w-full py-2.5 rounded-xl bg-[var(--primary,#0e7490)] text-white font-bold">إنشاء نسخة الآن</button>
        </div>
        <div className="rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] p-5">
          <h3 className="font-bold flex items-center gap-2"><Lock size={18}/> التشفير</h3>
          <div className="text-sm text-slate-500 mt-2">جميع المفاتيح محفوظة بـ EncryptedSharedPreferences ولا تُرسل لخادم خارجي إلا برضا المستخدم.</div>
        </div>
      </div>
    </div>
  );
}
