import { useState } from "react";
import {
  Search,
  Building2,
  ChevronRight,
  ArrowLeft,
  Loader2,
} from "lucide-react";
import { Skeleton } from "@/components/ui/skeleton";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Label } from "@/components/ui/label";
import type { BankDto } from "@/api/generated/account-service/types.gen";
import { useBanks } from "@/hooks/useBanks";
import { authenticateWithBank } from "@/services/banksService";

export default function BankSelectPage() {
  const [country, setCountry] = useState("");
  const [submittedCountry, setSubmittedCountry] = useState<string | null>(null);
  const [search, setSearch] = useState("");
  const [selectingBank, setSelectingBank] = useState<string | null>(null);

  const { banks, isLoading } = useBanks(submittedCountry);

  const filtered = banks.filter((b) =>
    b.name?.toLowerCase().includes(search.toLowerCase()),
  );

  const handleSelectBank = async (bank: BankDto) => {
    if (!bank.name || !bank.country) return;
    setSelectingBank(bank.name);
    const url = await authenticateWithBank(bank.name, bank.country);
    window.location.assign(url);
  };

  if (!submittedCountry) {
    return (
      <div className="space-y-8">
        <div className="space-y-1">
          <p className="text-[11px] font-mono uppercase tracking-[0.2em] text-muted-foreground">
            Schritt 1 von 2
          </p>
          <h1 className="text-2xl font-semibold tracking-tight">
            In welchem Land ist deine Bank?
          </h1>
          <p className="text-sm text-muted-foreground">
            Gib den Ländercode ein, z.B. DE, AT, CH.
          </p>
        </div>

        <div className="space-y-2">
          <Label htmlFor="country">Ländercode</Label>
          <Input
            id="country"
            placeholder="DE"
            maxLength={2}
            className="uppercase"
            value={country}
            onChange={(e) => setCountry(e.target.value.toUpperCase())}
          />
        </div>

        <Button
          className="w-full cursor-pointer"
          disabled={country.length < 2}
          onClick={() => setSubmittedCountry(country)}
        >
          Weiter
        </Button>
      </div>
    );
  }

  return (
    <div className="space-y-8">
      <div className="space-y-1">
        <button
          onClick={() => setSubmittedCountry(null)}
          className="flex items-center gap-1 text-[11px] text-muted-foreground hover:text-foreground transition-colors mb-2 cursor-pointer"
        >
          <ArrowLeft className="h-3 w-3" />
          {submittedCountry}
        </button>
        <p className="text-[11px] font-mono uppercase tracking-[0.2em] text-muted-foreground">
          Schritt 2 von 2
        </p>
        <h1 className="text-2xl font-semibold tracking-tight">
          Wähle deine Bank
        </h1>
        <p className="text-sm text-muted-foreground">
          Verbinde dein Bankkonto um loszulegen.
        </p>
      </div>

      <div className="relative">
        <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground" />
        <Input
          placeholder="Bank suchen..."
          className="pl-9"
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />
      </div>

      <div className="space-y-1 max-h-80 overflow-y-auto pr-1">
        {isLoading ? (
          Array.from({ length: 5 }).map((_, i) => (
            <div
              key={i}
              className="flex items-center gap-3 rounded-lg border border-border px-4 py-3"
            >
              <Skeleton className="h-8 w-8 rounded-md" />
              <div className="flex-1 space-y-1.5">
                <Skeleton className="h-3.5 w-32" />
                <Skeleton className="h-3 w-8" />
              </div>
            </div>
          ))
        ) : filtered.length === 0 ? (
          <p className="py-8 text-center text-sm text-muted-foreground">
            Keine Bank gefunden.
          </p>
        ) : (
          filtered.map((bank) => (
            <button
              key={bank.name}
              onClick={() => handleSelectBank(bank)}
              disabled={selectingBank !== null}
              className="w-full flex items-center gap-3 rounded-lg border border-border px-4 py-3 text-left transition-colors hover:bg-accent hover:border-foreground/20 cursor-pointer disabled:opacity-50 disabled:cursor-not-allowed"
            >
              <div className="flex h-8 w-8 items-center justify-center rounded-md bg-muted">
                <Building2 className="h-4 w-4 text-muted-foreground" />
              </div>
              <div className="flex-1">
                <p className="text-sm font-medium">{bank.name}</p>
                <p className="text-[11px] text-muted-foreground">
                  {bank.country}
                </p>
              </div>
              {selectingBank === bank.name ? (
                <Loader2 className="h-4 w-4 text-muted-foreground animate-spin" />
              ) : (
                <ChevronRight className="h-4 w-4 text-muted-foreground" />
              )}
            </button>
          ))
        )}
      </div>
    </div>
  );
}
