import { useState } from "react";
import { ArrowDownLeft, ArrowUpRight, Search } from "lucide-react";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { MerchantAvatar } from "@/components/transactions/MerchantAvatar";
import { CategoryBadge } from "@/components/transactions/CategoryBadge";
import { TransactionPopover } from "@/components/transactions/TransactionPopover";
import { STATUS_LABEL } from "@/lib/transactionConfig";
import type { TransactionDto } from "@/api/generated/transaction-service";
import useTransactions from "@/hooks/useTransactions";

const ITEMS_PER_PAGE = 15;

function getMerchantName(transaction: TransactionDto): string {
  return transaction.type === "CRDT"
    ? (transaction.debtorName ?? "")
    : (transaction.creditorName ?? "");
}

function formatAmount(amount: string | undefined, currency: string | undefined): string {
  if (!amount || !currency) return "–";
  return new Intl.NumberFormat("de-DE", {
    style: "currency",
    currency,
  }).format(parseFloat(amount));
}

function formatDate(dateString: string | undefined): string {
  if (!dateString) return "–";
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
  transaction: TransactionDto;
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
          <CategoryBadge name={transaction.categoryName} />
        </div>
        <span className="text-xs text-muted-foreground">
          {transaction.status ? STATUS_LABEL[transaction.status] : "–"}
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
  const { transactions, isLoading } = useTransactions();

  const filtered = transactions.filter((t) => {
    const q = search.toLowerCase();
    return (
      getMerchantName(t).toLowerCase().includes(q) ||
      (t.categoryName ?? "").toLowerCase().includes(q)
    );
  });

  const totalPages = Math.ceil(filtered.length / ITEMS_PER_PAGE);
  const paged = filtered.slice(
    page * ITEMS_PER_PAGE,
    (page + 1) * ITEMS_PER_PAGE,
  );

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
        {isLoading ? (
          <div className="py-16 text-center text-sm text-muted-foreground">
            Laden...
          </div>
        ) : paged.length === 0 ? (
          <div className="py-16 text-center text-sm text-muted-foreground">
            Keine Transaktionen gefunden.
          </div>
        ) : (
          paged.map((t, i) => (
            <TransactionRow
              key={t.id}
              transaction={t}
              isLast={i === paged.length - 1}
            />
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
