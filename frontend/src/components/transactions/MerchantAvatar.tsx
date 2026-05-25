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
          "text-xs font-bold text-white",
          type === "CRDT" ? "bg-emerald-500" : "bg-slate-500",
        )}
      >
        {initials}
      </AvatarFallback>
    </Avatar>
  );
}
