// @ts-nocheck
// lib/prisma.ts — Prisma Client singleton (PostgreSQL)
// يعمل مع PostgreSQL (Web) و SQLite (PC محلي) حسب DATABASE_URL
// ملاحظة: يتطلب npx prisma generate قبل الاستخدام — وإلا سيعمل fallback إلى JSON/SQLite

import { PrismaClient } from '@prisma/client';

declare global {
  var prisma: PrismaClient | undefined;
}

function createPrisma() {
  const url = process.env.DATABASE_URL || '';
  // For SQLite file: file:./prisma/maktabi.db
  // For PostgreSQL: postgresql://user:pass@host:5432/maktabi
  return new PrismaClient({
    log: process.env.NODE_ENV === 'development' ? ['warn', 'error'] : ['error'],
  });
}

export const prisma = global.prisma || createPrisma();

if (process.env.NODE_ENV !== 'production') global.prisma = prisma;

export default prisma;
