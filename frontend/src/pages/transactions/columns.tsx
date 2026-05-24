import type { ColumnDef } from "@tanstack/react-table";
import type { Payment } from "@/lib/types";

export const columns: ColumnDef<Payment>[] = [
  {
    accessorKey: "date",
    header: "Datum",
  },
  {
    accessorKey: "creditor",
    header: "Händler",
  },
  {
    accessorKey: "category",
    header: "Kategorie",
  },
  {
    accessorKey: "status",
    header: "Status",
  },
  {
    // TODO: Das hier ggf im cell beabreiten, je nachdem wie api amount aussieht
    accessorKey: "amount",
    header: () => <div className="text-right">Amount</div>,
    cell: ({ row }) => {
      const amount = parseFloat(row.getValue("amount"));
      const formatted = new Intl.NumberFormat("de-DE", {
        style: "currency",
        currency: "EUR",
      }).format(amount);

      return <div className="text-right font-medium">{formatted}</div>;
    },
  },
];
