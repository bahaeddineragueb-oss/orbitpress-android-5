# مكتبي — Avocat DZ Pro ⚖️

**نظام تسيير مكاتب المحاماة الجزائرية — احترافي وليس مجرد دفتر إلكتروني**

> العميل → القضية → الإجراءات والجلسات → الوثائق → الأتعاب → المصاريف → الآجال → الأرشيف

![Next.js](https://img.shields.io/badge/Next.js-14-black) ![TypeScript](https://img.shields.io/badge/TypeScript-5-blue) ![Tailwind](https://img.shields.io/badge/Tailwind-3-38bdf8) ![RTL](https://img.shields.io/badge/RTL-عربي-green) ![License](https://img.shields.io/badge/License-MIT-yellow)

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

## 🚀 التشغيل

```bash
# 1) التثبيت
npm install

# 2) التشغيل (يفتح على 0.0.0.0 للـ Preview)
npm run dev
# افتح http://localhost:3000

# 3) البناء
npm run build
npm start
```

> يتطلب Node.js 18+

---

## 📁 الهيكل

```
avocat-dz/
├─ app/
│  ├─ page.tsx                 # Landing تسويقية
│  ├─ layout.tsx               # RTL + Fonts
│  ├─ globals.css
│  └─ (dashboard)/
│     ├─ layout.tsx            # Sidebar + Topbar + Drawer موبايل
│     ├─ dashboard/page.tsx    # لوحة التحكم + Charts
│     ├─ clients/page.tsx
│     ├─ cases/page.tsx        # Timeline
│     ├─ sessions/page.tsx     # List/Calendar
│     ├─ deadlines/page.tsx
│     ├─ documents/page.tsx
│     ├─ templates/page.tsx    # مولد النماذج
│     ├─ fees/page.tsx
│     ├─ expenses/page.tsx
│     ├─ courts/page.tsx
│     ├─ reports/page.tsx
│     ├─ assistant/page.tsx    # AI mock
│     ├─ archive/page.tsx
│     └─ settings/page.tsx
├─ components/
│  ├─ Sidebar.tsx
│  ├─ Topbar.tsx
│  └─ StatCard.tsx
├─ lib/
│  ├─ types.ts                 # كل الكيانات
│  ├─ data.ts                  # Mock + courtsDB + templates
│  └─ utils.ts
└─ docs/
   ├─ SCHEMA.md                # مخطط قاعدة البيانات
   └─ UI_SPEC.md
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
- [ ] v1.1 — ربط Prisma + Supabase + Auth حقيقي
- [ ] v1.2 — رفع ملفات S3 + معاينة PDF حقيقية + OCR لاستخراج آجال
- [ ] v1.3 — إشعارات Push للآجال والجلسات
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

**صُنع بـ ❤️ في الجزائر — مكتبي v1.0**
> هل تريد Repo جديد على GitHub؟ أنشئ repo فارغ باسم `avocat-dz` على https://github.com/new ثم أخبرني لأرفعه فوراً — أو سأضعه في `orbitpress-android-5/avocat-dz` مؤقتاً.

