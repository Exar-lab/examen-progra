# Design: Business Logic Services

## Technical Approach

Implement feature-scoped application services and inbound ports that orchestrate domain rules and outbound ports, keeping adapters isolated. The `sales` feature coordinates cross-feature purchase flows by calling other features’ outbound ports (repositories) through constructor-injected dependencies.

| Topic | Decision |
|---|---|
| Scope | Add inbound ports + application services for `customer`, `geography`, `fleet`, `route`, `service`, `sales`, `loyalty`. |
| Out of scope | Controllers, security/session wiring, and concrete PDF generation. |
| Dependencies | Existing domain entities + outbound repository ports in each feature. |
| Verification path | Unit tests for services (mock ports) + small Spring slices for wiring. |

## Architecture Decisions

### Decision: Feature-first application layer

| Option | Tradeoff | Decision |
|---|---|---|
| Centralized `application/` module | Fewer classes, but mixes feature responsibilities | Rejected |
| Feature-scoped `application/` per package | More files, but clearer boundaries | **Chosen** |

**Rationale**: Matches existing feature-first structure and Hexagonal boundaries in the repo.

### Decision: Sales as orchestrator via ports

| Option | Tradeoff | Decision |
|---|---|---|
| Sales service calls adapters directly | Faster to wire, but breaks Hexagonal rule | Rejected |
| Sales service depends only on outbound ports | More interfaces, but clean boundaries | **Chosen** |

**Rationale**: Keeps adapter details out of application, aligns with project conventions.

### Decision: Transaction boundaries in application services

| Option | Tradeoff | Decision |
|---|---|---|
| Transactions in adapters | Easy persistence, but splits business flow | Rejected |
| `@Transactional` on mutative use cases | Explicit business boundary | **Chosen** |

**Rationale**: Orchestration belongs to application layer; transactions must wrap cross-entity updates.

## Data Flow

Ticket purchase (sales):

    PurchaseTicketsUseCase
        └─ SalesService (application)
             ├─ ClienteRepositoryPort (customer)
             ├─ TarjetaRepositoryPort (customer)
             ├─ ServicioRepositoryPort (service)
             ├─ AsientoRepositoryPort (fleet)
             ├─ ReservaAsientoRepositoryPort (sales)
             ├─ TicketRepositoryPort (sales)
             ├─ CompraRepositoryPort (sales)
             └─ MovimientoPuntosRepositoryPort (loyalty)

Flow summary: validate customer + card, fetch service and seat, check reservation uniqueness, `servicio.reservarCupo()`, `Ticket.emitir(...)`, `Compra.agregarTicket(...)`, persist purchase, ticket, reservation, and points movement. Comprobante is created in-domain and persisted; PDF generation is deferred behind a future outbound port.

## File Changes

| File | Action | Description |
|---|---|---|
| `src/main/java/com/buses/examen/Progra/sales/application/port/in/PurchaseTicketsUseCase.java` | Create | Inbound port for purchase orchestration (Javadoc contract). |
| `src/main/java/com/buses/examen/Progra/sales/application/command/PurchaseTicketsCommand.java` | Create | Immutable command record (customer, service, seats, payment). |
| `src/main/java/com/buses/examen/Progra/sales/application/result/PurchaseTicketsResult.java` | Create | Result record (compraId, ticketCodes, comprobanteId). |
| `src/main/java/com/buses/examen/Progra/sales/application/SalesService.java` | Create | Application service implementing purchase use case. |
| `src/main/java/com/buses/examen/Progra/customer/application/port/in/RegisterCustomerUseCase.java` | Create | Inbound port for customer registration. |
| `src/main/java/com/buses/examen/Progra/customer/application/port/in/CustomerQueryUseCase.java` | Create | Inbound port for customer lookup. |
| `src/main/java/com/buses/examen/Progra/customer/application/CustomerService.java` | Create | Application service for customer operations. |
| `src/main/java/com/buses/examen/Progra/geography/application/port/in/GeographyQueryUseCase.java` | Create | Inbound port for countries/cities queries. |
| `src/main/java/com/buses/examen/Progra/geography/application/GeographyService.java` | Create | Application service for geography queries. |
| `src/main/java/com/buses/examen/Progra/fleet/application/port/in/FleetQueryUseCase.java` | Create | Inbound port for buses/companies/seats. |
| `src/main/java/com/buses/examen/Progra/fleet/application/FleetService.java` | Create | Application service for fleet queries. |
| `src/main/java/com/buses/examen/Progra/route/application/port/in/RouteQueryUseCase.java` | Create | Inbound port for route lookup + planner. |
| `src/main/java/com/buses/examen/Progra/route/application/RouteService.java` | Create | Application service for routes. |
| `src/main/java/com/buses/examen/Progra/service/application/port/in/ServiceQueryUseCase.java` | Create | Inbound port for scheduled services. |
| `src/main/java/com/buses/examen/Progra/service/application/ServiceService.java` | Create | Application service for scheduled services. |
| `src/main/java/com/buses/examen/Progra/loyalty/application/port/in/LoyaltyQueryUseCase.java` | Create | Inbound port for points history. |
| `src/main/java/com/buses/examen/Progra/loyalty/application/LoyaltyService.java` | Create | Application service for loyalty operations. |

## Interfaces / Contracts

Inbound ports (public, Javadoc required):

```java
public interface PurchaseTicketsUseCase {
    PurchaseTicketsResult purchase(PurchaseTicketsCommand command);
}

public record PurchaseTicketsCommand(
        Long clienteId,
        Long tarjetaId,
        Long servicioId,
        List<Long> asientoIds,
        String canalCompra,
        String codigoOperacionExterna) { }

public record PurchaseTicketsResult(
        Long compraId,
        List<String> ticketCodes,
        Long comprobanteId) { }
```

Dependency direction: inbound ports → application services → outbound ports → adapters. Application services must NOT depend on Spring Data repositories or web DTOs.

Missing outbound ports / query methods needed:

- `TicketRepositoryPort`: `boolean existsByCodigoTicket(String)` (or reuse `findByCodigoTicket`) for unique code generation.
- `ReservaAsientoRepositoryPort`: `Optional<ReservaAsiento> findActiveByServicioIdAndAsientoId(...)` if we need to attach ticket to an existing reservation; otherwise keep `exists...`.
- `ServicioRepositoryPort`: consider `Optional<Servicio> findByIdForUpdate(Long)` if we choose DB-level locking for capacity; otherwise rely on domain + transaction isolation.

## Testing Strategy

| Layer | What to Test | Approach |
|---|---|---|
| Unit | Application services enforce rules (5 tickets, 7-day window, capacity) | JUnit 5 + Mockito with mocked ports. |
| Integration | JPA adapters implement port contracts | `@DataJpaTest` for each adapter. |
| E2E | Not in scope | No E2E in this change. |

## Migration / Rollout

No migration required.

## Open Questions

- [ ] Should seat capacity be protected via explicit DB locking (`findByIdForUpdate`) or rely on optimistic flow + unique reservation constraint?
- [ ] Is PDF receipt generation expected to be triggered inside `SalesService` via a new outbound port in this change or deferred to a later change?
