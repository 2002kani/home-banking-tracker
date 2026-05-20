import { useState, useMemo } from "react"
import { Calendar, Search, Filter } from "lucide-react"
import { categories, transactions } from "@/data/mockData"
import { formatCurrency, formatDate } from "@/lib/utils"
import { Badge } from "@/components/ui/badge"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Card, CardContent } from "@/components/ui/card"
import { Separator } from "@/components/ui/separator"
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select"
import type { Transaction } from "@/types"

function groupByDate(txs: Transaction[]): Record<string, Transaction[]> {
  return txs.reduce<Record<string, Transaction[]>>((acc, tx) => {
    if (!acc[tx.date]) acc[tx.date] = []
    acc[tx.date].push(tx)
    return acc
  }, {})
}

export function Transactions() {
  const [search, setSearch] = useState("")
  const [selectedCategory, setSelectedCategory] = useState("")
  const [selectedType, setSelectedType] = useState("")

  const filtered = useMemo(() => {
    return transactions.filter((tx) => {
      const matchSearch =
        !search ||
        tx.title.toLowerCase().includes(search.toLowerCase()) ||
        (tx.merchant?.toLowerCase().includes(search.toLowerCase()) ?? false)
      const matchCategory = !selectedCategory || tx.category === selectedCategory
      const matchType = !selectedType || tx.type === selectedType
      return matchSearch && matchCategory && matchType
    })
  }, [search, selectedCategory, selectedType])

  const grouped = useMemo(() => groupByDate(filtered), [filtered])
  const sortedDates = Object.keys(grouped).sort((a, b) => b.localeCompare(a))

  const totalIncome = filtered
    .filter((t) => t.type === "income")
    .reduce((s, t) => s + t.amount, 0)
  const totalExpenses = filtered
    .filter((t) => t.type === "expense")
    .reduce((s, t) => s + Math.abs(t.amount), 0)

  const getCategoryById = (id: string) => categories.find((c) => c.id === id)

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold text-foreground">Transaktionen</h1>
        <Button>
          <Calendar />
          Diesen Monat
        </Button>
      </div>

      {/* Filter Section */}
      <Card className="rounded-xl border-neutral-200 dark:border-neutral-800">
        <CardContent className="p-4">
          <div className="flex items-center gap-2 mb-3">
            <Filter className="w-4 h-4 text-muted-foreground" />
            <span className="text-sm font-medium text-foreground">Filter</span>
          </div>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            {/* Search */}
            <div className="relative">
              <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
              <Input
                type="text"
                placeholder="Suchen..."
                value={search}
                onChange={(e) => setSearch(e.target.value)}
                className="pl-10"
              />
            </div>

            {/* Category Filter */}
            <Select value={selectedCategory} onValueChange={setSelectedCategory}>
              <SelectTrigger>
                <SelectValue placeholder="Alle Kategorien" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="">Alle Kategorien</SelectItem>
                {categories.map((cat) => (
                  <SelectItem key={cat.id} value={cat.id}>
                    {cat.icon} {cat.name}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>

            {/* Type Filter */}
            <Select value={selectedType} onValueChange={setSelectedType}>
              <SelectTrigger>
                <SelectValue placeholder="Alle Typen" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="">Alle Typen</SelectItem>
                <SelectItem value="income">Einnahmen</SelectItem>
                <SelectItem value="expense">Ausgaben</SelectItem>
              </SelectContent>
            </Select>
          </div>
        </CardContent>
      </Card>

      {/* Results Summary */}
      <div className="flex items-center justify-between text-sm">
        <span className="text-muted-foreground">
          {filtered.length} Transaktion{filtered.length !== 1 ? "en" : ""}
        </span>
        <div className="flex items-center gap-4">
          <span className="text-muted-foreground">
            Einnahmen:{" "}
            <span className="font-semibold text-green-600 dark:text-green-400">
              +{formatCurrency(totalIncome)}
            </span>
          </span>
          <span className="text-muted-foreground">
            Ausgaben:{" "}
            <span className="font-semibold text-red-600 dark:text-red-400">
              -{formatCurrency(totalExpenses)}
            </span>
          </span>
        </div>
      </div>

      {/* Transaction List */}
      {filtered.length === 0 ? (
        <Card className="rounded-xl border-neutral-200 dark:border-neutral-800">
          <CardContent className="p-12 text-center">
            <p className="text-muted-foreground text-sm">Keine Transaktionen gefunden</p>
          </CardContent>
        </Card>
      ) : (
        <div className="space-y-6">
          {sortedDates.map((date) => (
            <div key={date}>
              {/* Date Group Header */}
              <div className="flex items-center gap-3 mb-3">
                <span className="text-sm font-semibold text-foreground">
                  {formatDate(date)}
                </span>
                <Separator className="flex-1" />
              </div>

              {/* Transactions for this date */}
              <Card className="rounded-xl border-neutral-200 dark:border-neutral-800">
                <CardContent className="p-0">
                  {grouped[date].map((tx, i) => {
                    const category = getCategoryById(tx.category)
                    return (
                      <div key={tx.id}>
                        {i > 0 && <Separator />}
                        <div className="flex items-center gap-4 p-4 hover:bg-accent transition-colors first:rounded-t-xl last:rounded-b-xl">
                          <div
                            className="w-12 h-12 rounded-xl flex items-center justify-center shrink-0 transition-transform hover:scale-105"
                            style={{ backgroundColor: `${category?.color ?? "#888"}20` }}
                          >
                            <span className="text-xl">{category?.icon ?? "💳"}</span>
                          </div>
                          <div className="flex-1 min-w-0">
                            <p className="text-sm font-medium text-foreground truncate">{tx.title}</p>
                            <div className="flex items-center gap-2 mt-0.5">
                              <p className="text-xs text-muted-foreground truncate">{tx.merchant}</p>
                              {category && (
                                <Badge color={category.color}>{category.name}</Badge>
                              )}
                            </div>
                          </div>
                          <div className="text-right shrink-0">
                            <p
                              className={`font-semibold text-sm ${
                                tx.type === "income"
                                  ? "text-green-600 dark:text-green-400"
                                  : "text-foreground"
                              }`}
                            >
                              {tx.type === "income" ? "+" : ""}
                              {formatCurrency(tx.amount)}
                            </p>
                            {tx.time && (
                              <p className="text-xs text-muted-foreground mt-0.5">{tx.time}</p>
                            )}
                          </div>
                        </div>
                      </div>
                    )
                  })}
                </CardContent>
              </Card>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}
