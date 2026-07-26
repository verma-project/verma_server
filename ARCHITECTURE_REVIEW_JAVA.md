<!--
SPDX-FileCopyrightText: 2023-2025 Dom Rodriguez (shymega) <shymega@shymega.org.uk>

SPDX-License-Identifier: AGPL-3.0-only
-->

# Architectural Review — Java / Spring Boot Server

This document reflects the codebase as actually implemented, not an idealised
target. For the authoritative low-level breakdown, see `AGENT_JAVA.md`; this review
is the narrative companion to it.

## 1. Overview

`verma-server` is a **Spring Boot 4.1.0** application on **Java 21**, packaged
as a WAR. It is the backend for the Verma Project — software to run **Repair
Café** events (community sessions where volunteers repair visitors' items).

The codebase is at an **early, skeletal stage**. The domain model (JPA
entities, enums, converters) is fleshed out in reasonable detail. The
service layer is minimal. **There is no web/API layer at all** — no
controllers, no DTOs, no REST or WebSocket endpoints are wired up, despite the
relevant Spring Boot starters being present on the classpath.

## 2. Domain model

The entity model is the strongest part of the codebase. Core aggregates:
`Visitor`, `Volunteer`, `Cafe`, `CafeSession`, `Ticket`, `Repair`, `PatTest`,
plus one append-only `*StateEvent` entity per stateful aggregate
(`CafeStateEvent`, `TicketStateEvent`, `RepairStateEvent`,
`PatTestStateEvent`).

- **Optimistic locking:** `BaseEntity` is a `@MappedSuperclass` carrying a
  JPA `@Version` field, inherited by every entity.
- **Event-sourced-style auditing:** state transitions are recorded as new
  rows in a `*StateEvent` table (enum value + `ZonedDateTime` timestamp)
  rather than by overwriting a status column on the parent entity. This gives
  an append-only, auditable history — the standout architectural decision in
  the codebase so far.
- **Entity conventions:** UUID primary keys (`GenerationType.AUTO`), Lombok
  (`@Data @Builder @AllArgsConstructor @NoArgsConstructor`), `final` classes
  implementing `Serializable`, `@JsonInclude(NON_NULL)`, attribute converters
  in `utils/converters/` for enum persistence and string trimming, and
  `@PrePersist` hooks for derived defaults (e.g. `Cafe.cafeShortId`,
  `Ticket.associatedItems`).

## 3. Persistence layer

Standard Spring Data JPA / Hibernate: one `JpaRepository` per aggregate.
Datasource resolution (`ApplicationConfiguration`) tries, in order: env
`DATABASE_URL` → `spring.datasource.url` property → an in-memory H2 fallback
configured in PostgreSQL-compatibility mode. PostgreSQL is the evident
production target (driver present, runtime scope). `ddl-auto=update` and
`generate-ddl=true` are set — fine for early development, not a long-term
migration strategy.

## 4. Service layer — minimal, not a full layer yet

Only three services exist: `VisitorService`, `VolunteerService`,
`RepairService`. There is **no `CafeService`, no `PatTestService`**, and no
orchestration between tickets and cafés beyond what lives in `RepairService`.

Notable issues, not just gaps:
- Filtering (banned visitors, active volunteers by skill/type) is done by
  loading `findAll()` and filtering **in memory** with Java streams, rather
  than expressing the filter as a derived query or JPQL. This will not scale
  past a small dataset.
- `RepairService.createTicketEvent` / `createRepairEvent` accept an `evt`
  parameter but ignore it, always writing `TicketState.REGISTERED` /
  `RepairState.REGISTERED`. Any caller passing a different state silently
  gets the wrong event recorded — a real bug, not a stylistic nit.
- Field injection (`@Autowired` on fields) is used throughout rather than
  constructor injection, which is harder to unit test and hides required
  dependencies.

## 5. Presentation / API layer — absent

Despite `spring-boot-starter-web`, `-websocket`, `spring-webflux`, and
`springdoc-openapi-starter-webmvc-ui` being declared dependencies, there are
**no `@RestController` or `@Controller` classes anywhere in the codebase**.
The application currently boots (`Application.java`, a bare
`@SpringBootApplication`) and exposes nothing beyond Spring Boot framework
defaults. `spring-boot-starter-security` is present but commented out in
`pom.xml`, so there is no authentication/authorisation either.

Other declared-but-unused/partially-used dependencies:
- `spring-boot-starter-thymeleaf` + `org.xhtmlrenderer:flying-saucer-pdf` —
  intended for HTML→PDF ticket reports; a template exists
  (`templates/TicketsReport_template.html`) but no service renders it.
- `bucket4j-core` — rate limiting, not yet wired into any request path
  (unsurprising, since there is no request path).
- `hibernate-validator` — in use on entities (`@NotEmpty`, `@Email`).

## 6. Testing

The only test in the repository is `ApplicationTests.contextLoads()`, and
`pom.xml` sets `<skipTests>true</skipTests>` by default, so even that does not
run in a normal build. There is no test coverage of service logic, converters,
or entity lifecycle hooks.

## 7. Build & deployment

Build is Maven (wrapper committed), producing a WAR. Docker and Nix packaging
both exist and are reasonably mature (multi-stage `Dockerfile`, `flake.nix` /
`nix/package.nix` with a Maven fixed-output-derivation hash, `devenv` dev
shell). GraalVM `native-maven-plugin` is declared, suggesting native-image is
an eventual goal, but no evidence it's been exercised yet (WAR/Tomcat is the
active path).

## 8. Summary

The project has a well-thought-out **data model** with genuine architectural
merit (event-style auditing, optimistic locking, converter-based enum
persistence) but is **pre-alpha everywhere else**: no controllers, two of five
expected services, in-memory filtering instead of queries, tests effectively
disabled, and at least one concrete bug in state-event creation. Treat this as
a domain-modelling prototype, not a working API server, when planning next
steps or a rewrite.
