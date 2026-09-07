"use client";
import { useI18n } from "@/components/LanguageProvider";
import { Lang, LANGUAGES } from "@/lib/i18n";
import { Languages } from "lucide-react";

export function LanguageSwitcher({ compact }: { compact?: boolean }) {
  const { lang, setLang } = useI18n();
  const langs: Lang[] = ["ar", "fr", "en"];

  if (compact) {
    return (
      <div className="flex rounded-xl overflow-hidden border dark:border-[#1e2e50] text-xs font-bold">
        {langs.map((l) => (
          <button
            key={l}
            onClick={() => setLang(l)}
            className={`flex-1 py-2 px-2 transition ${lang === l ? "bg-[#0e7490] text-white" : "bg-white dark:bg-[#070e1f] hover:bg-slate-50"}`}
          >
            {l.toUpperCase()}
          </button>
        ))}
      </div>
    );
  }

  return (
    <div className="space-y-2">
      <div className="flex items-center gap-2 text-xs font-bold text-slate-600 dark:text-slate-400">
        <Languages size={14} /> اللغة • Langue • Language
      </div>
      <div className="grid grid-cols-3 gap-2">
        {langs.map((l) => (
          <button
            key={l}
            onClick={() => setLang(l)}
            className={`py-2.5 rounded-xl border font-bold text-sm flex flex-col items-center gap-1 transition ${
              lang === l
                ? "bg-[#0e7490] text-white border-[#0e7490] shadow"
                : "bg-white dark:bg-white/5 border-slate-200 dark:border-white/10 hover:border-[#0e7490]"
            }`}
          >
            <span className="text-lg">{LANGUAGES[l].flag}</span>
            <span className="text-xs">{LANGUAGES[l].label}</span>
          </button>
        ))}
      </div>
    </div>
  );
}
