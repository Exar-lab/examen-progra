# Tasks: Bus Ticket Domain Model (Model-Only)

## Phase 1: Scope Realignment
- [x] 1.1 Remove/ignore controller, security, PDF, and purchase-flow expectations from this change artifacts.
- [x] 1.2 Confirm feature/domain folder structure for model classes under `com/buses/examen/Progra/<feature>/domain`.

## Phase 2: Entity and Enum Baseline
- [x] 2.1 Ensure all 14 required JPA entities exist with stable PKs and explicit table mappings.
- [x] 2.2 Ensure required enums exist and are mapped with `EnumType.STRING`.
- [x] 2.3 Validate mandatory relationship cardinalities and optional links (`ReservaAsiento->Ticket`, `MovimientoPuntos->Compra`).

## Phase 3: Constraints and Invariants
- [x] 3.1 Implement/verify unique constraints and indexes for identity and seat conflict rules.
- [x] 3.2 Implement/verify entity-level invariant helpers: max 5 tickets per compra, immutable ticket code, and no CVV persistence in tarjeta.

## Phase 4: Model-Focused Verification
- [x] 4.1 Add/adjust `@DataJpaTest` coverage for mappings and constraints.
- [x] 4.2 Add/adjust domain tests for entity invariant helpers.
- [x] 4.3 Keep repositories only where needed by mapping tests; avoid introducing web/service flow dependencies.

## Explicitly Out of Scope for This Task Set
- Controllers, DTOs, web endpoints.
- Auth/session/security implementation.
- Purchase orchestration service.
- PDF generation and payment gateway integration.
- Full startup/bootstrap guarantees beyond model persistence proof.
