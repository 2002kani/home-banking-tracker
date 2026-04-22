# Real Banking Manager — Skills & Technologien

## Die Software

Real Banking Manager ist ein persönliches Finanzmanagement-Backend das echte Bankdaten in eine eigene Anwendung integriert. Der User verbindet seine Bank einmalig über einen OAuth2-Flow — danach läuft alles automatisch im Hintergrund.

Das System synchronisiert regelmäßig Transaktionen und Kontostände direkt von der Bank des Users über die Enable Banking Open Banking API. Die geholten Transaktionen werden kategorisiert, ausgewertet und als monatliche Übersichten aufbereitet. Der User kann Ausgaben nach Kategorien sortieren, eigene Kategorisierungsregeln definieren und seinen Kontostand jederzeit einsehen — ohne sich erneut bei seiner Bank einzuloggen.

### Was die App kann
- Echtzeit-Bankverbindung via Open Banking (OAuth2) — einmalige Autorisierung, dauerhafte Synchronisierung
- Automatischer Transaktions-Sync im Hintergrund (Scheduler) — neue Buchungen erscheinen ohne User-Interaktion
- Transaktionshistorie mit Filterung nach Zeitraum, Kategorie und Typ (Eingang/Ausgang)
- Manuelle und regelbasierte Kategorisierung von Transaktionen (Noch zu implermentieren)
- Kontostandsverfolgung mit automatischer Balance-Aktualisierung

### Technische Besonderheit
Die Anwendung nutzt keine Screenscraping-Methoden sondern offizielle PSD2-konforme Open Banking APIs. Die Authentifizierung gegenüber Enable Banking erfolgt über selbst-generierte RSA-Zertifikate und RS256-signierte JWTs — ein Verfahren das dem Standard professioneller Fintech-Anwendungen entspricht.

---

## Projektübersicht

Ein vollständiges Microservice-Backend für persönliches Finanzmanagement. Verbindet echte Bankkonten via Enable Banking Open Banking API, synchronisiert Transaktionen automatisch im Hintergrund und verteilt Daten über Apache Kafka zwischen unabhängigen Services.
Service Folders:
account-service/
open-banking-service/
transaction-service/
---

## Architektur & System Design

### Microservice-Architektur
- Entwurf und Implementierung von 4 unabhängigen Spring Boot Services mit klar abgegrenzten Verantwortlichkeiten
- Database-per-Service Pattern: jeder Service hat seine eigene PostgreSQL-Instanz — keine geteilten Tabellen
- Klare Trennung zwischen synchroner (HTTP) und asynchroner (Kafka) Kommunikation
- API Gateway als zentraler Einstiegspunkt für alle Client-Requests

### Event-Driven Architecture
- Producer/Consumer-Pattern mit Apache Kafka für entkoppelte Service-Kommunikation
- Drei Kafka Topics mit unterschiedlichen Aufgaben: `transactions.raw`, `accounts.updated`, `alerts.notify`
- Outbox-Prinzip: Daten werden erst lokal gespeichert, dann als Event publiziert
- Idempotente Event-Verarbeitung durch deterministischen Deduplication-Key (`externalId`)

### Datenbankdesign
- Relationales Datenbankmodell mit PostgreSQL
- JPA/Hibernate für ORM — Entity-Mapping mit `@ManyToOne`, `@Enumerated`, `@PreUpdate`
- Unique Constraints für Deduplication (`external_id UNIQUE`)
- Nullable Foreign Keys für System- vs. User-Kategorien

---

## Backend — Java & Spring Boot

### Spring Boot 3
- Aufbau von REST APIs mit `@RestController`, `@RequestMapping`, `@PathVariable`, `@RequestParam`
- Dependency Injection via Konstruktor (`@RequiredArgsConstructor`)
- Konfigurationsmanagement mit `@Value`, `application.properties`, Secrets über separate Properties-Dateien
- Bean-Lifecycle: `@PostConstruct` für einmalige Initialisierung beim Start
- `@Scheduled` für automatische Hintergrundtasks (Cron und fixedRate)
- `@PreUpdate` JPA Lifecycle Hook für automatische `updated_at` Aktualisierung

### Spring WebFlux / WebClient
- Non-blocking HTTP Client für externe API-Calls
- Builder-Pattern für WebClient-Konfiguration (baseUrl, defaultHeaders, maxInMemorySize)
- Reaktive Fehlerbehandlung mit `onStatus` und `Mono.error`
- `queryParamIfPresent` für optionale Query Parameter

### Spring Security
- JWT-basierte Authentifizierung mit RSA-256 Signierung
- Stateless Authentication: kein Session-Speicher, Token wird lokal validiert
- `@RequestHeader("X-User-Id")` Pattern für userId-Weitergabe nach Gateway-Validierung

### Spring Data JPA
- Repository-Pattern mit `JpaRepository`
- Custom Query Methods: `findByStatus`, `findByUidAndUserId`, `findBySessionId`
- `Optional<T>` mit `ifPresentOrElse` für sauberes Update-or-Insert Pattern

### Spring Kafka
- `KafkaTemplate` für Event-Publishing mit Partition Key
- `@KafkaListener` für automatischen Event-Empfang
- JSON-Serialisierung/Deserialisierung von Events
- Consumer Groups für skalierbare Verarbeitung

---

## Sicherheit & Authentifizierung

### RSA + JWT (RS256)
- Generierung eines RSA-4096 Schlüsselpaars mit OpenSSL
- PKCS8-Format für Java-Kompatibilität
- JWT-Aufbau: Header (kid, alg, typ) + Payload (iss, aud, iat, exp) + Signatur
- Manuelle Implementierung des JWT-Generierungsprozesses: PEM parsen → Base64 dekodieren → `PKCS8EncodedKeySpec` → `KeyFactory` → `PrivateKey`
- JJWT Library für JWT-Building und Signierung
- Verständnis des kryptografischen Ablaufs: SHA-256 Hash → RSA-Verschlüsselung mit Private Key → Base64url Encoding

### OAuth2 Authorization Code Flow
- Vollständige Implementierung des OAuth2 Flows mit Enable Banking
- State-Parameter für CSRF-Schutz
- Callback-Endpoint für Authorization Code Empfang
- Code-gegen-Session-Token Tausch

---

## Open Banking Integration

### Enable Banking API
- Registrierung und Zertifikat-Upload im Enable Banking Control Panel
- JWT-Authentifizierung gegenüber externer API mit selbst-signiertem Zertifikat
- Implementierung des vollständigen AIS (Account Information Service) Flows
- API-Endpoints: `GET /aspsps`, `POST /auth`, `POST /sessions`, `GET /accounts/{uid}/transactions`, `GET /accounts/{uid}/balances`
- Umgang mit API-Eigenheiten: einmalige Account-Daten aus Session-Response, nicht-stabile `transaction_id`, Sandbox vs. Production

### Datensynchronisierung
- Scheduled Polling Service mit intelligentem First-Sync vs. Follow-up-Sync (via `lastSyncedAt`)
- Deterministischer Deduplication-Key aus stabilen Transaktionsfeldern (Datum + Betrag + Währung + Typ)
- Fehlertoleranz: einzelne Account-Fehler brechen nicht den gesamten Sync ab

---

## Messaging — Apache Kafka

### Konzepte
- Topics, Partitions, Consumer Groups
- Partition Key für geordnete Verarbeitung (alle Events eines Users auf derselbe Partition)
- `trusted.packages` für sichere JSON-Deserialisierung

### Setup & Konfiguration
- Docker Compose Setup mit Zookeeper, Kafka Broker und Kafka UI
- Dual-Listener Konfiguration: `PLAINTEXT_HOST://localhost:9092` für lokale Services, `PLAINTEXT://kafka:29092` für Docker-interne Kommunikation
- Kafka UI für visuelles Monitoring von Topics und Messages

---

## Datenbank — PostgreSQL

### Schema & Tabellen
- `bank_sessions` — OAuth Sessions mit Gültigkeitsdatum
- `bank_accounts` — Kontodaten mit Enable Banking UID und `lastSyncedAt`
- `accounts` (Account Service) — gecachte Kontodaten mit aktuellem Saldo
- `transactions` — Transaktionshistorie mit Deduplication
- `categories` — System- und User-Kategorien mit nullable `user_id`

### Besonderheiten
- UUID als Primary Keys für Service-Unabhängigkeit
- `JSONB` für flexible Datenspeicherung
- Automatische Timestamps mit PostgreSQL Trigger-Funktionen
- Separate DB-Schemas pro Service (Database-per-Service Pattern)

---

## DevOps & Tooling

### Docker & Docker Compose
- Multi-Service Docker Compose Setup: PostgreSQL, Kafka, Zookeeper, Kafka UI
- Environment Variables für Secrets (nie im Code)
- Health Checks und Service-Dependencies
- Volume-Mounts für Datenpersistenz

### Entwicklungswerkzeuge
- ngrok für lokale HTTPS Callback-URLs (Enable Banking OAuth)
- Postman für API-Testing und manuellen Flow-Durchlauf
- Kafka UI für Event-Monitoring
- IntelliJ mit Spring Boot Devtools

### Sicherheitspraktiken
- Private Keys niemals in Git (`.gitignore`)
- Secrets in separaten Properties-Dateien
- Umgebungsvariablen für sensible Konfiguration

---

## Software-Designprinzipien

### Clean Architecture
- Controller → Service → Repository Schichtentrennung
- Thin Controller: keine Business-Logik im Controller
- Interface-Segregation: `IOpenBankingService`, `ISchedulerService`, `IKafkaConsumer`
- Separate Klassen für Infrastruktur (KafkaPublisher, KafkaConsumer) und Business-Logik (Service)

### Best Practices
- DTO-Pattern: klare Trennung zwischen API-DTOs, Events und Entities
- `@NoArgsConstructor` + `@AllArgsConstructor` bei `@Builder` Klassen für Jackson-Kompatibilität
- `@Enumerated(EnumType.STRING)` für lesbare DB-Werte
- Lombok für Boilerplate-Reduktion (`@Data`, `@Builder`, `@Slf4j`, `@RequiredArgsConstructor`)
- Null-Absicherung bei externen API-Responses
- Deterministisches Hashing statt zufälliger UUIDs für Deduplication

### Service-Kommunikation
- Synchron (HTTP) für User-initiierte Requests die sofortige Antwort erwarten
- Asynchron (Kafka) für Hintergrundprozesse und Service-Notifications
- Jeder Service kennt nur seine eigene DB — keine direkten DB-Zugriffe über Service-Grenzen