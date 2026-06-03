import { Outlet } from "react-router-dom";
import { version } from "@/lib/constants";

export default function AuthLayout() {
  return (
    <div className="min-h-screen grid lg:grid-cols-2">
      <div className="hidden lg:flex flex-col relative bg-foreground text-background overflow-hidden">
        <div
          className="absolute inset-0 opacity-[0.04]"
          style={{
            backgroundImage: `linear-gradient(to right, white 1px, transparent 1px),
                              linear-gradient(to bottom, white 1px, transparent 1px)`,
            backgroundSize: "40px 40px",
          }}
        />

        <div className="absolute top-1/4 -left-20 w-72 h-72 rounded-full bg-white/5 blur-3xl" />
        <div className="absolute bottom-1/4 right-0 w-96 h-96 rounded-full bg-white/5 blur-3xl" />

        <div className="relative z-10 p-10">
          <div className="flex items-center gap-3">
            <div className="flex h-8 w-8 items-center justify-center rounded-md bg-background text-foreground">
              <span className="font-mono text-xs font-bold">H</span>
            </div>
            <div className="flex flex-col leading-tight">
              <span className="text-sm font-semibold tracking-tight">
                Home Banking
              </span>
              <span className="text-[10px] text-background/40 font-mono">
                {version}
              </span>
            </div>
          </div>
        </div>

        <div className="relative z-10 flex-1 flex flex-col justify-center px-10 pb-10">
          <div className="space-y-6 max-w-sm">
            <div className="space-y-2">
              <p className="text-[11px] font-mono uppercase tracking-[0.2em] text-background/40">
                Private Finanzen
              </p>
              <h1 className="text-4xl font-semibold tracking-tight leading-tight">
                Deine Finanzen.
                <br />
                <span className="text-background/40">Übersichtlich.</span>
              </h1>
            </div>
            <p className="text-sm text-background/50 leading-relaxed">
              Alle Konten, Transaktionen und Ausgaben auf einen Blick — sicher
              und übersichtlich.
            </p>

            <div className="grid grid-cols-3 gap-4 pt-4 border-t border-background/10">
              {[
                { label: "Konten", value: "Multi" },
                { label: "Transaktionen", value: "Echtzeit" },
                { label: "Analytics", value: "Im Überblick" },
              ].map((stat) => (
                <div key={stat.label} className="space-y-1">
                  <p className="text-xs font-mono text-background/30 uppercase tracking-wider">
                    {stat.label}
                  </p>
                  <p className="text-sm font-medium text-background/70">
                    {stat.value}
                  </p>
                </div>
              ))}
            </div>
          </div>
        </div>

        <div className="relative z-10 px-10 pb-8">
          <p className="text-[11px] text-background/25 font-mono">
            End-to-end verschlüsselt · Open Banking · JWT Auth
          </p>
        </div>
      </div>

      <div className="flex flex-col items-center justify-center p-6 sm:p-10 bg-background">
        <div className="flex lg:hidden items-center gap-2 mb-10">
          <div className="flex h-7 w-7 items-center justify-center rounded-md bg-foreground text-background">
            <span className="font-mono text-xs font-bold">H</span>
          </div>
          <span className="text-sm font-semibold tracking-tight">
            Home Banking
          </span>
        </div>

        <div className="w-full max-w-sm">
          <Outlet />
        </div>
      </div>
    </div>
  );
}
