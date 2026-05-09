## Exploration: web-adapter-usecase-cleanup

### Current State
Current inbound web adapters are thin in business orchestration (good), but they still bundle controller + DTO definitions + mapping static factories in single `*WebAdapter` classes. This reduces readability and makes the next sales inbound adapter likely to repeat the same coupling pattern.

Application orchestration for writes is currently centered in `CustomerService` (customer registration/card registration) and `SalesService` (ticket purchase flow). Catalog modules (`geography`, `fleet`, `route`, `service`, `loyalty`) expose only query use cases today.

### Affected Areas
- `src/main/java/com/buses/examen/Progra/customer/adapter/in/web/CustomerWebAdapter.java` — mixes endpoint methods + request/response records + mapping in same class.
- `src/main/java/com/buses/examen/Progra/fleet/adapter/in/web/FleetWebAdapter.java` — mixes endpoint methods + response records + mapping.
- `src/main/java/com/buses/examen/Progra/geography/adapter/in/web/GeographyWebAdapter.java` — same coupling pattern.
- `src/main/java/com/buses/examen/Progra/route/adapter/in/web/RouteWebAdapter.java` — same coupling pattern including mapping for planner output.
- `src/main/java/com/buses/examen/Progra/service/adapter/in/web/ServiceWebAdapter.java` — same coupling pattern.
- `src/main/java/com/buses/examen/Progra/loyalty/adapter/in/web/LoyaltyWebAdapter.java` — same coupling pattern.
- `src/main/java/com/buses/examen/Progra/customer/application/CustomerService.java` — write orchestration exists but inbound port still exposes primitive-heavy signatures returning domain entities.
- `src/main/java/com/buses/examen/Progra/sales/application/SalesService.java` — write orchestration exists and is transaction-scoped; no inbound web adapter exists yet for sales.
- `src/test/java/com/buses/examen/Progra/adapter/in/web/WebAdapterHexagonalBoundaryTest.java` — enforces no dependency on concrete app services and no domain-type exposure in public web signatures.
- `src/test/java/com/buses/examen/Progra/application/port/in/InboundPortsContractTest.java` — currently locks primitive-style inbound signatures and some domain return types.

### Approaches
1. **Web adapter structure cleanup first (recommended)**
   - Split each `*WebAdapter` into: `*Controller`, DTO files (`dto/request`, `dto/response`), and `mapper/*WebMapper`.
   - Keep same endpoint contracts and same inbound port invocations.
   - Introduce command/result records where write ports are currently primitive-heavy (start with customer registration/card registration) while preserving behavior.
   - Pros: prepares consistent template before creating `SalesWebAdapter`; improves readability and maintenance; keeps risk low by avoiding domain behavior changes.
   - Cons: touches many files and test contracts; requires careful backward-compatible endpoint payloads.
   - Effort: Medium

2. **Add SalesWebAdapter now, clean up later**
   - Implement sales inbound adapter following existing `*WebAdapter` style.
   - Refactor all adapters in a later slice.
   - Pros: faster path to expose purchase endpoint.
   - Cons: duplicates current coupling pattern; increases later migration scope; harder to enforce consistent naming.
   - Effort: Low now, High later

### Recommendation
Do a dedicated cleanup slice **before** adding `SalesWebAdapter`: standardize inbound web architecture and tighten write use-case contracts.

Target standard:
- `adapter/in/web/<Feature>Controller`
- `adapter/in/web/dto/request/*Request`
- `adapter/in/web/dto/response/*Response`
- `adapter/in/web/mapper/*WebMapper`
- `application/port/in/*UseCase` with command/result records for write operations
- `application/command/*Command`, `application/result/*Result` for mutative flows

Write/use-case focus for this cleanup:
- Keep existing `PurchaseTicketsUseCase` as write use case for sales (already correct at application layer).
- Refactor `RegisterCustomerUseCase` to command-based input and result-based output to avoid leaking domain-centric signatures into boundary-facing contracts.
- Keep query use cases as-is for this slice unless needed for mapper extraction.

### Risks
- Contract tests currently assert exact method signatures in inbound ports; command/result migration will require synchronized test updates.
- Refactor across many adapters may create package-move churn and temporary broken imports.
- If endpoint DTOs accidentally change JSON shape during extraction, clients could break.

### Ready for Proposal
Yes — scope is clear, low-to-medium risk, and directly unblocks a clean `SalesWebAdapter` implementation pattern.
