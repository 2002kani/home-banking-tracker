import PageHeader from "@/components/shared/pageHeader";
import StatCard from "./statisticsCard";
import { Wallet } from "lucide-react";

function DashboardPage() {
  return (
    <>
      <PageHeader
        title="Dashboard"
        description="Übersicht über Ihre Konten, Cashflow und Aktivität."
      />
      <br />
      <div className="grid gap-4 lg:grid-cols-4">
        <StatCard
          label={"Gesamtvermögen"}
          value={"49.853,62 €"}
          delta={4.2}
          hint="vs. Vormonat"
          icon={<Wallet className="h-4 w-4" />}
        />
        <StatCard
          label={"Gesamtvermögen"}
          value={"49.853,62 €"}
          delta={4.2}
          hint="vs. Vormonat"
          icon={<Wallet className="h-4 w-4" />}
        />
        <StatCard
          label={"Gesamtvermögen"}
          value={"49.853,62 €"}
          delta={4.2}
          hint="vs. Vormonat"
          icon={<Wallet className="h-4 w-4" />}
        />
        <StatCard
          label={"Gesamtvermögen"}
          value={"49.853,62 €"}
          delta={4.2}
          hint="vs. Vormonat"
          icon={<Wallet className="h-4 w-4" />}
        />
      </div>
    </>
  );
}

export default DashboardPage;
