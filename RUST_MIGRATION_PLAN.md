<!--
SPDX-FileCopyrightText: 2023-2025 Dom Rodriguez (shymega) <shymega@shymega.org.uk>

SPDX-License-Identifier: AGPL-3.0-only
-->

# Rust Migration Plan — Verma Server

Planning document for migrating `verma-server` from Spring Boot/Java to a
Rust microservice. Companion to `AGENT_RUST.md` (agent instructions for doing
the work) and `ARCHITECTURE_REVIEW_JAVA.md` (what exists today).

**Starting premise:** the Java codebase is a domain-modelling prototype, not
a working API (see `ARCHITECTURE_REVIEW_JAVA.md` §8 — no controllers, minimal
services, tests disabled). That is actually favourable for a rewrite: there is
a real, fairly well-thought-out domain model to port faithfully, but very
little request-handling behaviour that must be reverse-engineered. Treat the
JPA entities and enums as the specification; do not treat the two-and-a-half
existing services as the target behaviour to preserve — they have known bugs
(see below) that should be *fixed*, not ported.

## 1. Goals and non-goals

**Goals**
- Reproduce the domain model (entities, enums, state-event auditing,
  optimistic locking) faithfully in Rust with equivalent or stronger
  guarantees.
- Build the API layer that Java never got: a real HTTP interface over the
  domain, since that's the actual gap versus a rewrite-for-its-own-sake.
  Frame this as new work, not "migration," since nothing exists to migrate.
  from on that front.
- Preserve the Postgres-first, H2-fallback-for-dev-only intent, but consider
  whether an embedded dev database is still needed in Rust (SQLite via
  `sqlx` is the natural analogue if a zero-setup dev mode is wanted).
- Keep it deployable the same ways: Docker/OCI image, Nix package (the repo
  already has strong Nix infrastructure — reuse the pattern, not the Java
  build steps).
- Fix the known Java bug where `RepairService` ignores the `evt` argument and
  hard-codes `REGISTERED` when creating state events.

**Non-goals (for the initial migration)**
- Feature parity with a UI — there isn't one to be compatible with yet.
- Microservice *decomposition* beyond "one Rust service replacing one Java
  service." Splitting into multiple services (e.g. separate PAT-testing or
  reporting services) is a later concern once there's a single working
  service to observe in production. Don't over-design service boundaries
  before there's traffic to justify them.
- Real-time/WebSocket features — `spring-boot-starter-websocket` was on the
  classpath but nothing used it; don't invent requirements for it now.

## 2. Target stack

| Concern | Choice | Why |
|---|---|---|
| Web framework | `axum` | Tower-based, good ecosystem fit, ergonomic extractors map cleanly onto "controller" responsibilities Java never wrote |
| Async runtime | `tokio` | De facto standard, required by axum/sqlx |
| DB access | `sqlx` (Postgres) | Compile-time checked queries close the gap left by Java's in-memory `findAll()+filter` anti-pattern — push filtering into SQL from day one |
| Migrations | `sqlx-cli` / `refinery` | Explicit, versioned migrations — a deliberate improvement over Hibernate `ddl-auto=update`, which is unsuitable for a real migration story |
| Serialization | `serde` + `serde_json` | Direct analogue to Jackson usage on entities |
| Validation | `validator` crate | Analogue to `hibernate-validator` (`@NotEmpty`, `@Email`) |
| Error handling | `thiserror` (domain errors) + `anyhow` (app-level) | Replace Java's checked/unchecked exception mix with explicit `Result<T, E>` |
| Config | `figment` or plain env vars + `serde` | Mirror the `DATABASE_URL` / `spring.datasource.url` fallback logic from `ApplicationConfiguration` |
| Logging/tracing | `tracing` + `tracing-subscriber` | Structured logging analogue to SLF4J usage (`@Slf4j`) |
| PDF generation | `printpdf` or shell out to `weasyprint`/`typst` | Only needed once the ticket-report feature is actually built; Java never finished this either (template exists, no renderer wired) — don't block on it |
| Rate limiting | `tower_governor` or `governor` crate | Analogue to unused `bucket4j-core` |
| Testing | `cargo test` + `sqlx::test` + `testcontainers-rs` | Real integration tests against Postgres — the one thing Java conspicuously skipped (`skipTests=true`, one no-op test) |

## 3. Domain model mapping

Port faithfully from `src/main/java/.../db/entities/` and `enums/`. One Rust
module per aggregate is reasonable, e.g. `domain::ticket`, `domain::repair`.

- **Entities → structs.** UUID primary keys stay UUIDs (`uuid` crate).
  `BaseEntity`'s `@Version` optimistic-locking field becomes an explicit
  `version: i32` column checked in `UPDATE ... WHERE id = $1 AND version = $2`
  statements — sqlx makes this an explicit, visible pattern rather than
  Hibernate's implicit interceptor magic. Treat that explicitness as a
  feature, not a downside, when documenting the port.
- **Enums → Rust `enum`s** with `sqlx::Type` derives mapping to Postgres
  native enum types (a real improvement over the Java `AttributeConverter`
  boilerplate — one converter class per enum). Carry over every variant
  listed in `AGENT_JAVA.md` §2 exactly; these are the actual domain vocabulary.
- **State events stay append-only.** This is the one architectural pattern
  from the Java side worth preserving deliberately: never `UPDATE` a status
  column, always `INSERT` a `*_state_events` row with a timestamp. Model it
  as a dedicated repository method (`record_transition`), not as a side
  effect buried in an unrelated service method — that's exactly the bug that
  needs *not* to be repeated (Java's `RepairService.createTicketEvent`
  ignoring its own argument).
- **Relationships:** `ManyToOne`/`OneToMany`/`ManyToMany` become explicit
  foreign keys plus join tables, loaded via explicit `sqlx` queries. No lazy
  loading, no N+1 surprises to inherit — every load is an intentional query.
- **`@PrePersist` derived fields** (`Cafe.cafeShortId`, `Ticket.associatedItems`)
  become constructor logic or a `before_insert` step at the repository
  boundary — not hidden lifecycle callbacks. Also: fix the existing off-by-one
  (comment says "four chars," code takes three) while porting, don't carry
  the bug forward silently — note the fix in the PR description.

## 4. Layering

Java has data + a sliver of service layer and nothing else. The Rust service
should have all the layers Java was missing, from the start:

```
src/
  domain/       # structs, enums, invariants — pure, no I/O
  repository/   # sqlx queries, one module per aggregate
  service/      # business logic: VisitorService, VolunteerService,
                #   RepairService, + the CafeService/PatTestService
                #   Java never wrote
  api/          # axum routers/handlers, request/response DTOs
  config.rs     # datasource resolution (env fallback chain)
  main.rs
```

Push filtering (banned visitors, active volunteers by skill) into SQL
`WHERE` clauses in `repository/`, not into `service/` as in-memory
`.filter()` chains — that in-memory approach is a named problem in
`ARCHITECTURE_REVIEW_JAVA.md` §4 and should not reappear.

## 5. Migration phases

1. **Phase 0 — Scaffolding.** Cargo workspace, `axum` skeleton with a health
   endpoint, `sqlx` connection pool wired to the same env-var fallback chain
   (`DATABASE_URL` → explicit config → local Postgres/dev default), CI
   pipeline mirroring `.github/workflows/*` intent (build, test, lint,
   container image, Nix build) adapted to `cargo`/`clippy`/`rustfmt`.
2. **Phase 1 — Schema & migrations.** Write explicit `sqlx` migrations
   reproducing the current Hibernate-managed schema (tables, enum types,
   FKs, unique constraints from `Cafe`'s `cafeName`/`cafeWebsite`/
   `cafeContactEmail`). This is the first real artifact — Java never had
   versioned migrations (`ddl-auto=update`), so this is new infrastructure,
   not a straight port.
3. **Phase 2 — Domain + repositories.** Port entities/enums per §3. Write
   repository methods with real `WHERE`/`JOIN` queries replacing Java's
   `findAll()+stream()` pattern. Unit-test repositories against a real
   Postgres via `testcontainers-rs`.
4. **Phase 3 — Services.** Port `VisitorService`, `VolunteerService`,
   `RepairService` behaviour (ban/unban, enable/disable, skill filtering,
   state-event creation — fixing the ignored-`evt` bug), and *add*
   `CafeService`/`PatTestService`, which Java never had.
5. **Phase 4 — API layer.** Build the axum routers/handlers this project has
   never had: CRUD + workflow endpoints for visitors, volunteers, cafés,
   tickets, repairs, PAT tests. Define request/response DTOs distinct from
   domain structs from day one (Java's mistake to avoid was never making this
   distinction, because it never got this far).
6. **Phase 5 — Cross-cutting.** Validation (`validator`), rate limiting,
   structured tracing/logging, OpenAPI docs (`utoipa`, analogue to
   `springdoc-openapi`), auth (Java left `spring-boot-starter-security`
   commented out — decide deliberately here rather than deferring again).
7. **Phase 6 — Packaging & cutover.** Dockerfile + Nix package/OCI image
   following the existing `nix/package.nix`/`nix/oci.nix` patterns but for a
   Rust binary (likely simpler — no Maven FOD hash dance, just
   `buildRustPackage`/`crane`). Retire the Java build once the Rust service
   has parity on the ported subset plus the new API surface.

## 6. Open questions to resolve before Phase 1

- Single service now, or split reporting/PAT-testing out early? (Recommendation:
  single service — see non-goals.)
- SQLite-for-dev-via-`sqlx` to replace H2, or just require Postgres locally
  (Nix devenv can provide it trivially)? Given the existing Nix/devenv
  infrastructure, requiring real Postgres in dev is plausible and removes a
  whole class of H2-vs-Postgres SQL-dialect divergence bugs.
- Auth strategy — deferred in Java; needs an actual decision this time
  (session-based, JWT, or OAuth2 proxy in front).
- Whether `CafeSession` (currently a near-empty entity) needs real behaviour
  defined, or is still a placeholder — check with the domain owner before
  building it out further than Java did.

## 7. What "done" looks like for the initial migration

- All entities/enums from `AGENT_JAVA.md` §2 ported with equivalent invariants.
- All three existing services ported with their bugs fixed, plus the two
  missing services (`Cafe`, `PatTest`) built out.
- A real HTTP API exists (the actual milestone this project has never
  reached).
- Migrations are versioned and reproducible; tests actually run in CI
  (unlike `skipTests=true` today).
- Docker + Nix packaging both build a working Rust binary/image.
