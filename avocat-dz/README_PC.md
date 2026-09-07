# مكتبي — Logiciel PC (Windows) 🖥️

**تحميل مباشر للويندوز — يعمل بدون انترنت**

![Windows](https://img.shields.io/badge/Windows-10%2F11-0078D6) ![Electron](https://img.shields.io/badge/Electron-30-47848F) ![Offline](https://img.shields.io/badge/Offline-100%25-green)

## ⬇️ التحميل

| النسخة | الملف | الحجم | الرابط |
|---|---|---|---|
| **المثبت** (موصى به) | `Maktabi-Setup-1.0.0.exe` | ~85MB | [تحميل من /download](#) أو من Releases |
| **المحمولة** | `Maktabi-Portable-1.0.0.exe` | ~80MB | تعمل بدون تثبيت — USB |

## 🚀 التثبيت في دقيقة

1. حمّل `Maktabi-Setup-1.0.0.exe`
2. شغّله → اختر المجلد → أنشئ اختصار سطح المكتب
3. افتح **مكتبي** — جاهز!

## ✨ لماذا PC؟

- **Offline 100%** — لا يحتاج متصفح أو انترنت
- **بياناتك على جهازك**: `AppData/Roaming/maktabi/maktabi-data.json` (لا سحابة)
- **سريع وخفيف** — يفتح في ثانية
- **طباعة و PDF مباشرة** — `Ctrl+P`
- **قائمة سطح مكتب + اختصارات**: `Ctrl+N` عميل جديد، `Ctrl+1` لوحة التحكم
- **نسخ احتياطي بضغطة**: تصدير/استيراد JSON
- **مجلد وثائق**: `Documents/Maktabi/` — كل ملفاتك المنظمة

## 🛠️ بناء المثبت من المصدر

```bash
npm install
npm run dist:win      # → dist/Maktabi-Setup-1.0.0.exe
npm run dist:portable # → dist/Maktabi-Portable-1.0.0.exe
```

راجع `docs/PC_GUIDE_AR.md` للتفاصيل الكاملة.
