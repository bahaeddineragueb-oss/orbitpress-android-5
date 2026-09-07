// lib/theme.ts — تخصيص الثيم والخط للمحامي
export type Theme = "light" | "dark" | "system";
export type PrimaryColor = "azraq" | "akhdar" | "banafseji" | "zahri";
export type FontSize = 14 | 15 | 16 | 17 | 18 | 19 | 20;

export const THEMES: Record<Theme, string> = {
  light: "فاتح ☀️",
  dark: "داكن 🌙",
  system: "حسب النظام 🖥️",
};

export const PRIMARY_COLORS: Record<PrimaryColor, { label: string; hex: string; bg: string }> = {
  azraq: { label: "أزرق مكتبي - maktabi", hex: "#0e7490", bg: "bg-[#0e7490]" },
  akhdar: { label: "أخضر عدالة", hex: "#059669", bg: "bg-[#059669]" },
  banafseji: { label: "بنفسجي قضاء", hex: "#7c3aed", bg: "bg-[#7c3aed]" },
  zahri: { label: "زهري", hex: "#db2777", bg: "bg-[#db2777]" },
};

export const FONT_SIZES: { value: FontSize; label: string; desc: string }[] = [
  { value: 14, label: "صغير", desc: "14px — للمحتوى الكثيف" },
  { value: 15, label: "صغير+", desc: "15px" },
  { value: 16, label: "متوسط", desc: "16px — افتراضي" },
  { value: 17, label: "متوسط+", desc: "17px" },
  { value: 18, label: "كبير", desc: "18px — للقراءة المريحة" },
  { value: 19, label: "كبير+", desc: "19px" },
  { value: 20, label: "كبير جداً", desc: "20px — لضعف البصر" },
];

export function applyTheme(theme: Theme, primary: PrimaryColor, fontSize: FontSize) {
  if (typeof document === "undefined") return;
  const root = document.documentElement;
  // Theme
  root.classList.remove("light", "dark");
  const resolved = theme === "system" ? (window.matchMedia("(prefers-color-scheme: dark)").matches ? "dark" : "light") : theme;
  root.classList.add(resolved);
  if (resolved === "dark") root.classList.add("dark");
  // Primary
  root.style.setProperty("--primary", PRIMARY_COLORS[primary].hex);
  // Font size
  root.style.fontSize = `${fontSize}px`;
  // Persist
  localStorage.setItem("maktabi-theme", theme);
  localStorage.setItem("maktabi-primary", primary);
  localStorage.setItem("maktabi-font", String(fontSize));
}

export function loadTheme(): { theme: Theme; primary: PrimaryColor; fontSize: FontSize } {
  if (typeof window === "undefined") return { theme: "light", primary: "azraq", fontSize: 16 };
  const t = (localStorage.getItem("maktabi-theme") as Theme) || "light";
  const p = (localStorage.getItem("maktabi-primary") as PrimaryColor) || "azraq";
  const f = Number(localStorage.getItem("maktabi-font") || 16) as FontSize;
  return { theme: t, primary: p, fontSize: f };
}
