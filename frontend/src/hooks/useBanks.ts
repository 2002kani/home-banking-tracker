import useSWR from "swr";
import { getBanks } from "@/services/banksService";

export function useBanks(country: string | null) {
  const { data, isLoading, error } = useSWR(
    country ? ["banks", country] : null,
    () => getBanks(country!),
  );

  return { banks: data ?? [], isLoading, error };
}
