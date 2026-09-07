# قاعدة البيانات — PostgreSQL vs SQLite

## للـ PC (Offline) — SQLite
- الملف: `prisma/maktabi.db` (164KB)
- provider: `sqlite` في `prisma/schema.sqlite.prisma`
- `DATABASE_URL="file:./prisma/maktabi.db"`
- يعمل بدون انترنت، مع Electron
- للبناء: `npx prisma db push --schema=prisma/schema.sqlite.prisma`

## للـ Web/Production — PostgreSQL
- الملف: `prisma/schema.prisma` (provider = postgresql)
- `prisma/maktabi-postgres.sql` (SQL dump جاهز للاستيراد)
- `docker-compose.yml` يشغل PostgreSQL محلياً
- `DATABASE_URL="postgresql://maktabi:maktabi123@localhost:5432/maktabi"`

### تشغيل PostgreSQL محلياً
```bash
docker compose up -d
# انتظر 5 ثواني ثم:
npx prisma db push
npx prisma db seed
# أو استيراد مباشر:
psql postgresql://maktabi:maktabi123@localhost:5432/maktabi -f prisma/maktabi-postgres.sql
```

### التحويل من SQLite إلى PostgreSQL
- `prisma/maktabi.db` → `prisma/maktabi-postgres.sql` (تم تلقائياً)
- أو: `npm run db:migrate` (يقرأ SQLite ويكتب إلى PostgreSQL)

## التحقق
```bash
# SQLite
sqlite3 prisma/maktabi.db "SELECT COUNT(*) FROM tribunals"

# PostgreSQL
psql $DATABASE_URL -c "SELECT COUNT(*) FROM tribunals"
# يجب أن يكون 256
```
