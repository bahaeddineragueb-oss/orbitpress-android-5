import { LucideIcon } from "lucide-react";
import { cn } from "@/lib/utils";

export function StatCard({
  title, value, sub, icon: Icon, color = "cyan", trend,
}: {
  title: string; value: string | number; sub?: string; icon: LucideIcon; color?: "cyan" | "amber" | "emerald" | "rose" | "violet"; trend?: string;
}) {
  const map: Record<string, string> = {
    cyan: "from-cyan-500 to-blue-600",
    amber: "from-amber-500 to-orange-600",
    emerald: "from-emerald-500 to-teal-600",
    rose: "from-rose-500 to-pink-600",
    violet: "from-violet-500 to-indigo-600",
  };
  return (
    <div className="rounded-2xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] p-5 shadow-card">
      <div className="flex items-start justify-between">
        <div>
          <div className="text-sm text-slate-500 dark:text-slate-400">{title}</div>
          <div className="mt-1 text-2xl font-extrabold font-display">{value}</div>
          {sub && <div className="text-xs text-slate-500 mt-1">{sub}</div>}
        </div>
        <div className={cn("w-11 h-11 rounded-xl grid place-items-center text-white shadow-lg bg-gradient-to-br", map[color])}>
          <Icon size={20} />
        </div>
      </div>
      {trend && <div className="mt-3 text-xs px-2 py-1 rounded-full bg-slate-50 dark:bg-white/5 border dark:border-white/10 inline-block">{trend}</div>}
    </div>
  );
}
