"use client";
import { useEffect, useState } from "react";
import { Search, Bell, Moon, Sun, Menu, Gavel } from "lucide-react";

export function Topbar({ onMenu }: { onMenu?: () => void }) {
  const [q, setQ] = useState("");
  const [dark, setDark] = useState(false);

  useEffect(() => {
    const isDark = document.documentElement.classList.contains("dark");
    setDark(isDark);
  }, []);

  const toggleDark = () => {
    document.documentElement.classList.toggle("dark");
    setDark((v) => !v);
  };

  return (
    <header className="sticky top-0 z-30 backdrop-blur-xl bg-white/80 dark:bg-[#070e1f]/80 border-b dark:border-[#1e2e50]">
      <div className="flex items-center gap-3 px-4 lg:px-6 py-3">
        <button onClick={onMenu} className="lg:hidden p-2 rounded-xl hover:bg-slate-100 dark:hover:bg-white/10">
          <Menu size={20} />
        </button>

        <div className="hidden lg:flex items-center gap-2 text-sm">
          <span className="w-8 h-8 rounded-lg bg-[#0e7490] grid place-items-center text-white"><Gavel size={16} /></span>
          <span className="font-display font-bold">مكتبي</span>
          <span className="text-slate-400">/</span>
          <span className="text-slate-500">لوحة التحكم</span>
        </div>

        <div className="flex-1 flex justify-center">
          <div className="relative w-full max-w-[560px]">
            <Search size={18} className="absolute right-3 top-1/2 -translate-y-1/2 text-slate-400" />
            <input
              value={q}
              onChange={(e) => setQ(e.target.value)}
              placeholder="بحث ذكي: اسم عميل، رقم قضية، محكمة، وثيقة..."
              className="w-full pr-10 pl-4 py-2.5 rounded-xl border bg-slate-50 dark:bg-[#0f1b33] dark:border-[#1e2e50] focus:outline-none focus:ring-2 focus:ring-[#0e7490]/20 focus:border-[#0e7490] text-sm"
            />
            <kbd className="hidden md:inline absolute left-2 top-1/2 -translate-y-1/2 text-[11px] px-2 py-1 rounded bg-white dark:bg-[#1e2e50] border dark:border-[#2a3f66] text-slate-500">⌘ K</kbd>
          </div>
        </div>

        <div className="flex items-center gap-2">
          <button onClick={toggleDark} className="w-10 h-10 grid place-items-center rounded-xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50] hover:bg-slate-50">
            {dark ? <Sun size={18} /> : <Moon size={18} />}
          </button>
          <button className="relative w-10 h-10 grid place-items-center rounded-xl border bg-white dark:bg-[#0f1b33] dark:border-[#1e2e50]">
            <Bell size={18} />
            <span className="absolute -top-1 -left-1 w-5 h-5 grid place-items-center bg-red-500 text-white text-[11px] rounded-full font-bold">4</span>
          </button>
          <img src="https://i.pravatar.cc/100?img=15" alt="avatar" className="w-10 h-10 rounded-xl object-cover border-2 border-white shadow" />
        </div>
      </div>
    </header>
  );
}
