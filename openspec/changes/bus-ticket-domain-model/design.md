# Design: Bus Ticket Domain Model (Model-Only)

## Technical Approach
Implement only the persistence/domain baseline for the bus ticket core: feature-based model folders, 14 JPA entities, required enums, explicit FK cardinalities, and constraint-backed invariants. Keep behavior at entity/value-object level where natural (e.g., ticket-count cap, immutable ticket code, card-data safety). Do not design web/API/security/application flow in this change.

## Architecture Decisions

| Decision | Option | Tradeoff | Decision |
|---|---|---|---|
| Scope boundary | Full vertical slice vs model-only | Full slice gives earlier UX but mixes concerns | **Model-only** to stabilize domain first |
| Persistence identity | UUID vs `Long` identity | UUID is distributed-friendly; `Long` is simpler for JPA/MySQL tests | **`Long` + `IDENTITY`** |
| Invariant location | Service-only vs entity + DB constraints | Service-only is easier short-term but weaker as model contract | **Entity helpers + DB constraints** |
| Reservation conflict control | App checks only vs uniqueness constraints | App-only is race-prone | **Unique constraint strategy + domain validation** |
| Card storage | Persist PAN/CVV vs tokenized metadata | PAN/CVV raises PCI risk | **Token/masked-only, CVV excluded** |

## Data Flow
Model persistence flow for tests only:

`Entity construction -> invariant helper check -> JPA persist -> DB constraint validation`

```
Compra.addTicket(ticket)
   -> checks max 5
   -> persists Compra/Ticket
   -> DB enforces unique codigo_ticket

ReservaAsiento persist
   -> domain status validation
   -> DB uniqueness blocks duplicate servicio+asiento active combination
```

## File Changes

| File | Action | Description |
|---|---|---|
| `openspec/changes/bus-ticket-domain-model/proposal.md` | Modify | Narrow scope to model-only and explicit non-goals |
| `openspec/changes/bus-ticket-domain-model/spec.md` | Modify | Replace with model-only requirements and acceptance scenarios |
| `openspec/changes/bus-ticket-domain-model/design.md` | Modify | Align technical design to model-only implementation |
| `openspec/changes/bus-ticket-domain-model/tasks.md` | Modify | Replace tasks to remove controller/security/PDF/purchase flow work |

## Interfaces / Contracts
No new API/web contracts in this phase.

Model contracts to preserve:
- `Compra` exposes helper(s) that prevent adding more than 5 tickets.
- `Ticket` prevents mutation of `codigoTicket` after initialization.
- `Tarjeta` contains no CVV field and only tokenized/masked card metadata.

## Testing Strategy

| Layer | What to Test | Approach |
|---|---|---|
| Unit (domain) | Invariant helpers (`Compra`, `Ticket`, `Tarjeta`) | JUnit 5 + AssertJ focused tests |
| Integration (`@DataJpaTest`) | Entity mappings, FK cardinality, unique constraints, optional links | Persist/load entities in JPA slice tests |
| Schema behavior | Duplicate-key and reservation-conflict rejection | Constraint violation assertions in repository tests |

No `@WebMvcTest`, auth, or endpoint tests are part of this change.

## Migration / Rollout
No functional rollout plan required. If seed data exists, keep only what is necessary for mapping/invariant proof in persistence tests. No startup/runtime bootstrap guarantees are required in this change.

## Open Questions
- [ ] For active reservation uniqueness, do we encode status in composite unique key or enforce active-state filtering fully in domain logic?
