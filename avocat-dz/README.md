# مكتبي — Avocat DZ Pro ⚖️

**نظام تسيير مكاتب المحاماة الجزائرية — احترافي وليس مجرد دفتر إلكتروني**

> العميل → القضية → الإجراءات والجلسات → الوثائق → الأتعاب → المصاريف → الآجال → الأرشيف

![Next.js](https://img.shields.io/badge/Next.js-14-black) ![Electron](https://img.shields.io/badge/Electron-30-47848F) ![Windows](https://img.shields.io/badge/Windows-10%2F11-0078D6) ![Offline](https://img.shields.io/badge/Offline-100%25-green) ![TypeScript](https://img.shields.io/badge/TypeScript-5-blue) ![License](https://img.shields.io/badge/License-MIT-yellow)

---

## 🎯 الفكرة

بُني هذا التطبيق حول **دورة حياة الملف كاملة** كما طلبت:

- **لوحة تحكم Dashboard** تعطي المحامي صورة كاملة في ثوانٍ: جلسات اليوم، آجال قريبة، ملفات تحتاج متابعة، أتعاب غير محصلة
- **إدارة العملاء**: ملف كامل، سجل تواصل، وثائق، كل قضاياه في صفحة واحدة
- **إدارة القضايا**: Timeline كامل من فتح الملف إلى الحكم والاستئناف والطعن والأرشيف
- **الجلسات والآجال**: تقويم يومي/أسبوعي/شهري + تنبيهات 🚨 “بقي 10 أيام على الاستئناف”
- **الوثائق**: مجلد إلكتروني لكل ملف مع تصنيف وبحث ومعاينة PDF
- **نماذج قانونية**: مكتبة عرائض ومذكرات قابلة للتعبئة التلقائية `{{client}}`
- **الأتعاب والمصاريف**: إجمالي/مدفوع/متبقي + صافي المداخيل
- **قاعدة المحاكم**: 58 ولاية — مجلس → محكمة → قسم
- **المساعد الذكي**: تلخيص ملف، استخراج آجال، صياغة مسودة مذكرة (مع تنبيه المراجعة)

---

## ✨ المميزات المنفذة (v1.0)

| القسم | ما تم |
|---|---|
| 👥 العملاء | CRUD كامل، بحث، عرض كل قضايا العميل، سجل تواصل |
| ⚖️ القضايا | Kanban + جدول، فلترة بالحالة، Timeline تاريخي، استراتيجية الدفاع |
| 📅 الجلسات | قائمة + تقويم، قاعة/قاضي/قرار/تأجيل، تمييز جلسات اليوم بالأحمر |
| ⏰ الآجال | أولويات (عاجل/هام/عادي)، عد تنازلي، تنبيه ≤3 أيام pulsing، إنجاز |
| 📄 الوثائق | تصنيف (9 أنواع)، بحث، رفع، معاينة، تنزيل، ربط بملف |
| 📝 النماذج | 4 نماذج جاهزة + مولّد `{{key}}` → نص جاهز للطباعة/PDF |
| 💰 الأتعاب | دفعات، نسبة تحصيل، “+20,000” و “تسديد كامل”، متبقي |
| 💸 المصاريف | 7 تصنيفات + صافي المداخيل |
| 🏛️ المحاكم | فلترة ولاية، بطاقات محاكم، جاهز للاستيراد الكامل |
| 📊 التقارير | Charts (Recharts) — حسب النوع، إيرادات/مصاريف |
| 🤖 AI | 3 أوامر: تلخيص، استخراج آجال، مسودة مذكرة |
| 🔐 الأمان | صفحة إعدادات: أدوار، تشفير، نسخ احتياطي، Audit Log (تصميم) |
| 📱 PWA-ready | Responsive 100%، Dark/Light، RTL كامل، خط Tajawal/Cairo |

---

## 🖥️ Logiciel PC (Windows) — الجديد في v1.1

**الآن logiciel PC حقيقي لـ Windows 10/11 — يعمل بدون انترنت!**

| النسخة | الملف | الحجم |
|---|---|---|
| **المثبت** | `Maktabi-Setup-1.0.0.exe` | ~85MB |
| **المحمولة** | `Maktabi-Portable-1.0.0.exe` | ~80MB |

**التثبيت في دقيقة:**
1. حمّل المثبت من صفحة `/download` أو من `Releases`
2. شغّله → اختر المجلد → أنشئ اختصار سطح المكتب
3. افتح **مكتبي** — بياناتك تُحفظ في `AppData/Roaming/maktabi/maktabi-data.json` (لا سحابة)

راجع `docs/PC_GUIDE_AR.md` + `README_PC.md` للتفاصيل.

```bash
# بناء المثبت من المصدر (Windows)
npm install
npm run dist:win      # → dist/Maktabi-Setup-1.0.0.exe
npm run dist:portable # → dist/Maktabi-Portable-1.0.0.exe
```

---

## 🚀 التشغيل (Web + PC)

```bash
# Web فقط
npm install
npm run dev              # http://localhost:3000
npm run build            # build للويب (Vercel)

# PC (Electron) — للتطوير
npm run dev:pc           # يشغّل Next + Electron معاً

# PC — بناء المثبت
npm run build:pc         # تصدير Static للـ PC
npm run dist:win         # بناء .exe
```

> يتطلب Node.js 18+

---

## 📁 الهيكل

```
avocat-dz/
├─ electron/               # Logiciel PC
│  ├─ main.js              # نافذة + قوائم + IPC
│  └─ preload.js           # جسر window.electronAPI
├─ app/
│  ├─ page.tsx             # Landing + زر تحميل PC
│  └─ (dashboard)/
│     ├─ dashboard/page.tsx # + PCBanner
│     ├─ download/page.tsx  # صفحة تحميل PC
│     └─ ... (13 قسم)
├─ components/
│  ├─ Sidebar.tsx          # + رابط تحميل PC
│  ├─ Topbar.tsx
│  ├─ StatCard.tsx
│  └─ PCBanner.tsx         # بانر PC/ويب
├─ lib/
│  ├─ types.ts
│  ├─ data.ts
│  ├─ utils.ts
│  └─ db.ts                # تخزين موحد: localStorage (ويب) / JSON (PC)
├─ build/                  # أيقونات
│  └─ icon.png / icon.ico
├─ out/                    # تصدير Static للـ PC (ELECTRON=true)
└─ docs/
   ├─ SCHEMA.md
   ├─ UI_SPEC.md
   └─ PC_GUIDE_AR.md       # دليل PC
```

---

## 🗄️ قاعدة البيانات (مقترح Prisma)

راجع `docs/SCHEMA.md` — مخطط كامل بـ 13 جدول: Client, Case, Hearing, Deadline, Document, Fee, Expense, Template, Court, User, Role, AuditLog, Notification

يمكن التحويل لاحقاً إلى:
- **Supabase / PostgreSQL** (موصى به)
- **Prisma + SQLite** للـ offline أولاً
- **Encrypted localStorage** حالياً (يعمل بدون Backend)

---

## 🔐 الأمان والصلاحيات

- **مدير**: كل شيء + حذف نهائي + تقارير مالية
- **محامي**: ملفاته فقط
- **متربص**: قراءة + إضافة إجراءات بإشراف
- **سكرتير**: جلسات/مواعيد/وثائق دون المالية السرية

+ تشفير، نسخ احتياطي يومي، Face ID/بصمة (عبر Capacitor لاحقاً)

---

## 📱 Web + Android + iOS

هذا المشروع **Web أولاً** (Next.js) ويمكن تحويله لتطبيق موبايل في يوم واحد:

```bash
# عبر Capacitor
npm i @capacitor/core @capacitor/cli
npx cap init
npx cap add android
npx cap add ios
npm run build && npx cap sync
```

أو كـ **PWA** مباشرة (يعمل Offline).

---

## 🛣️ خارطة الطريق

- [x] v1.0 — كل الأقسام الأساسية + Mock data جزائرية
- [x] v1.1 — **Logiciel PC (Windows)**: Electron + Offline + Installer + Portable
- [ ] v1.2 — ربط Prisma/SQLite + Auth + نسخ احتياطي سحابي اختياري
- [ ] v1.3 — OCR لاستخراج آجال من PDF + إشعارات Desktop
- [ ] v1.4 — تصدير Word/PDF للنماذج + ختم المكتب
- [ ] v2.0 — تطبيق Android/iOS + مزامنة

---

## 🤝 المساهمة

1. Fork
2. Branch: `feat/amazing`
3. Commit + PR

---

## 📄 الترخيص

MIT — استخدمه لمكتبك أو لعملائك بحرية.

---

**صُنع بـ ❤️ في الجزائر — مكتبي v1.1 PC**
> هل تريد Repo جديد على GitHub؟ أنشئ repo فارغ باسم `avocat-dz` على https://github.com/new ثم أخبرني لأرفعه فوراً — أو سأضعه في `orbitpress-android-5/avocat-dz` مؤقتاً.

