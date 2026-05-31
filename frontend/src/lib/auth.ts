import { client as transactionClient } from "@/api/generated/transaction-service/client.gen";
import { client as accountClient } from "@/api/generated/account-service/client.gen";

export function setAuthToken(token: string) {
  const headers = { Authorization: `Bearer ${token}` };
  transactionClient.setConfig({ headers });
  accountClient.setConfig({ headers });
  localStorage.setItem("jwt", token);
}

export function clearToken() {
  localStorage.removeItem("jwt");
  // Maybe reset clients here since the credentials are still cached till rerender
}

export function getStoredToken() {
  return localStorage.getItem("jwt");
}
