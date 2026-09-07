const { app, BrowserWindow, ipcMain, dialog, shell, Menu } = require('electron');
const path = require('path');
const fs = require('fs');
const isDev = !app.isPackaged;

// Single instance
const gotLock = app.requestSingleInstanceLock();
if (!gotLock) app.quit();
else {
  app.on('second-instance', () => {
    const win = BrowserWindow.getAllWindows()[0];
    if (win) { if (win.isMinimized()) win.restore(); win.focus(); }
  });
}

let mainWindow;

function getDataPath() {
  const userData = app.getPath('userData');
  if (!fs.existsSync(userData)) fs.mkdirSync(userData, { recursive: true });
  return path.join(userData, 'maktabi-data.json');
}

function getDocsPath() {
  const docs = path.join(app.getPath('documents'), 'Maktabi');
  if (!fs.existsSync(docs)) fs.mkdirSync(docs, { recursive: true });
  return docs;
}

function createWindow() {
  mainWindow = new BrowserWindow({
    width: 1400,
    height: 900,
    minWidth: 1100,
    minHeight: 700,
    show: false,
    backgroundColor: '#070e1f',
    title: 'مكتبي — Avocat DZ Pro',
    icon: path.join(__dirname, '../public/icon.png'),
    webPreferences: {
      preload: path.join(__dirname, 'preload.js'),
      contextIsolation: true,
      nodeIntegration: false,
      sandbox: false,
    },
    autoHideMenuBar: false,
  });

  // Menu AR
  const template = [
    {
      label: 'ملف',
      submenu: [
        { label: 'لوحة التحكم', click: () => mainWindow.loadURL(getStartURL('/dashboard')) , accelerator: 'CmdOrCtrl+1' },
        { label: 'عميل جديد', click: () => mainWindow.loadURL(getStartURL('/clients')), accelerator: 'CmdOrCtrl+N' },
        { type: 'separator' },
        { label: 'نسخ احتياطي', click: () => handleBackup() },
        { label: 'استيراد نسخة', click: () => handleRestore() },
        { type: 'separator' },
        { role: 'quit', label: 'خروج' },
      ]
    },
    { label: 'عرض', submenu: [
      { role: 'reload', label: 'تحديث' },
      { role: 'toggleDevTools', label: 'أدوات المطور' },
      { type: 'separator' },
      { role: 'resetZoom', label: 'حجم افتراضي' },
      { role: 'zoomIn', label: 'تكبير' },
      { role: 'zoomOut', label: 'تصغير' },
      { role: 'togglefullscreen', label: 'ملء الشاشة' },
    ]},
    { label: 'مساعدة', submenu: [
      { label: 'حول مكتبي', click: () => dialog.showMessageBox(mainWindow, { type: 'info', title: 'حول مكتبي', message: 'مكتبي — Avocat DZ Pro v1.0\nنظام تسيير مكاتب المحاماة الجزائرية\nصُنع في الجزائر 🇩🇿\n\nالدعم: support@maktabi.dz' }) },
      { label: 'فتح مجلد الوثائق', click: () => shell.openPath(getDocsPath()) },
      { label: 'فتح مجلد البيانات', click: () => shell.openPath(app.getPath('userData')) },
    ]}
  ];
  Menu.setApplicationMenu(Menu.buildFromTemplate(template));

  const startURL = getStartURL('/');
  if (isDev) {
    mainWindow.loadURL('http://localhost:3000');
    // mainWindow.webContents.openDevTools();
  } else {
    mainWindow.loadURL(startURL);
  }

  mainWindow.once('ready-to-show', () => mainWindow.show());
  mainWindow.webContents.setWindowOpenHandler(({ url }) => { shell.openExternal(url); return { action: 'deny' }; });
}

function getStartURL(route) {
  if (isDev) return `http://localhost:3000${route}`;
  // production: load from out folder
  const outPath = path.join(__dirname, '../out', route === '/' ? 'index.html' : `${route.replace(/^\//,'')}/index.html`);
  // fallback to index.html if route file not exist (SPA routing)
  if (fs.existsSync(outPath)) return `file://${outPath}`;
  return `file://${path.join(__dirname, '../out/index.html')}`;
}

// IPC: Data persistence (JSON in userData)
ipcMain.handle('db:load', () => {
  try {
    const p = getDataPath();
    if (!fs.existsSync(p)) return null;
    return fs.readFileSync(p, 'utf8');
  } catch (e) { return null; }
});

ipcMain.handle('db:save', (e, data) => {
  try {
    fs.writeFileSync(getDataPath(), typeof data === 'string' ? data : JSON.stringify(data, null, 2), 'utf8');
    return true;
  } catch (err) { return false; }
});

ipcMain.handle('db:export', async () => {
  const { canceled, filePath } = await dialog.showSaveDialog(mainWindow, {
    title: 'حفظ نسخة احتياطية',
    defaultPath: `maktabi-backup-${new Date().toISOString().slice(0,10)}.json`,
    filters: [{ name: 'JSON', extensions: ['json'] }]
  });
  if (canceled || !filePath) return null;
  try {
    const data = fs.existsSync(getDataPath()) ? fs.readFileSync(getDataPath(), 'utf8') : '{}';
    fs.writeFileSync(filePath, data, 'utf8');
    return filePath;
  } catch { return null; }
});

ipcMain.handle('db:import', async () => {
  const { canceled, filePaths } = await dialog.showOpenDialog(mainWindow, {
    title: 'اختر ملف النسخة',
    filters: [{ name: 'JSON', extensions: ['json'] }],
    properties: ['openFile']
  });
  if (canceled || !filePaths[0]) return null;
  try {
    const data = fs.readFileSync(filePaths[0], 'utf8');
    JSON.parse(data); // validate
    fs.writeFileSync(getDataPath(), data, 'utf8');
    return true;
  } catch { return false; }
});

// Files
ipcMain.handle('file:save', async (e, { defaultName, buffer, filters }) => {
  const { canceled, filePath } = await dialog.showSaveDialog(mainWindow, {
    defaultPath: defaultName,
    filters: filters || [{ name: 'All Files', extensions: ['*'] }]
  });
  if (canceled || !filePath) return null;
  try {
    fs.writeFileSync(filePath, Buffer.from(buffer));
    shell.showItemInFolder(filePath);
    return filePath;
  } catch { return null; }
});

ipcMain.handle('file:open', async () => {
  const { canceled, filePaths } = await dialog.showOpenDialog(mainWindow, { properties: ['openFile'] });
  if (canceled) return null;
  return filePaths[0];
});

ipcMain.handle('file:openDocs', () => shell.openPath(getDocsPath()));
ipcMain.handle('app:getPath', (e, name) => app.getPath(name));
ipcMain.handle('app:getVersion', () => app.getVersion());

// Notifications — تنبيهات الجلسات (الساعة)
const { Notification } = require('electron');
ipcMain.handle('notify', (e, { title, body }) => {
  try {
    if (Notification.isSupported()) {
      const n = new Notification({ title: title || 'مكتبي — تنبيه جلسة', body: body || '', icon: path.join(__dirname, '../public/icon.png'), silent: false });
      n.show();
      n.on('click', () => { if (mainWindow) { if (mainWindow.isMinimized()) mainWindow.restore(); mainWindow.focus(); } });
    }
    return true;
  } catch { return false; }
});

async function handleBackup() {
  const res = await dialog.showSaveDialog(mainWindow, { defaultPath: `maktabi-backup-${new Date().toISOString().slice(0,10)}.json`, filters: [{ name: 'JSON', extensions: ['json'] }] });
  if (!res.canceled && res.filePath) {
    try {
      const data = fs.existsSync(getDataPath()) ? fs.readFileSync(getDataPath(), 'utf8') : '{}';
      fs.writeFileSync(res.filePath, data);
      dialog.showMessageBox(mainWindow, { type: 'info', message: `تم حفظ النسخة في:\n${res.filePath}` });
    } catch {}
  }
}
async function handleRestore() {
  const res = await dialog.showOpenDialog(mainWindow, { properties: ['openFile'], filters: [{ name: 'JSON', extensions: ['json'] }] });
  if (!res.canceled && res.filePaths[0]) {
    try {
      const data = fs.readFileSync(res.filePaths[0], 'utf8');
      JSON.parse(data);
      fs.writeFileSync(getDataPath(), data);
      dialog.showMessageBox(mainWindow, { type: 'info', message: 'تم استيراد النسخة بنجاح. سيتم إعادة التشغيل.' });
      app.relaunch(); app.exit();
    } catch { dialog.showErrorBox('خطأ', 'ملف النسخة غير صالح'); }
  }
}

app.whenReady().then(createWindow);
app.on('window-all-closed', () => { if (process.platform !== 'darwin') app.quit(); });
app.on('activate', () => { if (BrowserWindow.getAllWindows().length === 0) createWindow(); });
