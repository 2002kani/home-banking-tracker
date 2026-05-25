import { useState } from "react";
import { ArrowDownLeft, ArrowUpRight, Search } from "lucide-react";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { MerchantAvatar } from "@/components/transactions/MerchantAvatar";
import { CategoryBadge } from "@/components/transactions/CategoryBadge";
import { TransactionPopover } from "@/components/transactions/TransactionPopover";
import { STATUS_LABEL } from "@/lib/transactionConfig";
import type { Transaction } from "@/lib/types";

// TODO: Replace with API data
const MOCK_DATA: Transaction[] = [
  {
    id: 1,
    accountId: "DE89370400440532013000",
    amount: "100.00",
    currency: "EUR",
    creditorName: "REWE GmbH",
    debtorName: "Max Mustermann",
    type: "DBIT",
    bookingDate: "2024-01-01",
    status: "BOOK",
    categoryId: 1,
    categoryName: "Lebensmittel",
    categoryColor: "#4CAF50",
  },
  {
    id: 2,
    accountId: "DE89370400440532013000",
    amount: "2850.00",
    currency: "EUR",
    creditorName: "Max Mustermann",
    debtorName: "Arbeitgeber GmbH",
    type: "CRDT",
    bookingDate: "2024-01-02",
    status: "BOOK",
    categoryId: 7,
    categoryName: "Gehalt",
    categoryColor: "#8BC34A",
  },
  {
    id: 3,
    accountId: "DE89370400440532013000",
    amount: "125.49",
    currency: "EUR",
    creditorName: "Amazon",
    debtorName: "Max Mustermann",
    type: "DBIT",
    bookingDate: "2024-01-05",
    status: "BOOK",
    categoryId: 2,
    categoryName: "Shopping",
    categoryColor: "#FF5722",
  },
  {
    id: 4,
    accountId: "DE89370400440532013000",
    amount: "9.99",
    currency: "EUR",
    creditorName: "Spotify AB",
    debtorName: "Max Mustermann",
    type: "DBIT",
    bookingDate: "2024-01-10",
    status: "BOOK",
    categoryId: 3,
    categoryName: "Unterhaltung",
    categoryColor: "#9C27B0",
  },
  {
    id: 5,
    accountId: "DE89370400440532013000",
    amount: "45.20",
    currency: "EUR",
    creditorName: "Edeka",
    debtorName: "Max Mustermann",
    type: "DBIT",
    bookingDate: "2024-01-12",
    status: "BOOK",
    categoryId: 1,
    categoryName: "Lebensmittel",
    categoryColor: "#4CAF50",
  },
  {
    id: 6,
    accountId: "DE89370400440532013000",
    amount: "14.99",
    currency: "EUR",
    creditorName: "Netflix",
    debtorName: "Max Mustermann",
    type: "DBIT",
    bookingDate: "2024-01-15",
    status: "PDNG",
    categoryId: 3,
    categoryName: "Unterhaltung",
    categoryColor: "#9C27B0",
  },
  {
    id: 7,
    accountId: "DE89370400440532013000",
    amount: "500.00",
    currency: "EUR",
    creditorName: "Max Mustermann",
    debtorName: "Jonas Weber",
    type: "CRDT",
    bookingDate: "2024-01-16",
    status: "BOOK",
    categoryId: 8,
    categoryName: "Überweisung",
    categoryColor: "#2196F3",
  },
  {
    id: 8,
    accountId: "DE89370400440532013000",
    amount: "999.00",
    currency: "EUR",
    creditorName: "Apple Store",
    debtorName: "Max Mustermann",
    type: "DBIT",
    bookingDate: "2024-01-18",
    status: "BOOK",
    categoryId: 4,
    categoryName: "Elektronik",
    categoryColor: "#607D8B",
  },
  {
    id: 9,
    accountId: "DE89370400440532013000",
    amount: "60.00",
    currency: "EUR",
    creditorName: "Shell",
    debtorName: "Max Mustermann",
    type: "DBIT",
    bookingDate: "2024-01-20",
    status: "BOOK",
    categoryId: 5,
    categoryName: "Transport",
    categoryColor: "#FF9800",
  },
  {
    id: 10,
    accountId: "DE89370400440532013000",
    amount: "32.80",
    currency: "EUR",
    creditorName: "Lidl",
    debtorName: "Max Mustermann",
    type: "DBIT",
    bookingDate: "2024-01-22",
    status: "SCHD",
    categoryId: 1,
    categoryName: "Lebensmittel",
    categoryColor: "#4CAF50",
  },
  {
    id: 11,
    accountId: "DE89370400440532013000",
    amount: "59.99",
    currency: "EUR",
    creditorName: "Adobe Systems",
    debtorName: "Max Mustermann",
    type: "DBIT",
    bookingDate: "2024-01-25",
    status: "BOOK",
    categoryId: 6,
    categoryName: "Software",
    categoryColor: "#3F51B5",
  },
  {
    id: 12,
    accountId: "DE89370400440532013000",
    amount: "22.50",
    currency: "EUR",
    creditorName: "Uber",
    debtorName: "Max Mustermann",
    type: "DBIT",
    bookingDate: "2024-01-27",
    status: "RJCT",
    categoryId: 5,
    categoryName: "Transport",
    categoryColor: "#FF9800",
  },
  {
    id: 13,
    accountId: "DE89370400440532013000",
    amount: "250.00",
    currency: "EUR",
    creditorName: "IKEA",
    debtorName: "Max Mustermann",
    type: "DBIT",
    bookingDate: "2024-01-28",
    status: "BOOK",
    categoryId: 9,
    categoryName: "Wohnen",
    categoryColor: "#2196F3",
  },
  {
    id: 14,
    accountId: "DE89370400440532013000",
    amount: "200.00",
    currency: "EUR",
    creditorName: "Max Mustermann",
    debtorName: "Eltern",
    type: "CRDT",
    bookingDate: "2024-01-30",
    status: "HOLD",
    categoryId: 8,
    categoryName: "Überweisung",
    categoryColor: "#2196F3",
  },
];

const ITEMS_PER_PAGE = 10;

function getMerchantName(transaction: Transaction): string {
  return transaction.type === "CRDT"
    ? transaction.debtorName
    : transaction.creditorName;
}

function formatAmount(amount: string, currency: string): string {
  return new Intl.NumberFormat("de-DE", {
    style: "currency",
    currency,
  }).format(parseFloat(amount));
}

function formatDate(dateString: string): string {
  return new Date(dateString).toLocaleDateString("de-DE", {
    day: "2-digit",
    month: "short",
    year: "numeric",
  });
}

function TransactionRow({
  transaction,
  isLast,
}: {
  transaction: Transaction;
  isLast: boolean;
}) {
  const isCredit = transaction.type === "CRDT";
  const merchant = getMerchantName(transaction);

  return (
    <div
      className={`flex items-center gap-3 px-3 py-2 transition-colors hover:bg-muted/40 ${
        !isLast ? "border-b border-border" : ""
      }`}
    >
      <MerchantAvatar name={merchant} type={transaction.type} />

      <div className="min-w-0 flex-1">
        <div className="flex items-center gap-2">
          <span className="truncate text-sm font-medium">{merchant}</span>
          <CategoryBadge
            name={transaction.categoryName}
            color={transaction.categoryColor}
          />
        </div>
        <span className="text-xs text-muted-foreground">
          {STATUS_LABEL[transaction.status]}
        </span>
      </div>

      <div className="shrink-0 text-right">
        <div
          className={`flex items-center justify-end gap-1 text-sm font-semibold ${
            isCredit ? "text-emerald-500" : "text-foreground"
          }`}
        >
          {isCredit ? (
            <ArrowDownLeft className="h-3.5 w-3.5" />
          ) : (
            <ArrowUpRight className="h-3.5 w-3.5" />
          )}
          {isCredit ? "+" : "-"}
          {formatAmount(transaction.amount, transaction.currency)}
        </div>
        <div className="text-xs text-muted-foreground">
          {formatDate(transaction.bookingDate)}
        </div>
      </div>

      <TransactionPopover transaction={transaction} />
    </div>
  );
}

function TransactionsTable() {
  const [search, setSearch] = useState("");
  const [page, setPage] = useState(0);

  const filtered = MOCK_DATA.filter((t) => {
    const q = search.toLowerCase();
    return (
      getMerchantName(t).toLowerCase().includes(q) ||
      t.categoryName.toLowerCase().includes(q)
    );
  });

  const totalPages = Math.ceil(filtered.length / ITEMS_PER_PAGE);
  const paged = filtered.slice(page * ITEMS_PER_PAGE, (page + 1) * ITEMS_PER_PAGE);

  return (
    <div className="space-y-4">
      <div className="relative max-w-sm">
        <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
        <Input
          placeholder="Händler oder Kategorie suchen..."
          className="pl-9"
          value={search}
          onChange={(e) => {
            setSearch(e.target.value);
            setPage(0);
          }}
        />
      </div>

      <div className="overflow-hidden rounded-xl border border-border bg-card">
        {paged.length === 0 ? (
          <div className="py-16 text-center text-sm text-muted-foreground">
            Keine Transaktionen gefunden.
          </div>
        ) : (
          paged.map((t, i) => (
            <TransactionRow key={t.id} transaction={t} isLast={i === paged.length - 1} />
          ))
        )}
      </div>

      {totalPages > 1 && (
        <div className="flex items-center justify-end gap-2">
          <Button
            variant="outline"
            size="sm"
            onClick={() => setPage((p) => p - 1)}
            disabled={page === 0}
          >
            Zurück
          </Button>
          <span className="text-sm text-muted-foreground">
            {page + 1} / {totalPages}
          </span>
          <Button
            variant="outline"
            size="sm"
            onClick={() => setPage((p) => p + 1)}
            disabled={page >= totalPages - 1}
          >
            Weiter
          </Button>
        </div>
      )}
    </div>
  );
}

export default TransactionsTable;
