import PageHeader from "@/components/shared/pageHeader";
import { ComingSoon } from "@/components/shared/ComingSoon";

function SettingsPage() {
  return (
    <>
      <PageHeader
        title="Einstellungen"
        description="Konto- und Sicherheitseinstellungen."
      />
      <ComingSoon feature="Einstellungen" />
    </>
  );
}

export default SettingsPage;
