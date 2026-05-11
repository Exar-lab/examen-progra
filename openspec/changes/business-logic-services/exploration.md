## Exploration: business-logic-services

### Current State
The codebase already has domain entities and outbound repository ports/adapters per feature (`customer`, `fleet`, `geography`, `route`, `service`, `sales`, `loyalty`), but has **no inbound ports (`application/port/in`) and no application services** yet. Business invariants currently live mainly in domain methods (`Compra.agregarTicket`, `Ticket.emitir`, `Servicio.reservarCupo`), and persistence is exposed through repository ports created in the previous `entity-repositories` change.

### Affected Areas
- `src/main/java/com/buses/examen/Progra/**/application/port/out/*.java` — existing dependencies for future use cases.
- `src/main/java/com/buses/examen/Progra/sales/domain/Ticket.java` — purchase window invariant (<= 7 days) must be orchestrated by sales use cases.
- `src/main/java/com/buses/examen/Progra/sales/domain/Compra.java` — max 5 tickets invariant must be enforced through purchase orchestration.
- `src/main/java/com/buses/examen/Progra/service/domain/Servicio.java` — seat/capacity rule must be coordinated with reservation/ticket issuance use cases.
- `src/main/java/com/buses/examen/Progra/**/adapter/out/persistence/Jpa*Repository.java` — existing adapter implementations that application services should consume via ports only.
- `src/test/java/com/buses/examen/Progra/persistence/adapter/out/persistence/RepositoryAdaptersDataJpaTest.java` — confirms available repository queries and current aggregate persistence behavior.

### Approaches
1. **Feature-first use-case slices (recommended)** — Create inbound ports and application services inside each feature package, with cross-feature orchestration centered in `sales` use cases.
   - Pros: Preserves hexagonal boundaries, keeps business logic near domain language, avoids a global technical “service layer”, scales by feature.
   - Cons: Requires clear naming discipline to prevent duplicated “query/list” use cases across features.
   - Effort: Medium

2. **Global technical services package** — Introduce centralized services (e.g., `services/CustomerService`, `services/SalesService`) shared across all entities.
   - Pros: Faster initial wiring, fewer packages upfront.
   - Cons: Violates requested architecture style, increases coupling, blurs use-case boundaries, makes transaction scope harder to reason about.
   - Effort: Low (short-term), High (long-term refactor cost)

### Recommendation
Adopt **feature-first use-case slices** with one application-service adapter per inbound port and explicit per-feature boundaries:

| Feature | Inbound port(s) to add | Application service responsibility | Required outbound dependencies (existing ports) |
|---|---|---|---|
| `customer` | `RegistrarClienteUseCase`, `ConsultarClienteUseCase`, `RegistrarTarjetaUseCase` | registration/lookup and card token metadata persistence | `ClienteRepositoryPort`, `TarjetaRepositoryPort` |
| `geography` | `ConsultarGeografiaUseCase` | country/city lookup for route/service composition | `PaisRepositoryPort`, `CiudadRepositoryPort` |
| `fleet` | `ConsultarFlotaUseCase` | bus and seat lookup for scheduling/reservations | `BusRepositoryPort`, `AsientoRepositoryPort`, `CompaniaRepositoryPort` |
| `route` | `GestionarRutaUseCase`, `BuscarRutaUseCase` | create/find routes and route suggestions | `RutaRepositoryPort`, `RoutePlannerPort` |
| `service` | `ProgramarServicioUseCase`, `ConsultarServicioUseCase` | schedule/list services and expose availability | `ServicioRepositoryPort`, `RutaRepositoryPort`, `BusRepositoryPort` |
| `sales` | `ComprarTicketsUseCase`, `ReservarAsientoUseCase`, `ConsultarComprobanteUseCase` | purchase transaction orchestration, seat reservation, ticket issuance, receipt persistence | `CompraRepositoryPort`, `TicketRepositoryPort`, `ReservaAsientoRepositoryPort`, `ComprobanteRepositoryPort`, plus read dependencies `ClienteRepositoryPort`, `TarjetaRepositoryPort`, `ServicioRepositoryPort`, `AsientoRepositoryPort` |
| `loyalty` | `RegistrarMovimientoPuntosUseCase`, `ConsultarHistorialPuntosUseCase` | points ledger write/read after successful purchase | `MovimientoPuntosRepositoryPort` |

Transaction boundary guidance:
- Put `@Transactional` on application services that mutate aggregates (`ComprarTickets`, `ReservarAsiento`, `ProgramarServicio`, `RegistrarCliente`, `RegistrarTarjeta`, `RegistrarMovimientoPuntos`).
- Keep read use cases non-transactional (or read-only where needed).
- Keep domain invariants in domain entities; application services coordinate repository calls, existence checks, and cross-feature flow.

### Risks
- Missing repository queries for some read use cases (e.g., listing services by richer filters, ticket lookup by purchase/customer) may require **port extension** in follow-up changes.
- Concurrency race between seat reservation and ticket issuance if `ReservarAsiento` and `ComprarTickets` are split without explicit locking/uniqueness strategy.
- If route/search logic is pushed into application services instead of `RoutePlannerPort`, route module may become infrastructure-coupled.

### Ready for Proposal
Yes — the use-case slice boundaries, per-feature service responsibilities, and dependency map are explicit enough for `sdd-propose` and `sdd-spec`.
