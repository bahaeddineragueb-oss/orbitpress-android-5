"use client";
import Link from "next/link";
import { usePathname } from "next/navigation";
import { LayoutDashboard, Users, Scale, CalendarDays, Wallet, Settings, Gavel, Sparkles } from "lucide-react";
import { cn } from "@/lib/utils";
import { useI18n } from "@/components/LanguageProvider";
import { LanguageSwitcher } from "@/components/LanguageSwitcher";

export function Sidebar() {
  const pathname = usePathname();
  const { t, lang } = useI18n();

  const nav = [
    { href: "/dashboard", key: "nav.dashboard", icon: LayoutDashboard },
    { href: "/clients", key: "nav.clients", icon: Users },
    { href: "/cases", key: "nav.cases", icon: Scale },
    { href: "/sessions", key: "nav.sessions", icon: CalendarDays },
    { href: "/treasury", key: "nav.treasury", icon: Wallet },
  ];

  const isRTL = lang === "ar";

  return (
    <aside className={`hidden lg:flex w-[300px] shrink-0 flex-col bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] sticky top-0 h-[100dvh] overflow-hidden ${isRTL ? "border-l" : "border-r"}`}>
      <div className="p-6 border-b dark:border-[#1e2e50]">
        <Link href="/dashboard" className="flex items-center gap-3">
          <div className="w-10 h-10 rounded-xl bg-gradient-to-br from-[#0e7490] to-[#063544] grid place-items-center text-white shadow-lg">
            <Gavel size={20} />
          </div>
          <div>
            <div className="font-display font-extrabold text-[18px] leading-none">{t("brand.name")}</div>
            <div className="text-xs text-slate-500 dark:text-slate-400">مكتبي - maktabi • v1.0</div>
          </div>
          <Sparkles size={16} className="ms-auto text-amber-500" />
        </Link>
        <div className="mt-4 p-3 rounded-xl bg-gradient-to-br from-cyan-50 to-blue-50 dark:from-[#0b2a3a] dark:to-[#0f1b33] border border-cyan-100 dark:border-[#1a3a52]">
          <div className="text-xs font-bold text-cyan-800 dark:text-cyan-200">{lang === "ar" ? "مكتب الأستاذ بهاء الدين" : lang === "fr" ? "Cabinet Me Bahaa Eddine" : "Office of Me Bahaa Eddine"}</div>
          <div className="text-[11px] text-cyan-700/70 dark:text-cyan-300/70">Alger • Rouiba • 0550 12 34 56</div>
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
              <span className="font-medium">{t(item.key)}</span>
              {item.badge && (
                <span className={cn("ms-auto text-xs px-2 py-0.5 rounded-full font-bold", active ? "bg-white text-[#0e7490]" : "bg-red-500 text-white")}>
                  {item.badge}
                </span>
              )}
            </Link>
          );
        })}
      </nav>

      <div className="p-3 border-t dark:border-[#1e2e50] space-y-2">
        <LanguageSwitcher compact />
        <Link href="/settings" className="flex items-center gap-3 px-3 py-2.5 rounded-xl hover:bg-slate-50 dark:hover:bg-white/5 text-slate-700 dark:text-slate-300">
          <Settings size={18} /> {t("nav.settings")}
        </Link>
        <div className="mt-3 text-[11px] text-center text-slate-400">{t("brand.copyright")}</div>
      </div>
    </aside>
  );
}
