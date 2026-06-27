import type { TransactionDto } from "@/api/generated/transaction-service";

export const STATUS_LABEL: Record<NonNullable<TransactionDto["status"]>, string> = {
  BOOK: "Gebucht",
  CNCL: "Storniert",
  HOLD: "Zurückgehalten",
  OTHR: "Sonstig",
  PDNG: "Ausstehend",
  RJCT: "Abgelehnt",
  SCHD: "Geplant",
};
