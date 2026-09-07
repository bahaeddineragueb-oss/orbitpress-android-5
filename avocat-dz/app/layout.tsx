import "./globals.css";
import type { Metadata } from "next";

export const metadata: Metadata = {
  title: "مكتبي — Avocat DZ Pro | تسيير مكاتب المحاماة",
  description: "نظام احترافي لتسيير مكاتب المحاماة في الجزائر: العملاء، القضايا، الجلسات، الآجال، الوثائق، الأتعاب",
};

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="ar" dir="rtl">
      <body className="min-h-dvh bg-[#f8fafc] dark:bg-[#070e1f] text-slate-900 dark:text-slate-100">
        {children}
      </body>
    </html>
  );
}
