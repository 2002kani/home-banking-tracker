import { LogOut, Moon, Sun, Shield } from "lucide-react";
import PageHeader from "@/components/shared/pageHeader";
import { Button } from "@/components/ui/button";
import { Separator } from "@/components/ui/separator";
import { useTheme } from "@/context/themeContext";
import AlertModal from "@/components/shared/AlertModal";
import { useAuth } from "@/context/authContext";

function SettingsPage() {
  const { logout } = useAuth();
  const { theme, setTheme } = useTheme();

  return (
    <>
      <PageHeader
        title="Einstellungen"
        description="Konto- und Sicherheitseinstellungen."
      />

      <div className="mt-6 space-y-6 max-w-lg">
        <div className="space-y-3">
          <div>
            <h2 className="text-sm font-medium">Erscheinungsbild</h2>
            <p className="text-xs text-muted-foreground mt-0.5">
              Wähle zwischen hellem und dunklem Design.
            </p>
          </div>
          <div className="flex gap-2">
            <Button
              variant={theme === "light" ? "default" : "outline"}
              size="sm"
              className="gap-2"
              onClick={() => setTheme("light")}
            >
              <Sun className="h-4 w-4" />
              Hell
            </Button>
            <Button
              variant={theme === "dark" ? "default" : "outline"}
              size="sm"
              className="gap-2"
              onClick={() => setTheme("dark")}
            >
              <Moon className="h-4 w-4" />
              Dunkel
            </Button>
          </div>
        </div>

        <Separator />

        <div className="space-y-3">
          <div>
            <h2 className="text-sm font-medium">Sicherheit</h2>
            <p className="text-xs text-muted-foreground mt-0.5">
              Passwort ändern und Sitzungen verwalten.
            </p>
          </div>
          <Button variant="outline" size="sm" className="gap-2 cursor-pointer">
            <Shield className="h-4 w-4" />
            Passwort ändern
          </Button>
        </div>

        <Separator />

        <div className="space-y-3">
          <div>
            <h2 className="text-sm font-medium">Sitzung</h2>
            <p className="text-xs text-muted-foreground mt-0.5">
              Aktuell angemeldete Sitzung beenden.
            </p>
          </div>
          <AlertModal
            alertDescription="Du musst dich anschließend erneut anmelden."
            alertTitle="Wirklich abmelden?"
            handleSubmit={logout}
            trigger={
              <Button
                variant="destructive"
                size="sm"
                className="gap-2 cursor-pointer"
              >
                <LogOut className="h-4 w-4" />
                Abmelden
              </Button>
            }
          ></AlertModal>
        </div>
      </div>
    </>
  );
}

export default SettingsPage;
