// Types — دورة حياة الملف كاملة
export type Client = {
  id: string;
  fullName: string;
  phone: string;
  email?: string;
  address?: string;
  profession?: string;
  nationalId?: string;
  notes?: string;
  createdAt: string;
};

export type CaseStatus = "جديد" | "قيد المتابعة" | "مؤجل" | "محكوم" | "استئناف" | "طعن" | "مغلق" | "مؤرشف";
export type CaseCategory = "مدني" | "جزائي" | "إداري" | "تجاري" | "أسرة" | "اجتماعي" | "عقاري" | "أخرى";
export type ClientRole = "مدعي" | "مدعية" | "مدعى عليه" | "ضحية" | "متهم" | "مستأنف" | "مستأنف عليه";

export type CourtCase = {
  id: string;
  fileNumber: string; // رقم الملف الداخلي
  caseNumber: string; // رقم القضية في المحكمة
  clientId: string;
  title: string;
  court: string; // e.g., "محكمة الرويبة"
  council: string; // المجلس القضائي
  section: string; // القسم
  category: CaseCategory;
  clientRole: ClientRole;
  opponent: string;
  opponentLawyer?: string;
  status: CaseStatus;
  assignedLawyer: string;
  openDate: string;
  notes?: string;
  strategy?: string;
};

export type Hearing = {
  id: string;
  caseId: string;
  date: string; // ISO
  court: string;
  room?: string;
  judge?: string;
  reason?: string;
  decision?: string;
  nextDate?: string;
  status: "قادمة" | "تمت" | "مؤجلة" | "ملغاة";
};

export type Deadline = {
  id: string;
  caseId: string;
  title: string;
  dueDate: string;
  type: "استئناف" | "طعن" | "تبليغ" | "خبرة" | "دفع" | "أخرى";
  priority: "عادي" | "هام" | "عاجل";
  done: boolean;
};

export type DocCategory = "عريضة افتتاح" | "مذكرة" | "محضر" | "حكم" | "قرار" | "استدعاء" | "وكالة" | "مراسلة" | "أخرى";
export type Document = {
  id: string;
  caseId: string;
  title: string;
  category: DocCategory;
  date: string;
  fileName?: string;
  uploadedBy: string;
  notes?: string;
};

export type Fee = {
  id: string;
  caseId: string;
  clientId: string;
  total: number;
  paid: number;
  dueDate?: string;
  method?: string;
};

export type Expense = {
  id: string;
  caseId?: string;
  title: string;
  amount: number;
  date: string;
  category: "تبليغ" | "محضر قضائي" | "تنقل" | "طوابع" | "نسخ" | "خبرة" | "بريد" | "أخرى";
};

export type TemplateField = { key: string; label: string; placeholder: string };
export type LegalTemplate = {
  id: string;
  title: string;
  category: string;
  description: string;
  fields: TemplateField[];
  content: string; // with {{key}} placeholders
};

export type CourtInfo = {
  wilaya: string;
  council: string;
  court: string;
  section: string;
  phone?: string;
  address?: string;
};
