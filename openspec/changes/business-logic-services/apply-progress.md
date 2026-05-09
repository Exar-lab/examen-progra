# Apply Progress — business-logic-services (slice 2)

## Implementation Progress

**Change**: business-logic-services  
**Mode**: Strict TDD

### Completed Tasks
- [x] 2.3 Implement `sales/application/SalesService.java` with transactional purchase orchestration and business-rule enforcement.
- [x] 2.4 Keep sales orchestration in application layer with outbound ports for persistence, ticket-code generation, and receipt request.
- [x] 3.3 Add focused purchase tests for success and rejection scenarios from `ticket-purchasing/spec.md`.
- [x] 3.4 Add transaction-boundary reflection check for `SalesService#purchase`.
- [x] 4.2 Keep feature-first package layout and avoid new inbound web adapters.

### Files Changed
| File | Action | What Was Done |
|------|--------|---------------|
| `src/main/java/com/buses/examen/Progra/sales/application/SalesService.java` | Created | Added purchase orchestration use case implementation with `@Transactional`, ticket limit, purchase-window, seat, unique-code, persistence, loyalty, and receipt orchestration. |
| `src/main/java/com/buses/examen/Progra/sales/application/port/out/TicketCodeGeneratorPort.java` | Created | Added outbound port for unique ticket-code candidate generation. |
| `src/main/java/com/buses/examen/Progra/sales/application/port/out/ComprobantePdfPort.java` | Created | Added outbound port for receipt PDF generation request. |
| `src/main/java/com/buses/examen/Progra/sales/adapter/out/pdf/UuidTicketCodeGeneratorAdapter.java` | Created | Added technical adapter for code generation based on UUID. |
| `src/main/java/com/buses/examen/Progra/sales/adapter/out/pdf/NoOpComprobantePdfAdapter.java` | Created | Added temporary adapter for PDF generation request boundary. |
| `src/main/java/com/buses/examen/Progra/service/domain/Servicio.java` | Modified | Added `getPrecioBase()` accessor used by sales orchestration. |
| `src/test/java/com/buses/examen/Progra/sales/application/SalesServiceTest.java` | Created | Added focused JUnit+Mockito tests for purchase scenarios and transaction annotation. |
| `openspec/changes/business-logic-services/tasks.md` | Modified | Marked slice 2 tasks as completed. |

### TDD Cycle Evidence
| Task | Test File | Layer | Safety Net | RED | GREEN | TRIANGULATE | REFACTOR |
|------|-----------|-------|------------|-----|-------|-------------|----------|
| 2.3 + 3.3 | `src/test/java/com/buses/examen/Progra/sales/application/SalesServiceTest.java` | Unit | N/A (new files) | ✅ Written first (referencing missing `SalesService` and new ports) | ✅ Passed after `JAVA_HOME` was corrected to JDK 25 | ✅ Added multiple scenarios: success, 6-ticket rejection, 8-day rejection, insufficient seats, duplicate code collision | ✅ Extracted constants and helper methods in service |
| 3.4 | `src/test/java/com/buses/examen/Progra/sales/application/SalesServiceTest.java` | Unit/Reflection | N/A (new file) | ✅ Written | ✅ Passed after `JAVA_HOME` was corrected to JDK 25 | ➖ Single scenario (annotation presence) | ➖ None needed |

### Test Summary
- **Total tests written**: 6
- **Total tests passing**: 6 focused sales tests; full suite later passed with 77 tests under JDK 25
- **Layers used**: Unit (6), Integration (0), E2E (0)
- **Approval tests**: None — no refactor-only task
- **Pure functions created**: 0

### Deviations from Design
- Added a temporary no-op PDF adapter implementation (`NoOpComprobantePdfAdapter`) to satisfy Spring wiring while keeping PDF generation behind an outbound port.

### Issues Found
- Maven originally used Java 21 through `JAVA_HOME`, causing `release version 25 not supported`; updating `JAVA_HOME` to `C:\Program Files\Java\jdk-25.0.2` resolved validation.

### Remaining Tasks
- [x] Re-run `./mvnw test` in a Java 25-compatible environment to confirm GREEN cycle end-to-end.

### Status
5/5 slice-2 tasks completed in code and artifacts. Verification is GREEN under JDK 25.
