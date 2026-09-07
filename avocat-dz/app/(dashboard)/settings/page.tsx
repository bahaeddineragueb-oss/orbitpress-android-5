"use client";
import { useState, useEffect } from "react";
import { Settings, Shield, Database, Users, Lock, Palette, Type, Languages } from "lucide-react";
import { THEMES, PRIMARY_COLORS, FONT_SIZES, applyTheme, loadTheme } from "@/lib/theme";
import type { Theme, PrimaryColor, FontSize } from "@/lib/theme";
import { useI18n } from "@/components/LanguageProvider";
import { LanguageSwitcher } from "@/components/LanguageSwitcher";

export default function SettingsPage(){
  const [theme, setTheme] = useState<Theme>("light");
  const [primary, setPrimary] = useState<PrimaryColor>("azraq");
  const [fontSize, setFontSize] = useState<FontSize>(16);
  const { t, lang } = useI18n();

  useEffect(() => {
    const loaded = loadTheme();
    setTheme(loaded.theme);
    setPrimary(loaded.primary);
    setFontSize(loaded.fontSize);
  }, []);

  const handleTheme = (th: Theme) => {
    setTheme(th);
    applyTheme(th, primary, fontSize);
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
      <h1 className="font-display font-extrabold text-2xl flex items-center gap-2"><Settings className="text-[var(--primary,#0e7490)]"/> {t("settings.title")}</h1>

      {/* اللغة */}
      <div className="rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] p-5">
        <h3 className="font-bold flex items-center gap-2"><Languages size={18}/> {t("settings.language")}</h3>
        <p className="text-xs text-slate-500 mt-1">{t("settings.language.desc")}</p>
        <div className="mt-4 max-w-xl">
          <LanguageSwitcher />
          <div className="mt-4 p-3 rounded-xl bg-slate-50 dark:bg-white/5 border dark:border-white/10">
            <div className="font-bold text-sm">{t("settings.language.preview")} — {lang.toUpperCase()} ({t(`lang.${lang}`)})</div>
            <div className="text-sm text-slate-600 dark:text-slate-400 mt-1">
              {lang === "ar" && "بسم الله الرحمن الرحيم — نظام مكتبي - maktabi لتسيير مكاتب المحاماة في الجزائر."}
              {lang === "fr" && "Au nom de Dieu — Maktabi, système de gestion des cabinets d'avocats en Algérie."}
              {lang === "en" && "In the name of God — Maktabi, Algerian law firm management system."}
            </div>
          </div>
        </div>
      </div>

      {/* تخصيص — الثيم والخط */}
      <div className="grid md:grid-cols-2 gap-4">
        <div className="rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] p-5">
          <h3 className="font-bold flex items-center gap-2"><Palette size={18}/> {t("settings.appearance")}</h3>
          <p className="text-xs text-slate-500 mt-1">{t("settings.appearance.desc")}</p>
          <div className="mt-3 grid grid-cols-3 gap-2">
            {(Object.keys(THEMES) as Theme[]).map(th => (
              <button
                key={th}
                onClick={() => handleTheme(th)}
                className={`py-3 rounded-xl border font-bold text-sm transition ${theme === th ? "bg-[var(--primary,#0e7490)] text-white border-[var(--primary,#0e7490)]" : "bg-white dark:bg-white/5 border-slate-200 dark:border-white/10 hover:border-[var(--primary,#0e7490)]"}`}
              >
                {t(`theme.${th}`)}
              </button>
            ))}
          </div>
          <div className="mt-4">
            <h4 className="font-bold text-sm">{t("settings.primary")}</h4>
            <div className="mt-2 grid grid-cols-2 gap-2">
              {(Object.entries(PRIMARY_COLORS) as [PrimaryColor, typeof PRIMARY_COLORS[PrimaryColor]][]).map(([k, v]) => (
                <button
                  key={k}
                  onClick={() => handlePrimary(k)}
                  className={`py-2.5 rounded-xl border font-bold text-sm flex items-center justify-center gap-2 ${primary === k ? "text-white border-transparent" : "bg-white dark:bg-white/5 border-slate-200 dark:border-white/10"}`}
                  style={primary === k ? { background: v.hex } : {}}
                >
                  <span className="w-3 h-3 rounded-full border border-white/30" style={{ background: v.hex }} /> {t(`theme.${k}`)}
                </button>
              ))}
            </div>
          </div>
        </div>

        <div className="rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] p-5">
          <h3 className="font-bold flex items-center gap-2"><Type size={18}/> {t("settings.font")}</h3>
          <p className="text-xs text-slate-500 mt-1">{t("settings.font.desc")}</p>
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
              <span>14 {lang === "ar" ? "صغير" : lang === "fr" ? "Petit" : "Small"}</span><span>16 {lang === "ar" ? "افتراضي" : lang === "fr" ? "Défaut" : "Default"}</span><span>20 {lang === "ar" ? "كبير جداً" : lang === "fr" ? "Très grand" : "Very large"}</span>
            </div>
          </div>
          <div className="mt-3 grid grid-cols-4 md:grid-cols-7 gap-1.5">
            {FONT_SIZES.map(f => (
              <button
                key={f.value}
                onClick={() => handleFont(f.value)}
                className={`py-2 rounded-xl border text-xs font-bold ${fontSize === f.value ? "bg-[var(--primary,#0e7490)] text-white border-[var(--primary,#0e7490)]" : "bg-white dark:bg-white/5 border-slate-200 dark:border-white/10"}`}
              >
                {f.value}px<br/><span className="text-[10px] opacity-70">{lang === "ar" ? f.label : lang === "fr" ? (f.value <=15 ? "Petit" : f.value===16 ? "Moyen" : "Grand") : (f.value <=15 ? "Small" : f.value===16 ? "Medium" : "Large")}</span>
              </button>
            ))}
          </div>
          <div className="mt-4 p-3 rounded-xl bg-slate-50 dark:bg-white/5 border dark:border-white/10" style={{ fontSize: `${fontSize}px` }}>
            <div className="font-bold">{lang === "ar" ? `معاينة — هذا حجم الخط الحالي (${fontSize}px)` : lang === "fr" ? `Aperçu — taille actuelle (${fontSize}px)` : `Preview — current size (${fontSize}px)`}</div>
            <div className="text-slate-600 dark:text-slate-400 mt-1 leading-relaxed">
              {lang === "ar" && "بسم الله الرحمن الرحيم — نظام مكتبي - maktabi لتسيير مكاتب المحاماة في الجزائر. يمكنك تكبير النص لراحة القراءة أو تصغيره لعرض محتوى أكثر."}
              {lang === "fr" && "Au nom de Dieu — Maktabi, système pour les cabinets d'avocats algériens. Agrandissez le texte pour un confort de lecture ou réduisez-le pour voir plus de contenu."}
              {lang === "en" && "In the name of God — Maktabi, Algerian law firm management. Increase text for comfortable reading or decrease to see more content."}
            </div>
          </div>
          <div className="mt-3 text-[11px] text-emerald-600 font-bold">✓ {lang === "ar" ? "يُحفظ في المتصفح ويُطبّق عند كل فتح" : lang === "fr" ? "Enregistré et appliqué à chaque ouverture" : "Saved and applied on every launch"}</div>
        </div>
      </div>

      <div className="grid md:grid-cols-2 gap-4">
        <div className="rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] p-5">
          <h3 className="font-bold flex items-center gap-2"><Shield size={18}/> {t("settings.security")}</h3>
          <ul className="mt-3 space-y-2 text-sm list-disc ps-5 text-slate-600 dark:text-slate-400">
            <li>{lang === "ar" ? "تسجيل دخول آمن + بصمة/Face ID" : lang === "fr" ? "Connexion sécurisée + empreinte/Face ID" : "Secure login + Fingerprint/Face ID"}</li>
            <li>{lang === "ar" ? "تشفير EncryptedStorage للبيانات الحساسة" : lang === "fr" ? "Chiffrement EncryptedStorage pour données sensibles" : "EncryptedStorage encryption for sensitive data"}</li>
            <li>{lang === "ar" ? "صلاحيات: مدير/محامي/متربص/سكرتير" : lang === "fr" ? "Rôles : admin/avocat/stagiaire/secrétaire" : "Roles: admin/lawyer/trainee/secretary"}</li>
            <li>Audit Log — {lang === "ar" ? "من فتح/عدّل/حذف" : lang === "fr" ? "qui a ouvert/modifié/supprimé" : "who opened/edited/deleted"}</li>
            <li>{lang === "ar" ? "نسخ احتياطي تلقائي يومي" : lang === "fr" ? "Sauvegarde automatique quotidienne" : "Daily automatic backup"}</li>
          </ul>
        </div>
        <div className="rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] p-5">
          <h3 className="font-bold flex items-center gap-2"><Users size={18}/> {t("settings.team")}</h3>
          <div className="mt-3 space-y-2 text-sm">
            <div className="p-3 rounded-xl bg-slate-50 dark:bg-white/5 border dark:border-white/10 flex justify-between"><span>{lang === "ar" ? "مدير المكتب" : lang === "fr" ? "Directeur" : "Manager"}</span><span className="text-xs px-2 py-1 rounded-full bg-[#0e7490] text-white">{lang === "ar" ? "كل الصلاحيات" : lang === "fr" ? "Tous droits" : "All rights"}</span></div>
            <div className="p-3 rounded-xl bg-slate-50 dark:bg-white/5 border dark:border-white/10 flex justify-between"><span>{lang === "ar" ? "محامي" : lang === "fr" ? "Avocat" : "Lawyer"}</span><span className="text-xs px-2 py-1 rounded-full bg-slate-200 dark:bg-white/10">{lang === "ar" ? "ملفاته فقط" : lang === "fr" ? "Ses dossiers" : "Own cases"}</span></div>
            <div className="p-3 rounded-xl bg-slate-50 dark:bg-white/5 border dark:border-white/10 flex justify-between"><span>{lang === "ar" ? "سكرتير" : lang === "fr" ? "Secrétaire" : "Secretary"}</span><span className="text-xs px-2 py-1 rounded-full bg-slate-200 dark:bg-white/10">{lang === "ar" ? "جلسات ووثائق فقط" : lang === "fr" ? "Audiences & docs" : "Hearings & docs only"}</span></div>
          </div>
        </div>
        <div className="rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] p-5">
          <h3 className="font-bold flex items-center gap-2"><Database size={18}/> {t("settings.backup")}</h3>
          <div className="mt-3 text-sm text-slate-600 dark:text-slate-400">{lang === "ar" ? "آخر نسخة: اليوم 03:00 صباحاً — Google Drive / Local" : lang === "fr" ? "Dernière sauvegarde : aujourd'hui 03:00 — Google Drive / Local" : "Last backup: today 03:00 AM — Google Drive / Local"}</div>
          <button className="mt-3 w-full py-2.5 rounded-xl bg-[var(--primary,#0e7490)] text-white font-bold">{lang === "ar" ? "إنشاء نسخة الآن" : lang === "fr" ? "Créer une sauvegarde maintenant" : "Create backup now"}</button>
        </div>
        <div className="rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] p-5">
          <h3 className="font-bold flex items-center gap-2"><Lock size={18}/> {t("settings.encryption")}</h3>
          <div className="text-sm text-slate-500 mt-2">{lang === "ar" ? "جميع المفاتيح محفوظة بـ EncryptedSharedPreferences ولا تُرسل لخادم خارجي إلا برضا المستخدم." : lang === "fr" ? "Toutes les clés sont stockées via EncryptedSharedPreferences et ne sont envoyées à aucun serveur sans consentement." : "All keys are stored via EncryptedSharedPreferences and never sent to an external server without consent."}</div>
        </div>
      </div>
    </div>
  );
}
