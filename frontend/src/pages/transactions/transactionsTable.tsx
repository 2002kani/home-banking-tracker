import { columns } from "./columns";
import { DataTable } from "@/components/shared/dataTable";
import type { Payment } from "@/lib/types";

const data: Payment[] = [
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
  {
    id: "a1b2c3d4",
    date: "12.01.2024",
    creditor: "Edeka",
    category: "Groceries",
    status: "success",
    amount: 45,
  },
  {
    id: "b2c3d4e5",
    date: "15.01.2024",
    creditor: "Netflix",
    category: "Subscription",
    status: "success",
    amount: 15,
  },
  {
    id: "c3d4e5f6",
    date: "18.01.2024",
    creditor: "Apple",
    category: "Electronics",
    status: "processing",
    amount: 999,
  },
  {
    id: "d4e5f6g7",
    date: "20.01.2024",
    creditor: "Shell",
    category: "Transport",
    status: "success",
    amount: 60,
  },
  {
    id: "e5f6g7h8",
    date: "22.01.2024",
    creditor: "Lidl",
    category: "Groceries",
    status: "pending",
    amount: 32,
  },
  {
    id: "f6g7h8i9",
    date: "25.01.2024",
    creditor: "Adobe",
    category: "Software",
    status: "success",
    amount: 60,
  },
  {
    id: "g7h8i9j0",
    date: "27.01.2024",
    creditor: "IKEA",
    category: "Furniture",
    status: "processing",
    amount: 250,
  },
  {
    id: "h8i9j0k1",
    date: "28.01.2024",
    creditor: "DM",
    category: "Groceries",
    status: "success",
    amount: 18,
  },
  {
    id: "i9j0k1l2",
    date: "29.01.2024",
    creditor: "Uber",
    category: "Transport",
    status: "failed",
    amount: 22,
  },
  {
    id: "j0k1l2m3",
    date: "30.01.2024",
    creditor: "Steam",
    category: "Gaming",
    status: "success",
    amount: 59,
  },
];

function TransactionsTable() {
  return <DataTable columns={columns} data={data} />;
}

export default TransactionsTable;
