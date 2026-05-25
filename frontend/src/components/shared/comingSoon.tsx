import { Construction } from "lucide-react";

interface ComingSoonProps {
  feature?: string;
}

export function ComingSoon({ feature }: ComingSoonProps) {
  return (
    <div className="mt-6 flex min-h-72 items-center justify-center rounded-2xl border border-dashed border-border bg-card">
      <div className="flex flex-col items-center gap-3 px-6 text-center">
        <div className="flex h-12 w-12 items-center justify-center rounded-xl bg-muted">
          <Construction className="h-6 w-6 text-muted-foreground" />
        </div>
        <div className="space-y-1">
          <p className="text-sm font-medium">
            {feature ? `„${feature}" ist noch nicht verfügbar` : "Noch nicht implementiert"}
          </p>
          <p className="text-xs text-muted-foreground">
            Dieser Bereich befindet sich noch in der Entwicklung.
          </p>
        </div>
      </div>
    </div>
  );
}
