import { useState } from "react";
import { MoreHorizontal } from "lucide-react";
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "@/components/ui/popover";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
} from "@/components/ui/select";
import { Separator } from "@/components/ui/separator";
import { CategoryBadge } from "@/components/transactions/CategoryBadge";
import { STATUS_LABEL } from "@/lib/transactionConfig";
import type { Transaction } from "@/lib/types";

// TODO: Replace with API data
const MOCK_CATEGORIES = [
  { id: 1, name: "Lebensmittel", color: "#4CAF50" },
  { id: 2, name: "Shopping", color: "#FF5722" },
  { id: 3, name: "Unterhaltung", color: "#9C27B0" },
  { id: 4, name: "Elektronik", color: "#607D8B" },
  { id: 5, name: "Transport", color: "#FF9800" },
  { id: 6, name: "Software", color: "#3F51B5" },
  { id: 7, name: "Gehalt", color: "#8BC34A" },
  { id: 8, name: "Überweisung", color: "#2196F3" },
  { id: 9, name: "Wohnen", color: "#2196F3" },
  { id: 10, name: "Gesundheit", color: "#E91E63" },
];

function DetailRow({ label, value }: { label: string; value: string }) {
  return (
    <div className="flex items-center justify-between gap-4">
      <span className="text-xs text-muted-foreground">{label}</span>
      <span className="max-w-40 truncate text-right text-xs font-medium">
        {value}
      </span>
    </div>
  );
}

export function TransactionPopover({
  transaction,
}: {
  transaction: Transaction;
}) {
  const [selectedCategoryId, setSelectedCategoryId] = useState(
    transaction.categoryId,
  );

  const selectedCategory =
    MOCK_CATEGORIES.find((c) => c.id === selectedCategoryId) ??
    MOCK_CATEGORIES[0];

  return (
    <Popover>
      <PopoverTrigger
        className="flex h-7 w-7 shrink-0 items-center justify-center rounded-md text-muted-foreground transition-colors hover:bg-muted hover:text-foreground"
        aria-label="Details anzeigen"
      >
        <MoreHorizontal className="h-4 w-4" />
      </PopoverTrigger>

      <PopoverContent className="w-72 p-0" side="left" align="start">
        <div className="px-4 py-3">
          <p className="text-sm font-semibold">Transaktionsdetails</p>
          <p className="text-xs text-muted-foreground">#{transaction.id}</p>
        </div>

        <Separator />

        <div className="space-y-2 px-4 py-3">
          <DetailRow label="Konto" value={transaction.accountId} />
          <DetailRow label="Gläubiger" value={transaction.creditorName} />
          <DetailRow label="Schuldner" value={transaction.debtorName} />
          <DetailRow
            label="Typ"
            value={transaction.type === "CRDT" ? "Eingang" : "Ausgang"}
          />
          <DetailRow label="Status" value={STATUS_LABEL[transaction.status]} />
          <DetailRow label="Buchungsdatum" value={transaction.bookingDate} />
          <DetailRow
            label="Betrag"
            value={`${transaction.amount} ${transaction.currency}`}
          />
        </div>

        <Separator />

        <div className="px-4 py-3">
          <p className="mb-2 text-xs text-muted-foreground">Kategorie ändern</p>
          <div className="flex items-center gap-2">
            <CategoryBadge
              name={selectedCategory.name}
              color={selectedCategory.color}
            />
            <Select
              value={selectedCategoryId}
              onValueChange={(value) => {
                if (value !== null) setSelectedCategoryId(value);
              }}
            >
              <SelectTrigger size="sm" className="flex-1">
                {selectedCategory.name}
              </SelectTrigger>
              <SelectContent>
                {MOCK_CATEGORIES.map((cat) => (
                  <SelectItem key={cat.id} value={cat.id}>
                    <span className="flex items-center gap-2">
                      <span
                        className="h-2 w-2 shrink-0 rounded-full"
                        style={{ backgroundColor: cat.color }}
                      />
                      {cat.name}
                    </span>
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          </div>
        </div>
      </PopoverContent>
    </Popover>
  );
}
