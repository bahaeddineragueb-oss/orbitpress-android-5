# مكتبي - maktabi — Avocat DZ Pro ⚖️

**نظام ثلاثي اللغات لتسيير مكاتب المحاماة الجزائرية — Arabe / Français / English**

> العميل → القضية → الإجراءات والجلسات → الوثائق → الأتعاب → المصاريف → الآجال → الأرشيف

[![Next.js](https://img.shields.io/badge/Next.js-14-black)](https://nextjs.org) [![Electron](https://img.shields.io/badge/Electron-30-47848F)](https://www.electronjs.org) [![Windows](https://img.shields.io/badge/Windows-10%2F11-0078D6)](https://microsoft.com) [![i18n](https://img.shields.io/badge/i18n-AR%2FFR%2FEN-blue)](./avocat-dz/lib/i18n.ts) [![Offline](https://img.shields.io/badge/Offline-100%25-green)]() [![License](https://img.shields.io/badge/License-MIT-yellow)]()

**الاسم الرسمي للمشروع هو `مكتبي - maktabi`** — لا OrbitPress. هذا الريبو يحتوي على تطبيق الويب + logiciel PC (Windows .exe) في المجلد `avocat-dz/`.

> **تنبيه حول اسم الريبو على GitHub:** إذا كان لا يزال يظهر باسم `orbitpress-android-5` على GitHub، غيّره يدوياً من GitHub → Settings → General → Repository name → `maktabi` → Rename. الكود المحلي صار جاهزاً باسم `maktabi` (انظر `avocat-dz/package.json` : `name: "maktabi"`, `homepage: https://github.com/bahaeddineragueb-oss/maktabi`).

---

## 🌐 ثلاث لغات — بشكل جيد

| اللغة | الاتجاه | التغطية |
|---|---|---|
| **العربية 🇩🇿** | RTL | 100% — الواجهة الافتراضية |
| **Français 🇫🇷** | LTR | 100% — كل القوائم والصفحات |
| **English 🇬🇧** | LTR | 100% — كل القوائم والصفحات |

- التبديل فوري من **Topbar** (سطح المكتب) أو **Sidebar** أو **الإعدادات → اللغة**، ويُحفظ في `localStorage: maktabi-lang`.
- `LanguageProvider` يضبط `html[lang]` و `html[dir]` تلقائياً (RTL للعربية، LTR للفرنسية/الإنجليزية).
- جميع القوائم (Sidebar + Topbar + Landing + Dashboard + Settings) مترجمة عبر `lib/i18n.ts`.

```ts
// lib/i18n.ts
import { useI18n } from "@/components/LanguageProvider";
const { t, lang, setLang } = useI18n();
t("nav.dashboard") // لوحة التحكم / Tableau de bord / Dashboard
```

### أين زر اللغة؟
- **سطح المكتب:** Topbar → زر `AR / FR / EN` يفتح قائمة اختيار.
- **الهاتف:** داخل Topbar (compact) + داخل درج الـ Sidebar.
- **الإعدادات:** `/settings` → قسم **اللغة — Langue — Language** مع معاينة حية.

---

## 🎯 الفكرة

بُني حول **دورة حياة الملف كاملة**:

- **Dashboard** — جلسات اليوم، آجال قريبة، ملفات نشطة، أتعاب غير محصلة
- **العملاء** — ملف كامل، سجل تواصل، وثائق، كل قضاياه
- **القضايا** — Kanban + جدول، Timeline، استراتيجية الدفاع
- **الجلسات والآجال** — تقويم + تنبيهات “بقي 10 أيام على الاستئناف” + إشعارات Electron/ويب
- **الوثائق** — مجلد إلكتروني لكل ملف (9 تصنيفات + PDF)
- **النماذج القانونية** — `{{client}}` → نص جاهز للطباعة
- **الأتعاب والمصاريف** — دفعات + صافي المداخيل + Charts
- **قاعدة المحاكم** — 58 ولاية + 256 محكمة + 58 إدارية + الهيئات العليا + التجارية المتخصصة (60 جهة، 379 محكمة — من وزارة العدل)
- **المساعد الذكي** — تلخيص ملف، استخراج آجال، مسودة مذكرة

---

## 🖥️ Logiciel PC (Windows)

| النسخة | الملف | الحجم |
|---|---|---|
| **المثبت** | `Maktabi-Setup-1.0.0.exe` | ~85MB |
| **المحمولة** | `Maktabi-Portable-1.0.0.exe` | ~80MB |

```bash
cd avocat-dz
npm install --ignore-scripts   # بدون تحميل Electron (لتفادي مشاكل الشهادة)
npx next build                 # أو ELECTRON=true npx next build للـ PC
npm run dist:win               # يحتاج Windows + Electron مثبت
```

- بياناتك تُحفظ في `AppData/Roaming/maktabi/maktabi-data.json` (لا سحابة).
- المثبت ثلاثي اللغات: `nsis.installerLanguages: [en_US, fr_FR, ar_AR]`.

---

## 🚀 التشغيل

```bash
cd avocat-dz
npm install --ignore-scripts
npm run dev              # http://localhost:3000  (AR/FR/EN)
npm run build            # production
```

---

## 📁 الهيكل

```
avocat-dz/
├─ lib/i18n.ts                    # ← القاموس ثلاثي اللغات
├─ components/LanguageProvider.tsx # ← مزود اللغة + hook useI18n
├─ components/LanguageSwitcher.tsx # ← أزرار AR/FR/EN
├─ components/ThemeProvider.tsx
├─ app/layout.tsx                 # ← يغلّف Theme + Language
├─ app/page.tsx                   # ← Landing ثلاثي اللغات
├─ app/(dashboard)/layout.tsx     # ← Sidebar/Topbar مترجمان
├─ app/(dashboard)/settings/page.tsx # ← ثيم + لغة + خط
├─ lib/courts.ts                  # 60 جهة (58 + 00 + 99)
├─ electron/main.js
├─ prisma/schema.prisma (PostgreSQL) + schema.sqlite.prisma (maktabi.db)
└─ package.json (name: "maktabi")
```

---

## 🔄 تغيير اسم الريبو على GitHub (يدوي — مطلوب مرة واحدة)

```bash
# الكود المحلي صار باسم maktabi، لكن GitHub لا يسمح للتوكن بتغيير الاسم (403)
# افعلها يدوياً:
# 1. افتح https://github.com/bahaeddineragueb-oss/orbitpress-android-5/settings
# 2. General → Repository name → اكتب maktabi → Rename
# 3. حدّث الـ remote محلياً:
git remote set-url origin https://github.com/bahaeddineragueb-oss/maktabi.git
```

بعدها سيكون الرابط الجديد: `https://github.com/bahaeddineragueb-oss/maktabi`

---

## 📜 الترخيص

MIT — © 2026 Maktabi DZ — صُنع في الجزائر 🇩🇿

> **ملاحظة OrbitPress:** المجلدات `app/`, `docs/`, `wp-plugin/` في جذر الريبو هي بقايا مشروع OrbitPress 5.0 Android القديم. المشروع النشط الآن هو `avocat-dz/` باسم **مكتبي | Maktabi**. يمكنك أرشفة تلك المجلدات لاحقاً.
