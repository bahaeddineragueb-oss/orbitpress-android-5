# مخطط قاعدة البيانات — مكتبي (Avocat DZ Pro)

## الكيانات (13 جدول)

### 1. Client
- id, fullName, phone, email, address, profession, nationalId, notes, createdAt
- relations: cases[]

### 2. CourtCase (Case)
- id, fileNumber (unique), caseNumber, clientId FK, title
- court, council, section, category (enum), clientRole (enum)
- opponent, opponentLawyer, status (enum), assignedLawyerId FK
- openDate, closeDate, notes, strategy, archivedAt

### 3. Hearing
- id, caseId FK, date, court, room, judge, reason, decision, nextDate, status

### 4. Deadline
- id, caseId FK, title, dueDate, type (استئناف/طعن/...), priority, done, notifiedAt

### 5. Document
- id, caseId FK, title, category, date, fileName, fileUrl, fileSize, mime, uploadedBy FK, notes

### 6. Fee
- id, caseId FK, clientId FK, total, paid, dueDate, method, installments JSON, receipts[]

### 7. Expense
- id, caseId FK nullable, title, amount, date, category, paidBy FK, receiptUrl

### 8. LegalTemplate
- id, title, category, description, fields JSON, content (with {{keys}}), createdBy

### 9. CourtInfo
- id, wilaya, council, court, section, phone, address, hours

### 10. User
- id, name, email, phone, role (admin/lawyer/trainee/secretary), avatar, active

### 11. AuditLog
- id, userId FK, action (create/update/delete/view), entity, entityId, at, ip, details JSON

### 12. Notification
- id, userId FK, title, body, type, relatedId, read, at

### 13. CommunicationLog (سجل التواصل)
- id, clientId FK, caseId FK nullable, channel (call/whatsapp/visit), at, notes, by FK

---

## Prisma مثال

```prisma
model Client {
  id        String   @id @default(cuid())
  fullName  String
  phone     String
  email     String?
  address   String?
  profession String?
  nationalId String?
  notes     String?
  createdAt DateTime @default(now())
  cases     CourtCase[]
}

model CourtCase {
  id            String @id @default(cuid())
  fileNumber    String @unique
  caseNumber    String
  clientId      String
  client        Client @relation(fields:[clientId], references:[id])
  title         String
  court         String
  council       String
  section       String
  category      String
  clientRole    String
  opponent      String
  opponentLawyer String?
  status        String
  assignedLawyer String
  openDate      DateTime
  notes         String?
  strategy      String?
  hearings      Hearing[]
  deadlines     Deadline[]
  documents     Document[]
  fees          Fee[]
}

model Hearing {
  id       String @id @default(cuid())
  caseId   String
  case     CourtCase @relation(fields:[caseId], references:[id])
  date     DateTime
  court    String
  room     String?
  judge    String?
  decision String?
  nextDate DateTime?
  status   String
}
```

---

## فهارس مقترحة
- fileNumber unique
- caseNumber + court composite
- clientId, status, category
- deadlines.dueDate (للبحث عن العاجل)
- documents.caseId + category
