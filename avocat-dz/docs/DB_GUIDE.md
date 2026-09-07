# قاعدة البيانات الحقيقية — الدليل الكامل

## نظرة عامة

**مكتبي - maktabi الآن لديه قاعدة بيانات حقيقية لكل التطبيق** — ليست Mock فقط.

- **للمحاكم:** 58 ولاية، 58 مجلس قضائي، 256 محكمة/فرع، 58 محكمة إدارية — من وزارة العدل مباشرة
- **للمكتب:** العملاء، القضايا، الجلسات، الآجال، الوثائق، الأتعاب، المصاريف — كلها في DB

---

## الملفات

| الملف | النوع | الحجم | الاستخدام |
|---|---|---|---|
| `data/courts.json` | JSON | 111KB | للويب — تحميل فوري بدون DB (fallback) |
| `lib/courts-data.json` | JSON | 111KB | للـ Import المباشر في الواجهة |
| `prisma/maktabi.db` | **SQLite** | 164KB | **للـ PC (Electron) — Offline** |
| `prisma/maktabi-postgres.sql` | **SQL** | 93KB | **للـ Web — PostgreSQL** |
| `prisma/schema.prisma` | Prisma | 5.6KB | **PostgreSQL** (الإنتاج) |
| `prisma/schema.sqlite.prisma` | Prisma | 5.4KB | SQLite (PC محلي) |

---

## SQLite (PC — Offline)

```bash
# الملف جاهز: prisma/maktabi.db
sqlite3 prisma/maktabi.db "SELECT COUNT(*) FROM tribunals" # 256
sqlite3 prisma/maktabi.db "SELECT * FROM wilayas WHERE code='16'" # الجزائر
sqlite3 prisma/maktabi.db "SELECT name FROM tribunals WHERE wilayaCode='16'"
```

- provider: `sqlite` في `schema.sqlite.prisma`
- `DATABASE_URL="file:./prisma/maktabi.db"`
- يعمل بدون انترنت، مع Electron
- يُنسخ تلقائياً في المثبت (`extraResources`)

---

## PostgreSQL (Web — Production)

### الخيار 1: Docker محلي (موصى به للتطوير)

```bash
# 1. شغّل PostgreSQL
docker compose up -d
# → postgres على localhost:5432
# → adminer على http://localhost:8080 (user: maktabi / pass: maktabi123)

# 2. إنشاء الجداول
npx prisma db push
# أو استيراد مباشر:
psql postgresql://maktabi:maktabi123@localhost:5432/maktabi -f prisma/maktabi-postgres.sql

# 3. تعبئة البيانات (58 ولاية + demo)
npx prisma db seed
# أو: npm run db:seed

# 4. تحقق
psql $DATABASE_URL -c "SELECT COUNT(*) FROM tribunals" # 256
psql $DATABASE_URL -c "SELECT * FROM wilayas WHERE code='16'"
```

### الخيار 2: Supabase / Neon / Render (الإنتاج)

1. أنشئ مشروع على https://supabase.com أو https://neon.tech
2. انسخ `DATABASE_URL` (مثال: `postgresql://user:pass@host:5432/maktabi?sslmode=require`)
3. ضعه في `.env`:
   ```
   DATABASE_URL="postgresql://user:pass@host:5432/maktabi?sslmode=require"
   ```
4. ثم:
   ```bash
   npx prisma db push
   npx prisma db seed
   ```

### الخيار 3: استيراد من SQLite

```bash
# حوّل SQLite إلى PostgreSQL (تم تلقائياً):
# prisma/maktabi.db → prisma/maktabi-postgres.sql (93KB)
psql $DATABASE_URL -f prisma/maktabi-postgres.sql
```

---

## Prisma

```bash
# توليد العميل (بعد تغيير schema)
npx prisma generate

# إنشاء migration
npx prisma migrate dev --name init

# فتح Studio (واجهة DB)
npx prisma studio
# → http://localhost:5555

# Seed
npx prisma db seed
```

**schema.prisma** يحتوي 13 جدول:

- `Wilaya`, `Council`, `Tribunal`, `AdminCourt`, `CourtSection` — الجهات القضائية
- `Client`, `CourtCase`, `Hearing`, `Deadline`, `Document`, `Fee`, `Expense` — المكتب
- `User`, `AuditLog` — النظام

---

## API — كل التطبيق لديه API حقيقي

| Endpoint | الوصف | DB |
|---|---|---|
| `GET /api/courts?q=&code=` | المحاكم (58 ولاية) | JSON + SQLite + PostgreSQL |
| `GET /api/clients` | العملاء | Prisma → mock fallback |
| `POST /api/clients` | إنشاء عميل | Prisma |
| `GET /api/cases?q=&status=` | القضايا | Prisma → mock |
| `POST /api/cases` | إنشاء قضية | Prisma |
| `GET /api/hearings?caseId=` | الجلسات | Prisma → mock |
| `POST /api/hearings` | إنشاء جلسة | Prisma |
| `GET /api/deadlines` | الآجال | Prisma |
| `GET /api/documents?caseId=` | الوثائق | Prisma |
| `GET /api/fees` | الأتعاب | Prisma |
| `GET /api/expenses` | المصاريف | Prisma |
| `GET /api/stats` | إحصائيات | Prisma |

**مثال:**
```bash
curl http://localhost:3000/api/courts?code=16
curl http://localhost:3000/api/clients
curl -X POST http://localhost:3000/api/cases -H "Content-Type: application/json" -d '{"fileNumber":"2026/999","title":"نزاع جديد","court":"محكمة الرويبة","wilayaCode":"16"}'
```

**Fallback:** إذا كان PostgreSQL غير متصل، يعود تلقائياً إلى Mock/JSON — التطبيق لا يتوقف.

---

## الجلسات — اختيار المحكمة من نفس القاعدة

**قبل:** كتابة يدوية `محكمة الرويبة`

**الآن:** في `/sessions` → **+ جلسة جديدة** → يظهر نفس `CourtSelector`:

```
ولاية (58) → مجلس قضائي → محكمة/فرع (256) → قسم (9) → قاعة + قاضي
```

- نفس قاعدة وزارة العدل
- اختيار فقط، لا كتابة
- يُحفظ مع الجلسة: `court`, `wilayaCode`, `council`, `section`

---

## التطبيق كله — قاعدة حقيقية

| القسم | الجدول | API | واجهة |
|---|---|---|---|
| العملاء | `clients` | `/api/clients` | `/clients` يقرأ من API |
| القضايا | `cases` | `/api/cases` | `/cases` مع CourtSelector |
| الجلسات | `hearings` | `/api/hearings` | `/sessions` مع CourtSelector |
| الآجال | `deadlines` | `/api/deadlines` | `/deadlines` |
| الوثائق | `documents` | `/api/documents` | `/documents` |
| الأتعاب | `fees` | `/api/fees` | `/fees` |
| المصاريف | `expenses` | `/api/expenses` | `/expenses` |
| المحاكم | `wilayas/councils/tribunals` | `/api/courts` | `/courts` + CourtSelector |

---

## التحقق

```bash
# SQLite (PC)
sqlite3 prisma/maktabi.db "SELECT COUNT(*) FROM wilayas" # 58
sqlite3 prisma/maktabi.db "SELECT COUNT(*) FROM tribunals" # 256

# PostgreSQL (Web) — بعد docker compose up
psql postgresql://maktabi:maktabi123@localhost:5432/maktabi -c "SELECT COUNT(*) FROM tribunals"

# API
curl http://localhost:3000/api/stats
# → { wilayas: 58, tribunals: 256, ... }
```

---

## الإنتاج

- **Vercel + Neon/Supabase:** ضع `DATABASE_URL` في Environment Variables → `npx prisma db push` → `npx prisma db seed`
- **PC (Electron):** `prisma/maktabi.db` يُضمّن في المثبت تلقائياً — لا يحتاج PostgreSQL
- **Web + PC معاً:** نفس الكود، نفس القاعدة، نفس الـ API

**صُنع في الجزائر 🇩🇿 — قاعدة حقيقية 100%**
