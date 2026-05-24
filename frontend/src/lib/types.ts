export type Payment = {
  id: string;
  date: string;
  creditor: string;
  category: string; // TODO: change that
  status: "pending" | "processing" | "success" | "failed"; // TODO: change that
  amount: number;
};

export const payments: Payment[] = [
  {
    id: "728ed52f",
    date: "01.01.2024",
    creditor: "REWE",
    category: "Groceries",
    status: "pending",
    amount: 100,
  },
  {
    id: "489e1d42",
    date: "05.01.2024",
    creditor: "Amazon",
    category: "Shopping",
    status: "processing",
    amount: 125,
  },
  {
    id: "1a2b3c4d",
    date: "10.01.2024",
    creditor: "Spotify",
    category: "Subscription",
    status: "success",
    amount: 150,
  },
];
