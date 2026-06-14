import type {
  AuthenticationRequest,
  RegisterRequest,
} from "@/api/generated/auth-service";
import { authenticate, register } from "@/api/generated/auth-service/sdk.gen";

export type TokenPair = { accessToken: string; refreshToken: string };

function extractTokens(
  data: { accessToken?: string; refreshToken?: string } | undefined,
  errorMsg: string,
): TokenPair {
  if (!data?.accessToken || !data?.refreshToken) throw new Error(errorMsg);
  return { accessToken: data.accessToken, refreshToken: data.refreshToken };
}

export async function loginUser(
  req: AuthenticationRequest,
): Promise<TokenPair> {
  const res = await authenticate({ body: req });
  return extractTokens(res.data, "Login fehlgeschlagen");
}

export async function registerUser(req: RegisterRequest): Promise<TokenPair> {
  const res = await register({ body: req });
  return extractTokens(res.data, "Registrierung fehlgeschlagen");
}
