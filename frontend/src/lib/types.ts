export type Transaction = {
  id: number;
  accountId: string;
  amount: string;
  currency: string;
  creditorName: string;
  debtorName: string;
  type: "CRDT" | "DBIT";
  bookingDate: string;
  status: "BOOK" | "CNCL" | "HOLD" | "OTHR" | "PDNG" | "RJCT" | "SCHD";
  categoryId: number;
  categoryName: string;
  categoryColor: string;
};
