#!/usr/bin/env bash
set -euo pipefail

REPO="${REPO:-eduardoneira/edusupreme}"

require_tool() {
  if ! command -v "$1" >/dev/null 2>&1; then
    echo "Missing required command: $1" >&2
    exit 1
  fi
}

require_tool gh

if ! gh repo view "$REPO" >/dev/null 2>&1; then
  cat >&2 <<'EOF'
Could not access the target repo with GitHub CLI.

Make sure your fine-grained token is available, for example:

  export GH_TOKEN="your_fine_grained_token"

The token should be limited to eduardoneira/edusupreme with:
- Metadata: read
- Issues: read and write
EOF
  exit 1
fi

ensure_label() {
  local name="$1"
  local color="$2"
  local description="$3"

  if gh label list -R "$REPO" --search "$name" --json name --jq '.[].name' | grep -Fxq "$name"; then
    gh label edit "$name" -R "$REPO" --color "$color" --description "$description" >/dev/null
  else
    gh label create "$name" -R "$REPO" --color "$color" --description "$description" >/dev/null
  fi
}

ensure_milestone() {
  local title="$1"
  local description="$2"

  if gh api "repos/$REPO/milestones?state=all" --jq ".[] | select(.title == \"$title\") | .title" | grep -Fxq "$title"; then
    echo "Milestone exists: $title"
  else
    gh api -X POST "repos/$REPO/milestones" -f title="$title" -f description="$description" >/dev/null
    echo "Created milestone: $title"
  fi
}

issue_exists() {
  local title="$1"
  TITLE="$title" gh issue list -R "$REPO" --state all --search "\"$title\" in:title" --json title --jq '.[] | select(.title == env.TITLE) | .title' | grep -Fxq "$title"
}

create_issue() {
  local title="$1"
  local milestone="$2"
  local labels="$3"
  local body="$4"

  if issue_exists "$title"; then
    echo "Issue exists: $title"
    return
  fi

  local body_file
  body_file="$(mktemp)"
  printf '%s\n' "$body" > "$body_file"
  gh issue create -R "$REPO" --title "$title" --body-file "$body_file" --milestone "$milestone" --label "$labels" >/dev/null
  rm -f "$body_file"
  echo "Created issue: $title"
}

echo "Creating labels in $REPO..."
ensure_label "area:angular" "c2e0ff" "Angular frontend work."
ensure_label "area:spring" "bfdadc" "Spring Boot backend work."
ensure_label "area:security" "d93f0b" "Authentication, authorization, and secure defaults."
ensure_label "area:infra" "5319e7" "Docker, CI, deployment, and local infrastructure."
ensure_label "area:kafka" "fbca04" "Kafka, async events, and messaging."
ensure_label "area:testing" "0e8a16" "Unit, integration, contract, and e2e tests."
ensure_label "area:docs" "006b75" "Documentation and planning."
ensure_label "type:feature" "a2eeef" "User-facing or system-facing feature."
ensure_label "type:learning" "fef2c0" "Task primarily designed to practice a technology."
ensure_label "type:bug" "d73a4a" "Bug or incorrect behavior."
ensure_label "type:refactor" "ededed" "Code structure improvement without behavior change."
ensure_label "type:chore" "f9d0c4" "Project maintenance or setup."
ensure_label "size:xs" "d4c5f9" "Tiny task, usually under one focused session."
ensure_label "size:s" "b4a7d6" "Small task with limited scope."
ensure_label "size:m" "8e7cc3" "Medium task that may need decomposition."
ensure_label "priority:p0" "b60205" "Do first or unblocker."
ensure_label "priority:p1" "fbca04" "Important next work."
ensure_label "priority:p2" "c5def5" "Useful but not immediate."

echo "Creating milestones..."
ensure_milestone "M0 - Foundation" "Repository, local tooling, and first application skeletons."
ensure_milestone "M1 - First Vertical Slice" "Create and list tournaments through API-first Spring and Angular."
ensure_milestone "M2 - Angular App Experience" "Routing, lazy loading, forms, UI states, and e2e coverage."
ensure_milestone "M3 - Security" "OIDC login, JWT resource servers, roles, route guards, and gateway protection."
ensure_milestone "M4 - Tournament Domain" "Players, registrations, categories, brackets, scheduling, and results."
ensure_milestone "M5 - Microservices Split" "Separate services, separate databases, gateway routing, and resilience."
ensure_milestone "M6 - Kafka & Events" "Domain events, consumers, read models, retries, and live updates."
ensure_milestone "M7 - Cloud Readiness" "Containers, health checks, observability, Kubernetes, and CI/CD readiness."

echo "Creating M0 issues..."
create_issue "M0: Define repository structure" "M0 - Foundation" "area:docs,type:learning,size:xs,priority:p0" "## Goal
Define the initial monorepo layout for the table tennis platform.

## Acceptance Criteria
- The repo has top-level folders for apps, contracts, infra, docs, and scripts.
- The README briefly explains the learning goal and architecture direction.
- The structure supports Angular, Spring services, OpenAPI contracts, and Docker-based local infrastructure.

## Technology Practiced
Repository organization, monorepo conventions, architecture communication.

## Notes / Resources
Keep this lightweight. Do not scaffold all services yet."

create_issue "M0: Choose LTS-oriented platform versions" "M0 - Foundation" "area:docs,type:learning,size:xs,priority:p0" "## Goal
Document the initial technology versions for Java, Spring Boot, Angular, Node, and database tooling.

## Acceptance Criteria
- README or docs list the chosen versions.
- The choices prefer LTS or current stable versions suitable for a learning project.
- Any version tradeoffs are captured briefly.

## Technology Practiced
Version strategy, dependency lifecycle awareness, cloud-first planning.

## Notes / Resources
Prefer Java 21 LTS and an actively supported Angular/Spring line."

create_issue "M0: Add Docker Compose with PostgreSQL" "M0 - Foundation" "area:infra,type:learning,size:s,priority:p1" "## Goal
Create local infrastructure for the first backend service.

## Acceptance Criteria
- Docker Compose starts PostgreSQL for local development.
- Database name, user, and password are documented for local use.
- A health check confirms PostgreSQL readiness.
- No application service depends on Kafka or Keycloak yet.

## Technology Practiced
Docker Compose, local development infrastructure, service health checks.

## Notes / Resources
Keep optional future services out of the default Compose path."

create_issue "M0: Scaffold tournament-service Spring Boot app" "M0 - Foundation" "area:spring,type:learning,size:s,priority:p1" "## Goal
Create the first Spring Boot service for tournament management.

## Acceptance Criteria
- The service starts locally.
- A health endpoint is available.
- The app has a basic package structure for API, application, domain, and persistence.
- A README section documents how to run it.

## Technology Practiced
Spring Boot, Actuator, Gradle or Maven, local run workflow.

## Notes / Resources
Do not implement tournament CRUD in this task."

create_issue "M0: Scaffold Angular web app" "M0 - Foundation" "area:angular,type:learning,size:s,priority:p1" "## Goal
Create the Angular frontend shell.

## Acceptance Criteria
- Angular app starts locally.
- The first screen shows an application shell suitable for the tournament platform.
- Basic lint/build commands are documented.
- The app is ready for feature routes.

## Technology Practiced
Angular CLI, standalone components, app shell, local frontend workflow.

## Notes / Resources
No backend integration yet."

create_issue "M0: Add basic CI checks" "M0 - Foundation" "area:infra,type:learning,size:s,priority:p2" "## Goal
Add GitHub Actions checks for the initial backend and frontend.

## Acceptance Criteria
- CI runs on pull requests.
- Backend build/test command is included when the backend exists.
- Frontend build/test command is included when the frontend exists.
- The workflow avoids secrets and external deployment.

## Technology Practiced
GitHub Actions, continuous integration, build hygiene.

## Notes / Resources
This may follow the first app scaffolds."

echo "Creating M1 issues..."
create_issue "M1: Write tournament OpenAPI contract" "M1 - First Vertical Slice" "area:spring,type:learning,size:s,priority:p0" "## Goal
Define the first API-first contract for tournaments.

## Acceptance Criteria
- OpenAPI spec defines create tournament request and response.
- OpenAPI spec defines list tournaments response.
- Validation expectations are documented in schema fields where practical.
- The contract lives under contracts/openapi.

## Technology Practiced
OpenAPI, API-first design, REST resource modeling.

## Notes / Resources
Keep the first contract intentionally small."

create_issue "M1: Implement POST /tournaments" "M1 - First Vertical Slice" "area:spring,type:feature,size:s,priority:p0" "## Goal
Allow creating tournaments through the backend API.

## Acceptance Criteria
- Valid requests create a tournament.
- Invalid name or date input returns 400.
- Tournament data is persisted in PostgreSQL.
- Integration tests cover success and validation failure.

## Technology Practiced
Spring MVC, Bean Validation, JPA, Flyway, Testcontainers.

## Notes / Resources
Prefer simple fields first: name, start date, end date, status."

create_issue "M1: Implement GET /tournaments" "M1 - First Vertical Slice" "area:spring,type:feature,size:xs,priority:p0" "## Goal
Allow listing tournaments through the backend API.

## Acceptance Criteria
- Endpoint returns tournaments ordered predictably.
- Empty database returns an empty list.
- Integration test covers the endpoint.
- Response shape matches the OpenAPI contract.

## Technology Practiced
Spring MVC, persistence queries, API response DTOs.

## Notes / Resources
Pagination can wait unless it is trivial."

create_issue "M1: Add Flyway schema for tournaments" "M1 - First Vertical Slice" "area:spring,type:learning,size:xs,priority:p1" "## Goal
Manage tournament database schema through migrations.

## Acceptance Criteria
- Flyway migration creates the tournament table.
- Local service startup applies migrations.
- Migration naming follows a consistent convention.
- Tests use the migration instead of ad hoc schema setup.

## Technology Practiced
Flyway, relational schema design, migration discipline.

## Notes / Resources
Use PostgreSQL-oriented types where useful."

create_issue "M1: Generate Angular API client from OpenAPI" "M1 - First Vertical Slice" "area:angular,type:learning,size:s,priority:p1" "## Goal
Connect Angular to the backend through generated API types and client code.

## Acceptance Criteria
- OpenAPI generator command is documented.
- Generated client code is isolated from handwritten UI code.
- Angular can call the tournament list endpoint through the generated client.
- The generation workflow is repeatable.

## Technology Practiced
OpenAPI Generator, Angular HttpClient, typed API contracts.

## Notes / Resources
Generated files should not become hand-edited business logic."

create_issue "M1: Build tournament list page" "M1 - First Vertical Slice" "area:angular,type:feature,size:s,priority:p1" "## Goal
Show tournaments in the Angular app.

## Acceptance Criteria
- Page fetches tournaments from the backend.
- Loading, empty, and error states are visible.
- Tournament rows show name, dates, and status.
- The route is ready for a future detail page.

## Technology Practiced
Angular routing, HttpClient integration, signals or RxJS, UI state handling.

## Notes / Resources
Keep styling functional and focused."

create_issue "M1: Build create tournament form" "M1 - First Vertical Slice" "area:angular,type:feature,size:s,priority:p1" "## Goal
Create tournaments from the Angular app.

## Acceptance Criteria
- Form captures name, start date, and end date.
- Client-side validation prevents obvious invalid input.
- Successful submit creates a tournament and returns to the list.
- Backend validation errors are displayed clearly.

## Technology Practiced
Angular Reactive Forms, validation, generated API client, navigation.

## Notes / Resources
This is the first complete user workflow."

create_issue "M1: Add first end-to-end tournament flow test" "M1 - First Vertical Slice" "area:testing,type:learning,size:s,priority:p2" "## Goal
Verify the first complete create-and-list workflow from the browser.

## Acceptance Criteria
- E2E test opens the Angular app.
- Test creates a tournament.
- Test verifies the tournament appears in the list.
- Test setup documents any required local services.

## Technology Practiced
Playwright, e2e testing, local full-stack verification.

## Notes / Resources
This can be added after the UI and API are both running."

echo "Done."
