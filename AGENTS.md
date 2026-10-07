# WayraPass - Codex Instructions

## Before making changes

Before modifying this repository:

1. Read `CONTEXT.md`.
2. Inspect `database/wayrapass_db.sql`.
3. Inspect the existing `wayrapass/` Spring Boot project.
4. Work on the existing project. Do not create a replacement project.

## Sources of truth

When information conflicts, use this precedence:

1. `database/wayrapass_db.sql` for the physical PostgreSQL schema.
2. `CONTEXT.md` for WayraPass domain rules and project decisions.
3. Architecture and patterns taught in Software Engineering laboratories.
4. Existing implementation.

Do not arbitrarily redesign the database.

## Backend architecture

Keep the architecture aligned with the course laboratories:

Controller
-> DTO / Validation
-> Service
-> Repository
-> JPA Entity
-> PostgreSQL

Main packages should remain organized around:

- controller
- dto.request
- dto.response
- exception
- mapper
- model
- repository
- security
- service

Business logic belongs in services, not controllers.

## Course patterns

The implementation must preserve the progression taught in the laboratories:

### Week 4
- Spring Data JPA
- PostgreSQL
- entity relationships
- repositories
- services
- controllers
- REST CRUD

### Week 5
- request/response DTOs
- MapStruct
- Jakarta Validation
- GlobalExceptionHandler
- domain exceptions
- transactions
- concurrency control when justified

### Week 6
- Spring Security
- BCrypt
- JWT with JJWT
- JwtAuthenticationFilter
- UserDetailsService / UserPrincipal
- SecurityContext
- CurrentUser
- @PreAuthorize
- role and resource ownership authorization

## WayraPass invariants

The valid roles are exactly:

- STUDENT
- FAMILY
- DRIVER
- COORDINATOR

Do not introduce ADMIN.

STUDENT and FAMILY may register publicly.

DRIVER and COORDINATOR must be created or enabled administratively.

Do not create separate FamilyMember or Coordinator tables unless the SQL schema explicitly contains them.

The SQL schema is authoritative for table names, columns, nullability, foreign keys, uniqueness, checks and lengths.

Use Hibernate schema validation rather than allowing Hibernate to silently redesign the schema.

## Scope

Focus on the backend.

Do not:
- implement the frontend;
- implement the full WayrIA/LLM integration;
- replace the PostgreSQL schema;
- maintain two authentication systems;
- expose JPA entities directly from REST endpoints;
- identify the authenticated user through client-provided user IDs when the JWT already identifies them;
- perform Postman evidence work.

## Validation

After meaningful changes, run tests when practical.

Before considering the task complete, run from the Maven module:

```bash
./mvnw clean test
./mvnw clean package
```

Do not claim completion while the project has compilation or test failures.

## Final report

At the end report:

- major changes;
- implemented entities/modules;
- available endpoints;
- roles and permissions;
- relevant business rules;
- tests/build results;
- unresolved issues, if any.

Postman testing will be performed manually by the user after implementation.
