# Tournament Service

Spring Boot service for tournament management.

This scaffold intentionally contains only the application entry point, Actuator health, and the initial package structure. Tournament CRUD belongs to later vertical-slice issues.

## Prerequisites

- Java 21 or newer.
- Gradle 8.14 or newer, or Gradle 9.x.

## Run Locally

From this directory:

```bash
gradle bootRun
```

The service starts on port `8081`.

Check health:

```bash
curl http://localhost:8081/actuator/health
```

Expected response:

```json
{"groups":["liveness","readiness"],"status":"UP"}
```

## Test

```bash
gradle test
```

## Package Structure

```text
com.edusupreme.tournament
  api/          HTTP-facing inbound adapter
  application/  use cases, ports, and application orchestration
  domain/       business concepts, rules, and invariants
  persistence/  outbound persistence adapter and database mapping
  config/       Spring and infrastructure configuration
```

See [../../docs/architecture/hexagonal-architecture.md](../../docs/architecture/hexagonal-architecture.md) for the full backend package shape expected once the first vertical slice adds implementation classes.
