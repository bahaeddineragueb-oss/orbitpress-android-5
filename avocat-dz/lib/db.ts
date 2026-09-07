// lib/db.ts — طبقة تخزين موحدة: Web (localStorage) + PC (Electron JSON)
export type StoreKey = 'makta_clients' | 'makta_cases' | 'makta_hearings' | 'makta_deadlines' | 'makta_docs' | 'makta_fees' | 'makta_expenses';

const isElectron = () => typeof window !== 'undefined' && (window as any).electronAPI?.isElectron;

async function loadElectron(): Promise<Record<string, any> | null> {
  try {
    const raw = await (window as any).electronAPI.dbLoad();
    if (!raw) return null;
    return JSON.parse(raw);
  } catch { return null; }
}

async function saveElectron(data: Record<string, any>) {
  try {
    await (window as any).electronAPI.dbSave(JSON.stringify(data));
  } catch {}
}

// Web fallback: localStorage
function loadWeb(key: StoreKey): any {
  try {
    const v = localStorage.getItem(key);
    return v ? JSON.parse(v) : null;
  } catch { return null; }
}
function saveWeb(key: StoreKey, value: any) {
  try { localStorage.setItem(key, JSON.stringify(value)); } catch {}
}

export async function dbGet<T>(key: StoreKey, fallback: T): Promise<T> {
  if (isElectron()) {
    const all = await loadElectron();
    if (all && key in all) return all[key] as T;
    return fallback;
  } else {
    const v = loadWeb(key);
    return v !== null ? v : fallback;
  }
}

export async function dbSet<T>(key: StoreKey, value: T): Promise<void> {
  if (isElectron()) {
    const all = (await loadElectron()) || {};
    all[key] = value;
    await saveElectron(all);
  } else {
    saveWeb(key, value);
  }
}

export function isPC(): boolean {
  return !!isElectron();
}

// Helpers for backup
export async function exportBackup(): Promise<string | null> {
  if (isElectron()) return await (window as any).electronAPI.dbExport();
  // web: download JSON
  const all: Record<string, any> = {};
  const keys: StoreKey[] = ['makta_clients','makta_cases','makta_hearings','makta_deadlines','makta_docs','makta_fees','makta_expenses'];
  keys.forEach(k => { const v = loadWeb(k); if (v) all[k]=v; });
  const blob = new Blob([JSON.stringify(all, null, 2)], { type: 'application/json' });
  const url = URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url; a.download = `maktabi-backup-${new Date().toISOString().slice(0,10)}.json`; a.click();
  URL.revokeObjectURL(url);
  return a.download;
}

export async function importBackup(): Promise<boolean> {
  if (isElectron()) {
    const ok = await (window as any).electronAPI.dbImport();
    if (ok) location.reload();
    return !!ok;
  }
  return false;
}
