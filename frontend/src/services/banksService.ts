import {
  getAvailableBanks,
  startAuthorization,
} from "@/api/generated/account-service";
import type { BankDto } from "@/api/generated/account-service";

export async function getBanks(country: string): Promise<BankDto[]> {
  const res = await getAvailableBanks({ query: { country } });
  if (!res.data?.aspsps) throw new Error("Bank Anfrage fehlgeschlagen");
  return res.data.aspsps;
}

export async function authenticateWithBank(bank: string, country: string) {
  const res = await startAuthorization({ query: { bank, country } });
  if (!res.data?.url)
    throw new Error("Authentifizierung zur Bank fehlgeschlagen");
  return res.data.url;
}
