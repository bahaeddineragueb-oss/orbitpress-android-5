"use client";
import { useEffect } from "react";
import { applyTheme, loadTheme } from "@/lib/theme";

export function ThemeProvider({ children }: { children: React.ReactNode }) {
  useEffect(() => {
    const { theme, primary, fontSize } = loadTheme();
    applyTheme(theme, primary, fontSize);
    // Listen for system changes if system
    const mql = window.matchMedia("(prefers-color-scheme: dark)");
    const handler = () => {
      const { theme, primary, fontSize } = loadTheme();
      if (theme === "system") applyTheme(theme, primary, fontSize);
    };
    mql.addEventListener?.("change", handler);
    return () => mql.removeEventListener?.("change", handler);
  }, []);
  return <>{children}</>;
}
