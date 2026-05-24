import PageHeader from "@/components/shared/pageHeader";
import TransactionsTable from "./transactionsTable";

function TransactionsPage() {
  return (
    <>
      <PageHeader
        title="Transaktionen"
        description="Alle Bewegungen über sämtliche Konten."
      />
      <div className="mt-8 max-w-5xl">
        <TransactionsTable />
      </div>
    </>
  );
}

export default TransactionsPage;
