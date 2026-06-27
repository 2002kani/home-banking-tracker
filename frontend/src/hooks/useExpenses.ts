import { getExpensesThisMonth } from "@/api/generated/transaction-service";
import type { TransactionDto } from "@/api/generated/transaction-service";
import useSWR from "swr";

type CreditDebitIndicator = NonNullable<TransactionDto["type"]>;

function useExpenses(type: CreditDebitIndicator) {
  const { data, isLoading } = useSWR(["expenses", type], async () => {
    const result = await getExpensesThisMonth({ query: { type } });
    if (result.error) throw result.error;
    return result;
  });
  return {
    data: data?.data?.expenses,
    changePercent: data?.data?.changePercent,
    isLoading,
  };
}

export default useExpenses;
