import { Badge } from "@/components/ui/badge";

interface CategoryBadgeProps {
  name: string;
  color: string;
}

export function CategoryBadge({ name, color }: CategoryBadgeProps) {
  return (
    <Badge
      variant="outline"
      style={{
        borderRadius: "7px",
        backgroundColor: `${color}20`,
        color,
        borderColor: `${color}50`,
      }}
      className="shrink-0 px-1.5 py-0 text-[10px] font-medium"
    >
      {name}
    </Badge>
  );
}
