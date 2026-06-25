import PageHeader from "@/components/shared/pageHeader";
import StatCard from "./statisticsCard";
import { Wallet, TrendingDown, TrendingUp, PiggyBank } from "lucide-react";
import useNetWorth from "@/hooks/useNetWorth";
import useExpenses from "@/hooks/useExpenses";
import useSWR from "swr";
import { getSavingsLastMonth } from "@/api/generated/transaction-service";

function formatEur(value?: number) {
  return value != null
    ? value.toLocaleString("de-DE", { style: "currency", currency: "EUR" })
    : "–";
}

function DashboardPage() {
  const { netWorth, isLoading } = useNetWorth();
  const {
    data: expenses,
    changePercent: expensesChange,
    isLoading: isExpensesLoading,
  } = useExpenses("DBIT");
  const {
    data: income,
    changePercent: incomeChange,
    isLoading: isIncomeLoading,
  } = useExpenses("CRDT");

  const { data, isLoading: savingsLoading } = useSWR(
    ["api/savings"],
    async () => {
      const result = await getSavingsLastMonth();
      if (result.error) throw result.error;
      return result;
    },
  );

  return (
    <>
      <PageHeader
        title="Dashboard"
        description="Übersicht über Ihre Konten, Cashflow und Aktivität."
      />
      <br />
      <div className="grid gap-4 lg:grid-cols-4">
        <StatCard
          label="Gesamtvermögen"
          value={isLoading ? "..." : formatEur(netWorth?.totalBalance)}
          delta={netWorth?.changePercent ?? 0}
          hint="vs. Vormonat"
          icon={<Wallet className="h-4 w-4" />}
          hideable
        />
        <StatCard
          label="Einnahmen letzten Monat"
          value={isIncomeLoading ? "..." : formatEur(income)}
          delta={incomeChange}
          hint="letzten Monat"
          icon={<TrendingUp className="h-4 w-4" />}
        />
        <StatCard
          label="Ausgaben letzten Monat"
          value={isExpensesLoading ? "..." : formatEur(expenses)}
          delta={expensesChange}
          hint="letzten Monat"
          icon={<TrendingDown className="h-4 w-4" />}
        />
        <StatCard
          label="Sparrate letzten Monat"
          value={savingsLoading ? "..." : `${data?.data?.savingsRate ?? "–"} %`}
          delta={data?.data?.savingsAmount}
          deltaUnit=" €"
          hint="Sparrate letzten Monat"
          icon={<PiggyBank className="h-4 w-4" />}
        />
      </div>
    </>
  );
}

export default DashboardPage;
