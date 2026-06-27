import { getTransactions } from "@/api/generated/transaction-service";
import useSWR from "swr";

function useTransactions() {
  const { data, isLoading } = useSWR("transactions", async () => {
    const result = await getTransactions();
    if (result.error) throw result.error;
    return result.data ?? [];
  });
  return { transactions: data ?? [], isLoading };
}

export default useTransactions;
