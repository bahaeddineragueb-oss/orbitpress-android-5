import { clsx, type ClassValue } from "clsx";
import { twMerge } from "tailwind-merge";
export function cn(...inputs: ClassValue[]) { return twMerge(clsx(inputs)); }
export const uid = () => Math.random().toString(36).slice(2, 9);
export const formatDZD = (n: number) => new Intl.NumberFormat("ar-DZ", { style: "currency", currency: "DZD", maximumFractionDigits: 0 }).format(n);
export const formatDate = (d: string | Date) => new Intl.DateTimeFormat("ar-DZ", { day: "2-digit", month: "long", year: "numeric" }).format(new Date(d));
export const formatDateShort = (d: string | Date) => new Intl.DateTimeFormat("ar-DZ", { day: "2-digit", month: "2-digit", year: "numeric" }).format(new Date(d));
export const daysUntil = (d: string) => Math.ceil((new Date(d).getTime() - Date.now()) / 86400000);
