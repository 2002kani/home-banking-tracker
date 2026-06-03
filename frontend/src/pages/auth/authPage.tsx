import { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Button } from "@/components/ui/button";

export default function AuthPage() {
  const location = useLocation();
  const navigate = useNavigate();
  const defaultTab = location.pathname === "/register" ? "register" : "login";
  const [passwordMismatch, setPasswordMismatch] = useState(false);

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
          <form className="space-y-4">
            <div className="space-y-2">
              <Label htmlFor="login-email">E-Mail</Label>
              <Input
                id="login-email"
                type="email"
                placeholder="name@beispiel.de"
                autoComplete="email"
                required
              />
            </div>
            <div className="space-y-2">
              <div className="flex items-center justify-between">
                <Label htmlFor="login-password">Passwort</Label>
                <button
                  type="button"
                  className="text-[11px] text-muted-foreground hover:text-foreground transition-colors"
                >
                  Vergessen?
                </button>
              </div>
              <Input
                id="login-password"
                type="password"
                autoComplete="current-password"
                required
                minLength={8}
              />
            </div>
            <Button type="submit" className="w-full mt-2">
              Anmelden
            </Button>
          </form>
        </TabsContent>

        <TabsContent value="register">
          <form className="space-y-4">
            <div className="grid grid-cols-2 gap-3">
              <div className="space-y-2">
                <Label htmlFor="register-firstname">Vorname</Label>
                <Input
                  id="register-firstname"
                  type="text"
                  placeholder="Max"
                  autoComplete="given-name"
                  required
                />
              </div>
              <div className="space-y-2">
                <Label htmlFor="register-lastname">Nachname</Label>
                <Input
                  id="register-lastname"
                  type="text"
                  placeholder="Mustermann"
                  autoComplete="family-name"
                  required
                />
              </div>
            </div>
            <div className="space-y-2">
              <Label htmlFor="register-email">E-Mail</Label>
              <Input
                id="register-email"
                type="email"
                placeholder="name@beispiel.de"
                autoComplete="email"
                required
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="register-password">Passwort</Label>
              <Input
                id="register-password"
                type="password"
                autoComplete="new-password"
                required
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="register-confirm">Passwort bestätigen</Label>
              <Input
                id="register-confirm"
                type="password"
                autoComplete="new-password"
                required
                onChange={(e) => {
                  const pw = (
                    document.getElementById(
                      "register-password",
                    ) as HTMLInputElement
                  )?.value;
                  setPasswordMismatch(
                    e.target.value.length > 8 && e.target.value !== pw,
                  );
                }}
                className={
                  passwordMismatch
                    ? "border-destructive focus-visible:ring-destructive"
                    : ""
                }
              />
              {passwordMismatch && (
                <p className="text-[12px] text-destructive">
                  Passwörter stimmen nicht überein.
                </p>
              )}
            </div>
            <Button type="submit" className="w-full mt-2">
              Konto erstellen
            </Button>
          </form>
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
