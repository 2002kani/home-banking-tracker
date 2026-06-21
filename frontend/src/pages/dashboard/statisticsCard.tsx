import { Card, CardContent } from "@/components/ui/card";
import { cn } from "@/lib/utils";
import { ArrowUpRight, ArrowDownRight } from "lucide-react";

interface IProps {
  label: string;
  icon?: React.ReactNode;
  value: string;
  delta?: number;
  hint?: string;
}

export default function StatCard(props: IProps) {
  const { label, value, delta, hint, icon } = props;
  const positive = (delta ?? 0) >= 0;
  return (
    <Card className="rounded-lg border-border shadow-none">
      <CardContent className="pl-5 pt-1">
        <div className="flex items-center justify-between">
          <span className="text-xs font-medium uppercase tracking-wider text-muted-foreground">
            {label}
          </span>
          {icon && <div className="text-muted-foreground">{icon}</div>}
        </div>
        <div className="mt-3 flex items-baseline gap-2">
          <div className="font-mono text-2xl font-semibold tracking-tight">
            {value}
          </div>
        </div>
        <div className="mt-3 flex items-center gap-2 text-xs">
          {delta !== undefined && (
            <span
              className={cn(
                "inline-flex items-center gap-0.5 rounded-md px-1.5 py-0.5 font-mono font-medium",
                positive
                  ? "bg-foreground/5 text-foreground"
                  : "bg-foreground/5 text-muted-foreground",
              )}
            >
              {positive ? (
                <ArrowUpRight className="h-3 w-3" />
              ) : (
                <ArrowDownRight className="h-3 w-3" />
              )}
              {positive ? "+" : "+"}
              {delta}%
            </span>
          )}
          {hint && <span className="text-muted-foreground">{hint}</span>}
        </div>
      </CardContent>
    </Card>
  );
}
