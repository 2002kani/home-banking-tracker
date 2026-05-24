import { columns } from "./columns";
import { DataTable } from "@/components/shared/dataTable";
import type { Payment } from "@/lib/types";

const data: Payment[] = [
  {
    id: "728ed52f",
    amount: 100,
    status: "pending",
    email: "m@example.com",
  },
];

function TransactionsTable() {
  return <DataTable columns={columns} data={data} />;
}

export default TransactionsTable;
