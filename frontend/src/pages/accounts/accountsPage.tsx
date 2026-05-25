import { ComingSoon } from "@/components/shared/ComingSoon";
import PageHeader from "@/components/shared/pageHeader";

function AccountsPage() {
  return (
    <>
      <PageHeader
        title="Konten"
        description="Alle verbundenen Konten und Karten."
      />
      <ComingSoon feature="Konten" />
    </>
  );
}

export default AccountsPage;
