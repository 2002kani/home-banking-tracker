import type { Transaction } from "@/lib/types";

export const STATUS_LABEL: Record<Transaction["status"], string> = {
  BOOK: "Gebucht",
  CNCL: "Storniert",
  HOLD: "Zurückgehalten",
  OTHR: "Sonstig",
  PDNG: "Ausstehend",
  RJCT: "Abgelehnt",
  SCHD: "Geplant",
};
