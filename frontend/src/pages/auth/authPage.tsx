import { useLocation, useNavigate } from "react-router-dom";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { LoginForm } from "./loginForm";
import { RegisterForm } from "./registerForm";

export default function AuthPage() {
  const location = useLocation();
  const navigate = useNavigate();
  const defaultTab = location.pathname === "/register" ? "register" : "login";

  return (
    <div className="space-y-6">
      <div className="space-y-1">
        <h2 className="text-2xl font-semibold tracking-tight">Willkommen</h2>
        <p className="text-sm text-muted-foreground">
          Melde dich an oder erstelle ein neues Konto.
        </p>
      </div>

      <Tabs
        defaultValue={defaultTab}
        className="w-full flex-col"
        onValueChange={(v) =>
          navigate(v === "register" ? "/register" : "/login", { replace: true })
        }
      >
        <TabsList className="grid w-full! grid-cols-2 mb-6">
          <TabsTrigger value="login">Anmelden</TabsTrigger>
          <TabsTrigger value="register">Registrieren</TabsTrigger>
        </TabsList>

        <TabsContent value="login">
          <LoginForm />
        </TabsContent>

        <TabsContent value="register">
          <RegisterForm />
        </TabsContent>
      </Tabs>

      <p className="text-center text-[11px] text-muted-foreground">
        Durch die Anmeldung stimmst du unseren{" "}
        <span className="underline underline-offset-4 cursor-pointer hover:text-foreground transition-colors">
          Nutzungsbedingungen
        </span>{" "}
        zu.
      </p>
    </div>
  );
}
