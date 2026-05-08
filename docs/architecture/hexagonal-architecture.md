# Hexagonal Architecture

Hexagonal architecture is the default shape for backend services in this project.

The goal is to keep domain and application logic independent from delivery mechanisms, persistence frameworks, messaging tools, and cloud infrastructure. Spring, JPA, Kafka, HTTP, and generated code are useful tools, but they belong at the edges of a service.

## Dependency Rule

Dependencies point inward.

```text
adapter -> application -> domain
```

Outer packages may depend on inner packages. Inner packages must not depend on outer packages.

- `domain` must not depend on Spring, JPA, Kafka, web DTOs, database entities, generated API classes, or application ports.
- `application` may depend on the domain and its own ports, but not on adapter implementations or framework-specific DTOs.
- `adapter` packages may depend on application ports and use cases. They translate between external tools and the application model.
- `config` wires concrete adapters to application use cases and keeps framework setup outside the core.

If a class imports a framework because of how data enters or leaves the service, it probably belongs in an adapter or configuration package.

## Service Package Shape

Each Spring service should follow this package shape unless a specific service has a documented reason to differ.

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

Small services do not need every package on day one. Create a package when the first vertical slice needs it, then keep new code inside the same shape. The initial `tournament-service` scaffold starts with coarse package markers only:

```text
com.edusupreme.tournament
  api/          maps to adapter.in.web
  application/  maps to application
  domain/       maps to domain
  persistence/  maps to adapter.out.persistence
  config/       maps to config
```

When implementation classes are added, prefer the more explicit package names from the service shape above if the coarse package would become unclear.

## Responsibilities

| Package | Responsibility |
| --- | --- |
| `domain.model` | Entities, value objects, enums, domain events, and invariant checks. |
| `domain.service` | Pure domain operations that do not naturally belong to one model object. |
| `application.port.in` | Use case contracts called by inbound adapters. These describe what the service can do. |
| `application.port.out` | Capabilities required from outside the application core, such as repositories, event publishers, clocks, or external clients. |
| `application.usecase` | Application orchestration, command/query handling, transaction boundaries, and calls to outbound ports. |
| `adapter.in.web` | REST controllers, request/response DTOs, HTTP validation, and mapping into inbound port commands or queries. |
| `adapter.out.persistence` | JPA entities, Spring Data repositories, database mappers, and implementations of persistence outbound ports. |
| `adapter.out.messaging` | Kafka producers/consumers, message DTOs, event mapping, and messaging port implementations. |
| `config` | Spring bean wiring, infrastructure configuration, security setup, and cross-cutting framework configuration. |

## Ports And Use Cases

Use inbound ports when an adapter needs to call application behavior. A web controller should call an inbound port such as `CreateTournamentUseCase` instead of reaching into domain objects or repositories directly.

Use outbound ports when application behavior needs an external capability. A use case should depend on a port such as `TournamentRepository` or `TournamentEventPublisher`, while the persistence or messaging adapter provides the implementation.

Avoid creating ports just to make every class look symmetrical. Add a port when there is a real boundary to protect, an external dependency to hide, or a test seam that improves confidence in application behavior.

## Practical Rules

- Keep controllers thin: validate HTTP input, map it, call one inbound port, and map the result.
- Keep persistence entities out of the domain model.
- Keep generated OpenAPI classes at the web boundary unless a generated type is explicitly intended as an external contract.
- Prefer explicit mappers at boundaries over leaking adapter DTOs inward.
- Put transaction handling in application use cases or configuration, not in domain objects.
- Use Spring annotations mostly in adapters, configuration, and use case implementations.
- Do not add abstractions before the first vertical slice needs them.

## Testing Guidance

Tests should match the layer being protected.

| Test type | Scope | Guidance |
| --- | --- | --- |
| Domain tests | `domain` | Plain unit tests with no Spring context. Cover invariants, value object rules, state transitions, and domain services. |
| Application tests | `application.usecase` | Exercise use cases with fake or mocked outbound ports. Verify orchestration, error handling, and transaction-level decisions without HTTP or database setup. |
| Adapter tests | `adapter.in` and `adapter.out` | Verify framework integration and mapping. Use MVC tests for web adapters, persistence slice tests for database adapters, and messaging tests for serialization and topic contracts. |
| Integration tests | Full service | Prove that Spring wiring, ports, adapters, configuration, database migrations, and key workflows work together. Keep these fewer and focused on important paths. |

Start with domain and application tests for business rules, then add adapter and integration tests when a slice touches HTTP, persistence, messaging, or service wiring.

## First Service Target

The first backend service, `tournament-service`, should follow this architecture from the start. Its first vertical slice should prove the structure with one simple workflow:

```text
POST /tournaments
  -> adapter.in.web
  -> application.port.in.CreateTournamentUseCase
  -> application.usecase.CreateTournamentHandler
  -> domain.model.Tournament
  -> application.port.out.TournamentRepository
  -> adapter.out.persistence
```

The first implementation can keep names smaller if the workflow is still small, but the dependency direction should stay the same.
