"use client";
import { useState } from "react";
import { Sidebar } from "@/components/Sidebar";
import { Topbar } from "@/components/Topbar";
import { X } from "lucide-react";
import Link from "next/link";
import { usePathname } from "next/navigation";
import { LayoutDashboard, Users, Scale, CalendarDays, AlarmClock, Files, FileText, Wallet, Receipt, Building2, BarChart3, Bot, Archive, Settings, Gavel, MonitorDown } from "lucide-react";
import { cn } from "@/lib/utils";
import { useI18n } from "@/components/LanguageProvider";
import { LanguageSwitcher } from "@/components/LanguageSwitcher";

export default function DashboardLayout({ children }: { children: React.ReactNode }) {
  const [open, setOpen] = useState(false);
  const pathname = usePathname();
  const { t, lang } = useI18n();
  const isRTL = lang === "ar";

  const nav = [
    { href: "/dashboard", key: "nav.dashboard", icon: LayoutDashboard },
    { href: "/clients", key: "nav.clients", icon: Users },
    { href: "/cases", key: "nav.cases", icon: Scale },
    { href: "/sessions", key: "nav.sessions", icon: CalendarDays },
    { href: "/deadlines", key: "nav.deadlines", icon: AlarmClock },
    { href: "/documents", key: "nav.documents", icon: Files },
    { href: "/templates", key: "nav.templates", icon: FileText },
    { href: "/fees", key: "nav.fees", icon: Wallet },
    { href: "/expenses", key: "nav.expenses", icon: Receipt },
    { href: "/courts", key: "nav.courts", icon: Building2 },
    { href: "/reports", key: "nav.reports", icon: BarChart3 },
    { href: "/assistant", key: "nav.assistant", icon: Bot },
    { href: "/archive", key: "nav.archive", icon: Archive },
  ];

  return (
    <div className="min-h-dvh flex">
      <Sidebar />
      {/* Mobile drawer */}
      {open && (
        <div className="fixed inset-0 z-40 lg:hidden">
          <div className="absolute inset-0 bg-black/40" onClick={() => setOpen(false)} />
          <div className={`absolute top-0 h-full w-[300px] bg-white dark:bg-[#0f1b33] border dark:border-[#1e2e50] p-0 overflow-auto ${isRTL ? "right-0 border-l" : "left-0 border-r"}`}>
            <div className="p-4 border-b dark:border-[#1e2e50] flex items-center justify-between">
              <div className="flex items-center gap-2 font-display font-extrabold"><span className="w-8 h-8 rounded-lg bg-[#0e7490] grid place-items-center text-white"><Gavel size={16} /></span> {t("brand.name")}</div>
              <button onClick={() => setOpen(false)} className="w-9 h-9 grid place-items-center rounded-xl hover:bg-slate-100 dark:hover:bg-white/10"><X size={18} /></button>
            </div>
            <nav className="p-3 space-y-1">
              {nav.map(i => {
                const active = pathname === i.href;
                const Icon = i.icon;
                return (
                  <Link key={i.href} href={i.href} onClick={() => setOpen(false)} className={cn("flex items-center gap-3 px-3 py-2.5 rounded-xl", active ? "bg-[#0e7490] text-white" : "hover:bg-slate-50 dark:hover:bg-white/5")}>
                    <Icon size={18} /> {t(i.key)}
                  </Link>
                );
              })}
              <Link href="/download" onClick={() => setOpen(false)} className="flex items-center gap-3 px-3 py-2.5 rounded-xl hover:bg-slate-50 dark:hover:bg-white/5"><MonitorDown size={18} /> {t("nav.download")}</Link>
              <Link href="/settings" onClick={() => setOpen(false)} className="flex items-center gap-3 px-3 py-2.5 rounded-xl hover:bg-slate-50 dark:hover:bg-white/5"><Settings size={18} /> {t("nav.settings")}</Link>
              <div className="mt-4"><LanguageSwitcher /></div>
            </nav>
          </div>
        </div>
      )}

      <div className="flex-1 min-w-0 flex flex-col">
        <Topbar onMenu={() => setOpen(true)} />
        <main className="flex-1 p-4 lg:p-6 max-w-[1600px] w-full mx-auto">{children}</main>
      </div>
    </div>
  );
}
