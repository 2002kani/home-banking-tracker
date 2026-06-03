import type {
  AuthenticationRequest,
  RegisterRequest,
} from "@/api/generated/auth-service";
import { authenticate, register } from "@/api/generated/auth-service/sdk.gen";

export async function loginUser(req: AuthenticationRequest): Promise<string> {
  const res = await authenticate({ body: req });
  if (!res.data?.token) throw new Error("Login fehlgeschlagen");
  return res.data.token;
}

export async function registerUser(req: RegisterRequest): Promise<string> {
  const res = await register({ body: req });
  if (!res.data?.token) throw new Error("Registrierung fehlgeschlagen");
  return res.data.token;
}
