import { Search, Command } from "lucide-react";

import { Separator } from "../ui/separator";
import { Input } from "../ui/input";

interface IProps {
  title: string;
  breadcrumb?: string;
}

export default function Header(props: IProps) {
  const { breadcrumb, title } = props;

  return (
    <header className="sticky top-0 z-20 flex h-15.5 items-center gap-3 border-b border-border bg-background/80 px-4 backdrop-blur">
      {/* <SidebarTrigger className="-ml-1" /> */}
      <Separator orientation="vertical" className="h-5" />
      <div className="flex items-center gap-2 text-sm">
        <span className="text-muted-foreground">
          {breadcrumb ?? "Workspace"}
        </span>
        <span className="text-muted-foreground/40">/</span>
        <span className="font-medium">{title}</span>
      </div>
      <div className="ml-auto flex items-center gap-2">
        <div className="relative hidden md:block">
          <Search className="absolute left-2.5 top-1/2 h-3.5 w-3.5 -translate-y-1/2 text-muted-foreground" />
          <Input
            placeholder="Suchen..."
            className="h-8 w-64 rounded-md border-border bg-muted/40 pl-8 pr-12 text-sm shadow-none focus-visible:ring-1"
          />
          <kbd className="pointer-events-none absolute right-2 top-1/2 hidden -translate-y-1/2 items-center gap-1 rounded border border-border bg-background px-1.5 font-mono text-[10px] text-muted-foreground md:inline-flex">
            <Command className="h-2.5 w-2.5" />K
          </kbd>
        </div>
      </div>
    </header>
  );
}
