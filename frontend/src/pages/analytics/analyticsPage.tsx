import { ComingSoon } from "@/components/shared/ComingSoon";
import PageHeader from "@/components/shared/pageHeader";

function AnalyticsPage() {
  return (
    <>
      <PageHeader
        title="Analytics"
        description="Tiefere Einblicke in Ihre Finanzen."
      />
      <ComingSoon feature="Analytics" />
    </>
  );
}

export default AnalyticsPage;
