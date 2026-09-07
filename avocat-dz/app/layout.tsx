import "./globals.css";
import type { Metadata } from "next";
import { ThemeProvider } from "@/components/ThemeProvider";
import { LanguageProvider } from "@/components/LanguageProvider";

export const metadata: Metadata = {
  title: "مكتبي - maktabi — Avocat DZ Pro | تسيير مكاتب المحاماة",
  description: "مكتبي - maktabi — نظام احترافي ثلاثي اللغات لتسيير مكاتب المحاماة في الجزائر: العملاء، القضايا، الجلسات، الآجال، الوثائق، الأتعاب — Arabe / Français / English",
  keywords: ["مكتبي - maktabi", "مكتبي", "maktabi", "Avocat", "Algerie", "DZ", "cabinet avocat", "law firm", "محامي"],
  openGraph: {
    title: "مكتبي - maktabi — Avocat DZ Pro",
    description: "مكتبي - maktabi — نظام احترافي لتسيير مكاتب المحاماة — Arabe / Français / English — يعمل على الويب و Windows بدون انترنت",
  },
};

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="ar" dir="rtl" suppressHydrationWarning>
      <body className="min-h-dvh bg-[#f8fafc] dark:bg-[#070e1f] text-slate-900 dark:text-slate-100">
        <ThemeProvider>
          <LanguageProvider>{children}</LanguageProvider>
        </ThemeProvider>
      </body>
    </html>
  );
}
