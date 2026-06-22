import { getAccounts } from "@/api/generated/account-service";

import useSWR from "swr";

function useAccounts() {
  const { data, isLoading, isValidating } = useSWR("accounts", async () => {
    const result = await getAccounts();
    if (result.error) throw result.error;
    return result;
  });
  const isPending = isLoading || isValidating;
  const hasAccounts = isPending ? null : (data?.data?.length ?? 0) > 0;
  return { hasAccounts, isLoading: isPending };
}

export default useAccounts;
