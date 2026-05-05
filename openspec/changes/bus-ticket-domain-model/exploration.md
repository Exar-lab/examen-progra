## Exploration: bus-ticket-domain-model

### Current State
The project is a Spring Boot 4.0.6 skeleton with only `PrograApplication`, a context-load test, and minimal config (`spring.application.name=Progra`). There is no domain model, persistence mapping, security config, session/auth flow, PDF generation, or seed-data bootstrap yet.

### Affected Areas
- `pom.xml` — dependencies already include JPA, Security, OAuth2 Authorization Server, Validation, WebMVC; PDF/crypto needs review.
- `src/main/java/com/buses/examen/Progra/` — requires new feature-first packages and all business modules.
- `src/main/resources/application.properties` — will need datasource/security/session/JPA/bootstrap settings.
- `src/test/java/com/buses/examen/Progra/` — needs focused test slices for controller/service/repository/security rules.
- `src/main/resources/data.sql` or startup seeding component — initial data preload for countries/cities/routes/services/buses/seats.

### Approaches
1. **Relational-first domain model (recommended)** — Model 14 entities in JPA with strict service invariants and leave graph concerns for a later optimization phase.
   - Pros: Fastest path to a reference-quality CRUD+business-rule system; aligns with Spring Data JPA and exam scope; easier validation/testing/security hardening now.
   - Cons: Complex route search/optimization will be limited vs. graph traversal; migration effort later if graph analytics becomes core.
   - Effort: Medium

2. **Graph-first route model now** — Introduce node/edge abstractions (or external graph DB) from day one.
   - Pros: Better fit for future multi-hop route optimization and shortest-path queries.
   - Cons: Higher complexity, dual persistence concerns, steeper testing/ops burden, distracts from mandatory invariants and security/payment correctness.
   - Effort: High

### Recommendation
Use **relational-first with graph-ready boundaries**. Implement `Ruta`/`Servicio` as relational aggregates now, and isolate route-query logic behind a domain service interface (`RoutePlannerPort`) so a graph backend can be added later without rewriting purchase/validation flows.

Proposed feature hierarchy under `src/main/java/com/buses/examen/Progra/`:

- `shared/` (common value objects, mappers, errors)
- `security/` (session/auth config, filters, access rules)
- `catalog/` (master/reference data)
  - `pais/` (`Pais`)
  - `ciudad/` (`Ciudad`)
  - `compania/` (`Compania`)
  - `bus/` (`Bus`, `Asiento`)
- `route/`
  - `ruta/` (`Ruta`)
  - `servicio/` (`Servicio` schedule + fare)
- `customer/`
  - `cliente/` (`Cliente`)
  - `tarjeta/` (`Tarjeta` tokenized metadata only)
- `sales/`
  - `compra/` (`Compra`)
  - `ticket/` (`Ticket`)
  - `reserva/` (`ReservaAsiento`)
  - `comprobante/` (`Comprobante` + PDF metadata)
- `loyalty/` (`MovimientoPuntos`)
- `bootstrap/` (seed-data initializer)

Entity boundaries and relationships:
- `Pais 1..* Ciudad`
- `Compania 1..* Bus`
- `Bus 1..* Asiento`
- `Ruta` references origin/destination `Ciudad` (and optionally ordered stops later)
- `Servicio` links `Ruta + Bus + departureDateTime + fare + capacitySnapshot`
- `Cliente 1..* Tarjeta` (store PAN masked/tokenized; never CVV)
- `Compra 1..* Ticket`, `Compra 1..1 Comprobante`, `Compra *..1 Cliente`
- `Ticket *..1 Servicio`, unique immutable `ticketCode`
- `ReservaAsiento *..1 Servicio`, `ReservaAsiento *..1 Asiento`, optional link to `Ticket`
- `MovimientoPuntos *..1 Cliente`, optional link to `Compra`

Key invariants to enforce at service/domain layer:
- Max 5 tickets per purchase (`Compra` aggregate rule).
- Travel date must be `now <= departure <= now + 7 days`.
- `ticketCode` globally unique, immutable, collision-safe generation strategy.
- Seat capacity: no duplicate active reservation for `(servicio, asiento)` and sold tickets must not exceed service capacity.
- Session/security: only authenticated registered users can purchase or view own receipts.
- Receipt generation: successful purchase must emit PDF receipt including unique ticket codes + route/date/time + buyer metadata.
- Seed data: preload baseline countries, cities, companies, buses, seats, routes, and services in deterministic order.

Payment/card handling recommendations (critical):
- **Do not store CVV at rest** (PCI DSS prohibition).
- Prefer external payment gateway tokenization; persist only token, brand, last4, expiration, holder reference.
- Encrypt sensitive card metadata at application/database level and mask in logs/DTOs.
- Separate payment authorization from ticket issuance with idempotency key to avoid duplicate charges/tickets.

### Risks
- PCI/security non-compliance if CVV/PAN is persisted or logged.
- Race conditions on seat reservation and concurrent purchases without DB constraints + transactional locking.
- Timezone/date-window bugs for the 1-week rule across countries.
- Overly coupled route model if graph concerns leak into current transactional domain.

### Ready for Proposal
Yes — scope, boundaries, invariants, and risks are clear enough to produce proposal/spec/design next.
