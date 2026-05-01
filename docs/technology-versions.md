# Technology Versions

This project favors stable, widely supported versions that are common in modern Java and Angular work.

## Initial Choices

| Area | Choice | Why |
| --- | --- | --- |
| Java | Java 21 LTS | Strong baseline for current Spring applications and long-term support. |
| Spring Boot | Spring Boot 4.0.x | Current stable Spring Boot line for new learning work. |
| Spring Cloud | Spring Cloud 2025.1.x | Compatible line for Spring Boot 4.0.x services. |
| Angular | Angular 20 LTS or Angular 21 | Prefer Angular 20 when strict LTS matters; Angular 21 is useful for current-feature learning. |
| Node.js | Node.js LTS | Keep Angular tooling on an LTS runtime. |
| Database | PostgreSQL | Primary relational database for transactional services. |
| Messaging | Kafka | Event-driven practice after the first REST slice is working. |
| Local infra | Docker Compose | Local, reproducible development environment. |

## Version Policy

- Prefer LTS runtimes for Java and Node.js.
- Prefer active or LTS framework versions over preview releases.
- Document version changes before upgrading generated code, build plugins, or service templates.
- Keep the first milestone simple: PostgreSQL only, then add Keycloak, Kafka, and observability as later learning milestones.
