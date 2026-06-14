import { client as transactionClient } from "@/api/generated/transaction-service/client.gen";
import { client as accountClient } from "@/api/generated/account-service/client.gen";
import { client as authClient } from "@/api/generated/auth-service/client.gen";
import { refresh } from "@/api/generated/auth-service/sdk.gen";

authClient.setConfig({ baseUrl: "" });
transactionClient.setConfig({ baseUrl: "" });
accountClient.setConfig({ baseUrl: "" });

export function setAuthToken(accessToken: string) {
  const headers = { Authorization: `Bearer ${accessToken}` };
  transactionClient.setConfig({ headers });
  accountClient.setConfig({ headers });
  localStorage.setItem("jwt", accessToken);
}

export function setRefreshToken(token: string) {
  localStorage.setItem("refreshToken", token);
}

export function getStoredToken() {
  return localStorage.getItem("jwt");
}

export function getStoredRefreshToken() {
  return localStorage.getItem("refreshToken");
}

export function clearToken() {
  localStorage.removeItem("jwt");
  localStorage.removeItem("refreshToken");
  transactionClient.setConfig({ headers: { Authorization: undefined } });
  accountClient.setConfig({ headers: { Authorization: undefined } });
}

// Silent-Refresh Interceptor: bei 401 automatisch neues Access Token holen und Request wiederholen
let isRefreshing = false;

function setupRefreshInterceptor(apiClient: typeof transactionClient) {
  apiClient.interceptors.response.use(async (response, request) => {
    if (response.status !== 401 || isRefreshing) return response;

    const storedRefreshToken = getStoredRefreshToken();
    if (!storedRefreshToken) return response;

    isRefreshing = true;
    try {
      const res = await refresh({ body: { refreshToken: storedRefreshToken } });
      if (!res.data?.accessToken) return response;

      setAuthToken(res.data.accessToken);
      if (res.data.refreshToken) setRefreshToken(res.data.refreshToken);

      // Original-Request mit neuem Token wiederholen
      const newHeaders = new Headers(request.headers);
      newHeaders.set("Authorization", `Bearer ${res.data.accessToken}`);
      return fetch(new Request(request, { headers: newHeaders }));
    } catch {
      clearToken();
      return response;
    } finally {
      isRefreshing = false;
    }
  });
}

setupRefreshInterceptor(transactionClient);
setupRefreshInterceptor(accountClient);
