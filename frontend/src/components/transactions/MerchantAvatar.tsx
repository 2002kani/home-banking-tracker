import { Avatar, AvatarFallback } from "@/components/ui/avatar";
import { cn } from "@/lib/utils";

interface MerchantAvatarProps {
  name: string;
  type: "CRDT" | "DBIT";
}

export function MerchantAvatar({ name, type }: MerchantAvatarProps) {
  const initials = name
    .split(" ")
    .map((w) => w[0])
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
