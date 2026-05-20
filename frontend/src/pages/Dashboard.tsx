import { useState } from "react";
import { Link } from "react-router-dom";
import { TrendingUp, TrendingDown, Eye, EyeOff } from "lucide-react";
import {
  categories,
  transactions,
  TOTAL_BALANCE,
  MONTHLY_INCOME,
  MONTHLY_EXPENSES,
} from "@/data/mockData";
import { formatCurrency, formatShortDate } from "@/lib/utils";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Separator } from "@/components/ui/separator";

const budgetCategories = categories
  .filter((c) => c.budget !== undefined)
  .slice(0, 4);
const recentTransactions = transactions.slice(0, 5);

export function Dashboard() {
  const [balanceVisible, setBalanceVisible] = useState(true);

  const getCategoryById = (id: string) => categories.find((c) => c.id === id);

  return (
    <div className="space-y-6">
      {/* Balance Card */}
      <div className="bg-linear-to-br from-neutral-900 via-neutral-800 to-neutral-900 dark:from-neutral-950 dark:via-neutral-900 dark:to-neutral-950 rounded-2xl p-6 sm:p-8 shadow-lg">
        <div className="flex items-center justify-between mb-2">
          <p className="text-sm text-neutral-300 dark:text-neutral-400">
            Gesamtguthaben
          </p>
          <Button
            variant="ghost"
            size="icon"
            onClick={() => setBalanceVisible((v) => !v)}
            className="text-neutral-300 hover:bg-white/10 hover:text-white"
            aria-label={
              balanceVisible ? "Guthaben verstecken" : "Guthaben anzeigen"
            }
          >
            {balanceVisible ? (
              <EyeOff className="w-5 h-5" />
            ) : (
              <Eye className="w-5 h-5" />
            )}
          </Button>
        </div>

        {balanceVisible ? (
          <p className="text-4xl sm:text-5xl font-bold text-white tracking-tight mb-6">
            {formatCurrency(TOTAL_BALANCE)}
          </p>
        ) : (
          <div className="flex items-center gap-2 mb-6 h-12 sm:h-14">
            {[...Array(4)].map((_, i) => (
              <div key={i} className="w-3 h-3 rounded-full bg-white/30" />
            ))}
          </div>
        )}

        <div className="grid grid-cols-2 gap-4">
          <div className="bg-white/10 backdrop-blur-sm rounded-xl p-4">
            <div className="flex items-center gap-2 mb-1">
              <TrendingUp className="w-4 h-4 text-green-400" />
              <span className="text-xs text-neutral-300">Einnahmen</span>
            </div>
            <p className="text-2xl font-semibold text-white">
              {balanceVisible ? formatCurrency(MONTHLY_INCOME) : "••••"}
            </p>
          </div>
          <div className="bg-white/10 backdrop-blur-sm rounded-xl p-4">
            <div className="flex items-center gap-2 mb-1">
              <TrendingDown className="w-4 h-4 text-red-400" />
              <span className="text-xs text-neutral-300">Ausgaben</span>
            </div>
            <p className="text-2xl font-semibold text-white">
              {balanceVisible ? formatCurrency(MONTHLY_EXPENSES) : "••••"}
            </p>
          </div>
        </div>
      </div>

      {/* Budget Overview */}
      <div>
        <div className="flex items-center justify-between mb-4">
          <h2 className="text-xl font-semibold text-neutral-900 dark:text-white">
            Budget Übersicht
          </h2>
          <Button
            variant="link"
            size="sm"
            className="text-muted-foreground hover:text-foreground p-0 h-auto"
            asChild
          >
            <Link to="/categories">Alle anzeigen</Link>
          </Button>
        </div>
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
          {budgetCategories.map((category) => {
            const percent = Math.round(
              ((category.spent ?? 0) / (category.budget ?? 1)) * 100,
            );
            return (
              <Card
                key={category.id}
                className="rounded-xl border-neutral-200 dark:border-neutral-800 hover:border-neutral-300 dark:hover:border-neutral-700 transition-colors p-5"
              >
                <div className="flex items-center justify-between mb-3">
                  <div
                    className="w-10 h-10 rounded-lg flex items-center justify-center"
                    style={{ backgroundColor: `${category.color}20` }}
                  >
                    <span className="text-lg">{category.icon}</span>
                  </div>
                  <span className="text-xs font-medium text-muted-foreground">
                    {percent}%
                  </span>
                </div>
                <p className="text-sm font-medium text-foreground mb-1">
                  {category.name}
                </p>
                <p className="text-xs text-muted-foreground mb-3">
                  {formatCurrency(category.spent ?? 0)} /{" "}
                  {formatCurrency(category.budget ?? 0)}
                </p>
                <div className="h-1.5 bg-neutral-100 dark:bg-neutral-800 rounded-full overflow-hidden">
                  <div
                    className="h-full rounded-full transition-all"
                    style={{
                      width: `${Math.min(percent, 100)}%`,
                      backgroundColor: category.color,
                    }}
                  />
                </div>
              </Card>
            );
          })}
        </div>
      </div>

      {/* Recent Transactions */}
      <div>
        <div className="flex items-center justify-between mb-4">
          <h2 className="text-xl font-semibold text-neutral-900 dark:text-white">
            Letzte Transaktionen
          </h2>
          <Button
            variant="link"
            size="sm"
            className="text-muted-foreground hover:text-foreground p-0 h-auto"
            asChild
          >
            <Link to="/transactions">Alle anzeigen</Link>
          </Button>
        </div>
        <Card className="rounded-xl border-neutral-200 dark:border-neutral-800">
          <CardContent className="p-0">
            {recentTransactions.map((tx, i) => {
              const category = getCategoryById(tx.category);
              return (
                <div key={tx.id}>
                  {i > 0 && <Separator />}
                  <div className="flex items-center gap-4 p-4 hover:bg-accent transition-colors first:rounded-t-xl last:rounded-b-xl">
                    <div
                      className="w-11 h-11 rounded-xl flex items-center justify-center shrink-0"
                      style={{
                        backgroundColor: `${category?.color ?? "#888"}20`,
                      }}
                    >
                      <span className="text-lg">{category?.icon ?? "💳"}</span>
                    </div>
                    <div className="flex-1 min-w-0">
                      <p className="text-sm font-medium text-foreground truncate">
                        {tx.title}
                      </p>
                      <p className="text-sm text-muted-foreground truncate">
                        {tx.merchant}
                      </p>
                    </div>
                    <div className="text-right shrink-0">
                      <p
                        className={`font-semibold text-sm ${
                          tx.type === "income"
                            ? "text-green-600 dark:text-green-400"
                            : "text-foreground"
                        }`}
                      >
                        {tx.type === "income" ? "+" : ""}
                        {formatCurrency(tx.amount)}
                      </p>
                      <p className="text-xs text-muted-foreground mt-0.5">
                        {formatShortDate(tx.date)}
                      </p>
                    </div>
                  </div>
                </div>
              );
            })}
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
