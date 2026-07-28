# Plan: Automatische Kategorisierung via MCC-Mapping

> **Scope dieses PRs:** ausschließlich **MCC-basierte** Auto-Kategorisierung über ein
> System-Mapping.
> **Bewusst NICHT in diesem PR:** Counterparty-/Remittance-Keyword-Regeln und User-MCC-Overrides.
> Die Architektur wird aber so gebaut, dass diese Erweiterungen später ohne Umbau andocken
> (siehe [§8 Skalierbarkeit](#8-skalierbarkeit--geplante-erweiterungen)).

---

## 0. Vergleich mit dem Ausgangsplan & getroffene Entscheidungen

Dieser Plan baut auf einem soliden Entwurf auf und übernimmt dessen Kern (MCC durchreichen →
System-Mapping → beim Ingest setzen). Vier bewusste Änderungen für Skalierbarkeit:

| Thema | Ausgangsplan | Dieser Plan | Warum |
|-------|-------------|-------------|-------|
| Engine | eine `CategorizationEngine`-Klasse mit direkter If-Logik | **Resolver-Kette** (`CategoryResolver`-Strategien) | Der spätere Keyword-Resolver wird nur als weitere `@Component` eingehängt — **kein** Umbau der Engine. |
| `category_source` | `NONE / AUTO / MANUAL` | `NONE / MANUAL / MCC / RULE` | Granular nötig, damit ein MCC-Re-Run ein späteres `RULE`-Ergebnis (höhere Prio) nicht überschreibt. `AUTO` kann Quelle & Prio nicht unterscheiden. |
| `remittance_information` | nicht mitgeführt | **schon jetzt persistiert** | Vermeidet im Keyword-PR eine erneute Migration **und** einen vollen Re-Sync historischer Transaktionen. Kostet fast nichts. |
| User-MCC-Override | — | als **optionale** Erweiterung dokumentiert (§8), nicht im Core-Scope | Hält den ersten PR schlank; Resolver bleibt identisch, wenn es später kommt. |

Übernommen wird ausdrücklich auch der **ehrliche Hinweis zur Rückwirkung** (siehe §6.3): Der
Altbestand hat noch keinen gespeicherten MCC — voll wirksam ist das Feature für ab jetzt (mit MCC)
eingelesene Transaktionen.

---

## 1. Ausgangslage & zentrale Erkenntnis

**Ingestion-Pfad heute:**
`open-banking-service` `SchedulerService.mapAccountTransaction`
→ `TransactionRawEvent` → Kafka (`transactions-raw`)
→ `KafkaConsumerTransactions` → `TransactionService.persistTransactions`
→ `TransactionMapper.mapToEntity` → speichern.

**Kategorisierung heute:** nur manuell via `PATCH /transaction/{id}?categoryId=`.

**Blocker Nr. 1 — der MCC kommt aktuell gar nicht an.**
Der Upstream-`TransactionDto` (open-banking-service) parst nur `transaction_amount`, `creditor`,
`debtor`, `credit_debit_indicator`, `booking_date`, `status`. `merchant_category_code` wird
verworfen, ebenso `remittance_information`. Ohne Upstream-Änderung ist MCC-Kategorisierung
unmöglich → **Phase 0, Voraussetzung für alles Weitere.**

**MCC-Eigenheiten:**
- MCC (ISO 18245) ist standardisiert und erfordert **null User-Konfiguration** → ideal als Default.
- MCC ist nur bei **Kartenzahlungen** zuverlässig vorhanden. SEPA-Überweisungen/Lastschriften haben
  oft **kein** MCC → bleiben `NONE`. Diese Lücke schließen später die Keyword-Regeln.

---

## 2. Zielbild & Präzedenz

Jede Transaktion trägt eine **Provenienz** (`category_source`), damit Auto-Kategorisierung eine
manuelle Wahl nie überschreibt und Re-Runs deterministisch bleiben.

| Prio | Quelle | `category_source` | Scope |
|------|--------|-------------------|-------|
| 1 | Manuelle Kategorisierung (gesperrt) | `MANUAL` | vorhanden |
| 2 | *(später)* User-Keyword-Regel (Counterparty/Remittance) | `RULE` | Folge-PR |
| 3 | *(optional, später)* User-MCC-Override | `MCC` | §8 |
| 4 | System-MCC-Default-Mapping | `MCC` | **dieser PR** |
| — | kein Treffer | `NONE` | **dieser PR** |

In diesem PR existiert nur Prio 1 (Sperre) und Prio 4. Die Resolver-Kette hat schlicht einen
Eintrag. Reihenfolge ist final — Prio 2/3 werden später nur eingeschoben.

---

## 3. Datenmodell-Änderungen (transaction-service)

### 3.1 `Transaction`-Entity + Migration `V3`

```sql
-- V3__add_categorization_fields.sql
ALTER TABLE transactions ADD COLUMN merchant_category_code VARCHAR(4);
ALTER TABLE transactions ADD COLUMN category_source VARCHAR(16) NOT NULL DEFAULT 'NONE';
ALTER TABLE transactions ADD COLUMN remittance_information TEXT;
```

- `merchantCategoryCode` (String) — `VARCHAR(4)`, damit führende Nullen erhalten bleiben.
- `categorySource` als Enum `CategorySource { NONE, MANUAL, MCC, RULE }`
  (`@Enumerated(EnumType.STRING)`).
- `remittanceInformation` (String) — die API liefert `[string]`; beim Mapping mit `\n` joinen.
  Fachlich in diesem PR ungenutzt, aber schon persistiert (siehe §0).

### 3.2 System-Mapping — Migration `V4`

```sql
-- V4__mcc_category_mapping.sql
CREATE TABLE mcc_category_mapping (
    mcc         VARCHAR(4)  PRIMARY KEY,
    category_id BIGINT NOT NULL REFERENCES category (id)
);
```

Seed bildet gängige MCCs auf die 5 bereits geseedeten System-Kategorien ab (Lebensmittel, Wohnen,
Transport, Gesundheit, Unterhaltung), z. B.:

```
5411, 5422, 5451, 5462  -> Lebensmittel
5541, 5542, 4111, 4121  -> Transport
5912, 8011, 8021, 8062  -> Gesundheit
5813, 7832, 7922, 7996  -> Unterhaltung
4900                    -> Wohnen
```

> Seed referenziert die Kategorien über `name` (Sub-Select auf `category` mit `is_system = true`),
> **nicht** über hartkodierte IDs — unabhängig von der `V2`-Seed-Reihenfolge.

---

## 4. Phase 0 — Pipeline erweitern (MCC durchreichen, 5 Stellen)

1. **open-banking** `dto/sessionResponses/TransactionDto`:
   ```java
   @JsonProperty("merchant_category_code")
   private String merchantCategoryCode;

   @JsonProperty("remittance_information")   // schon jetzt, für späteren Keyword-PR
   private List<String> remittanceInformation;
   ```
2. **open-banking** `event/TransactionRawEvent`: beide Felder ergänzen + in
   `SchedulerService.mapAccountTransaction` befüllen.
3. **transaction** `event/TransactionEvent`: beide Felder ergänzen.
4. **transaction** `entity/Transaction`: Spalten aus §3.1.
5. **transaction** `mapper/TransactionMapper.mapToEntity`: Felder mappen
   (`remittanceInformation`-Liste zu String joinen; `categorySource = NONE` default).

---

## 5. Resolver-Architektur (das skalierbare Herzstück)

Statt If-Kaskade eine **Chain of Responsibility** aus `CategoryResolver`-Strategien. Engine iteriert
in Präzedenz-Reihenfolge; erster nicht-leerer Treffer gewinnt.

```java
public interface CategoryResolver {
    int priority();                 // höhere Zahl = höhere Prio; bestimmt Reihenfolge
    CategorySource source();        // Provenienz eines Treffers
    Optional<Category> resolve(Transaction tx, Long userId);
}
```

**In diesem PR nur ein Resolver:**

```java
@Component
class MccCategoryResolver implements CategoryResolver {
    // liest mcc_category_mapping (System); source() == MCC
    // (späterer User-Override wird hier ergänzt, ohne die Kette zu ändern)
}
```

**Engine:**

```java
@Service
class CategorizationEngine {
    private final List<CategoryResolver> resolvers; // Spring injiziert, nach priority() sortiert

    CategorizationResult categorize(Transaction tx, Long userId) {
        return resolvers.stream()
                .map(r -> r.resolve(tx, userId).map(c -> new CategorizationResult(c, r.source())))
                .flatMap(Optional::stream)
                .findFirst()
                .orElse(CategorizationResult.none()); // -> category_source bleibt NONE
    }
}
```

**Warum das skaliert:** Der Keyword-PR fügt nur einen `KeywordRuleResolver` (`priority()` zwischen
`MANUAL` und `MCC`, `source() == RULE`) als weitere `@Component` hinzu — **keine** Änderung an
Engine, Aufruf oder Ingest-Hook. Manuelle Kategorisierung ist kein Resolver, sondern eine harte
Sperre vor dem Kettenaufruf (§6.2).

---

## 6. Integration

### 6.1 Bei Ingest (`TransactionService.persistTransactions`)
Nach `mapToEntity`: wenn `category == null` **und** `categorySource != MANUAL` → Engine aufrufen; bei
Treffer `category` + `categorySource` setzen; dann speichern.

> Reihenfolge: erst Duplikat-Handling (`DataIntegrityViolationException`), Kategorisierung nur für
> tatsächlich neu persistierte Transaktionen.

### 6.2 Manuelle Kategorisierung sperren
`TransactionService.categorizeTransaction` setzt zusätzlich `categorySource = MANUAL`. Jeder spätere
Auto-/Re-Run ignoriert diese Transaktion.

### 6.3 Optionaler Endpoint — retroaktives Apply
`POST /transactions/categorize/apply` läuft über **eigene** Transaktionen mit
`category_source != MANUAL` und wendet das MCC-Mapping an.

> **Ehrlicher Hinweis zur Rückwirkung:** Bestandstransaktionen haben noch **keinen** gespeicherten
> MCC (Feld ist neu). Der Apply-Endpoint kategorisiert sie erst, sobald sie **mit** MCC neu
> eingelesen wurden. Voll wirksam ist das Feature also für ab jetzt eingehende Transaktionen — bei
> „erst nur MCC" unvermeidbar. Die spätere Keyword-Stufe kann den Altbestand über die vorhandenen
> Namen/Remittance nachträglich abdecken.

---

## 7. Neue Dateien (transaction-service)

```
enums/CategorySource.java
entity/MccCategoryMapping.java
repository/MccCategoryMappingRepository.java
service/CategoryResolver.java              (Interface)
service/MccCategoryResolver.java
service/CategorizationEngine.java
service/CategorizationResult.java          (kleines Record: category + source)
db/migration/V3__add_categorization_fields.sql
db/migration/V4__mcc_category_mapping.sql
```
Plus Änderungen an: `Transaction`, `TransactionEvent`, `TransactionMapper`, `TransactionService`
(+ optional `TransactionController` für §6.3) sowie die 3 open-banking-Dateien aus §4.

---

## 8. Skalierbarkeit — geplante Erweiterungen (NICHT dieser PR)

Bewusst vorbereitet, damit die Folge-PRs minimal-invasiv werden:

- **Keyword-Regeln (Counterparty/Remittance):** neuer `KeywordRuleResolver` als `@Component`, Prio
  zwischen `MANUAL` und `MCC`, `source() == RULE`. Geplante Artefakte: Tabelle `categorization_rule`
  (`user_id, pattern, category_id, direction?, priority, enabled`), CONTAINS-Matching
  case-insensitive über `creditor_name` ∨ `debtor_name` ∨ `remittance_information`, CRUD unter
  `/rules` + `/rules/apply`. **Kein** Engine-Umbau, **keine** neue Migration für
  `remittance_information` (schon da), **kein** Enum-Change (`RULE` schon da).
- **User-MCC-Override:** Tabelle `mcc_category_override (user_id, mcc, category_id)` + Erweiterung des
  `MccCategoryResolver` (erst Override, dann System-Mapping) + Endpoints `/mcc-overrides`. Prio 3.

---

## 9. Edge Cases (dieser PR)

- Transaktion ohne MCC (SEPA/Lastschrift) → `NONE`.
- MCC ohne Mapping-Eintrag → `NONE` (nicht raten).
- Re-Run idempotent, überschreibt nie `MANUAL`.
- `merchant_category_code` als String (führende Nullen).
- Seed referenziert Kategorien über `name`, nicht über feste IDs.

---

## 10. Umsetzungsreihenfolge

0. **Pipeline-Enrichment** (open-banking + Event/Mapper transaction-service) — §4.
1. **Migrationen** V3 (Felder) → V4 (System-Mapping + Seed) — §3.
2. **Resolver-Architektur** `CategoryResolver` + `MccCategoryResolver` + `CategorizationEngine` — §5.
3. **Ingest-Hook + MANUAL-Sperre** — §6.1/6.2.
4. **(optional) retroaktives Apply** — §6.3.
