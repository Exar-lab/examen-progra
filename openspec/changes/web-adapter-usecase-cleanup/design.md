# Design: Web Adapter + Use Case Cleanup

## Technical Approach
Refactor inbound web adapters feature-by-feature (customer, fleet, geography, route, service, loyalty) to a consistent structure: thin HTTP controller, externalized DTOs, and dedicated web mapper classes. Keep endpoint paths, HTTP methods, status codes, and JSON field names unchanged. Apply command/result contracts only to **customer write** use cases in this slice; keep query ports and sales purchase use case unchanged.

## Architecture Decisions

| Decision | Option | Tradeoff | Decision |
|---|---|---|---|
| Web class naming | Keep `*WebAdapter` vs rename to `*Controller` | `*WebAdapter` aligns with adapter term but blurs Spring role; `*Controller` is explicit for inbound HTTP and matches tests/slices (`@WebMvcTest`) | Use `*Controller` consistently in all six features |
| DTO organization | Inner records vs dedicated files | Inner records are quick but coupled; dedicated files improve readability, reuse, and contract visibility | Use `adapter/in/web/dto/request` and `dto/response` per feature |
| Mapping location | Mapping inside controller vs `mapper/*WebMapper` | In-controller mapping increases class size; mapper isolates transformation and enables focused unit tests | Use `adapter/in/web/mapper/*WebMapper` (stateless component/utility) |
| Customer write contracts | Keep primitive/domain signatures vs command/result | Keeping as-is is lowest churn; command/result improves boundary clarity and matches `sales` pattern | Migrate only `RegisterCustomerUseCase` write methods to command/result in this change |

## Data Flow
Existing behavior remains; only boundary composition changes.

```text
HTTP Request
   ↓
<Feature>Controller (validation + status mapping)
   ↓
<Feature>WebMapper (Request DTO → Command)
   ↓
<Feature>UseCase port (application)
   ↓
<Feature>Service (orchestration + transactions)
   ↓
Domain + outbound ports
   ↓
<Feature>WebMapper (Domain/Result → Response DTO)
   ↓
HTTP JSON Response
```

## File Changes

| File | Action | Description |
|---|---|---|
| `src/main/java/com/buses/examen/Progra/customer/adapter/in/web/CustomerWebAdapter.java` | Rename/Modify | Replace with `CustomerController`; remove nested records and inline mapping. |
| `src/main/java/com/buses/examen/Progra/{fleet,geography,route,service,loyalty}/adapter/in/web/*WebAdapter.java` | Rename/Modify | Replace each with `*Controller`; keep routes and behavior identical. |
| `src/main/java/com/buses/examen/Progra/<feature>/adapter/in/web/dto/request/*.java` | Create | Request contracts (customer writes now; others only when requests exist). |
| `src/main/java/com/buses/examen/Progra/<feature>/adapter/in/web/dto/response/*.java` | Create | Response contracts previously nested in adapters. |
| `src/main/java/com/buses/examen/Progra/<feature>/adapter/in/web/mapper/*WebMapper.java` | Create | Domain/result ↔ DTO mapping extraction. |
| `src/main/java/com/buses/examen/Progra/customer/application/port/in/RegisterCustomerUseCase.java` | Modify | Move to command/result signatures for `register` and `registerCard`. |
| `src/main/java/com/buses/examen/Progra/customer/application/command/*.java` | Create | Customer write command records. |
| `src/main/java/com/buses/examen/Progra/customer/application/result/*.java` | Create | Customer write result records. |
| `src/main/java/com/buses/examen/Progra/customer/application/CustomerService.java` | Modify | Adapt implementation to new command/result port contract, unchanged business rules. |
| `src/test/java/com/buses/examen/Progra/adapter/in/web/WebAdapterHexagonalBoundaryTest.java` | Modify | Point to `*Controller` classes, keep boundary assertions. |
| `src/test/java/com/buses/examen/Progra/application/port/in/InboundPortsContractTest.java` | Modify | Assert customer write command/result contracts; keep query contracts unchanged. |
| `src/test/java/com/buses/examen/Progra/*/adapter/in/web/*ControllerWebMvcTest.java` | Create | Contract-preservation tests per feature (routes/status/json). |

## Interfaces / Contracts
Customer write contracts after cleanup (illustrative):

```java
public interface RegisterCustomerUseCase {
    RegisterCustomerResult register(RegisterCustomerCommand command);
    RegisterCardResult registerCard(RegisterCardCommand command);
}
```

Notes:
- `CustomerQueryUseCase` stays unchanged (`Optional<Cliente>`), since this slice targets write-port cleanup.
- `PurchaseTicketsUseCase` remains command/result and is not refactored.

## Testing Strategy

| Layer | What to Test | Approach |
|---|---|---|
| Architecture | Inbound adapters depend on ports, not concrete services; no domain types in public web signatures | Update `WebAdapterHexagonalBoundaryTest` class references to controllers |
| Port contracts | Customer write use case switched to command/result | Update `InboundPortsContractTest` reflection assertions |
| Web MVC/API | Route, method, status, and JSON schema preserved for each endpoint | Add focused `@WebMvcTest` per controller; assert exact JSON field names and required fields |
| Mapping | DTO↔domain/result mapping correctness where non-trivial (`route plan`, `service`, `loyalty null compra`) | Focused mapper unit tests without Spring context |

## Migration / Rollout
No migration required. Rollout is phased by feature in small PR commits:
1) geography/fleet/route/service/loyalty (read-only adapters),
2) customer adapter split,
3) customer write port command/result,
4) boundary + WebMvc tests.

Risk mitigation for JSON contract preservation:
- Freeze current API examples in WebMvc assertions before refactor.
- Preserve DTO property names exactly; avoid renaming Java record components that define JSON keys.
- Keep HTTP status mapping and exception behavior unchanged.

## Open Questions
- [ ] Should customer query port return a query result DTO in a follow-up change to fully eliminate domain leakage from inbound ports?
- [ ] Do we want a shared global web exception handler now, or keep current per-controller `ResponseStatusException` style for low-risk cleanup?
