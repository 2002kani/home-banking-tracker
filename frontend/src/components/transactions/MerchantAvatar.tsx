import { Avatar, AvatarFallback } from "@/components/ui/avatar";
import type { TransactionDto } from "@/api/generated/transaction-service";
import { cn } from "@/lib/utils";

interface MerchantAvatarProps {
  name: string;
  type: TransactionDto["type"];
}

export function MerchantAvatar({ name, type }: MerchantAvatarProps) {
  const initials = name
    .split(" ")
    .map((w) => w[0] ?? "")
    .join("")
    .slice(0, 2)
    .toUpperCase();

  return (
    <Avatar size="default">
      <AvatarFallback
        className={cn(
          "text-xs font-medium",
          type === "CRDT"
            ? "bg-foreground text-background"
            : "bg-muted text-muted-foreground",
        )}
      >
        {initials}
      </AvatarFallback>
    </Avatar>
  );
}
