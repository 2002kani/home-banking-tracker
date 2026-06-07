import { getAvailableBanks } from "@/api/generated/account-service";
import type { BankDto } from "@/api/generated/account-service";

export async function getBanks(country: string): Promise<BankDto[]> {
  const res = await getAvailableBanks({ query: { country } });
  if (!res.data?.aspsps) throw new Error("Bank Anfrage fehlgeschlagen");
  return res.data.aspsps;
}

/* TODO

- die /auth anfrage machen
- onbopardign soll immer nach registrierung erschienen (einmalig auch)
  - ggf backend anpassne damit es frontend tracken kann ob schon mal vorher registriert (zurückgeben?)
    - dafür ein /accounts endpunkt (den ich eh bald brauche) der dann im frotnend prüfen soll ob accounts existieren -> falls nein: onboarding erscheint nach auth 

*/
