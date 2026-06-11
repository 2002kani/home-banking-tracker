import useAccounts from "@/hooks/useAccounts";
import { Navigate, Outlet } from "react-router-dom";

function OnboardingGuard() {
  const { hasAccounts, isLoading } = useAccounts();
  console.log("haaccount", hasAccounts);
  if (isLoading) return null;
  if (!hasAccounts) return <Navigate to="/onboarding/bank-select" replace />;
  return <Outlet />;
}

export default OnboardingGuard;
