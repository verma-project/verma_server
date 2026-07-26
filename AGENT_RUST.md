<!--
SPDX-FileCopyrightText: 2023-2025 Dom Rodriguez (shymega) <shymega@shymega.org.uk>

SPDX-License-Identifier: AGPL-3.0-only
-->

# AGENT_RUST.md — Verma Server Rust Rewrite

> **Which doc do you need?** This repo is mid-migration from Java to Rust.
> If you're working on the **Rust rewrite**, this is your document. If
> you're working on the **existing Spring Boot/Java service**, read
> `AGENT_JAVA.md` instead — don't apply this file's rules there.

Agent instructions for the Java→Rust migration of `verma-server`. Read
alongside `AGENT_JAVA.md` (what the Java codebase actually contains) and
`RUST_MIGRATION_PLAN.md` (the phased plan and target stack) — this file is
the "how to operate" companion to those two "what/why" documents.

## 1. Role

You are acting as a pair-programmer driving the migration of a Spring
Boot/Java Repair Café management backend to a Rust microservice (`axum` +
`sqlx`, see `RUST_MIGRATION_PLAN.md` §2 for the full stack table). The Java
source is the **domain specification**, not a behavioural target to copy
uncritically — it is an early-stage prototype with a solid entity model but
no API layer and at least one known service-layer bug. Port the model
faithfully; do not port its gaps or bugs.

## 2. Before touching code

1. **Read `AGENT_JAVA.md`** for the current, verified state of the Java codebase
   (entities, enums, services, what's missing). Don't re-derive this from
   scratch — it's already been audited.
2. **Read `RUST_MIGRATION_PLAN.md`** for the phase you're working in and the
   target stack choices. Don't introduce a different web framework, ORM
   pattern, or crate for something already decided there without flagging the
   change to the user first.
3. **Check which phase is in flight.** Don't jump ahead — e.g. don't write
   `api/` handlers (Phase 4) before `domain/`+`repository/` (Phase 2) exist
   and are tested, and don't add cross-cutting concerns (Phase 5: auth, rate
   limiting) speculatively into early-phase code.

## 3. Translation rules

- **Entities → structs, not classes.** No inheritance for `BaseEntity`; use
  a shared `version: i32` field (or a small trait if genuinely useful) rather
  than trying to reproduce Java's `@MappedSuperclass` via composition
  gymnastics.
- **Enums → Rust `enum` + `sqlx::Type`.** Map directly to Postgres native
  enum types. Do **not** reproduce the one-`AttributeConverter`-class-per-enum
  pattern from `utils/converters/` — that's Java boilerplate working around a
  language limitation Rust doesn't have.
- **State-event auditing is the one Java pattern to preserve deliberately.**
  Every state transition is an `INSERT` into a `*_state_events` table, never
  an `UPDATE` of a status column. Implement this as an explicit repository
  method (e.g. `record_transition(&self, id, new_state) -> Result<...>`)
  called intentionally by services — not buried as a side effect of an
  unrelated method. This directly addresses the Java bug where
  `RepairService.createTicketEvent`/`createRepairEvent` accept a state
  argument and then ignore it, hard-coding `REGISTERED`. When you port this
  logic, the new code must actually use the passed-in state — treat this as
  a correctness requirement, not a style preference.
- **Push filtering into SQL.** Java's `VisitorService`/`VolunteerService`
  load `findAll()` and filter with Java streams in application code. Do not
  reproduce this — write `WHERE` clauses (`sqlx::query!` with parameters) in
  `repository/`. If you find yourself writing `.filter()` over a
  `Vec<T>` fetched by a broad query, stop and ask whether that filter belongs
  in SQL instead.
- **No lazy loading.** Java's `FetchType.LAZY` relationships have no direct
  Rust analogue and shouldn't get one — every relationship load should be an
  explicit, visible query call at the point of use.
- **DTOs are distinct from domain structs from the start.** Java never built
  an API layer, so it never had to make this call — make it deliberately in
  `api/`: request/response types live separately from `domain/` types, with
  explicit `From`/`TryFrom` conversions, not `#[serde(flatten)]` reuse of
  domain structs as wire types.

## 4. What to build vs. what to port

- **Port:** `Visitor`, `Volunteer`, `Cafe`, `CafeSession`, `Ticket`, `Repair`,
  `PatTest`, all `*StateEvent` types, all enums listed in `AGENT_JAVA.md` §2, and
  the three existing services' *intended* behaviour (ban/unban,
  enable/disable, skill-based filtering) — with the state-event bug fixed,
  not replicated.
- **Build new (Java never had this):** the entire `api/` layer,
  `CafeService`, `PatTestService`, versioned DB migrations, and any auth
  strategy. Don't describe this work as "porting" in commit messages or PR
  descriptions — it's new functionality being added during the rewrite, and
  should be reviewed as such (i.e., don't assume the absence of Java
  equivalent means no need for tests or design discussion).

## 5. Verifying correctness

- **Test against real Postgres**, not an in-memory substitute, using
  `testcontainers-rs` or an equivalent — the Java side's biggest testing gap
  was `skipTests=true` plus a single no-op `contextLoads()` test.
  Don't repeat that; every repository/service you port needs actual test
  coverage before it's considered migrated.
- When porting a service method, write the test **first** by reading the
  Java method's real behaviour (not just its name) — e.g.
  `VisitorService.getVisitorsBySurname` excludes banned visitors despite the
  ambiguous name; `getAllVisitorsBySurname` does not. Preserve that exact
  semantic distinction in the Rust names/behaviour, or explicitly flag the
  rename if you think the Rust API should disambiguate it better.
- Check `RUST_MIGRATION_PLAN.md` §7 ("what done looks like") before declaring
  a phase complete.

## 6. Packaging & tooling

- Reuse the existing Nix infrastructure patterns (`flake.nix`,
  `nix/package.nix`, `nix/oci.nix`) but adapt them for a Rust build
  (`buildRustPackage` or `crane`), not a transliteration of the Maven FOD
  (`mvnHash`) approach — Rust doesn't need that workaround.
- Match the existing repo conventions regardless of language: Conventional
  Commits, REUSE/SPDX headers on every new file
  (`SPDX-FileCopyrightText: 2023-2025 Dom Rodriguez (shymega)
  <shymega@shymega.org.uk>` / `SPDX-License-Identifier: AGPL-3.0-only`), and
  keep CI equivalents (`cargo test`, `cargo clippy -- -D warnings`,
  `cargo fmt --check`) to the same rigor the Java CI workflows implied even
  where the Java tests themselves were skipped.

## 7. Instructions for use (for whoever drives this agent)

1. **Never assume Java behaviour is correct** — verify against the actual
   method body, not the method name or the (partly inaccurate)
   `ARCHITECTURE_REVIEW_JAVA.md` narrative.
2. **Treat the API layer as new design work**, not translation — there is no
   Java controller to copy, so propose endpoint shapes explicitly and check
   them with the user before implementing broad swaths of `api/`.
3. **Use a `Plan`-style pass before each phase** in
   `RUST_MIGRATION_PLAN.md` §5 that touches more than one or two files —
   phases 2–4 in particular span domain, repository, and service layers
   together.
4. **Flag deviations from the migration plan** rather than silently picking a
   different crate/pattern — the plan encodes decisions already made; changes
   should be visible, not buried in a diff.
