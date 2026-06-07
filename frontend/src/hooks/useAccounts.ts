import { getAccounts } from "@/api/generated/account-service";

import useSWR from "swr";

function useAccounts() {
  const { data, isLoading } = useSWR("accounts", () => getAccounts());
  const hasAccounts = isLoading ? null : (data?.data?.length ?? 0) > 0;
  return { hasAccounts, isLoading };
}

export default useAccounts;
