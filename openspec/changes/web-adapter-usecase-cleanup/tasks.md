# Tasks: Web Adapter + Use Case Cleanup

## Phase 1: Test-first API and boundary checks

- [x] 1.1 Add `@WebMvcTest` contract tests for `geography`, `fleet`, `route`, `service`, and `loyalty` controllers under `src/test/java/com/buses/examen/Progra/*/adapter/in/web/*ControllerWebMvcTest.java`, freezing current paths, status codes, and JSON fields before refactor.
- [x] 1.2 Add `@WebMvcTest` contract tests for `customer/adapter/in/web/CustomerControllerWebMvcTest.java` covering register, register-card, and lookup responses (`201`, `404`, payload shape).
- [x] 1.3 Update `src/test/java/com/buses/examen/Progra/adapter/in/web/WebAdapterHexagonalBoundaryTest.java` and `src/test/java/com/buses/examen/Progra/application/port/in/InboundPortsContractTest.java` so they fail until the new controller/command-result shape exists.

## Phase 2: Refactor read-only inbound adapters

- [x] 2.1 Split `src/main/java/com/buses/examen/Progra/geography/adapter/in/web/GeographyWebAdapter.java` into `GeographyController`, top-level DTOs, and `GeographyWebMapper` without changing `/api/geography` behavior.
- [x] 2.2 Split `src/main/java/com/buses/examen/Progra/fleet/adapter/in/web/FleetWebAdapter.java` into `FleetController`, DTOs, and `FleetWebMapper`, preserving `/api/fleet` responses.
- [x] 2.3 Split `src/main/java/com/buses/examen/Progra/route/adapter/in/web/RouteWebAdapter.java` into `RouteController`, DTOs, and `RouteWebMapper`, keeping `/plan` mapping identical.
- [x] 2.4 Split `src/main/java/com/buses/examen/Progra/service/adapter/in/web/ServiceWebAdapter.java` into `ServiceController`, DTOs, and `ServiceWebMapper`, preserving query output and 404 handling.
- [x] 2.5 Split `src/main/java/com/buses/examen/Progra/loyalty/adapter/in/web/LoyaltyWebAdapter.java` into `LoyaltyController`, DTOs, and `LoyaltyWebMapper`, keeping null-compra JSON behavior unchanged.

## Phase 3: Refactor customer inbound adapter and write port

- [x] 3.1 Split `src/main/java/com/buses/examen/Progra/customer/adapter/in/web/CustomerWebAdapter.java` into `CustomerController`, request/response DTOs, and `CustomerWebMapper`.
- [x] 3.2 Add `src/main/java/com/buses/examen/Progra/customer/application/command/RegisterCustomerCommand.java`, `RegisterCardCommand.java`, and matching result records under `customer/application/result/`.
- [x] 3.3 Update `src/main/java/com/buses/examen/Progra/customer/application/port/in/RegisterCustomerUseCase.java` and `customer/application/CustomerService.java` to use command/result signatures while keeping registration logic intact.

## Phase 4: Update architecture and contract tests

- [x] 4.1 Refresh `WebAdapterHexagonalBoundaryTest` to reference `*Controller` classes and keep the no-concrete-service / no-domain-signature checks.
- [x] 4.2 Refresh `InboundPortsContractTest` for the new customer write-port method signatures and result types; keep query and purchase contracts unchanged.
- [x] 4.3 Add or update focused mapper unit tests for non-trivial conversions in `route`, `service`, and `loyalty`.

## Phase 5: Verification

- [x] 5.1 Run the focused controller, mapper, and boundary/contract tests for each touched feature.
- [x] 5.2 Run full `./mvnw test` with `JAVA_HOME` pointing to JDK 25, then fix any remaining contract regressions.
