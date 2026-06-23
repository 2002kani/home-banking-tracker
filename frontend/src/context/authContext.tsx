import { createContext, useContext, useEffect, useState } from "react";
import {
  clearToken,
  getStoredToken,
  setAuthToken,
  setRefreshCallbacks,
  setRefreshToken,
} from "@/lib/auth";
import type {
  AuthenticationRequest,
  RegisterRequest,
} from "@/api/generated/auth-service";
import { loginUser, registerUser } from "@/services/authService";

type AuthContextType = {
  token: string | null;
  isAuthenticated: boolean;
  login: (req: AuthenticationRequest) => Promise<void>;
  register: (req: RegisterRequest) => Promise<void>;
  logout: () => void;
};

const AuthContext = createContext<AuthContextType | null>(null);

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [token, setToken] = useState<string | null>(() => {
    const stored = getStoredToken();
    if (stored) setAuthToken(stored);
    return stored;
  });

  const login = async (req: AuthenticationRequest) => {
    const { accessToken, refreshToken } = await loginUser(req);
    setAuthToken(accessToken);
    setRefreshToken(refreshToken);
    setToken(accessToken);
  };

  const register = async (req: RegisterRequest) => {
    const { accessToken, refreshToken } = await registerUser(req);
    setAuthToken(accessToken);
    setRefreshToken(refreshToken);
    setToken(accessToken);
  };

  const logout = () => {
    clearToken();
    setToken(null);
  };

  useEffect(() => {
    setRefreshCallbacks(
      (newToken) => setToken(newToken),
      () => {
        clearToken();
        setToken(null);
      },
    );
  }, []);

  return (
    <AuthContext.Provider
      value={{ token, isAuthenticated: !!token, login, register, logout }}
    >
      {children}
    </AuthContext.Provider>
  );
}

// eslint-disable-next-line react-refresh/only-export-components
export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error("useAuth must be used within AuthProvider.");
  return ctx;
}
