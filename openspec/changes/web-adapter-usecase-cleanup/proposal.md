# Proposal: Web Adapter + Use Case Cleanup

## Intent
Clean inbound web architecture before introducing `sales` inbound endpoints by separating controller responsibilities from DTO and mapping concerns, and by clarifying write use-case contracts in the application layer.

## Scope

### In Scope
- Replace `*WebAdapter` naming/structure with explicit `*Controller` classes in current features (`customer`, `geography`, `fleet`, `route`, `service`, `loyalty`) without changing endpoint behavior.
- Extract inner request/response records into dedicated DTO files under `adapter/in/web/dto/request` and `adapter/in/web/dto/response`.
- Extract mapping from controllers into focused `adapter/in/web/mapper/*WebMapper` components (or stateless utility mappers per feature).
- Evolve write-focused inbound contracts where currently primitive-heavy (initial target: customer registration/card registration) toward command/result records in `application/command` and `application/result`.
- Preserve all business orchestration in application services (`CustomerService`, `SalesService`), keeping adapters thin.

### Out of Scope
- Implementing `SalesWebAdapter`/`SalesController` endpoints (handled in next slice).
- Changing domain invariants, persistence model, PDF generation logic, or security/session flows.
- New business rules for purchase, routing, or loyalty.

## Problem Statement
Current inbound classes are not violating orchestration boundaries, but they are structurally coupled: each class contains endpoint definitions, DTO declarations, and mapping logic together. This pattern reduces readability and increases maintenance cost. Adding sales web endpoints on top of this would spread the same coupling to a critical flow.

## Proposed Slice
Create a cleanup slice that standardizes inbound layer composition and write use-case contracts, then use that structure as the baseline for a future `sales` inbound adapter.

## Target Package Convention

For each feature `<feature>`:

- `com.buses.examen.Progra.<feature>.adapter.in.web.<Feature>Controller`
- `com.buses.examen.Progra.<feature>.adapter.in.web.dto.request.*Request`
- `com.buses.examen.Progra.<feature>.adapter.in.web.dto.response.*Response`
- `com.buses.examen.Progra.<feature>.adapter.in.web.mapper.*WebMapper`

For write-oriented application contracts:

- `com.buses.examen.Progra.<feature>.application.port.in.*UseCase`
- `com.buses.examen.Progra.<feature>.application.command.*Command`
- `com.buses.examen.Progra.<feature>.application.result.*Result`

## Affected Areas

| Area | Impact | Notes |
|------|--------|-------|
| `src/main/java/com/buses/examen/Progra/*/adapter/in/web/*WebAdapter.java` | Refactor | Split controller/DTO/mapper concerns. |
| `src/main/java/com/buses/examen/Progra/customer/application/port/in/RegisterCustomerUseCase.java` | Contract refinement | Move from many primitives toward command/result contract. |
| `src/main/java/com/buses/examen/Progra/customer/application/CustomerService.java` | Signature update | Keep behavior, adapt to refined inbound contract. |
| `src/test/java/com/buses/examen/Progra/adapter/in/web/WebAdapterHexagonalBoundaryTest.java` | Update | Reflect renamed controllers and preserve boundary assertions. |
| `src/test/java/com/buses/examen/Progra/application/port/in/InboundPortsContractTest.java` | Update | Assert command/result style for write use cases where applied. |

## Test Strategy (Behavior Preservation)
- Add/adjust `@WebMvcTest` slices for each controller to ensure endpoint routes, status codes, and JSON payloads remain unchanged after DTO extraction.
- Keep/extend application unit tests (`CustomerServiceTest`, `SalesServiceTest`) to ensure orchestration and domain-rule enforcement remain unchanged.
- Update architecture/contract tests:
  - `WebAdapterHexagonalBoundaryTest` to include renamed controllers and keep no-concrete-service dependency checks.
  - `InboundPortsContractTest` to verify refined write-port signatures (command/result) and unchanged query contracts.

## Risks

| Risk | Likelihood | Mitigation |
|------|------------|------------|
| DTO extraction accidentally changes public JSON contract | Medium | Add explicit WebMvc contract assertions per endpoint before/after refactor. |
| Port signature changes cascade into multiple classes/tests | Medium | Limit write-contract changes to customer in this slice; defer broader changes. |
| Large cross-feature refactor creates merge conflicts | Medium | Execute in small commits per feature and keep behavior-only scope. |

## Rollback Plan
Revert the cleanup slice commit(s), restoring original `*WebAdapter` classes and inbound port signatures. Since no domain/persistence behavior changes are included, rollback risk is low.

## Success Criteria
- [ ] No controller contains nested DTO record declarations.
- [ ] Controllers only orchestrate HTTP concerns and call inbound use cases.
- [ ] Mapping logic resides in dedicated mapper classes/files.
- [ ] Write orchestration remains in application services; no save/write business logic is moved to adapters.
- [ ] Existing behavior (status codes, payloads, use-case outcomes) is preserved by tests.
