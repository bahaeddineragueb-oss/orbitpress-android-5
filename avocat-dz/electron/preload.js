const { contextBridge, ipcRenderer } = require('electron');

contextBridge.exposeInMainWorld('electronAPI', {
  isElectron: true,
  // DB
  dbLoad: () => ipcRenderer.invoke('db:load'),
  dbSave: (data) => ipcRenderer.invoke('db:save', data),
  dbExport: () => ipcRenderer.invoke('db:export'),
  dbImport: () => ipcRenderer.invoke('db:import'),
  // Files
  saveFile: (opts) => ipcRenderer.invoke('file:save', opts),
  openFile: () => ipcRenderer.invoke('file:open'),
  openDocsFolder: () => ipcRenderer.invoke('file:openDocs'),
  // Notifications — تنبيهات الجلسات (الساعة)
  notify: (title, body) => ipcRenderer.invoke('notify', { title, body }),
  // App
  getPath: (name) => ipcRenderer.invoke('app:getPath', name),
  getVersion: () => ipcRenderer.invoke('app:getVersion'),
  platform: process.platform,
});

console.log('[Maktabi] Preload ready — Electron API exposed');
