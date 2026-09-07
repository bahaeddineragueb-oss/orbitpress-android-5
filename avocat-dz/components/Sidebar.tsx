"use client";
import Link from "next/link";
import { usePathname } from "next/navigation";
import { LayoutDashboard, Users, Scale, CalendarDays, AlarmClock, Files, FileText, Wallet, Receipt, Building2, BarChart3, Bot, Archive, Settings, Gavel, Sparkles, MonitorDown } from "lucide-react";
import { cn } from "@/lib/utils";

const nav = [
  { href: "/dashboard", label: "لوحة التحكم", icon: LayoutDashboard },
  { href: "/clients", label: "العملاء", icon: Users },
  { href: "/cases", label: "القضايا والملفات", icon: Scale },
  { href: "/sessions", label: "الجلسات والمواعيد", icon: CalendarDays },
  { href: "/deadlines", label: "الآجال والتنبيهات", icon: AlarmClock, badge: "4" },
  { href: "/documents", label: "الوثائق", icon: Files },
  { href: "/templates", label: "النماذج القانونية", icon: FileText },
  { href: "/fees", label: "الأتعاب", icon: Wallet },
  { href: "/expenses", label: "المصاريف", icon: Receipt },
  { href: "/courts", label: "المحاكم", icon: Building2 },
  { href: "/reports", label: "التقارير", icon: BarChart3 },
  { href: "/assistant", label: "المساعد الذكي", icon: Bot },
  { href: "/archive", label: "الأرشيف", icon: Archive },
  { href: "/download", label: "تحميل للـ PC", icon: MonitorDown },
];

export function Sidebar() {
  const pathname = usePathname();
  return (
    <aside className="hidden lg:flex w-[300px] shrink-0 flex-col border-l bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] sticky top-0 h-[100dvh] overflow-hidden">
      <div className="p-6 border-b dark:border-[#1e2e50]">
        <Link href="/dashboard" className="flex items-center gap-3">
          <div className="w-10 h-10 rounded-xl bg-gradient-to-br from-[#0e7490] to-[#063544] grid place-items-center text-white shadow-lg">
            <Gavel size={20} />
          </div>
          <div>
            <div className="font-display font-extrabold text-[18px] leading-none">مكتبي</div>
            <div className="text-xs text-slate-500 dark:text-slate-400">Avocat DZ Pro • v1.0</div>
          </div>
          <Sparkles size={16} className="mr-auto text-amber-500" />
        </Link>
        <div className="mt-4 p-3 rounded-xl bg-gradient-to-br from-cyan-50 to-blue-50 dark:from-[#0b2a3a] dark:to-[#0f1b33] border border-cyan-100 dark:border-[#1a3a52]">
          <div className="text-xs font-bold text-cyan-800 dark:text-cyan-200">مكتب الأستاذ بهاء الدين</div>
          <div className="text-[11px] text-cyan-700/70 dark:text-cyan-300/70">الجزائر • الرويبة • 0550 12 34 56</div>
        </div>
      </div>

      <nav className="flex-1 overflow-auto p-3 space-y-1">
        {nav.map((item) => {
          const active = pathname === item.href || pathname?.startsWith(item.href + "/");
          const Icon = item.icon;
          return (
            <Link
              key={item.href}
              href={item.href}
              className={cn(
                "flex items-center gap-3 px-3 py-2.5 rounded-xl text-[14px] transition",
                active
                  ? "bg-[#0e7490] text-white shadow-md"
                  : "hover:bg-slate-50 dark:hover:bg-white/5 text-slate-700 dark:text-slate-300"
              )}
            >
              <Icon size={18} className={cn(active ? "text-white" : "text-slate-500")} />
              <span className="font-medium">{item.label}</span>
              {item.badge && (
                <span className={cn("mr-auto text-xs px-2 py-0.5 rounded-full font-bold", active ? "bg-white text-[#0e7490]" : "bg-red-500 text-white")}>
                  {item.badge}
                </span>
              )}
            </Link>
          );
        })}
      </nav>

      <div className="p-3 border-t dark:border-[#1e2e50] space-y-2">
        <Link href="/download" className="flex items-center gap-3 px-3 py-2.5 rounded-xl bg-gradient-to-br from-[#0e7490] to-[#063544] text-white font-bold shadow">
          <MonitorDown size={18} /> تحميل للـ PC
        </Link>
        <Link href="/settings" className="flex items-center gap-3 px-3 py-2.5 rounded-xl hover:bg-slate-50 dark:hover:bg-white/5 text-slate-700 dark:text-slate-300">
          <Settings size={18} /> الإعدادات
        </Link>
        <div className="mt-3 text-[11px] text-center text-slate-400">© 2026 مكتبي — صُنع في الجزائر 🇩🇿</div>
      </div>
    </aside>
  );
}
