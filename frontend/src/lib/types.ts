export interface Transaction {
  id: number;
  accountId: string;
  amount: string;
  currency: string;
  creditorName: string;
  debtorName: string;
  type: CreditDebitIndicator;
  bookingDate: string;
  status: TransactionStatus;
  categoryId: number;
  categoryName: string;
  categoryColor: string;
}

export type TransactionStatus =
  | "BOOK"
  | "CNCL"
  | "HOLD"
  | "OTHR"
  | "PDNG"
  | "RJCT"
  | "SCHD";

export type CreditDebitIndicator = "CRDT" | "DBIT";
