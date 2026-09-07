"use client";
import { useState } from "react";
import { Sidebar } from "@/components/Sidebar";
import { Topbar } from "@/components/Topbar";
import { X } from "lucide-react";
import Link from "next/link";
import { usePathname } from "next/navigation";
import { LayoutDashboard, Users, Scale, CalendarDays, AlarmClock, Files, FileText, Wallet, Receipt, Building2, BarChart3, Bot, Archive, Settings, Gavel } from "lucide-react";
import { cn } from "@/lib/utils";

const nav = [
  { href: "/dashboard", label: "لوحة التحكم", icon: LayoutDashboard },
  { href: "/clients", label: "العملاء", icon: Users },
  { href: "/cases", label: "القضايا", icon: Scale },
  { href: "/sessions", label: "الجلسات", icon: CalendarDays },
  { href: "/deadlines", label: "الآجال", icon: AlarmClock },
  { href: "/documents", label: "الوثائق", icon: Files },
  { href: "/templates", label: "النماذج", icon: FileText },
  { href: "/fees", label: "الأتعاب", icon: Wallet },
  { href: "/expenses", label: "المصاريف", icon: Receipt },
  { href: "/courts", label: "المحاكم", icon: Building2 },
  { href: "/reports", label: "التقارير", icon: BarChart3 },
  { href: "/assistant", label: "المساعد الذكي", icon: Bot },
  { href: "/archive", label: "الأرشيف", icon: Archive },
];

export default function DashboardLayout({ children }: { children: React.ReactNode }) {
  const [open, setOpen] = useState(false);
  const pathname = usePathname();
  return (
    <div className="min-h-dvh flex">
      <Sidebar />
      {/* Mobile drawer */}
      {open && (
        <div className="fixed inset-0 z-40 lg:hidden">
          <div className="absolute inset-0 bg-black/40" onClick={() => setOpen(false)} />
          <div className="absolute right-0 top-0 h-full w-[300px] bg-white dark:bg-[#0f1b33] border-l dark:border-[#1e2e50] p-0 overflow-auto">
            <div className="p-4 border-b dark:border-[#1e2e50] flex items-center justify-between">
              <div className="flex items-center gap-2 font-display font-extrabold"><span className="w-8 h-8 rounded-lg bg-[#0e7490] grid place-items-center text-white"><Gavel size={16} /></span> مكتبي</div>
              <button onClick={() => setOpen(false)} className="w-9 h-9 grid place-items-center rounded-xl hover:bg-slate-100 dark:hover:bg-white/10"><X size={18} /></button>
            </div>
            <nav className="p-3 space-y-1">
              {nav.map(i => {
                const active = pathname === i.href;
                const Icon = i.icon;
                return (
                  <Link key={i.href} href={i.href} onClick={() => setOpen(false)} className={cn("flex items-center gap-3 px-3 py-2.5 rounded-xl", active ? "bg-[#0e7490] text-white" : "hover:bg-slate-50 dark:hover:bg-white/5")}>
                    <Icon size={18} /> {i.label}
                  </Link>
                );
              })}
              <Link href="/settings" className="flex items-center gap-3 px-3 py-2.5 rounded-xl hover:bg-slate-50 dark:hover:bg-white/5"><Settings size={18} /> الإعدادات</Link>
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
