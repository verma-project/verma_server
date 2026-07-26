<!--
SPDX-FileCopyrightText: 2023-2025 Dom Rodriguez (shymega) <shymega@shymega.org.uk>

SPDX-License-Identifier: AGPL-3.0-only
-->

# AGENT_JAVA.md — Verma Server (legacy Java codebase)

> **Which doc do you need?** This repo is mid-migration from Java to Rust.
> If you're working on the **existing Spring Boot/Java service**, this is
> your document. If you're working on the **Rust rewrite**, read
> `AGENT_RUST.md` instead (and `RUST_MIGRATION_PLAN.md` for the plan). Don't
> mix guidance from the two — the Rust doc deliberately departs from several
> patterns documented here.

Low-level reference for agents working in this repository. Describes what the
code **actually** contains as of this writing, not aspirational architecture.
Verify against source before relying on any claim here.

## 1. What this is

`verma-server` (Maven `name` is "Velma Server", groupId
`org.vermaproject.apps.server`, version `0.1.0`) is the backend for the **Verma
Project**, a management system for **Repair Cafés** — community events where
volunteers repair visitors' items (electricals, bikes, clothes, toys, etc.).

It is a **Spring Boot 4.1.0** application on **Java 21**, packaged as a **WAR**
(deployable to external Tomcat; also self-runnable via embedded Tomcat). License
is **AGPL-3.0-only**, tracked per-file with REUSE (`REUSE.toml`, `LICENSES/`).

> **Maturity: early / skeletal.** The domain model (JPA entities) is the most
> developed part. There is **no web/API layer yet** — see §4.

## 2. Domain model (the core of the project)

Repair Café workflow modelled by these entities. All live in
`src/main/java/org/vermaproject/apps/server/db/entities/`.

| Entity            | Represents                                        | Key relationships |
|-------------------|---------------------------------------------------|-------------------|
| `Visitor`         | Member of public bringing an item                 | ← `Ticket`; has `banned` flag |
| `Volunteer`       | Café staff (repairer / front-of-house / admin)    | ⇄ `Repair` (ManyToMany); has `active` flag, `skills`, `volunteerType` |
| `Cafe`            | A Repair Café venue/event organiser               | → `Ticket`, `CafeStateEvent`; auto-derives `cafeShortId` |
| `CafeSession`     | A single running session of a café                | (minimal) |
| `Ticket`          | A visitor's visit/request, groups their repairs   | → `Visitor`, `Cafe`, `Set<Repair>`, `TicketStateEvent` |
| `Repair`          | One item being repaired within a ticket           | → `Ticket`, ⇄ `Volunteer`, → `RepairStateEvent`; has `RepairType` |
| `PatTest`         | Portable Appliance Test on an electrical item     | → `PatTestStateEvent` |
| `*StateEvent`     | Append-only state-transition log per aggregate    | `Cafe/Ticket/Repair/PatTest` StateEvent, each with `eventTimestamp` |

**Base class:** `BaseEntity` (`@MappedSuperclass`) carries a JPA `@Version`
field → **optimistic locking** on every entity.

**State handling is event-based (audit trail):** state changes are recorded by
*inserting* a new `*StateEvent` row (with `enum` value + `ZonedDateTime`
timestamp) rather than mutating a status column. So history is append-only.

**Common entity conventions:**
- PK is `UUID` with `@GeneratedValue(strategy = AUTO)`, setter suppressed.
- Lombok `@Data @Builder @AllArgsConstructor @NoArgsConstructor`; classes are
  `final` and `implements Serializable`; `@JsonInclude(NON_NULL)`.
- Attribute converters in `utils/converters/` map enums ↔ DB and trim strings
  (`StringTrimConverter`, `StringListConverter`, and one `*Converter` per enum).
- `@PrePersist` hooks derive defaults, e.g. `Cafe.cafeShortId` = first 3 chars
  of name (comment says "four chars" — off-by-one, worth noting); `Ticket`
  populates `associatedItems` from `repairs.size()`.

### Enums (`enums/`)
- `RepairType`: COMPUTER, SMALL_ELECTRICAL, CLOTHES, FURNITURE, MECHANICAL, GARDEN_TOOL, TOY, BICYCLE, JEWELLERY, OTHER
- `SkillSet`: COMPUTER, ELECTRICAL_ELECTRONICS, MECHANICAL, GARDEN_TOOL, TOY, BICYCLE, JEWELLERY, OTHER, MUSICAL, TEXTILES, GARDEN, ALL_TRADES, WOOD_WORK
- `RepairState`: REGISTERED, TRIAGE, AWAITING, IN_PROGRESS, UNABLE_REPAIR, OFF_SITE_REPAIR, DELAYED, REPAIRED, REQUIRES_PARTS
- `TicketState`: REGISTERED, SCANNED, NO_SHOW, VOIDED
- `TicketType`: WALK_IN, ONLINE
- `CafeState`: SCHEDULED, CANCELLED, IN_PROGRESS, FINISHED
- `VolunteerType`: FRONT_OF_HOUSE, REFRESHMENTS, REPAIRER, ADMINISTRATOR
- `PatTestState`: TRIAGE_TESTED, REPAIR_COMPLETE_TESTED, UNTESTED
- `PatTestResult`: PASS, FAIL, INCONCLUSIVE, UNTESTED

## 3. Persistence layer

- **Spring Data JPA / Hibernate.** One `JpaRepository` per aggregate in
  `db/repositories/` (`VisitorRepository`, `VolunteerRepository`,
  `CafeRepository`, `TicketRepository`, `RepairRepository`, `PatTestRepository`,
  and the four `*StateEventRepository`s). Mostly default CRUD; `RepairRepository`
  adds `findAllRepairsByRepairType`.
- **Datasource selection** — `ApplicationConfiguration.getDataSource()` picks, in
  order: env `DATABASE_URL` (parsed from a URI, `postgres`→`postgresql`, creds
  moved to query params) → `spring.datasource.url` → **fallback H2 in-memory**
  (`jdbc:h2:mem:verma_server;MODE=PostgreSQL;...`). Production target is
  **PostgreSQL** (`org.postgresql` on runtime classpath).
- `application.properties` sets `ddl-auto=update`, `generate-ddl=true`,
  `sql.init.mode=always`. `application.properties.dist` is the committed
  template; `application.properties` is the local copy.

## 4. Service / web layer — INCOMPLETE

- **Services (`services/`):** only three exist — `VisitorService`,
  `VolunteerService`, `RepairService`. Field injection via `@Autowired`,
  `@Slf4j`.
  - `VisitorService`: create, ban/unban (`modifyBanned`), surname lookups,
    banned/unbanned filters. Note: filtering is done **in-memory** over
    `findAll()`, not via query methods.
  - `VolunteerService`: create, enable/disable (soft-delete via `active` flag),
    filter repairers by skill, front-of-house, admins — again in-memory streams.
  - `RepairService`: create repairs, and `initTicketRepairEvents` which seeds
    `TicketStateEvent`/`RepairStateEvent`. **Bug to be aware of:**
    `createTicketEvent`/`createRepairEvent` ignore their `evt` argument and
    hard-code `REGISTERED`.
- **There are NO controllers.** No `@RestController`/`@Controller`/
  `@RequestMapping` anywhere. No CafeService, no PatTest service, no DTOs, no
  REST endpoints, no WebSocket handlers wired up. The app currently boots and
  exposes nothing but framework/actuator defaults.
- ⚠️ The repo's `ARCHITECTURE_REVIEW_JAVA.md` describes controllers, a Cafe
  service, and request handling as if present — **that document is aspirational
  and does not match the code.** Trust source, not that file.

## 5. Dependencies present but not yet wired

These are on the classpath but have little/no application code using them yet —
treat as intended direction:
- `spring-boot-starter-web`, `-websocket`, `spring-webflux` (reactive) — web/RT.
- `springdoc-openapi-starter-webmvc-ui` — Swagger UI (needs controllers to be useful).
- `spring-boot-starter-thymeleaf` + `flying-saucer-pdf` (`org.xhtmlrenderer`) —
  HTML→PDF reports; template at
  `src/main/resources/templates/TicketsReport_template.html`.
- `bucket4j-core` — rate limiting.
- `hibernate-validator` — bean validation (`@NotEmpty`, `@Email` used on entities).
- `spring-boot-starter-security` is **commented out** in `pom.xml` → no auth yet.
- Lombok (compile-time), SLF4J + slf4j-simple (logging), Jackson JSR-310.

## 6. Build, run, deploy

- **Build:** `./mvnw clean package` → `target/verma-server-0.1.0.war`.
  **Tests are skipped by default** (`<skipTests>true</skipTests>` property);
  run them with `./mvnw test -DskipTests=false`. The only test is
  `ApplicationTests.contextLoads()`.
- **Run locally:** `./mvnw spring-boot:run` (uses H2 in-mem unless
  `DATABASE_URL`/`spring.datasource.url` set).
- **Docker:** `Dockerfile` (multi-stage, `eclipse-temurin:25-jdk` — note JDK 25
  image vs Java 21 source level), builds the WAR then `java -jar app.war`.
  `docker-compose.yml` present. GraalVM `native-maven-plugin` is declared
  (native-image ambitions) but not the primary path.
- **Nix:** first-class. `flake.nix` + `nix/package.nix` (Maven FOD build via
  `mvnHash`, Linux-only — `isLinux` assert, Darwin dropped), `nix/oci.nix` for
  OCI images, `devenv.nix`/`devenv.yaml` for the dev shell, `.envrc` (direnv).
  Update the Maven FOD hash with `scripts/update-mvn-hash.sh` when deps change.
- **CI (`.github/workflows/`):** `ci.yml`, `tests.yml`, `nix.yml`,
  `containers.yml`, `deploy.yml`, `codeql.yml`, `dependency-review.yml`,
  `verify-pr-commit.yml` (conventional commits), `clean-old-workflow-runs.yml`.
  Dependabot + Mergify configured.

## 7. Conventions for agents

- **Commits:** Conventional Commits are enforced in CI (`feat:`, `fix:`,
  `chore:`, `refactor:`, scopes like `chore(deps)`, `feat(nix)`). Match the
  existing style.
- **Licensing:** every file needs a REUSE SPDX header
  (`SPDX-FileCopyrightText: 2023-2025 Dom Rodriguez (shymega) <shymega@shymega.org.uk>`
  / `SPDX-License-Identifier: AGPL-3.0-only`). Non-code files get a `.license`
  sidecar (see the many `*.license` files). Keep `reuse lint` clean.
- **Formatting:** Nix files formatted with **Alejandra**; `.editorconfig`
  governs the rest.
- **Package root:** `org.vermaproject.apps.server`, laid out as
  `db.entities` / `db.repositories` / `services` / `enums` /
  `utils.converters`. Follow it for new code.
- **When adding a feature**, the missing layers are the obvious next work:
  controllers/DTOs, the Cafe & PatTest services, real query methods (the
  in-memory `findAll().stream()` filtering should become derived/JPQL queries),
  and turning tests back on.
