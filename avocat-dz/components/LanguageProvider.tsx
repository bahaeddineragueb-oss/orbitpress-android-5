"use client";
import { createContext, useContext, useEffect, useState, ReactNode } from "react";
import { Lang, LANGUAGES, translations, getLangFromStorage, setLangStorage } from "@/lib/i18n";

type Ctx = {
  lang: Lang;
  dir: "rtl" | "ltr";
  t: (key: string) => string;
  setLang: (l: Lang) => void;
};

const I18nContext = createContext<Ctx>({
  lang: "ar",
  dir: "rtl",
  t: (k) => translations.ar[k] ?? k,
  setLang: () => {},
});

export function useI18n() {
  return useContext(I18nContext);
}

export function LanguageProvider({ children }: { children: ReactNode }) {
  const [lang, setLangState] = useState<Lang>("ar");

  useEffect(() => {
    const stored = getLangFromStorage();
    setLangState(stored);
    setLangStorage(stored);
    // ensure dir on mount
    document.documentElement.lang = stored;
    document.documentElement.dir = LANGUAGES[stored].dir;
  }, []);

  const setLang = (l: Lang) => {
    setLangState(l);
    setLangStorage(l);
  };

  const t = (key: string) => translations[lang][key] ?? translations.ar[key] ?? key;

  const dir = LANGUAGES[lang].dir;

  // keep dir in sync if lang changes externally
  useEffect(() => {
    document.documentElement.lang = lang;
    document.documentElement.dir = dir;
  }, [lang, dir]);

  return (
    <I18nContext.Provider value={{ lang, dir, t, setLang }}>
      <div dir={dir} lang={lang} className={lang === "ar" ? "font-ar" : "font-latin"}>
        {children}
      </div>
    </I18nContext.Provider>
  );
}
