import { NavLink } from "react-router-dom"
import { useTheme } from "next-themes"
import { LayoutDashboard, ArrowLeftRight, Tags, Sun, Moon } from "lucide-react"
import { cn } from "@/lib/utils"
import { Button } from "@/components/ui/button"
import { useIsMounted } from "@/hooks/useIsMounted"

const navItems = [
  { to: "/", label: "Dashboard", icon: LayoutDashboard },
  { to: "/transactions", label: "Transaktionen", icon: ArrowLeftRight },
  { to: "/categories", label: "Kategorien", icon: Tags },
]

export function Header() {
  const { theme, setTheme } = useTheme()
  const mounted = useIsMounted()

  return (
    <header className="sticky top-0 z-50 h-16 bg-background border-b border-border">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-full flex items-center justify-between">
        {/* Logo */}
        <div className="flex items-center gap-3">
          <div className="w-8 h-8 rounded-lg bg-linear-to-br from-neutral-900 to-neutral-700 dark:from-neutral-100 dark:to-neutral-300 flex items-center justify-center">
            <span className="text-white dark:text-neutral-900 text-xs font-bold">FF</span>
          </div>
          <span className="text-lg font-semibold text-foreground">FinanzFlow</span>
        </div>

        {/* Desktop Navigation */}
        <nav className="hidden md:flex items-center gap-1">
          {navItems.map(({ to, label, icon: Icon }) => (
            <NavLink
              key={to}
              to={to}
              end={to === "/"}
              className={({ isActive }) =>
                cn(
                  "flex items-center gap-2 px-3 py-1.5 rounded-lg text-sm font-medium transition-colors",
                  isActive
                    ? "bg-primary text-primary-foreground"
                    : "text-muted-foreground hover:text-foreground hover:bg-accent"
                )
              }
            >
              <Icon className="w-4 h-4" />
              {label}
            </NavLink>
          ))}
        </nav>

        {/* Theme Toggle */}
        {mounted && (
          <Button
            variant="outline"
            size="icon"
            onClick={() => setTheme(theme === "dark" ? "light" : "dark")}
            aria-label="Theme wechseln"
          >
            {theme === "dark" ? <Sun className="w-4 h-4" /> : <Moon className="w-4 h-4" />}
          </Button>
        )}
      </div>
    </header>
  )
}
