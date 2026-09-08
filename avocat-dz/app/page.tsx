"use client";
import { useEffect } from "react";
import { useRouter } from "next/navigation";

export default function RootPage() {
  const router = useRouter();
  useEffect(() => { router.replace("/dashboard"); }, [router]);
  return (
    <div className="min-h-dvh grid place-items-center bg-[#f8fafc] dark:bg-[#070e1f]">
      <div className="text-center">
        <div className="w-10 h-10 mx-auto rounded-xl bg-[#0e7490] animate-pulse" />
        <div className="mt-4 font-bold text-slate-600">مكتبي - maktabi • جاري التحميل...</div>
      </div>
    </div>
  );
}
