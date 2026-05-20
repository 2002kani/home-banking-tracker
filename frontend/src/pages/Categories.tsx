import { AlertCircle, PlusCircle, Edit2, Trash2, TrendingUp } from "lucide-react"
import { categories, transactions } from "@/data/mockData"
import { formatCurrency } from "@/lib/utils"
import { Button } from "@/components/ui/button"
import { Card, CardContent } from "@/components/ui/card"
import { Separator } from "@/components/ui/separator"

const budgetCategories = categories.filter((c) => c.budget !== undefined)
const otherCategories = categories.filter((c) => c.budget === undefined)

function getCategoryTotal(categoryId: string): number {
  return transactions
    .filter((t) => t.category === categoryId)
    .reduce((sum, t) => sum + Math.abs(t.amount), 0)
}

function getCategoryAvg(categoryId: string): number | null {
  const txs = transactions.filter((t) => t.category === categoryId)
  if (txs.length === 0) return null
  return txs.reduce((sum, t) => sum + Math.abs(t.amount), 0) / txs.length
}

const totalBudget = budgetCategories.reduce((s, c) => s + (c.budget ?? 0), 0)
const totalSpent = budgetCategories.reduce((s, c) => s + (c.spent ?? 0), 0)
const overBudgetCount = budgetCategories.filter((c) => (c.spent ?? 0) > (c.budget ?? 0)).length
const mostUsedCategory = [...categories].sort(
  (a, b) => getCategoryTotal(b.id) - getCategoryTotal(a.id)
)[0]

export function Categories() {
  return (
    <div className="space-y-8">
      {/* Header */}
      <div className="flex items-start justify-between">
        <div>
          <h1 className="text-2xl font-bold text-foreground">Kategorien</h1>
          <p className="text-sm text-muted-foreground mt-1">
            Verwalte deine Ausgabenkategorien und Budgets
          </p>
        </div>
        <Button className="shrink-0">
          <PlusCircle />
          Kategorie hinzufügen
        </Button>
      </div>

      {/* Budget Categories */}
      <div>
        <h2 className="text-lg font-semibold text-foreground mb-4">Budget Kategorien</h2>
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
          {budgetCategories.map((category) => {
            const percent = Math.round(((category.spent ?? 0) / (category.budget ?? 1)) * 100)
            const isOver = percent > 100
            const isNear = percent >= 80 && percent <= 100
            const barColor = isOver ? "#ef4444" : isNear ? "#f59e0b" : category.color
            const percentColor = isOver
              ? "text-red-600 dark:text-red-400"
              : isNear
                ? "text-amber-600 dark:text-amber-400"
                : "text-muted-foreground"

            return (
              <Card
                key={category.id}
                className="group rounded-xl border-neutral-200 dark:border-neutral-800 hover:border-neutral-300 dark:hover:border-neutral-700 transition-colors"
              >
                <CardContent className="p-5">
                  {/* Top Row */}
                  <div className="flex items-start justify-between mb-4">
                    <div
                      className="w-10 h-10 rounded-lg flex items-center justify-center"
                      style={{ backgroundColor: `${category.color}20` }}
                    >
                      <span className="text-lg">{category.icon}</span>
                    </div>
                    <div className="flex items-center gap-1 opacity-0 group-hover:opacity-100 transition-opacity">
                      <Button variant="ghost" size="icon" className="h-8 w-8 text-muted-foreground">
                        <Edit2 className="w-3.5 h-3.5" />
                      </Button>
                      <Button
                        variant="ghost"
                        size="icon"
                        className="h-8 w-8 text-muted-foreground hover:text-destructive hover:bg-destructive/10"
                      >
                        <Trash2 className="w-3.5 h-3.5" />
                      </Button>
                    </div>
                  </div>

                  {/* Status Badges */}
                  {isOver && (
                    <div className="flex items-center gap-1.5 bg-red-50 dark:bg-red-950 text-red-700 dark:text-red-300 rounded-lg px-2.5 py-1.5 mb-3">
                      <AlertCircle className="w-3.5 h-3.5 text-red-500 shrink-0" />
                      <span className="text-xs font-medium">Budget überschritten!</span>
                    </div>
                  )}
                  {isNear && !isOver && (
                    <div className="flex items-center gap-1.5 bg-amber-50 dark:bg-amber-950 text-amber-700 dark:text-amber-300 rounded-lg px-2.5 py-1.5 mb-3">
                      <AlertCircle className="w-3.5 h-3.5 text-amber-500 shrink-0" />
                      <span className="text-xs font-medium">Budget fast erreicht</span>
                    </div>
                  )}

                  <p className="text-sm font-medium text-foreground mb-1">{category.name}</p>
                  <p className="text-2xl font-bold text-foreground">
                    {formatCurrency(category.spent ?? 0)}
                  </p>
                  <p className="text-sm text-muted-foreground mb-3">
                    von {formatCurrency(category.budget ?? 0)}
                  </p>

                  <div className="h-1.5 bg-neutral-100 dark:bg-neutral-800 rounded-full overflow-hidden mb-2">
                    <div
                      className="h-full rounded-full transition-all"
                      style={{ width: `${Math.min(percent, 100)}%`, backgroundColor: barColor }}
                    />
                  </div>
                  <div className="flex items-center justify-between">
                    <span className={`text-xs font-medium ${percentColor}`}>{percent}%</span>
                    <span className="text-xs text-muted-foreground">
                      {formatCurrency(Math.max((category.budget ?? 0) - (category.spent ?? 0), 0))} übrig
                    </span>
                  </div>
                </CardContent>
              </Card>
            )
          })}
        </div>
      </div>

      {/* Other Categories */}
      {otherCategories.length > 0 && (
        <div>
          <h2 className="text-lg font-semibold text-foreground mb-4">Weitere Kategorien</h2>
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
            {otherCategories.map((category) => {
              const total = getCategoryTotal(category.id)
              const avg = getCategoryAvg(category.id)

              return (
                <Card
                  key={category.id}
                  className="group rounded-xl border-neutral-200 dark:border-neutral-800 hover:border-neutral-300 dark:hover:border-neutral-700 transition-colors"
                >
                  <CardContent className="p-5">
                    <div className="flex items-start justify-between mb-4">
                      <div
                        className="w-10 h-10 rounded-lg flex items-center justify-center"
                        style={{ backgroundColor: `${category.color}20` }}
                      >
                        <span className="text-lg">{category.icon}</span>
                      </div>
                      <div className="flex items-center gap-1 opacity-0 group-hover:opacity-100 transition-opacity">
                        <Button variant="ghost" size="icon" className="h-8 w-8 text-muted-foreground">
                          <Edit2 className="w-3.5 h-3.5" />
                        </Button>
                        <Button
                          variant="ghost"
                          size="icon"
                          className="h-8 w-8 text-muted-foreground hover:text-destructive hover:bg-destructive/10"
                        >
                          <Trash2 className="w-3.5 h-3.5" />
                        </Button>
                      </div>
                    </div>

                    <p className="text-sm font-medium text-foreground mb-1">{category.name}</p>
                    <p className="text-2xl font-bold text-foreground">{formatCurrency(total)}</p>
                    <p className="text-sm text-muted-foreground mb-2">gesamt</p>

                    {avg !== null && (
                      <div className="flex items-center gap-1.5 text-sm text-muted-foreground">
                        <TrendingUp className="w-4 h-4 shrink-0" />
                        <span>Ø {formatCurrency(avg)} pro Transaktion</span>
                      </div>
                    )}
                  </CardContent>
                </Card>
              )
            })}
          </div>
        </div>
      )}

      {/* Statistics */}
      <div className="bg-linear-to-br from-neutral-100 to-neutral-200 dark:from-neutral-800 dark:to-neutral-900 rounded-xl border border-neutral-200 dark:border-neutral-700 p-6">
        <h2 className="text-lg font-semibold text-foreground mb-4">Statistiken</h2>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <Card>
            <CardContent className="p-4">
              <p className="text-sm text-muted-foreground mb-1">Gesamtbudget</p>
              <p className="text-3xl font-bold text-foreground">{formatCurrency(totalBudget)}</p>
            </CardContent>
          </Card>
          <Card>
            <CardContent className="p-4">
              <p className="text-sm text-muted-foreground mb-1">Budgets überschritten</p>
              <p className="text-3xl font-bold text-foreground">{overBudgetCount}</p>
            </CardContent>
          </Card>
          <Card>
            <CardContent className="p-4">
              <p className="text-sm text-muted-foreground mb-1">Meist genutzt</p>
              <p className="text-lg font-bold text-foreground flex items-center gap-2">
                <span>{mostUsedCategory?.icon}</span>
                {mostUsedCategory?.name}
              </p>
            </CardContent>
          </Card>
        </div>

        <Separator className="my-4 bg-neutral-200 dark:bg-neutral-700" />
        <div className="flex items-center justify-between text-sm">
          <span className="text-muted-foreground">Gesamtausgaben von Budget</span>
          <span className="font-semibold text-foreground">
            {formatCurrency(totalSpent)} / {formatCurrency(totalBudget)}
          </span>
        </div>
        <div className="mt-2 h-2 bg-neutral-200 dark:bg-neutral-700 rounded-full overflow-hidden">
          <div
            className="h-full bg-neutral-900 dark:bg-neutral-100 rounded-full transition-all"
            style={{ width: `${Math.min((totalSpent / totalBudget) * 100, 100)}%` }}
          />
        </div>
      </div>
    </div>
  )
}
