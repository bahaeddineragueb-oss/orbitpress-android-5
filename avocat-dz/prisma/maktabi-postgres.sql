-- Maktabi PostgreSQL dump — generated from SQLite (وزارة العدل 58 ولاية)
-- المصدر: prisma/maktabi.db (SQLite) → PostgreSQL
-- للاستيراد: psql $DATABASE_URL -f prisma/maktabi-postgres.sql
-- أو: docker exec -i maktabi-postgres psql -U maktabi maktabi < prisma/maktabi-postgres.sql

CREATE TABLE IF NOT EXISTS "wilayas" (
  code TEXT PRIMARY KEY,
  name TEXT UNIQUE NOT NULL,
  nameAr TEXT NOT NULL,
  isNew INTEGER DEFAULT 0
);

CREATE TABLE IF NOT EXISTS "councils" (
  id TEXT PRIMARY KEY,
  wilayaCode TEXT UNIQUE NOT NULL REFERENCES wilayas(code) ON DELETE CASCADE,
  name TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS "tribunals" (
  id TEXT PRIMARY KEY,
  councilId TEXT NOT NULL REFERENCES councils(id) ON DELETE CASCADE,
  wilayaCode TEXT NOT NULL REFERENCES wilayas(code),
  name TEXT NOT NULL,
  isBranch INTEGER DEFAULT 0,
  UNIQUE(councilId, name)
);

CREATE TABLE IF NOT EXISTS "admin_courts" (
  id TEXT PRIMARY KEY,
  wilayaCode TEXT UNIQUE NOT NULL REFERENCES wilayas(code),
  councilId TEXT UNIQUE NOT NULL REFERENCES councils(id),
  name TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS "court_sections" (
  id TEXT PRIMARY KEY,
  name TEXT UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS "clients" (
  id TEXT PRIMARY KEY,
  fullName TEXT NOT NULL,
  phone TEXT NOT NULL,
  email TEXT,
  address TEXT,
  profession TEXT,
  nationalId TEXT,
  notes TEXT,
  createdAt TEXT DEFAULT (datetime('now'))
);

CREATE TABLE IF NOT EXISTS "cases" (
  id TEXT PRIMARY KEY,
  fileNumber TEXT UNIQUE NOT NULL,
  caseNumber TEXT NOT NULL,
  clientId TEXT NOT NULL REFERENCES clients(id),
  title TEXT NOT NULL,
  wilayaCode TEXT,
  councilName TEXT,
  tribunalName TEXT,
  section TEXT,
  category TEXT NOT NULL,
  clientRole TEXT NOT NULL,
  opponent TEXT NOT NULL,
  opponentLawyer TEXT,
  status TEXT NOT NULL,
  assignedLawyer TEXT,
  openDate TEXT NOT NULL,
  closeDate TEXT,
  notes TEXT,
  strategy TEXT,
  createdAt TEXT DEFAULT (datetime('now')),
  updatedAt TEXT
);

-- Data for wilayas (58 rows)
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('01', 'أدرار', 'ولاية أدرار', 0) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('02', 'الشلف', 'ولاية الشلف', 0) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('03', 'الأغواط', 'ولاية الأغواط', 0) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('04', 'أم البواقي', 'ولاية أم البواقي', 0) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('05', 'باتنة', 'ولاية باتنة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('06', 'بجاية', 'ولاية بجاية', 0) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('07', 'بسكرة', 'ولاية بسكرة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('08', 'بشار', 'ولاية بشار', 0) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('09', 'البليدة', 'ولاية البليدة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('10', 'البويرة', 'ولاية البويرة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('11', 'تمنراست', 'ولاية تمنراست', 0) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('12', 'تبسة', 'ولاية تبسة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('13', 'تلمسان', 'ولاية تلمسان', 0) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('14', 'تيارت', 'ولاية تيارت', 0) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('15', 'تيزي وزو', 'ولاية تيزي وزو', 0) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('16', 'الجزائر', 'ولاية الجزائر', 0) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('17', 'الجلفة', 'ولاية الجلفة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('18', 'جيجل', 'ولاية جيجل', 0) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('19', 'سطيف', 'ولاية سطيف', 0) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('20', 'سعيدة', 'ولاية سعيدة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('21', 'سكيكدة', 'ولاية سكيكدة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('22', 'سيدي بلعباس', 'ولاية سيدي بلعباس', 0) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('23', 'عنابة', 'ولاية عنابة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('24', 'قالمة', 'ولاية قالمة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('25', 'قسنطينة', 'ولاية قسنطينة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('26', 'المدية', 'ولاية المدية', 0) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('27', 'مستغانم', 'ولاية مستغانم', 0) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('28', 'المسيلة', 'ولاية المسيلة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('29', 'معسكر', 'ولاية معسكر', 0) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('30', 'ورقلة', 'ولاية ورقلة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('31', 'وهران', 'ولاية وهران', 0) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('32', 'البيض', 'ولاية البيض', 0) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('33', 'إليزي', 'ولاية إليزي', 0) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('34', 'برج بوعريريج', 'ولاية برج بوعريريج', 0) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('35', 'بومرداس', 'ولاية بومرداس', 0) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('36', 'الطارف', 'ولاية الطارف', 0) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('37', 'تندوف', 'ولاية تندوف', 0) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('38', 'تيسمسيلت', 'ولاية تيسمسيلت', 0) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('39', 'الوادي', 'ولاية الوادي', 0) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('40', 'خنشلة', 'ولاية خنشلة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('41', 'سوق أهراس', 'ولاية سوق أهراس', 0) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('42', 'تيبازة', 'ولاية تيبازة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('43', 'ميلة', 'ولاية ميلة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('44', 'عين الدفلى', 'ولاية عين الدفلى', 0) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('45', 'النعامة', 'ولاية النعامة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('46', 'عين تموشنت', 'ولاية عين تموشنت', 0) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('47', 'غرداية', 'ولاية غرداية', 0) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('48', 'غليزان', 'ولاية غليزان', 0) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('49', 'تيميمون', 'ولاية تيميمون', 1) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('50', 'برج باجي مختار', 'ولاية برج باجي مختار', 1) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('51', 'أولاد جلال', 'ولاية أولاد جلال', 1) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('52', 'بني عباس', 'ولاية بني عباس', 1) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('53', 'إن صالح', 'ولاية إن صالح', 1) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('54', 'إن قزام', 'ولاية إن قزام', 1) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('55', 'توقرت', 'ولاية توقرت', 1) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('56', 'جانت', 'ولاية جانت', 1) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('57', 'المغير', 'ولاية المغير', 1) ON CONFLICT DO NOTHING;
INSERT INTO "wilayas" ("code", "name", "nameAr", "isNew") VALUES ('58', 'المنيعة', 'ولاية المنيعة', 1) ON CONFLICT DO NOTHING;

-- Data for councils (58 rows)
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('89593061-12fb-4dbf-b3d4-9980c20a7495', '01', 'مجلس قضاء أدرار') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('f0c3a685-89e3-44aa-a976-c7b149345238', '02', 'مجلس قضاء الشلف') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('8aec6fff-cd46-4d28-83de-fc32f71a9491', '03', 'مجلس قضاء الأغواط') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('71b28180-a27b-4fb9-ae98-98cc47c215ac', '04', 'مجلس قضاء أم البواقي') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('fb20cf65-0220-447d-92ab-8b27bce9e922', '05', 'مجلس قضاء باتنة') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('3a8ca6b7-cc4b-460b-83b9-c4d22daff00e', '06', 'مجلس قضاء بجاية') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('a220827c-3e28-445e-b73e-3d4e55855c40', '07', 'مجلس قضاء بسكرة') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('b8ccf7a1-c397-4d6a-a8b8-afa29d4b4f48', '08', 'مجلس قضاء بشار') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('b64a0367-e5b6-4ded-9835-77f8e540d56e', '09', 'مجلس قضاء البليدة') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('26089b85-7c56-42f4-96eb-a09ffe863ddc', '10', 'مجلس قضاء البويرة') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('ff8e1a0d-8012-4f06-b432-1822b108e001', '11', 'مجلس قضاء تمنراست') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('a909db65-4425-4149-97ff-ab3608bc9dbd', '12', 'مجلس قضاء تبسة') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('8931908c-5f66-4ec7-b98f-36359f1b6de7', '13', 'مجلس قضاء تلمسان') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('8285ebfb-7bfc-4c83-846a-bd6636c4daa8', '14', 'مجلس قضاء تيارت') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('d37a00f4-57cd-4767-a49e-c431347c3e96', '15', 'مجلس قضاء تيزي وزو') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('e696fb5d-d502-4917-ac9c-de057c04356d', '16', 'مجلس قضاء الجزائر') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('e3a031e8-3769-4293-995d-0e3395daf4c1', '17', 'مجلس قضاء الجلفة') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('aa8d6c53-b250-4ff9-99d2-894e61336257', '18', 'مجلس قضاء جيجل') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('4a45ac9f-e444-4839-8607-8e64ec73a3c8', '19', 'مجلس قضاء سطيف') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('edb32b44-c01f-4c4a-9abb-733bdbfea5db', '20', 'مجلس قضاء سعيدة') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('5a93514d-350c-488d-8b76-2d5e3731cabe', '21', 'مجلس قضاء سكيكدة') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('2e8b57c2-e3c6-4d73-8fa2-12007921d5cf', '22', 'مجلس قضاء سيدي بلعباس') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('c1f18858-5cdb-4bc2-80ba-16ba1085e138', '23', 'مجلس قضاء عنابة') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('64721872-c443-47d9-998d-2cd3c1def12c', '24', 'مجلس قضاء قالمة') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('cbc0f130-fd1f-4fd7-ac68-71919124e17b', '25', 'مجلس قضاء قسنطينة') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('695dc246-f5e6-4971-a052-f820f439c894', '26', 'مجلس قضاء المدية') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('bd73b584-a67d-43fc-91f5-5971bfc0f1d6', '27', 'مجلس قضاء مستغانم') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('04ccf245-44f4-460f-9376-7b6f51871b44', '28', 'مجلس قضاء المسيلة') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('459483ab-20c1-478a-b273-1e45bf20c278', '29', 'مجلس قضاء معسكر') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('32c7d09c-b669-400b-8086-4dd833e38531', '30', 'مجلس قضاء ورقلة') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('4db2fb76-62cc-4a32-9a70-c6dc2757b7f6', '31', 'مجلس قضاء وهران') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('a10850f0-d0d1-40a8-86a5-0b9a46f6c96f', '32', 'مجلس قضاء البيض') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('385a080d-8cc3-4eaa-b32e-98a8d0505d0e', '33', 'مجلس قضاء إليزي') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('8b0385c5-9d0f-429e-94b0-9c9a94fcbe57', '34', 'مجلس قضاء برج بوعريريج') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('fa381ec8-a95d-453f-8b56-b6624fd53fba', '35', 'مجلس قضاء بومرداس') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('7b79915e-478e-4a22-8941-0a18b4f94a74', '36', 'مجلس قضاء الطارف') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('aca65743-f890-4bd7-ae51-826dcc80a97a', '37', 'مجلس قضاء تندوف') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('6d55bd0c-521b-4c0c-911b-ec7c60f142de', '38', 'مجلس قضاء تيسمسيلت') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('70b04224-82ee-40c6-894e-1d4a4e52ebb2', '39', 'مجلس قضاء الوادي') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('fb889355-2e10-4326-87f0-e43ef7c297bd', '40', 'مجلس قضاء خنشلة') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('a1e8b1b4-befa-445d-88fe-bfe8f9a44983', '41', 'مجلس قضاء سوق أهراس') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('6ad0d006-f75e-4906-8891-02241156255e', '42', 'مجلس قضاء تيبازة') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('95fc3967-2800-41f7-8c1e-4d520bf6d53b', '43', 'مجلس قضاء ميلة') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('4600f3fe-3af4-4a6e-b013-a3b41a377522', '44', 'مجلس قضاء عين الدفلى') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('9c29c7a8-8c42-43f9-8b15-8dd5017db1cd', '45', 'مجلس قضاء النعامة') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('fd8f7e16-8815-4386-ba2d-2f85d17827f6', '46', 'مجلس قضاء عين تموشنت') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('bb5b4444-d1b7-4a54-9841-2e2213c9bf4e', '47', 'مجلس قضاء غرداية') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('96dca960-9c34-4665-b2be-fb14ca0962cc', '48', 'مجلس قضاء غليزان') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('e484257f-89df-411a-89dd-2a930f33122d', '49', 'مجلس قضاء تيميمون') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('2eae8e5a-dc05-49b5-9514-e4ae094a4baa', '50', 'مجلس قضاء برج باجي مختار') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('5ca9bd4f-51b5-4740-911f-6c7e897e6ada', '51', 'مجلس قضاء أولاد جلال') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('d73eb17c-0aed-4e27-9567-7571e2526c0a', '52', 'مجلس قضاء بني عباس') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('229c1334-ae60-454a-9aaa-cad5215190aa', '53', 'مجلس قضاء إن صالح') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('5db15c83-dfc3-4d40-ba50-83bd1bc15899', '54', 'مجلس قضاء إن قزام') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('348c5e09-2c9a-4374-8a6e-ebc70b1de16e', '55', 'مجلس قضاء توقرت') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('059bb023-04b2-4dc4-83c0-c8ed3c6af6d1', '56', 'مجلس قضاء جانت') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('d937246a-a359-44c2-be20-fcb2fd1f7098', '57', 'مجلس قضاء المغير') ON CONFLICT DO NOTHING;
INSERT INTO "councils" ("id", "wilayaCode", "name") VALUES ('ba0f63e4-115b-439b-bc7e-f96c377f6e9a', '58', 'مجلس قضاء المنيعة') ON CONFLICT DO NOTHING;

-- Data for tribunals (256 rows)
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('f843e0ae-7c51-4e9f-b8dd-8465363271df', '89593061-12fb-4dbf-b3d4-9980c20a7495', '01', 'محكمة رقان', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('2231db9c-1b11-4286-a8ba-0b4ee0668ffc', '89593061-12fb-4dbf-b3d4-9980c20a7495', '01', 'محكمة تميمون', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('f6649064-e186-44f5-ac9e-b613e4287dbd', '89593061-12fb-4dbf-b3d4-9980c20a7495', '01', 'محكمة أدرار', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('757262be-10c5-4958-8154-4ed9f7ff49eb', '89593061-12fb-4dbf-b3d4-9980c20a7495', '01', 'محكمة أولف', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('e809cf88-8627-4eae-9379-590f05e5a57d', '89593061-12fb-4dbf-b3d4-9980c20a7495', '01', 'فرع برج باجي مختار', 1) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('69809517-919f-4b37-9ba4-128de1bf8d1a', 'f0c3a685-89e3-44aa-a976-c7b149345238', '02', 'محكمة الشلف', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('23cfc06d-1bd5-4abc-a8eb-e05454333f72', 'f0c3a685-89e3-44aa-a976-c7b149345238', '02', 'محكمة تنس', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('019d2a59-4b2f-4a27-848e-a3d1ce53e53b', 'f0c3a685-89e3-44aa-a976-c7b149345238', '02', 'محكمة بوقادير', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('216c5839-e714-467a-a4e4-b45c9ff69a41', 'f0c3a685-89e3-44aa-a976-c7b149345238', '02', 'فرع عين مران', 1) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('6b15d273-e856-46d0-b3e3-d000b0ffbe5a', 'f0c3a685-89e3-44aa-a976-c7b149345238', '02', 'فرع الشطية', 1) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('5ba93372-2282-41e6-9e6c-cb4947769628', '8aec6fff-cd46-4d28-83de-fc32f71a9491', '03', 'محكمة آفـلـو', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('76b4576b-24d7-43ac-8acc-614ac39c61af', '8aec6fff-cd46-4d28-83de-fc32f71a9491', '03', 'محكمة الأغواط', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('e32b0257-27cd-4e9d-9444-416daa3aa797', '8aec6fff-cd46-4d28-83de-fc32f71a9491', '03', 'محكمة عين ماضي', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('fcab15fe-75ba-4922-9102-64ec5f11f8a1', '71b28180-a27b-4fb9-ae98-98cc47c215ac', '04', 'محكمة أم البواقي', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('c49f0e54-d1a7-467e-9f90-b1130ac09a10', '71b28180-a27b-4fb9-ae98-98cc47c215ac', '04', 'محكمة عين البيضاء', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('e4c0534a-e61c-4c5d-9d29-8e44803d545e', '71b28180-a27b-4fb9-ae98-98cc47c215ac', '04', 'محكمة عين مليلة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('b494e484-978c-4bd4-a4ab-c4be1c781587', '71b28180-a27b-4fb9-ae98-98cc47c215ac', '04', 'محكمة مسكيانة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('e2b7643e-c8de-4412-9a9e-a521dacdc00e', '71b28180-a27b-4fb9-ae98-98cc47c215ac', '04', 'محكمة عين فكرون', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('1b71ebbe-1478-42b5-b28b-1e12a655ab09', 'fb20cf65-0220-447d-92ab-8b27bce9e922', '05', 'محكمة باتنة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('5232067e-20f3-47f3-8fc6-1e4924c5c080', 'fb20cf65-0220-447d-92ab-8b27bce9e922', '05', 'محكمة أريس', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('cc63b116-98d5-4b1c-996e-e5e691fa7a11', 'fb20cf65-0220-447d-92ab-8b27bce9e922', '05', 'محكمة عين التوتة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('0e482519-485a-4e5d-8e41-e32547a2ba81', 'fb20cf65-0220-447d-92ab-8b27bce9e922', '05', 'محكمة بريكة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('a671e047-228c-4071-9c58-18f903c4c2e2', 'fb20cf65-0220-447d-92ab-8b27bce9e922', '05', 'محكمة نقاوس', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('74b95c98-f549-4123-a2e8-d8b4c4b641ef', 'fb20cf65-0220-447d-92ab-8b27bce9e922', '05', 'محكمة سريانة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('fe9b2d59-4213-4f70-8387-26855ed00137', 'fb20cf65-0220-447d-92ab-8b27bce9e922', '05', 'محكمة مروانة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('6b504eff-ff9a-4fa1-8d82-96ca2ff333d5', 'fb20cf65-0220-447d-92ab-8b27bce9e922', '05', 'فرع رأس العيون', 1) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('6700432e-a6b6-4c97-aaeb-ff65307916df', '3a8ca6b7-cc4b-460b-83b9-c4d22daff00e', '06', 'محكمة بجاية', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('95727329-4f33-44a2-be85-a517d1fa2709', '3a8ca6b7-cc4b-460b-83b9-c4d22daff00e', '06', 'محكمة أميزور', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('8fc5a726-d5ae-4385-9048-9e49c475d440', '3a8ca6b7-cc4b-460b-83b9-c4d22daff00e', '06', 'محكمة أقبو', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('7c0cfda0-4fce-45df-9084-b22919647a67', '3a8ca6b7-cc4b-460b-83b9-c4d22daff00e', '06', 'محكمة سيدي عيش', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('69c42577-09a3-4ff5-b302-a209424fa522', '3a8ca6b7-cc4b-460b-83b9-c4d22daff00e', '06', 'محكمة خراطة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('8a7a2d2c-3b37-4110-a776-c978d403d0b0', '3a8ca6b7-cc4b-460b-83b9-c4d22daff00e', '06', 'فرع تازمالت', 1) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('a4fb5dea-c533-4887-9b4d-124673a99dfe', '3a8ca6b7-cc4b-460b-83b9-c4d22daff00e', '06', 'فرع القصر', 1) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('79657167-e0d1-4285-aac2-d9f4691b665f', 'a220827c-3e28-445e-b73e-3d4e55855c40', '07', 'محكمة بسكرة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('b3b3bd95-70db-489d-8df7-d4772d671432', 'a220827c-3e28-445e-b73e-3d4e55855c40', '07', 'محكمة سيدي عقبة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('5fe8e18c-7a0b-45ea-9b6c-8297e6a2c8fa', 'a220827c-3e28-445e-b73e-3d4e55855c40', '07', 'محكمة طولقة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('50621fd0-e3ac-4884-b3d9-01c6a3306ad3', 'a220827c-3e28-445e-b73e-3d4e55855c40', '07', 'محكمة أولاد جلال', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('a3fb25aa-699b-468b-946d-51f92bed7a9e', 'b8ccf7a1-c397-4d6a-a8b8-afa29d4b4f48', '08', 'محكمة بشار', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('125711b6-be31-4292-aeae-ba15f6662890', 'b8ccf7a1-c397-4d6a-a8b8-afa29d4b4f48', '08', 'محكمة بني عباس', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('81e0688f-4858-47a7-9685-c5f912ddce5a', 'b8ccf7a1-c397-4d6a-a8b8-afa29d4b4f48', '08', 'محكمة العبادلة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('8cb6ef12-465b-4082-86ad-02d45b190dba', 'b8ccf7a1-c397-4d6a-a8b8-afa29d4b4f48', '08', 'فرع بني ونيف', 1) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('22989cc8-b254-474f-854b-a68780eb6e44', 'b8ccf7a1-c397-4d6a-a8b8-afa29d4b4f48', '08', 'فرع تبلبالة', 1) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('07e90f34-f616-4286-800a-94b17044b254', 'b8ccf7a1-c397-4d6a-a8b8-afa29d4b4f48', '08', 'فرع كرزاز', 1) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('e1582d75-b110-4c79-b4d0-44c6007fe916', 'b64a0367-e5b6-4ded-9835-77f8e540d56e', '09', 'محكمة البليدة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('8a8f4158-5522-4d61-9d68-ad21f0829d9d', 'b64a0367-e5b6-4ded-9835-77f8e540d56e', '09', 'محكمة بوفاريك', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('82747166-3fcd-4c27-a284-175d4592fc25', 'b64a0367-e5b6-4ded-9835-77f8e540d56e', '09', 'محكمة العفرون', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('83a6221a-a77a-42a7-ba47-2035d7cd36e0', 'b64a0367-e5b6-4ded-9835-77f8e540d56e', '09', 'محكمة الأربعاء', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('359b2a6a-57b8-4298-b3e1-007e336a6a02', '26089b85-7c56-42f4-96eb-a09ffe863ddc', '10', 'محكمة البويرة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('5d49c185-b20e-4e27-a36d-f409da9a9bce', '26089b85-7c56-42f4-96eb-a09ffe863ddc', '10', 'محكمة سور الغزلان', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('fd4f5438-69fe-4e92-b5a1-05436ec057b5', '26089b85-7c56-42f4-96eb-a09ffe863ddc', '10', 'محكمة عين بسام', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('575267f7-d598-4451-a431-b3858b9c80c5', '26089b85-7c56-42f4-96eb-a09ffe863ddc', '10', 'محكمة الاخضرية', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('b5b175b5-5557-43a7-9411-32fd09af2c5a', '26089b85-7c56-42f4-96eb-a09ffe863ddc', '10', 'فرع أمشدالة', 1) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('0ffb5304-69a2-4969-919e-0cbde77a69c1', 'ff8e1a0d-8012-4f06-b432-1822b108e001', '11', 'محكمة تمنراست', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('696a80d7-9441-4ada-a9b9-feb2845afeb1', 'ff8e1a0d-8012-4f06-b432-1822b108e001', '11', 'محكمة عين قزام', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('330bd793-5e9e-4756-bc50-d4330b231897', 'ff8e1a0d-8012-4f06-b432-1822b108e001', '11', 'محكمة عين صالح', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('83edd3c5-4ed9-4d2b-bef0-39e77452f631', 'ff8e1a0d-8012-4f06-b432-1822b108e001', '11', 'فرع تاظروك', 1) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('2443ffdc-30f8-4c31-9bb5-c89ad2a04318', 'ff8e1a0d-8012-4f06-b432-1822b108e001', '11', 'فرع تين زواتين', 1) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('18e41032-dcb0-42d3-ab16-1d409121fea8', 'a909db65-4425-4149-97ff-ab3608bc9dbd', '12', 'محكمة تبسة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('349bc268-cd13-423c-a5a5-540cd836d245', 'a909db65-4425-4149-97ff-ab3608bc9dbd', '12', 'محكمة بئر العاتر', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('5a5bc1ba-7662-4b3c-9e09-2dabf1936a83', 'a909db65-4425-4149-97ff-ab3608bc9dbd', '12', 'محكمة محكة العوينات', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('e4241a37-2253-4a5e-b2a1-fb22296c6f70', 'a909db65-4425-4149-97ff-ab3608bc9dbd', '12', 'محكمة الشريعة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('9b5313ce-325c-4615-b0a2-ada4d36681d0', 'a909db65-4425-4149-97ff-ab3608bc9dbd', '12', 'فرع محكمة الكويف', 1) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('0b46381a-d838-4e0e-96e6-fbc4170ad3e5', 'a909db65-4425-4149-97ff-ab3608bc9dbd', '12', 'فرع محكمة الونزة', 1) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('ae85ac89-bb59-4e3c-9052-28e0fb636188', '8931908c-5f66-4ec7-b98f-36359f1b6de7', '13', 'محكمة تلمسان', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('2d0c146e-2576-4046-948d-84c98830b3e0', '8931908c-5f66-4ec7-b98f-36359f1b6de7', '13', 'محكمة مغنية', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('b1f46fe6-b5cd-483c-8aa0-3c0f01ab21d4', '8931908c-5f66-4ec7-b98f-36359f1b6de7', '13', 'محكمة سبدو', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('1416f09e-a4ee-46ec-93bd-7942c74a10e7', '8931908c-5f66-4ec7-b98f-36359f1b6de7', '13', 'محكمة أولاد ميمون', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('8c1847c0-4c24-4bb9-85e7-40422efb6268', '8931908c-5f66-4ec7-b98f-36359f1b6de7', '13', 'محكمة الرمشي', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('2cf7e3ff-ea76-4d9a-b5ea-accbc77771db', '8931908c-5f66-4ec7-b98f-36359f1b6de7', '13', 'محكمة ندرومة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('e50d6a7d-20cb-4a31-93c3-5ecfbbae960d', '8931908c-5f66-4ec7-b98f-36359f1b6de7', '13', 'محكمة الغزوات', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('8e8bf04e-eac8-4310-a197-e5b135963a58', '8931908c-5f66-4ec7-b98f-36359f1b6de7', '13', 'محكمة باب العسة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('1d4b25d3-f791-441b-9b2d-4dfdf3ea1642', '8931908c-5f66-4ec7-b98f-36359f1b6de7', '13', 'فرع سيدي الجيلالي', 1) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('ea7c9396-8bb5-4b0a-9443-27e499cf3324', '8285ebfb-7bfc-4c83-846a-bd6636c4daa8', '14', 'محكمة تيارت', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('f6cf3ed2-e42e-4810-91c6-e2d6baacde52', '8285ebfb-7bfc-4c83-846a-bd6636c4daa8', '14', 'محكمة فرندة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('7938691b-7158-44f2-b72d-aee4b3db9171', '8285ebfb-7bfc-4c83-846a-bd6636c4daa8', '14', 'محكمة قصر الشلالة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('1e240fc3-5028-45be-ad40-94823f110fc1', '8285ebfb-7bfc-4c83-846a-bd6636c4daa8', '14', 'محكمة السوقر', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('e7816114-a3cc-4110-9209-debf0e0f9713', 'd37a00f4-57cd-4767-a49e-c431347c3e96', '15', 'محكمة تيزي وزو', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('ab8567d9-38ee-4d84-b157-dd6a6d5ec5d2', 'd37a00f4-57cd-4767-a49e-c431347c3e96', '15', 'محكمة عزازقة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('42ae271c-2d0d-483c-b562-56e4021addb3', 'd37a00f4-57cd-4767-a49e-c431347c3e96', '15', 'محكمة عين الحمام', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('ec06bdcb-80b4-4598-b75d-79e0cb69f5a7', 'd37a00f4-57cd-4767-a49e-c431347c3e96', '15', 'محكمة ذراع الميزان', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('8ba2be48-f6fd-4394-8c53-a708f93a5eee', 'd37a00f4-57cd-4767-a49e-c431347c3e96', '15', 'محكمة تيقزيرت', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('985c215e-3cba-422a-886b-b90ef65c0107', 'd37a00f4-57cd-4767-a49e-c431347c3e96', '15', 'محكمة الأربعاء ناث ايراثن', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('5441d3c2-9064-45c8-80f7-3c082ca3588a', 'd37a00f4-57cd-4767-a49e-c431347c3e96', '15', 'محكمة واسيف', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('1424b0fb-52ba-45eb-a7b6-16f96753b661', 'd37a00f4-57cd-4767-a49e-c431347c3e96', '15', 'فرع أزفون', 1) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('1d34363d-4920-4667-85eb-3b62db7b14a6', 'e696fb5d-d502-4917-ac9c-de057c04356d', '16', 'محكمة سيدي امحمد', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('042dfc15-f978-4bf4-9048-aae13870c50c', 'e696fb5d-d502-4917-ac9c-de057c04356d', '16', 'محكمة باب الوادي', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('48aba68d-103c-4f3c-988b-df4d6ccfd6c7', 'e696fb5d-d502-4917-ac9c-de057c04356d', '16', 'محكمة حسين داي', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('88bc2264-7e39-47d2-b16b-073bc68252e1', 'e696fb5d-d502-4917-ac9c-de057c04356d', '16', 'محكمة الحراش', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('47a9df29-6026-46a5-af4e-ace55a4e3eae', 'e696fb5d-d502-4917-ac9c-de057c04356d', '16', 'محكمة بئر مراد رايس', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('2b6a999b-12c1-41ff-ae72-53cde6aaffcd', 'e696fb5d-d502-4917-ac9c-de057c04356d', '16', 'محكمة ملحقة الحراش', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('b1e8c1ce-5cb2-42a4-b94c-dcf1d294bb2e', 'e696fb5d-d502-4917-ac9c-de057c04356d', '16', 'محكمة الرويبة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('8b876ea0-4993-42c0-bf96-daa6032eb0e7', 'e696fb5d-d502-4917-ac9c-de057c04356d', '16', 'محكمة الدار البيضاء', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('7a272913-44b2-4d07-b675-12e1e7c47db7', 'e3a031e8-3769-4293-995d-0e3395daf4c1', '17', 'محكمة الجلفة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('ca3d3d0e-b03d-4e47-9996-09f80768a2f4', 'e3a031e8-3769-4293-995d-0e3395daf4c1', '17', 'محكمة عين وسارة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('0b0f0141-cc83-4b44-a6f9-b2fd67404676', 'e3a031e8-3769-4293-995d-0e3395daf4c1', '17', 'محكمة حاسي بحبح', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('485ce4dd-96e8-498a-8d24-06f19f2b2acf', 'e3a031e8-3769-4293-995d-0e3395daf4c1', '17', 'محكمة مسعد', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('48510370-2ea9-4c4a-afc7-03a50fe518ed', 'e3a031e8-3769-4293-995d-0e3395daf4c1', '17', 'محكمة الادريسية', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('b9387f25-5db2-4ebf-ab81-022a64a18f69', 'aa8d6c53-b250-4ff9-99d2-894e61336257', '18', 'محكمة جيجل', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('354cfbf6-e50c-4f13-b793-d8618e303abd', 'aa8d6c53-b250-4ff9-99d2-894e61336257', '18', 'محكمة الطاهير', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('5e4b5712-7e73-4397-8c8a-23c7cef25f77', 'aa8d6c53-b250-4ff9-99d2-894e61336257', '18', 'محكمة الميلية', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('9e33ad4c-fad1-43aa-a1e3-386710143ae6', '4a45ac9f-e444-4839-8607-8e64ec73a3c8', '19', 'محكمة سطيف', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('6c074c9c-c9a3-49f1-ae70-4c76096af9a9', '4a45ac9f-e444-4839-8607-8e64ec73a3c8', '19', 'محكمة العلمة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('77d2588f-f087-4d25-877b-126668038a12', '4a45ac9f-e444-4839-8607-8e64ec73a3c8', '19', 'محكمة عين الكبيرة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('61e4474e-a697-4039-ac02-0f1020b2483b', '4a45ac9f-e444-4839-8607-8e64ec73a3c8', '19', 'محكمة بني ورثيلان', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('e152f832-e15b-44fd-bacf-b81cafbed5a7', '4a45ac9f-e444-4839-8607-8e64ec73a3c8', '19', 'محكمة عين ولمان', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('e92d13a8-f0ee-42ed-a0c9-8fab78b90507', '4a45ac9f-e444-4839-8607-8e64ec73a3c8', '19', 'فرع بني عزيز', 1) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('d7b8b433-4224-4d10-ab0c-26d5e5f7eff9', '4a45ac9f-e444-4839-8607-8e64ec73a3c8', '19', 'محكمة بوقاعة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('2dd3d7fe-acad-464e-a476-af660ebba390', '4a45ac9f-e444-4839-8607-8e64ec73a3c8', '19', 'محكمة عين أزال', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('555538c8-61f0-41f5-94a7-eb2ac570c942', 'edb32b44-c01f-4c4a-9abb-733bdbfea5db', '20', 'محكمة سعيدة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('ccceb4ed-05af-492b-a1b0-0fa9a36dc507', 'edb32b44-c01f-4c4a-9abb-733bdbfea5db', '20', 'محكمة الحساسنة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('f16ffb9b-e983-4808-821e-3ee3d8447c4c', '5a93514d-350c-488d-8b76-2d5e3731cabe', '21', 'محكمة سكيكدة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('03247637-46cd-459f-a8f9-8a98499033b8', '5a93514d-350c-488d-8b76-2d5e3731cabe', '21', 'محكمة عزابة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('c1cd8be6-26f1-4bf5-87b6-489760d6d4d5', '5a93514d-350c-488d-8b76-2d5e3731cabe', '21', 'محكمة الحروش', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('86d98c1c-c1b5-409f-ab5f-46dec5106b02', '5a93514d-350c-488d-8b76-2d5e3731cabe', '21', 'محكمة القل', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('53929d08-d1a5-4bb6-a74c-6b1d3272ec02', '5a93514d-350c-488d-8b76-2d5e3731cabe', '21', 'محكمة تمالوس', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('e96e4170-b1e2-427b-9850-d60b874e61f0', '2e8b57c2-e3c6-4d73-8fa2-12007921d5cf', '22', 'محكمة سيدي بلعباس', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('1c984933-48c0-46f1-9fd6-5416c001b6ce', '2e8b57c2-e3c6-4d73-8fa2-12007921d5cf', '22', 'فرع محكمة راس الماء', 1) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('0e3b02c7-daef-4d0a-b3b1-918ec36b8659', '2e8b57c2-e3c6-4d73-8fa2-12007921d5cf', '22', 'محكمة بن باديس', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('f862840a-3804-4cd2-9263-3bec633eb1bb', '2e8b57c2-e3c6-4d73-8fa2-12007921d5cf', '22', 'محكمة تلاغ', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('c2d71cc7-34b1-4bd0-910e-bdf780be652c', '2e8b57c2-e3c6-4d73-8fa2-12007921d5cf', '22', 'محكمة سفيزف', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('91f43df8-71fb-4b2c-897d-6a5940e36faf', 'c1f18858-5cdb-4bc2-80ba-16ba1085e138', '23', 'محكمة عنابة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('109116bd-6acb-40db-b828-5f72b21ca325', 'c1f18858-5cdb-4bc2-80ba-16ba1085e138', '23', 'محكمة الحجار', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('6fd8914e-2a10-435d-b8d4-ac276014db6c', 'c1f18858-5cdb-4bc2-80ba-16ba1085e138', '23', 'محكمة برحال', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('a24ecb78-8586-4fce-aaf6-1f438628c363', '64721872-c443-47d9-998d-2cd3c1def12c', '24', 'محكمة قالمة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('92cc7d3f-a893-4de0-b145-47de72fae4d1', '64721872-c443-47d9-998d-2cd3c1def12c', '24', 'محكمة بوشقوف', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('559cc474-9baf-487a-9eb5-f726133fce10', '64721872-c443-47d9-998d-2cd3c1def12c', '24', 'محكمة وادي الزناتي', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('504d3edd-96fa-4577-9537-fd1f03f97b42', 'cbc0f130-fd1f-4fd7-ac68-71919124e17b', '25', 'محكمة قسنطينة (الزيادية)', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('0ac247b5-1542-44e8-9ad5-d7a862ecb63e', 'cbc0f130-fd1f-4fd7-ac68-71919124e17b', '25', 'محكمة قسنطينة (مسعود بو جريو)', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('0e269aea-ccbc-458b-a5af-12d0996b0ca8', 'cbc0f130-fd1f-4fd7-ac68-71919124e17b', '25', 'محكمة الخروب', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('c781e9dd-b533-4632-9d2b-b7c7fc329ab3', 'cbc0f130-fd1f-4fd7-ac68-71919124e17b', '25', 'محكمة زيغود يوسف', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('34a16e19-009e-478d-ae9e-25af277ebea7', '695dc246-f5e6-4971-a052-f820f439c894', '26', 'محكمة المدية', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('2749c7f6-c410-4b23-85bc-2b374339e10a', '695dc246-f5e6-4971-a052-f820f439c894', '26', 'محكمة البرواقية', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('627a2bd1-31e4-419f-b66e-b98425e24065', '695dc246-f5e6-4971-a052-f820f439c894', '26', 'محكمة قصر البخاري', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('8a2b8f98-a3e8-4db4-a3d8-e8902afb1618', '695dc246-f5e6-4971-a052-f820f439c894', '26', 'محكمة عين بوسيف', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('f37cff85-ea35-4ea8-8c6b-b1d3ac41665c', '695dc246-f5e6-4971-a052-f820f439c894', '26', 'محكمة العمارية', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('6da3ab00-8ef2-4e28-a318-bc430c4503e5', '695dc246-f5e6-4971-a052-f820f439c894', '26', 'محكمة تابلاط', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('c784475d-ebc4-4e14-813c-825d6f4c2859', '695dc246-f5e6-4971-a052-f820f439c894', '26', 'محكمة بني سليمان', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('76693f60-cd65-4ee1-ab00-041a10f06d2e', 'bd73b584-a67d-43fc-91f5-5971bfc0f1d6', '27', 'محكمة مستغانم', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('5aa99b98-9aca-4f3c-a374-067a9df3991c', 'bd73b584-a67d-43fc-91f5-5971bfc0f1d6', '27', 'محكمة سيدي علي', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('ab09c93f-b0ef-46ae-ac8c-acb800e145e5', 'bd73b584-a67d-43fc-91f5-5971bfc0f1d6', '27', 'محكمة عين تادلس', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('187f64cd-705b-404b-90a6-12caaab53ba4', '04ccf245-44f4-460f-9376-7b6f51871b44', '28', 'محكمة المسيلة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('28b88e1b-a35b-4464-adf3-9681df008be1', '04ccf245-44f4-460f-9376-7b6f51871b44', '28', 'محكمة بوسعادة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('73657f7e-beda-498a-9902-d2bb9b4c17a7', '04ccf245-44f4-460f-9376-7b6f51871b44', '28', 'محكمة سيدي عيسى', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('56d248cd-a1a5-43e3-a4ee-b717b9f6e839', '04ccf245-44f4-460f-9376-7b6f51871b44', '28', 'محكمة عين الملح', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('c7692572-40eb-466a-9a8d-2d0c8f112a09', '04ccf245-44f4-460f-9376-7b6f51871b44', '28', 'محكمة حمام الضلعة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('f41df3db-3c0b-410f-bfab-70849739c044', '04ccf245-44f4-460f-9376-7b6f51871b44', '28', 'محكمة مقرة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('e67b4efa-2bc8-4708-862d-263f7b78db58', '04ccf245-44f4-460f-9376-7b6f51871b44', '28', 'فرع بن سرور', 1) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('39386e95-2493-492e-8c02-7a4dbcb6442f', '459483ab-20c1-478a-b273-1e45bf20c278', '29', 'محكمة معسكر', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('4e9c5430-d1d2-404d-aa35-01fdf7c8a86b', '459483ab-20c1-478a-b273-1e45bf20c278', '29', 'محكمة المحمدية', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('f15c7d1d-2b0d-40ec-8340-4816879907ea', '459483ab-20c1-478a-b273-1e45bf20c278', '29', 'محكمة سيق', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('0c793cba-c157-4cac-82a9-6dd2a691d3c1', '459483ab-20c1-478a-b273-1e45bf20c278', '29', 'محكمة تيغنيف', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('87737458-47a4-4d47-a584-efa0c77fb15e', '459483ab-20c1-478a-b273-1e45bf20c278', '29', 'محكمة غريس', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('296098e3-d130-4f74-a0de-b4841ea1fc18', '459483ab-20c1-478a-b273-1e45bf20c278', '29', 'محكمة بوحنيفية', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('38d43ac0-489d-41d4-a544-563bbe4ccd2a', '32c7d09c-b669-400b-8086-4dd833e38531', '30', 'محكمة ورقلة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('e7d1d463-18fc-4f29-8ea2-c2573c1ddb1d', '32c7d09c-b669-400b-8086-4dd833e38531', '30', 'محكمة تقرت', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('15e691b7-276c-4df8-8896-d666b00d1501', '32c7d09c-b669-400b-8086-4dd833e38531', '30', 'محكمة حاسي مسعود', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('b7754746-6361-4dee-af40-8635f740627e', '32c7d09c-b669-400b-8086-4dd833e38531', '30', 'فرع محكمة الحجيرة', 1) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('5e65e0c0-df45-46ef-bcb3-3f08daeac7e0', '32c7d09c-b669-400b-8086-4dd833e38531', '30', 'فرع محكمة الطيبات', 1) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('998c9498-a76f-41c2-9945-13942e5724ca', '4db2fb76-62cc-4a32-9a70-c6dc2757b7f6', '31', 'محكمة وهران', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('5f22ed5a-f257-47c6-b313-20f8fcbf9efc', '4db2fb76-62cc-4a32-9a70-c6dc2757b7f6', '31', 'محكمة عين الترك', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('6a9acd4e-b48f-4929-a2a0-73d25054c9f5', '4db2fb76-62cc-4a32-9a70-c6dc2757b7f6', '31', 'محكمة أرزيو', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('de274e8c-9d29-4866-809c-d09008ab0362', '4db2fb76-62cc-4a32-9a70-c6dc2757b7f6', '31', 'محكمة قديل', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('d5a90d52-8ede-444f-b60a-c6c8a925d7cd', '4db2fb76-62cc-4a32-9a70-c6dc2757b7f6', '31', 'محكمة وادي تليلات', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('2fae4d26-9cb9-42b9-916a-65eebe8ae8fc', '4db2fb76-62cc-4a32-9a70-c6dc2757b7f6', '31', 'محكمة السانية', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('3792f4d5-5c35-4934-88ae-9e2cd9819203', '4db2fb76-62cc-4a32-9a70-c6dc2757b7f6', '31', 'محكمة فلاوسن', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('fd1da9bd-7828-43a6-bd1f-a5351f920435', '4db2fb76-62cc-4a32-9a70-c6dc2757b7f6', '31', 'محكمة العثمانية', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('7ff9a053-6533-4d01-8d0b-5244535d63d5', 'a10850f0-d0d1-40a8-86a5-0b9a46f6c96f', '32', 'محكمة البيض', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('fe82e785-4293-48f6-b787-5ddde2196a19', 'a10850f0-d0d1-40a8-86a5-0b9a46f6c96f', '32', 'محكمة بوقطب', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('f6cf4884-5586-450a-aad1-8244b9b3a529', 'a10850f0-d0d1-40a8-86a5-0b9a46f6c96f', '32', 'محكمة الأبيض سيدي الشيخ', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('76964a6a-432d-4276-856c-702dc3bc08f2', '385a080d-8cc3-4eaa-b32e-98a8d0505d0e', '33', 'محكمة إليزي', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('b8e06de0-fe9f-4a6e-bb3f-e1329c27cede', '385a080d-8cc3-4eaa-b32e-98a8d0505d0e', '33', 'محكمة جانت', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('7ba31971-e9f7-446a-9cad-f528844c00a9', '385a080d-8cc3-4eaa-b32e-98a8d0505d0e', '33', 'محكمة ان امناس', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('e911d79f-2bad-4b53-90b5-d72db794ff4b', '8b0385c5-9d0f-429e-94b0-9c9a94fcbe57', '34', 'محكمة برج بوعريريج', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('de708955-9fe7-472b-ad3c-4147adab40da', '8b0385c5-9d0f-429e-94b0-9c9a94fcbe57', '34', 'محكمة راس الوادي', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('62c97d8c-15fc-4b7e-96bb-cd479a5ccbcd', '8b0385c5-9d0f-429e-94b0-9c9a94fcbe57', '34', 'محكمة المنصورة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('2af9643d-91ce-476d-a59a-840fbf7524a1', '8b0385c5-9d0f-429e-94b0-9c9a94fcbe57', '34', 'محكمة برج زمورة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('458ef205-ed71-4008-bff8-f216c8967657', 'fa381ec8-a95d-453f-8b56-b6624fd53fba', '35', 'محكمة بومرداس', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('2ba600bc-98ec-4e80-8ba7-659d4ec9c0f1', 'fa381ec8-a95d-453f-8b56-b6624fd53fba', '35', 'محكمة بودواو', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('f65d1586-6c80-419a-aebf-7bbe7db9701c', 'fa381ec8-a95d-453f-8b56-b6624fd53fba', '35', 'محكمة برج منايل', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('573e1eda-9a6a-45ae-9c0a-c4612209bd6f', 'fa381ec8-a95d-453f-8b56-b6624fd53fba', '35', 'محكمة دلس', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('93d18e8f-b619-40fc-8545-5962568bbbb7', 'fa381ec8-a95d-453f-8b56-b6624fd53fba', '35', 'محكمة خميس الخشنة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('3d2eabdf-b57c-4aaa-ba55-678af812244d', '7b79915e-478e-4a22-8941-0a18b4f94a74', '36', 'محكمة الطارف', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('9dfe3009-157a-45a9-80d6-f00c039f0830', '7b79915e-478e-4a22-8941-0a18b4f94a74', '36', 'محكمة بوحجار', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('420876fe-f0c9-4d95-991f-53dd2f3ccdd9', '7b79915e-478e-4a22-8941-0a18b4f94a74', '36', 'محكمة القالة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('c066defd-91bc-4966-849c-94d6339dc10f', '7b79915e-478e-4a22-8941-0a18b4f94a74', '36', 'محكمة الذرعان', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('73e06a03-9ae0-4067-b6c6-cc72bf8286ba', 'aca65743-f890-4bd7-ae51-826dcc80a97a', '37', 'محكمة تندوف', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('4c7655f7-3218-4660-83ae-39ca14b78739', '6d55bd0c-521b-4c0c-911b-ec7c60f142de', '38', 'محكمة تيسمسيلت', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('92b9e918-448a-4af1-9056-1995d2044ee3', '6d55bd0c-521b-4c0c-911b-ec7c60f142de', '38', 'محكمة برج بونعامة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('d42ad3ee-594e-4c45-9b5e-d73c7041213f', '6d55bd0c-521b-4c0c-911b-ec7c60f142de', '38', 'فرع مهدية', 1) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('647a89be-e475-41b4-ac21-723c072617b0', '6d55bd0c-521b-4c0c-911b-ec7c60f142de', '38', 'محكمة ثنية الحد', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('bf5d5eec-9e6b-40cb-b707-35ee35cd1efa', '70b04224-82ee-40c6-894e-1d4a4e52ebb2', '39', 'محكمة الوادي', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('7ed0e265-73ca-440d-a5a4-587e397af388', '70b04224-82ee-40c6-894e-1d4a4e52ebb2', '39', 'محكمة قمار', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('a2230e21-9160-4f23-ac49-3826629db1df', '70b04224-82ee-40c6-894e-1d4a4e52ebb2', '39', 'محكمة دبيلة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('dddfcaf3-42e1-4582-97b5-e2a4561c37b8', '70b04224-82ee-40c6-894e-1d4a4e52ebb2', '39', 'محكمة المغير', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('31d3cac4-cd2c-44f9-870d-ab0b21a45c1b', '70b04224-82ee-40c6-894e-1d4a4e52ebb2', '39', 'محكمة جامعة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('643f9f8d-4ed3-4eb0-a92a-6310b05dc098', '70b04224-82ee-40c6-894e-1d4a4e52ebb2', '39', 'فرع الرباح', 1) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('4ece7c98-aa3a-49c1-b387-a0569fb5a9eb', 'fb889355-2e10-4326-87f0-e43ef7c297bd', '40', 'محكمة خنشلة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('28dd2b9c-39be-4980-8fab-efa1d9b9424f', 'fb889355-2e10-4326-87f0-e43ef7c297bd', '40', 'محكمة قايس', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('b0886eb6-d037-49f1-98d1-6e2fb3182f52', 'fb889355-2e10-4326-87f0-e43ef7c297bd', '40', 'محكمة أولاد رشاش', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('0dc139f5-8fa6-4ce3-b8e5-fc9c76284e16', 'fb889355-2e10-4326-87f0-e43ef7c297bd', '40', 'محكمة ششار', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('dd62846c-ec56-407f-b9c8-5cb8f69ac350', 'fb889355-2e10-4326-87f0-e43ef7c297bd', '40', 'محكمة بوحمامة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('0dab08c9-5d60-48c2-b9ff-cfdd113c3f9d', 'fb889355-2e10-4326-87f0-e43ef7c297bd', '40', 'فرع عين الطويلة', 1) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('bbacb452-f8e8-4d94-a593-0ca9a27aa6d1', 'a1e8b1b4-befa-445d-88fe-bfe8f9a44983', '41', 'محكمة سوق أهراس', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('b863e5b3-924f-44c6-bb30-d735ab5a3637', 'a1e8b1b4-befa-445d-88fe-bfe8f9a44983', '41', 'محكمة سدراتة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('1315c28a-a2df-4ba6-9a8c-eef251aadabe', 'a1e8b1b4-befa-445d-88fe-bfe8f9a44983', '41', 'محكمة تاورة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('0b359301-e96f-4bb6-91e8-39635b0a9c10', '6ad0d006-f75e-4906-8891-02241156255e', '42', 'محكمة تيبازة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('374d399c-61c1-4a37-9806-24fe1d0ef7dd', '6ad0d006-f75e-4906-8891-02241156255e', '42', 'محكمة شرشال', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('ef4577a2-0633-4724-a199-aa8958d79ffd', '6ad0d006-f75e-4906-8891-02241156255e', '42', 'محكمة حجوط', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('24f4c4b0-ac13-42e8-928c-a9eebfc88385', '6ad0d006-f75e-4906-8891-02241156255e', '42', 'محكمة القليعة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('6a2837ff-7708-4269-8e5c-8a60b2f082b0', '6ad0d006-f75e-4906-8891-02241156255e', '42', 'محكمة الشراقة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('0cce4ed6-8fbb-49f1-be64-e6b8cfbf96ca', '95fc3967-2800-41f7-8c1e-4d520bf6d53b', '43', 'محكمة ميلة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('6b93bc2b-199d-4e06-8096-4acbfb59b222', '95fc3967-2800-41f7-8c1e-4d520bf6d53b', '43', 'محكمة شلغوم العيد', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('a067c452-2325-4da0-8580-cd1b2815dedd', '95fc3967-2800-41f7-8c1e-4d520bf6d53b', '43', 'محكمة فرجيوة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('6febda52-477d-403e-baf2-82374f8ced28', '4600f3fe-3af4-4a6e-b013-a3b41a377522', '44', 'محكمة عين الدفلى', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('e20df014-ed60-414e-b7a0-13e47dd47843', '4600f3fe-3af4-4a6e-b013-a3b41a377522', '44', 'محكمة مليانة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('f780cdc2-21a0-4a9c-89aa-a8bcc388a2c9', '4600f3fe-3af4-4a6e-b013-a3b41a377522', '44', 'محكمة خميس مليانة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('2b3fe188-80f2-4989-9668-e4a28a810263', '4600f3fe-3af4-4a6e-b013-a3b41a377522', '44', 'محكمة العطاف', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('c8612644-f4af-4011-90f4-6acbaeaea03b', '9c29c7a8-8c42-43f9-8b15-8dd5017db1cd', '45', 'محكمة النعامة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('46174402-47af-48d9-9c8a-7991b6509582', '9c29c7a8-8c42-43f9-8b15-8dd5017db1cd', '45', 'محكمة المشرية', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('0d84f7e6-b644-4b48-abca-45d39785286e', '9c29c7a8-8c42-43f9-8b15-8dd5017db1cd', '45', 'محكمة عين الصفراء', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('eef81f28-6014-4f1a-ba0f-759f5e304ae2', 'fd8f7e16-8815-4386-ba2d-2f85d17827f6', '46', 'محكمة عين تموشنت', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('d1794bb5-78b7-42c5-a89e-0cb3d7b48dc6', 'fd8f7e16-8815-4386-ba2d-2f85d17827f6', '46', 'محكمة حمام بوحجر', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('2f05918c-f35a-47f6-9964-494d57cafb38', 'fd8f7e16-8815-4386-ba2d-2f85d17827f6', '46', 'محكمة بني صاف', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('49330e84-0050-48db-ae41-70cced52e477', 'fd8f7e16-8815-4386-ba2d-2f85d17827f6', '46', 'محكمة العامرية', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('d7da94ec-c7d4-4b61-9cea-a90176e1b80b', 'bb5b4444-d1b7-4a54-9841-2e2213c9bf4e', '47', 'محكمة غرداية', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('13085346-4ce3-4de6-bbdd-329486f01883', 'bb5b4444-d1b7-4a54-9841-2e2213c9bf4e', '47', 'محكمة المنيعة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('38ec5e48-dadc-47f7-9530-6a9e91d7209f', 'bb5b4444-d1b7-4a54-9841-2e2213c9bf4e', '47', 'محكمة متليلي', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('9e7590da-239f-4a88-8d00-8e16e2c3b340', 'bb5b4444-d1b7-4a54-9841-2e2213c9bf4e', '47', 'محكمة بريان', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('c33efee3-e124-4397-be30-91d80eda1a91', 'bb5b4444-d1b7-4a54-9841-2e2213c9bf4e', '47', 'فرع القرارة', 1) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('0ab9a317-67d3-4c43-8afd-ff444efbfe09', '96dca960-9c34-4665-b2be-fb14ca0962cc', '48', 'محكمة غليزان', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('6b391176-c0f0-48b9-85f1-141f3c8c32c6', '96dca960-9c34-4665-b2be-fb14ca0962cc', '48', 'محكمة وادي ارهيو', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('4a0cae36-8247-422e-934e-dd040dfc6bad', '96dca960-9c34-4665-b2be-fb14ca0962cc', '48', 'محكمة عمي موسى', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('2896e723-cf5c-47f9-9ec2-bb3d91e56577', '96dca960-9c34-4665-b2be-fb14ca0962cc', '48', 'محكمة مازونة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('5d78aabb-0a88-48b7-8530-caf86a22483e', '96dca960-9c34-4665-b2be-fb14ca0962cc', '48', 'محكمة زمورة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('3813895c-0f8e-4b7c-852e-251ec580ba1b', 'e484257f-89df-411a-89dd-2a930f33122d', '49', 'محكمة تيميمون', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('8066192c-eb8f-4d3f-bb60-8ebc6a8c4464', 'e484257f-89df-411a-89dd-2a930f33122d', '49', 'محكمة أوقروت', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('fac8dce1-24cd-4968-ba04-8dca90896d56', '2eae8e5a-dc05-49b5-9514-e4ae094a4baa', '50', 'محكمة برج باجي مختار', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('900b1bd4-b2e5-4e16-a29b-4532a65a59f2', '2eae8e5a-dc05-49b5-9514-e4ae094a4baa', '50', 'محكمة تيمياوين', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('104c5e40-840e-4d03-baff-88bd5acb69c7', '5ca9bd4f-51b5-4740-911f-6c7e897e6ada', '51', 'محكمة أولاد جلال', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('3b591b9d-73ab-4ae8-be14-66d463427a33', '5ca9bd4f-51b5-4740-911f-6c7e897e6ada', '51', 'محكمة سيدي خالد', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('bf718a34-bab6-4720-a06a-b20c5f1d83fc', 'd73eb17c-0aed-4e27-9567-7571e2526c0a', '52', 'محكمة بني عباس', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('b4c4cf7f-58ff-4aa5-958e-8281c66dc61a', 'd73eb17c-0aed-4e27-9567-7571e2526c0a', '52', 'محكمة كرزاز', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('505c2d6d-3d55-42fc-89d3-7566b4eac2f0', 'd73eb17c-0aed-4e27-9567-7571e2526c0a', '52', 'محكمة تبلبالة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('a49721b4-f399-4492-a2dd-8de0bb9fb323', '229c1334-ae60-454a-9aaa-cad5215190aa', '53', 'محكمة إن صالح', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('d36c344b-08a0-4384-853e-d47f507e6c21', '229c1334-ae60-454a-9aaa-cad5215190aa', '53', 'محكمة عين صالح', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('029afc36-9d4f-41c0-8c15-05da60a260e9', '5db15c83-dfc3-4d40-ba50-83bd1bc15899', '54', 'محكمة إن قزام', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('e95560f5-9c2c-4675-9920-c7e07ef7b6ed', '5db15c83-dfc3-4d40-ba50-83bd1bc15899', '54', 'محكمة تين زواتين', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('5ed559c7-fd94-4220-86f6-ffd9587ead68', '348c5e09-2c9a-4374-8a6e-ebc70b1de16e', '55', 'محكمة توقرت', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('8981711d-f9d9-414b-9518-1b574d844625', '348c5e09-2c9a-4374-8a6e-ebc70b1de16e', '55', 'محكمة الطيبات', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('53a22525-8f65-4bcf-b550-5c5789ac08f0', '348c5e09-2c9a-4374-8a6e-ebc70b1de16e', '55', 'محكمة الحجيرة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('7b460bfa-42de-4b55-9e1d-50d266908604', '059bb023-04b2-4dc4-83c0-c8ed3c6af6d1', '56', 'محكمة جانت', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('f715db1e-8368-4675-9bb2-6a24956c621f', '059bb023-04b2-4dc4-83c0-c8ed3c6af6d1', '56', 'محكمة برج الحواس', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('13c1b179-0a61-43d6-a620-14f795a7a4aa', 'd937246a-a359-44c2-be20-fcb2fd1f7098', '57', 'محكمة المغير', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('dbb37482-93b7-4d26-bfa0-a91168dc33e8', 'd937246a-a359-44c2-be20-fcb2fd1f7098', '57', 'محكمة جامعة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('00bbd591-3264-4b0b-afc7-7e0137d5ac88', 'ba0f63e4-115b-439b-bc7e-f96c377f6e9a', '58', 'محكمة المنيعة', 0) ON CONFLICT DO NOTHING;
INSERT INTO "tribunals" ("id", "councilId", "wilayaCode", "name", "isBranch") VALUES ('5d4a7b82-85b4-4830-a488-41e2648215e3', 'ba0f63e4-115b-439b-bc7e-f96c377f6e9a', '58', 'محكمة حاسي القارة', 0) ON CONFLICT DO NOTHING;

-- Data for admin_courts (58 rows)
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('ca3ff73b-0629-49f6-b8dd-932dba874187', '01', '89593061-12fb-4dbf-b3d4-9980c20a7495', 'المحكمة الإدارية بأدرار') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('fc876e96-97c2-40a1-9ec3-3daa4383bd8b', '02', 'f0c3a685-89e3-44aa-a976-c7b149345238', 'المحكمة الإدارية بالشلف') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('8c8a3569-c771-4bd5-8173-499610a2c9c6', '03', '8aec6fff-cd46-4d28-83de-fc32f71a9491', 'المحكمة الإدارية بالأغواط') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('230f1de0-fb5f-46e1-bf2a-88def04670cc', '04', '71b28180-a27b-4fb9-ae98-98cc47c215ac', 'المحكمة الإدارية بأم البواقي') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('1286a989-f881-4dc5-917a-4f871810fbae', '05', 'fb20cf65-0220-447d-92ab-8b27bce9e922', 'المحكمة الإدارية بباتنة') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('81cc65c7-63c3-4368-b595-d6419e94d291', '06', '3a8ca6b7-cc4b-460b-83b9-c4d22daff00e', 'المحكمة الإدارية ببجاية') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('2430803d-8ac0-405f-881b-3dc129e4d16f', '07', 'a220827c-3e28-445e-b73e-3d4e55855c40', 'المحكمة الإدارية ببسكرة') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('ee48ae31-46b6-4446-85a6-589f714cf8db', '08', 'b8ccf7a1-c397-4d6a-a8b8-afa29d4b4f48', 'المحكمة الإدارية ببشار') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('e490dcb6-c73d-42fd-8972-c087608cdb2b', '09', 'b64a0367-e5b6-4ded-9835-77f8e540d56e', 'المحكمة الإدارية بالبليدة') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('555db71d-0686-4bbb-8e19-3bcfee32a13f', '10', '26089b85-7c56-42f4-96eb-a09ffe863ddc', 'المحكمة الإدارية بالبويرة') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('456ee338-6fce-4063-8e48-cb8d0cf58867', '11', 'ff8e1a0d-8012-4f06-b432-1822b108e001', 'المحكمة الإدارية بتمنراست') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('4c1987a4-5a0a-4d33-aba7-d749ff2ab77a', '12', 'a909db65-4425-4149-97ff-ab3608bc9dbd', 'المحكمة الإدارية بتبسة') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('fe00a385-5ced-41d0-a52b-07395eb21e94', '13', '8931908c-5f66-4ec7-b98f-36359f1b6de7', 'المحكمة الإدارية بتلمسان') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('a4aad6f4-1b0d-4c21-81b3-00a5385fbca1', '14', '8285ebfb-7bfc-4c83-846a-bd6636c4daa8', 'المحكمة الإدارية بتيارت') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('50d221a5-478a-4754-8714-1c765ccf4c1c', '15', 'd37a00f4-57cd-4767-a49e-c431347c3e96', 'المحكمة الإدارية بتيزي وزو') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('29925ec5-1dbf-489f-a25d-6e4149748c44', '16', 'e696fb5d-d502-4917-ac9c-de057c04356d', 'المحكمة الإدارية بالجزائر') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('4a672ba6-5e9c-4b6e-99a7-299277d37a57', '17', 'e3a031e8-3769-4293-995d-0e3395daf4c1', 'المحكمة الإدارية بالجلفة') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('faacb623-9c7a-498a-8ac7-69a0e87cca5e', '18', 'aa8d6c53-b250-4ff9-99d2-894e61336257', 'المحكمة الإدارية بجيجل') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('c8864cc7-dcf4-461d-8d1a-7990e96c1830', '19', '4a45ac9f-e444-4839-8607-8e64ec73a3c8', 'المحكمة الإدارية بسطيف') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('4d640ddf-59b6-4d54-bd69-e39a0486ebc5', '20', 'edb32b44-c01f-4c4a-9abb-733bdbfea5db', 'المحكمة الإدارية بسعيدة') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('c2a5a4df-ed4c-49c4-b61c-1bc543564425', '21', '5a93514d-350c-488d-8b76-2d5e3731cabe', 'المحكمة الإدارية بسكيكدة') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('ad44d4b4-c299-4eef-ad70-2ba59848b74a', '22', '2e8b57c2-e3c6-4d73-8fa2-12007921d5cf', 'المحكمة الإدارية بسيدي بلعباس') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('86cc6baa-fae7-43f2-9e3c-96fb5fc9b600', '23', 'c1f18858-5cdb-4bc2-80ba-16ba1085e138', 'المحكمة الإدارية بعنابة') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('944e67cc-24a3-4f44-bb39-e2ea6188b211', '24', '64721872-c443-47d9-998d-2cd3c1def12c', 'المحكمة الإدارية بقالمة') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('1db37866-49a2-4f25-8569-0f9244064f57', '25', 'cbc0f130-fd1f-4fd7-ac68-71919124e17b', 'المحكمة الإدارية بقسنطينة') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('339b6738-ab5d-462a-bd53-f2525a3538e6', '26', '695dc246-f5e6-4971-a052-f820f439c894', 'المحكمة الإدارية بالمدية') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('95158b35-9a34-48b9-9c0b-f14dbea7fb5a', '27', 'bd73b584-a67d-43fc-91f5-5971bfc0f1d6', 'المحكمة الإدارية بمستغانم') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('94ed77e1-b7d1-4c22-b88f-508128b5ecce', '28', '04ccf245-44f4-460f-9376-7b6f51871b44', 'المحكمة الإدارية بالمسيلة') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('733c92aa-3da2-42be-81b3-deae9cf4dd11', '29', '459483ab-20c1-478a-b273-1e45bf20c278', 'المحكمة الإدارية بمعسكر') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('523b3ef1-7b3d-440e-a56b-3c6ecbe0c26a', '30', '32c7d09c-b669-400b-8086-4dd833e38531', 'المحكمة الإدارية بورقلة') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('237b503d-29cc-4e42-8cf9-43f40d0d088d', '31', '4db2fb76-62cc-4a32-9a70-c6dc2757b7f6', 'المحكمة الإدارية بوهران') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('38a69c00-4ca7-4e66-838b-dfa61ddd226b', '32', 'a10850f0-d0d1-40a8-86a5-0b9a46f6c96f', 'المحكمة الإدارية بالبيض') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('a03d9297-12fc-455c-b3ba-46d5ecec9ea6', '33', '385a080d-8cc3-4eaa-b32e-98a8d0505d0e', 'المحكمة الإدارية بإليزي') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('fb1ca3bc-5ace-4c18-ab45-82e04379acf8', '34', '8b0385c5-9d0f-429e-94b0-9c9a94fcbe57', 'المحكمة الإدارية ببرج بوعريريج') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('0425bb04-a3d7-472a-a8c6-2da2e3a8c3fb', '35', 'fa381ec8-a95d-453f-8b56-b6624fd53fba', 'المحكمة الإدارية ببومرداس') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('73a5c7da-0ab2-4555-8da3-aa8794e4f7f8', '36', '7b79915e-478e-4a22-8941-0a18b4f94a74', 'المحكمة الإدارية بالطارف') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('5727f6cf-be07-417e-a655-f72120e06667', '37', 'aca65743-f890-4bd7-ae51-826dcc80a97a', 'المحكمة الإدارية بتندوف') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('b1782192-c819-4b23-8ac7-741180f9781b', '38', '6d55bd0c-521b-4c0c-911b-ec7c60f142de', 'المحكمة الإدارية بتيسمسيلت') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('001b1edf-cc78-4ebe-95bb-6da7fb9e9470', '39', '70b04224-82ee-40c6-894e-1d4a4e52ebb2', 'المحكمة الإدارية بالوادي') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('3529499f-613f-49f4-b4cf-74c9b921ff67', '40', 'fb889355-2e10-4326-87f0-e43ef7c297bd', 'المحكمة الإدارية بخنشلة') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('7b8a1847-17b6-4391-b9ce-f0e5975ad530', '41', 'a1e8b1b4-befa-445d-88fe-bfe8f9a44983', 'المحكمة الإدارية بسوق أهراس') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('9decedd3-1f55-419d-b690-84710a93c066', '42', '6ad0d006-f75e-4906-8891-02241156255e', 'المحكمة الإدارية بتيبازة') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('264063ef-eff7-4e4b-9d8a-afb70995efea', '43', '95fc3967-2800-41f7-8c1e-4d520bf6d53b', 'المحكمة الإدارية بميلة') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('66848545-e8fe-49c3-8006-b3b90f8cecf5', '44', '4600f3fe-3af4-4a6e-b013-a3b41a377522', 'المحكمة الإدارية بعين الدفلى') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('7d8c9718-4d5e-4c5d-809b-abf0474ab54a', '45', '9c29c7a8-8c42-43f9-8b15-8dd5017db1cd', 'المحكمة الإدارية بالنعامة') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('aace0add-4657-4c56-9ddb-a1790d59557d', '46', 'fd8f7e16-8815-4386-ba2d-2f85d17827f6', 'المحكمة الإدارية بعين تموشنت') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('a547b1d9-40af-4a2d-995f-89d818f65657', '47', 'bb5b4444-d1b7-4a54-9841-2e2213c9bf4e', 'المحكمة الإدارية بغرداية') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('59780621-16e9-4680-bb82-69c40006e6a5', '48', '96dca960-9c34-4665-b2be-fb14ca0962cc', 'المحكمة الإدارية بغليزان') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('f6999e64-3e5e-4524-9454-22b85e226e45', '49', 'e484257f-89df-411a-89dd-2a930f33122d', 'المحكمة الإدارية بتيميمون') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('10854774-8d05-40f8-9975-7b572f7a9836', '50', '2eae8e5a-dc05-49b5-9514-e4ae094a4baa', 'المحكمة الإدارية ببرج باجي مختار') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('f87b81c8-bbce-4aca-8b39-0d082b34378c', '51', '5ca9bd4f-51b5-4740-911f-6c7e897e6ada', 'المحكمة الإدارية بأولاد جلال') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('2b44da58-7028-4f8d-b6ee-987fb76007fd', '52', 'd73eb17c-0aed-4e27-9567-7571e2526c0a', 'المحكمة الإدارية ببني عباس') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('59462c37-e3d4-42b2-87dd-f734e81a036b', '53', '229c1334-ae60-454a-9aaa-cad5215190aa', 'المحكمة الإدارية بإن صالح') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('84d64e84-c7b6-4466-a534-98ec47dec304', '54', '5db15c83-dfc3-4d40-ba50-83bd1bc15899', 'المحكمة الإدارية بإن قزام') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('cb30978b-1adc-4a57-81c2-645be16d60e8', '55', '348c5e09-2c9a-4374-8a6e-ebc70b1de16e', 'المحكمة الإدارية بتوقرت') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('64673862-1ffa-400d-b99b-15a41bf0b3a6', '56', '059bb023-04b2-4dc4-83c0-c8ed3c6af6d1', 'المحكمة الإدارية بجانت') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('f08288a8-830c-48dd-85c6-60fa10c5639b', '57', 'd937246a-a359-44c2-be20-fcb2fd1f7098', 'المحكمة الإدارية بالمغير') ON CONFLICT DO NOTHING;
INSERT INTO "admin_courts" ("id", "wilayaCode", "councilId", "name") VALUES ('a678d01f-24bb-42c7-92be-17bcbff4a9ad', '58', 'ba0f63e4-115b-439b-bc7e-f96c377f6e9a', 'المحكمة الإدارية بالمنيعة') ON CONFLICT DO NOTHING;

-- Data for court_sections (9 rows)
INSERT INTO "court_sections" ("id", "name") VALUES ('428ee9ec', 'المدني') ON CONFLICT DO NOTHING;
INSERT INTO "court_sections" ("id", "name") VALUES ('e325243d', 'الجزائي') ON CONFLICT DO NOTHING;
INSERT INTO "court_sections" ("id", "name") VALUES ('0ced6374', 'الأسرة') ON CONFLICT DO NOTHING;
INSERT INTO "court_sections" ("id", "name") VALUES ('9ee9de27', 'التجاري') ON CONFLICT DO NOTHING;
INSERT INTO "court_sections" ("id", "name") VALUES ('07f6f003', 'العقاري') ON CONFLICT DO NOTHING;
INSERT INTO "court_sections" ("id", "name") VALUES ('fca3a91a', 'الاجتماعي') ON CONFLICT DO NOTHING;
INSERT INTO "court_sections" ("id", "name") VALUES ('a3652e72', 'الاستعجالي') ON CONFLICT DO NOTHING;
INSERT INTO "court_sections" ("id", "name") VALUES ('5535eed2', 'البحري') ON CONFLICT DO NOTHING;
INSERT INTO "court_sections" ("id", "name") VALUES ('836c9a23', 'شؤون الأسرة') ON CONFLICT DO NOTHING;

-- Data for clients (1 rows)
INSERT INTO "clients" ("id", "fullName", "phone", "email", "address", "profession", "nationalId", "notes", "createdAt") VALUES ('c43469c8-f0f0-4c9b-a07f-5f8bd9327f3b', 'محمد راقب', '0550 12 34 56', NULL, 'الرويبة - الجزائر', 'تاجر', NULL, NULL, '2026-09-07 18:20:19') ON CONFLICT DO NOTHING;

-- Data for cases (1 rows)
INSERT INTO "cases" ("id", "fileNumber", "caseNumber", "clientId", "title", "wilayaCode", "councilName", "tribunalName", "section", "category", "clientRole", "opponent", "opponentLawyer", "status", "assignedLawyer", "openDate", "closeDate", "notes", "strategy", "createdAt", "updatedAt") VALUES ('995dfa06-c5c1-4fab-96bd-abdbb30bf5a7', '2026/184', '1234/2026', 'c43469c8-f0f0-4c9b-a07f-5f8bd9327f3b', 'نزاع عقاري - قطعة أرض الرويبة', '16', 'مجلس قضاء الجزائر', 'محكمة الرويبة', 'العقاري', 'عقاري', 'مدعي', 'سعيد مراد', NULL, 'قيد المتابعة', 'أ. بهاء الدين', '2026-01-15', NULL, NULL, NULL, '2026-09-07 18:20:19', NULL) ON CONFLICT DO NOTHING;

