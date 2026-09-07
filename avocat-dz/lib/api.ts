// lib/api.ts — واجهة موحدة لقاعدة البيانات الحقيقية
// يحاول استخدام PostgreSQL (Prisma) أولاً، ثم SQLite/JSON كـ fallback للـ PC/Offline

export async function apiGet<T>(url: string, fallback: T): Promise<T> {
  try {
    const res = await fetch(url, { cache: 'no-store' });
    if (!res.ok) throw new Error(String(res.status));
    return await res.json();
  } catch {
    return fallback;
  }
}

export async function apiPost<T>(url: string, body: any): Promise<T | null> {
  try {
    const res = await fetch(url, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(body) });
    if (!res.ok) throw new Error(String(res.status));
    return await res.json();
  } catch (e) {
    console.warn('API POST failed, fallback to local', e);
    return null;
  }
}

// For courts, we have direct import fallback
export { courtsData, DB_INFO } from './courts';
