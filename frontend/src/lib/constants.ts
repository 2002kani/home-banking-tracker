import {
  LayoutDashboard,
  ArrowLeftRight,
  Tags,
  Wallet,
  PieChart,
  Settings,
} from "lucide-react";

export const version = "v.0.1.0";

export const sidebarNav = [
  { title: "Dashboard", url: "/dashboard", icon: LayoutDashboard },
  { title: "Transaktionen", url: "/transactions", icon: ArrowLeftRight },
  { title: "Konten", url: "/accounts", icon: Wallet },
  { title: "Analytics", url: "/analytics", icon: PieChart },
];

export const sidebarSecondaryNav = [
  { title: "Kategorien", url: "/categories", icon: Tags },
  { title: "Einstellungen", url: "/settings", icon: Settings },
];
