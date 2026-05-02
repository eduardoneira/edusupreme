# Local Development

This project uses Docker Compose for local infrastructure. The first local dependency is PostgreSQL for the future `tournament-service`.

## Prerequisites

- Docker Desktop or another Docker Engine setup.
- Docker Compose v2, available through `docker compose`.

## PostgreSQL

The PostgreSQL service is defined in `infra/docker/compose.yml`.

Start it from the repository root:

```bash
docker compose -f infra/docker/compose.yml up -d postgres
```

Check status:

```bash
docker compose -f infra/docker/compose.yml ps
```

View logs:

```bash
docker compose -f infra/docker/compose.yml logs -f postgres
```

Stop it:

```bash
docker compose -f infra/docker/compose.yml down
```

Stop it and remove the local database volume:

```bash
docker compose -f infra/docker/compose.yml down -v
```

## Local Database Defaults

| Setting | Value |
| --- | --- |
| Host | `localhost` |
| Port | `5432` |
| Database | `edusupreme` |
| User | `edusupreme` |
| Password | `edusupreme_dev_password` |
| JDBC URL | `jdbc:postgresql://localhost:5432/edusupreme` |

These credentials are for local development only. For local overrides, copy `infra/docker/.env.example` to `infra/docker/.env`. The `.env` file is ignored by git.
