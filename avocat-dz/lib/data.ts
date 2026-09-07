import { Client, CourtCase, Hearing, Deadline, Document, Fee, Expense, LegalTemplate, CourtInfo } from "./types";

export const mockClients: Client[] = [
  { id: "c1", fullName: "محمد راقب", phone: "0550 12 34 56", email: "mohamed@example.dz", address: "الجزائر - الرويبة", profession: "تاجر", nationalId: "123456789", notes: "عميل دائم منذ 2021", createdAt: "2024-01-10" },
  { id: "c2", fullName: "فاطمة بن علي", phone: "0770 98 76 54", email: "fatima@example.dz", address: "بومرداس", profession: "موظفة", createdAt: "2024-03-22" },
  { id: "c3", fullName: "أحمد بوعلام", phone: "0561 22 33 44", address: "الجزائر العاصمة", profession: "مقاول", createdAt: "2024-05-05" },
  { id: "c4", fullName: "سارة حمدي", phone: "0662 55 66 77", address: "البليدة", profession: "طبيبة", createdAt: "2024-08-12" },
];

export const mockCases: CourtCase[] = [
  { id: "k1", fileNumber: "2026/184", caseNumber: "1234/2026", clientId: "c1", title: "نزاع عقاري - قطعة أرض الرويبة", court: "محكمة الرويبة", council: "مجلس قضاء الجزائر", section: "عقاري", category: "عقاري", clientRole: "مدعي", opponent: "سعيد مراد", opponentLawyer: "أ. كريم", status: "قيد المتابعة", assignedLawyer: "أ. بهاء الدين", openDate: "2026-01-15", notes: "عقد عرفي + شهادة حيازة", strategy: "إثبات الحيازة بالشهود والخبرة" },
  { id: "k2", fileNumber: "2026/201", caseNumber: "892/2026", clientId: "c2", title: "طلاق بالتراضي", court: "محكمة الحراش", council: "مجلس قضاء الجزائر", section: "أسرة", category: "أسرة", clientRole: "مدعية", opponent: "كمال بن علي", status: "مؤجل", assignedLawyer: "أ. ليلى", openDate: "2026-02-10", notes: "جلسة صلح أولى" },
  { id: "k3", fileNumber: "2026/045", caseNumber: "567/2026", clientId: "c3", title: "جنحة إصدار شيك بدون رصيد", court: "محكمة سيدي أمحمد", council: "مجلس قضاء الجزائر", section: "جزائي", category: "جزائي", clientRole: "متهم", opponent: "النيابة العامة", status: "استئناف", assignedLawyer: "أ. بهاء الدين", openDate: "2025-11-20" },
  { id: "k4", fileNumber: "2026/310", caseNumber: "2105/2026", clientId: "c1", title: "مطالبة بدين تجاري", court: "محكمة بئر مراد رايس", council: "مجلس قضاء الجزائر", section: "تجاري", category: "تجاري", clientRole: "مدعي", opponent: "شركة نور للخدمات", status: "جديد", assignedLawyer: "أ. بهاء الدين", openDate: "2026-08-01" },
  { id: "k5", fileNumber: "2025/412", caseNumber: "3421/2025", clientId: "c4", title: "تعويض عن حادث مرور", court: "محكمة البليدة", council: "مجلس قضاء البليدة", section: "مدني", category: "مدني", clientRole: "ضحية", opponent: "شركة التأمين SAA", status: "محكوم", assignedLawyer: "أ. ليلى", openDate: "2025-06-18" },
];

export const mockHearings: Hearing[] = [
  { id: "h1", caseId: "k1", date: new Date(Date.now() + 86400000).toISOString(), court: "محكمة الرويبة", room: "قاعة 03", judge: "الغرفة العقارية", status: "قادمة", reason: "سماع الشهود" },
  { id: "h2", caseId: "k2", date: new Date(Date.now() + 3*86400000).toISOString(), court: "محكمة الحراش", room: "قاعة 01", judge: "قاضي الأسرة", status: "قادمة" },
  { id: "h3", caseId: "k3", date: new Date(Date.now() - 5*86400000).toISOString(), court: "محكمة سيدي أمحمد", status: "تمت", decision: "تأجيل لجلسة 2026-09-20", nextDate: "2026-09-20" },
  { id: "h4", caseId: "k4", date: new Date(Date.now() + 7*86400000).toISOString(), court: "محكمة بئر مراد رايس", room: "قاعة 02", status: "قادمة" },
];

export const mockDeadlines: Deadline[] = [
  { id: "d1", caseId: "k3", title: "أجل استئناف الحكم الجزائي", dueDate: new Date(Date.now() + 10*86400000).toISOString(), type: "استئناف", priority: "عاجل", done: false },
  { id: "d2", caseId: "k1", title: "إيداع مذكرة جوابية", dueDate: new Date(Date.now() + 3*86400000).toISOString(), type: "أخرى", priority: "هام", done: false },
  { id: "d3", caseId: "k2", title: "تبليغ عريضة الافتتاح", dueDate: new Date(Date.now() + 15*86400000).toISOString(), type: "تبليغ", priority: "عادي", done: false },
  { id: "d4", caseId: "k5", title: "أجل الطعن بالنقض", dueDate: new Date(Date.now() + 45*86400000).toISOString(), type: "طعن", priority: "هام", done: false },
];

export const mockDocuments: Document[] = [
  { id: "doc1", caseId: "k1", title: "عريضة افتتاح الدعوى العقارية", category: "عريضة افتتاح", date: "2026-01-16", fileName: "arida_rouiba.pdf", uploadedBy: "أ. بهاء الدين" },
  { id: "doc2", caseId: "k1", title: "عقد عرفي - قطعة الأرض", category: "أخرى", date: "2026-01-10", fileName: "aqd_orfi.pdf", uploadedBy: "العميل" },
  { id: "doc3", caseId: "k2", title: "مذكرة جوابية - طلاق", category: "مذكرة", date: "2026-03-01", fileName: "mothakira.pdf", uploadedBy: "أ. ليلى" },
  { id: "doc4", caseId: "k3", title: "حكم ابتدائي", category: "حكم", date: "2026-08-20", fileName: "hukm.pdf", uploadedBy: "المحكمة" },
];

export const mockFees: Fee[] = [
  { id: "f1", caseId: "k1", clientId: "c1", total: 150000, paid: 80000, dueDate: "2026-09-30", method: "تحويل" },
  { id: "f2", caseId: "k2", clientId: "c2", total: 80000, paid: 80000, method: "نقداً" },
  { id: "f3", caseId: "k3", clientId: "c3", total: 200000, paid: 50000, dueDate: "2026-10-15" },
  { id: "f4", caseId: "k5", clientId: "c4", total: 120000, paid: 0, dueDate: "2026-09-20" },
];

export const mockExpenses: Expense[] = [
  { id: "e1", caseId: "k1", title: "تبليغ عن طريق المحضر القضائي", amount: 4000, date: "2026-01-20", category: "محضر قضائي" },
  { id: "e2", caseId: "k2", title: "طوابع جبائية", amount: 1500, date: "2026-02-12", category: "طوابع" },
  { id: "e3", title: "تنقل إلى مجلس قضاء الجزائر", amount: 2500, date: "2026-08-10", category: "تنقل" },
  { id: "e4", caseId: "k3", title: "نسخ ملف", amount: 800, date: "2026-08-22", category: "نسخ" },
];

export const legalTemplates: LegalTemplate[] = [
  {
    id: "t1", title: "عريضة افتتاح دعوى مدنية", category: "مدني",
    description: "نموذج عريضة افتتاح أمام القسم المدني",
    fields: [
      { key: "court", label: "المحكمة", placeholder: "محكمة الرويبة" },
      { key: "client", label: "اسم المدعي", placeholder: "محمد راقب" },
      { key: "opponent", label: "اسم المدعى عليه", placeholder: "أحمد ..." },
      { key: "caseNo", label: "رقم القضية", placeholder: "1234/2026" },
      { key: "facts", label: "الوقائع", placeholder: "حيث أن..." },
    ],
    content: `إلى السيد رئيس {{court}}\nالموضوع: عريضة افتتاح دعوى\nلفائدة: {{client}} ضد {{opponent}}\nرقم القضية: {{caseNo}}\n\nالوقائع:\n{{facts}}\n\nلهذه الأسباب نلتمس من المحكمة الحكم لصالح موكلنا...`
  },
  {
    id: "t2", title: "مذكرة جوابية", category: "عام",
    description: "رد على عريضة الخصم",
    fields: [
      { key: "court", label: "المحكمة", placeholder: "محكمة ..." },
      { key: "client", label: "الموكل", placeholder: "..." },
      { key: "reply", label: "الرد", placeholder: "حيث يدعي الخصم..." },
    ],
    content: `مذكرة جوابية أمام {{court}}\nعن {{client}}\n\n{{reply}}\n\nوعليه نطلب رفض طلبات الخصم.`
  },
  {
    id: "t3", title: "طلب تأجيل جلسة", category: "إجراء",
    description: "طلب تأجيل لسبب مشروع",
    fields: [
      { key: "court", label: "المحكمة", placeholder: "..." },
      { key: "caseNo", label: "رقم القضية", placeholder: "..." },
      { key: "reason", label: "سبب التأجيل", placeholder: "..." },
    ],
    content: `السيد رئيس {{court}}\nالموضوع: طلب تأجيل جلسة القضية رقم {{caseNo}}\nالسبب: {{reason}}\nنلتمس تأجيل الجلسة إلى تاريخ لاحق.`
  },
  {
    id: "t4", title: "إعذار", category: "إنذار",
    description: "إعذار قبل اللجوء للقضاء",
    fields: [
      { key: "recipient", label: "المرسل إليه", placeholder: "..." },
      { key: "subject", label: "الموضوع", placeholder: "..." },
    ],
    content: `إعذار إلى السيد {{recipient}}\nالموضوع: {{subject}}\nنعذركم بضرورة التسوية الودية خلال 15 يوما قبل اللجوء للقضاء.`
  },
];

export const courtsDB: CourtInfo[] = [
  { wilaya: "الجزائر", council: "مجلس قضاء الجزائر", court: "محكمة الرويبة", section: "مدني", phone: "023 XX XX XX", address: "الرويبة - الجزائر" },
  { wilaya: "الجزائر", council: "مجلس قضاء الجزائر", court: "محكمة الرويبة", section: "عقاري", phone: "023 XX XX XX", address: "الرويبة" },
  { wilaya: "الجزائر", council: "مجلس قضاء الجزائر", court: "محكمة الحراش", section: "أسرة", phone: "023 XX XX XX", address: "الحراش" },
  { wilaya: "الجزائر", council: "مجلس قضاء الجزائر", court: "محكمة سيدي أمحمد", section: "جزائي", phone: "021 XX XX XX", address: "سيدي أمحمد" },
  { wilaya: "الجزائر", council: "مجلس قضاء الجزائر", court: "محكمة بئر مراد رايس", section: "تجاري", phone: "021 XX XX XX", address: "بئر مراد رايس" },
  { wilaya: "البليدة", council: "مجلس قضاء البليدة", court: "محكمة البليدة", section: "مدني", phone: "025 XX XX XX", address: "البليدة" },
  { wilaya: "وهران", council: "مجلس قضاء وهران", court: "محكمة وهران", section: "مدني", phone: "041 XX XX XX", address: "وهران" },
  { wilaya: "قسنطينة", council: "مجلس قضاء قسنطينة", court: "محكمة قسنطينة", section: "إداري", phone: "031 XX XX XX", address: "قسنطينة" },
];
