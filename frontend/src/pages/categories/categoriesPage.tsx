import { ComingSoon } from "@/components/shared/ComingSoon";
import PageHeader from "@/components/shared/pageHeader";

function CategoriesPage() {
  return (
    <>
      <PageHeader
        title="Kategorien"
        description="Klassifizieren Sie Ihre Ausgaben und behalten Sie Budgets im Blick."
      />
      <ComingSoon feature="Kategorien" />
    </>
  );
}

export default CategoriesPage;
