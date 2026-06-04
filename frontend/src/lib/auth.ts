import { client as transactionClient } from "@/api/generated/transaction-service/client.gen";
import { client as accountClient } from "@/api/generated/account-service/client.gen";
import { client as authClient } from "@/api/generated/auth-service/client.gen";

authClient.setConfig({ baseUrl: "" });
transactionClient.setConfig({ baseUrl: "" });
accountClient.setConfig({ baseUrl: "" });

export function setAuthToken(token: string) {
  const headers = { Authorization: `Bearer ${token}` };
  transactionClient.setConfig({ headers });
  accountClient.setConfig({ headers });
  localStorage.setItem("jwt", token);
}

export function clearToken() {
  localStorage.removeItem("jwt");
  transactionClient.setConfig({ headers: { Authorization: undefined } });
  accountClient.setConfig({ headers: { Authorization: undefined } });
}

export function getStoredToken() {
  return localStorage.getItem("jwt");
}
