# Real Banking Manager

A personal finance management backend that connects to real bank accounts via the Enable Banking Open Banking API. The system performs automated OAuth2 authorization flows with RSA-signed JWT authentication, periodically syncs real transaction data and account balances through a scheduled polling service, and distributes the data across microservices via Apache Kafka. Users can view their transaction history, monitor account balances, and categorize spending — all sourced from their actual bank. Not production-ready yet. Built as a portfolio project to explore microservice architecture and Open Banking integration.

![Backend Architecture](github/backend-architecture.png)


**Personal note:** The backend implementation may be somewhat overengineered at this stage. It could definitely be built much more simpler without many of the components currently in use. However, this is primarily a learning project for myself focusing on fundamentals and getting deeper in topics such as JWT-based authentication, Kafka queues, and communication between microservices. In a prettier world the application could also be structured like this:

![Architecture Minimal](github/architecture-minimal.png)
