# Edusupreme

Edusupreme is a public, portfolio-facing learning project for building a cloud-oriented table tennis tournament platform with Java, Spring, Angular, and a gradual microservice architecture.

The goal is not to create the biggest system possible on day one. The goal is to practice modern development in small, useful slices: API-first contracts, Spring services, Angular screens, relational persistence, security, Docker, events, tests, and eventually cloud-ready deployment.

Because this repository is meant to be public, the project also emphasizes readable documentation, safe configuration, reproducible local setup, and a clean issue/PR workflow.

## Product Idea

The app will support table tennis tournament organizers, referees, players, and spectators.

Initial capabilities:

- Create and list tournaments.
- Register players.
- Organize categories and divisions.
- Generate round-robin groups and knockout brackets.
- Record match results.
- Show standings, rankings, and live updates.

## Learning Goals

- Rebuild practical Spring Boot fluency.
- Learn Angular through real screens and workflows.
- Practice hexagonal architecture with clear domain, application, and adapter boundaries.
- Practice API-first development with OpenAPI.
- Use PostgreSQL and schema migrations from the start.
- Add security with OIDC/JWT when the first feature slice is working.
- Grow from one backend service into clear service boundaries.
- Introduce Kafka for domain events and read models.
- Keep local development reproducible with Docker Compose.
- Prepare for future cloud deployment without overengineering early.

## Repository Layout

```text
apps/
  api-gateway/
  tournament-service/
  web-angular/
contracts/
  events/
  openapi/
docs/
infra/
  docker/
  k8s/
scripts/
```

## Planned Architecture

```text
Angular Web App
      |
API Gateway
      |
Tournament Service
      |
PostgreSQL
```

Later milestones will add player, match, ranking, and notification services, plus Kafka, Keycloak, observability, and Kubernetes manifests.

## Milestones

- `M0 - Foundation`: repository structure, local tooling, first app skeletons.
- `M1 - First Vertical Slice`: create and list tournaments through Angular and Spring.
- `M2 - Angular App Experience`: routing, forms, states, and e2e testing.
- `M3 - Security`: OIDC login, JWT resource servers, roles, and gateway protection.
- `M4 - Tournament Domain`: players, registrations, brackets, schedules, and results.
- `M5 - Microservices Split`: service boundaries, separate databases, and resilience.
- `M6 - Kafka & Events`: domain events, consumers, read models, retries, and live updates.
- `M7 - Cloud Readiness`: containers, health checks, observability, Kubernetes, and CI/CD.

## Technology Baseline

See [docs/technology-versions.md](docs/technology-versions.md).

## Local Development

Local infrastructure is managed with Docker Compose. See [docs/local-development.md](docs/local-development.md).

Start PostgreSQL from the repository root:

```bash
docker compose -f infra/docker/compose.yml up -d postgres
```

Run the first backend service:

```bash
cd apps/tournament-service
gradle bootRun
```

Check the service health endpoint:

```bash
curl http://localhost:8081/actuator/health
```

## Architecture

Backend services use hexagonal architecture as a guiding constraint. See [docs/architecture/hexagonal-architecture.md](docs/architecture/hexagonal-architecture.md).

## Planning

GitHub Issues and Milestones are the planning system for this project. See [docs/github-planning.md](docs/github-planning.md).

## Public Repo Standards

See [docs/public-repo-standards.md](docs/public-repo-standards.md).
