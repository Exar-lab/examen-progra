# Proposal: Bus Ticket Domain Model

## Summary
Narrow this change to a **model-only domain baseline** for `examen-progra`: feature/domain folder hierarchy, **14 JPA entities**, required enums, and entity-level invariants/constraints needed for correct persistence behavior.

## Problem
The current project needs a stable persistence model before higher layers can evolve safely. Previous artifacts mixed model concerns with controller/security/purchase-flow expectations; that coupling makes this change too broad and hard to verify.

## Goals
- Establish feature-based package hierarchy under `src/main/java/com/buses/examen/Progra/` aligned with project conventions.
- Define and map all 14 entities with explicit relationships and ownership boundaries.
- Encode key purchase/seat/identity constraints that naturally belong in entities and DB constraints.
- Keep implementation compatible with hybrid persistence strategy (OpenSpec artifacts + Engram state).

## Non-Goals
- No controllers, DTOs, web endpoints, auth/session endpoints, or security policy implementation.
- No application/service purchase orchestration flow.
- No PDF generation or payment gateway integration.
- No runtime startup guarantees beyond model persistence proof needed by model tests.

## Scope
### In Scope
- Create domain packages and entity placement by feature.
- Add JPA entity definitions, IDs, relationships, constraints/indexes, and enums.
- Add domain invariant helpers located in entities/value objects.
- Add repositories only when needed by `@DataJpaTest` mapping verification.
- Keep or adjust model seed data only if needed by model persistence tests.

### Out of Scope
- Business workflow orchestration (purchase transaction, ticket issuance, loyalty posting).
- API DTO contracts and web endpoints.
- Security/authentication implementation details.
- PDF and payment implementations.
- Full bootstrap/startup flow guarantees.

## Proposed Approach
1. Keep **feature-first packaging** with entity classes under each feature `domain/` folder.
2. Implement the 14 entities with explicit JPA mappings and cardinalities.
3. Use DB constraints plus entity helper methods for model-level invariants:
   - max 5 tickets per compra
   - ticket code uniqueness/immutability
   - reservation uniqueness per `(servicio, asiento[, estado])`
   - no CVV persistence in `Tarjeta`
4. Verify via focused `@DataJpaTest` and entity tests only.

## Acceptance Criteria
- All 14 entities exist and compile with JPA mappings.
- Package hierarchy follows feature-based convention under `com.buses.examen.Progra`.
- Entity relationships reflect agreed cardinalities.
- `Ticket` contains globally unique immutable code field.
- `Tarjeta` excludes CVV persistence and supports masked/tokenized metadata.
- Model tests cover mappings, constraints, and entity invariants without requiring controllers/services.

## Risks and Mitigations
- **Over-scoping risk**: mitigated by explicitly excluding controllers/security/purchase flow/PDF/payment from this change.
- **Concurrency risk on seat assignment**: mitigated at this phase by uniqueness constraints; transactional strategy deferred.
- **PCI risk on card data**: mitigated by forbidding CVV and clear PAN persistence from first model version.

## Rollback Plan
If model changes introduce schema/mapping issues, rollback removes model package changes and related tests/migrations only. Impact is isolated to persistence/model code.

## Dependencies
- Exploration artifact: `openspec/changes/bus-ticket-domain-model/exploration.md`
- Accepted decisions: Spring Boot (not PHP), all 14 entities included, relational-first/graph-ready, hybrid persistence.

## Next Step
Proceed to **sdd-design/sdd-tasks** aligned to model-only implementation and verification.
