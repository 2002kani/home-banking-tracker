# Real Banking Manager (WIP)

A personal finance management backend that connects to real bank accounts via the Enable Banking Open Banking API. The system performs automated OAuth2 authorization flows with RSA-signed JWT authentication, periodically syncs real transaction data and account balances through a scheduled polling service, and distributes the data across microservices via Apache Kafka. Users can view their transaction history, monitor account balances, and categorize spending — all sourced from their actual bank. Not production-ready yet. Built as a portfolio project to explore microservice architecture and Open Banking integration.

## Microservice Architecture Overview 

![Backend Architecture](github/backend-architecture.png)


## Services

### Open Banking Service

The core integration layer that connects the system to real bank accounts via the [EnableBanking API](https://enablebanking.com).

**Authorization Flow**
- Fetches available banks (ASPSPs) from EnableBanking, optionally filtered by country
- Initiates the OAuth2-like authorization flow and redirects the user to their bank
- Handles the callback with `code` + `state`, exchanges it for a session, and persists session and account data (IBAN, currency, name) in PostgreSQL
- Sessions are valid for 90 days

**Scheduled Sync**
- Periodically polls all active bank sessions for transactions and balances
- First sync: fetches the last 90 days of transactions; subsequent syncs: last 1 day
- Publishes `TransactionRawEvent` and `AccountUpdateEvent` to Kafka for downstream services

**JWT Authentication**
- Every EnableBanking API call is authenticated with an RS256-signed JWT
- The private RSA key is loaded from a PEM file at startup

**Kafka Events published**

| Event | Description |
|---|---|
| `AccountUpdateEvent` | Account metadata and current balance — consumed by Account Service |
| `TransactionRawEvent` | Raw transaction data — consumed by Transaction Service |

---


## Tech Stack
- Java (Spring Boot)
- Apache Kafka
- PostgreSQL
- Hibernate
- Flyway
- Docker
- JWT
- Spring Cloud Gateway
- ngrok


## Personal note
The backend implementation may be somewhat overengineered at this stage. It could definitely be built much more simpler without many of the components currently in use. However, this is primarily a learning project for myself focusing on fundamentals and getting deeper in topics such as JWT-based authentication, Kafka queues, and communication between microservices. In a prettier world the application could also be structured like this:

![Architecture Minimal](github/architecture-minimal.png)


