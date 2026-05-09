# Tasks: Business Logic Services

## Review Workload Forecast

| Field | Value |
|-------|-------|
| Estimated changed lines | 700-950 |
| 400-line budget risk | High |
| Chained PRs recommended | Yes |
| Suggested split | PR 1 foundation/query services → PR 2 sales orchestration + tests |
| Delivery strategy | ask-on-risk |
| Chain strategy | pending |

Decision needed before apply: Yes
Chained PRs recommended: Yes
Chain strategy: pending
400-line budget risk: High

### Suggested Work Units

| Unit | Goal | Likely PR | Notes |
|------|------|-----------|-------|
| 1 | Add inbound ports, command/result records, and simple query services for customer/catalog features | PR 1 | Base from tracker/main; include unit tests for registration and read use cases. |
| 2 | Implement sales purchase orchestration with transaction boundary and domain-rule enforcement | PR 2 | Base from PR 1; include purchase-rule tests and service wiring checks. |

## Phase 1: Foundation

- [x] 1.1 Create public inbound ports under `*/application/port/in/` for `RegisterCustomerUseCase`, `CustomerQueryUseCase`, `GeographyQueryUseCase`, `FleetQueryUseCase`, `RouteQueryUseCase`, `ServiceQueryUseCase`, `LoyaltyQueryUseCase`, and `PurchaseTicketsUseCase`.
- [x] 1.2 Add immutable application command/result records in `sales/application/command/` and `sales/application/result/` for purchase inputs/outputs.
- [x] 1.3 Define any missing outbound-port methods needed by the services, especially unique ticket-code lookup and service/card/customer query methods.

## Phase 2: Core Implementation

- [x] 2.1 Implement `customer/application/CustomerService.java` to handle registration, card registration, and passport lookup through ports.
- [x] 2.2 Implement `geography`, `fleet`, `route`, `service`, and `loyalty` application services as read-only query orchestrators with constructor injection.
- [x] 2.3 Implement `sales/application/SalesService.java` with `@Transactional`, enforcing the 1-5 ticket limit, 7-day purchase window, seat availability, and unique ticket generation.
- [x] 2.4 Keep all orchestration in application services; do not leak web DTOs, Spring Data, or PDF concerns into these classes.

## Phase 3: Testing / Verification

- [x] 3.1 Add JUnit 5 + Mockito tests for customer registration, card registration, and lookup scenarios from `customer-management/spec.md`.
- [x] 3.2 Add focused tests for catalog query services covering route/service listing scenarios from `catalog-browsing/spec.md`.
- [x] 3.3 Add purchase-service tests covering success, 6-ticket rejection, 8-day rejection, insufficient seats, and unique code generation from `ticket-purchasing/spec.md`.
- [x] 3.4 Add wiring/slice checks for constructor injection and transaction-boundary behavior where the design calls for application-layer orchestration.

## Phase 4: Cleanup

- [x] 4.1 Add concise Javadocs to all public inbound ports and remove any temporary scaffolding created during service extraction.
- [x] 4.2 Verify package layout stays feature-first under `com/buses/examen/Progra/<feature>/application/` and that no adapter or controller code is introduced.
