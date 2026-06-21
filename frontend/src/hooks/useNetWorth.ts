import { getAccountNetWorth } from "@/api/generated/account-service";
import useSWR from "swr";

function useNetWorth() {
  const { data, isLoading } = useSWR("net-worth", () => getAccountNetWorth());
  return { netWorth: data?.data, isLoading };
}

export default useNetWorth;
