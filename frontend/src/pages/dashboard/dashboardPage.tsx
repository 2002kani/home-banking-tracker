import PageHeader from "@/components/shared/pageHeader";
import StatCard from "./statisticsCard";
import { Wallet } from "lucide-react";
import useNetWorth from "@/hooks/useNetWorth";

function DashboardPage() {
  const { netWorth, isLoading } = useNetWorth();

  const totalBalance = netWorth?.totalBalance
    ? netWorth.totalBalance.toLocaleString("de-DE", {
        style: "currency",
        currency: "EUR",
      })
    : "–";

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
          value={isLoading ? "..." : totalBalance}
          delta={netWorth?.changePercent ?? 0}
          hint="vs. Vormonat"
          icon={<Wallet className="h-4 w-4" />}
        />
      </div>
    </>
  );
}

export default DashboardPage;
