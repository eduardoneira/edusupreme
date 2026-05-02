# Hexagonal Architecture

Hexagonal architecture is a primary design constraint for backend services in this project.

The goal is to keep domain and application logic independent from delivery mechanisms, persistence frameworks, messaging tools, and cloud infrastructure. Spring, JPA, Kafka, and HTTP are important tools, but they should live at the edges of each service.

## Dependency Rule

Dependencies point inward.

```text
adapters -> application -> domain
```

The domain should not depend on Spring, JPA, Kafka, web DTOs, database entities, or generated API classes.

## Service Package Shape

Each Spring service should follow this shape unless a specific service has a good reason to differ.

```text
com.edusupreme.<service>
  domain/
    model/
    service/
  application/
    port/
      in/
      out/
    usecase/
  adapter/
    in/
      web/
    out/
      persistence/
      messaging/
  config/
```

## Layer Responsibilities

| Layer | Responsibility |
| --- | --- |
| `domain` | Business concepts, rules, value objects, domain services. |
| `application.port.in` | Use case interfaces called by inbound adapters. |
| `application.port.out` | Required capabilities such as repositories, event publishers, or external service clients. |
| `application.usecase` | Application orchestration and transaction boundaries. |
| `adapter.in.web` | REST controllers, request/response DTO mapping, validation boundary. |
| `adapter.out.persistence` | JPA entities, Spring Data repositories, persistence mappers. |
| `adapter.out.messaging` | Kafka producers/consumers and event mapping. |
| `config` | Spring wiring and infrastructure configuration. |

## Testing Strategy

- Domain tests should be plain unit tests with no Spring context.
- Application use case tests should mock or fake outbound ports.
- Adapter tests should verify framework integration, mapping, and persistence behavior.
- Full integration tests should prove that the hexagon is wired correctly.

## Practical Rules

- Keep controllers thin.
- Keep persistence entities out of the domain model.
- Use ports for capabilities the application needs from the outside world.
- Prefer explicit mappers at boundaries.
- Avoid abstraction until there is a real boundary to protect.
- Use Spring annotations mostly in adapters, configuration, and use case implementations.

## First Service Target

The first backend service, `tournament-service`, should use this architecture from the start. The first vertical slice should prove the structure with one simple workflow:

```text
POST /tournaments
  -> web adapter
  -> create tournament use case
  -> tournament domain model
  -> tournament repository port
  -> PostgreSQL persistence adapter
```
