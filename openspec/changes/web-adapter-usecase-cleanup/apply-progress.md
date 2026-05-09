# Apply Progress — web-adapter-usecase-cleanup

## Implementation Progress

**Change**: web-adapter-usecase-cleanup  
**Mode**: Strict TDD

### Completed Tasks
- [x] Added focused `@WebMvcTest` contract tests for customer, fleet, geography, route, service, and loyalty controllers.
- [x] Replaced inbound `*WebAdapter` classes with thin `*Controller` classes.
- [x] Extracted request/response DTOs into top-level records under `adapter/in/web/dto/*`.
- [x] Extracted non-trivial mapping into `adapter/in/web/mapper/*WebMapper` classes.
- [x] Migrated customer write use case to command/result contracts.
- [x] Added Bean Validation to customer request DTOs and `@Valid` to customer controller request bodies.
- [x] Refined card expiration boundary to `fechaExpiracion` (`MM/yyyy`) parsed into `YearMonth` at application boundary.
- [x] Updated architecture, inbound port, mapper, controller, and customer service tests.

### Files Changed
| Area | What Was Done |
|------|---------------|
| `*/adapter/in/web/*Controller.java` | Introduced thin HTTP controllers replacing old web adapters. |
| `*/adapter/in/web/dto/**` | Added top-level request/response DTO records. |
| `*/adapter/in/web/mapper/**` | Added mapper classes for DTO/domain/result conversion. |
| `customer/application/command/**` | Added customer registration/card command records. |
| `customer/application/result/**` | Added customer registration/card result records. |
| `CustomerService`, `RegisterCustomerUseCase` | Migrated write methods to command/result contracts. |
| `src/test/java/**/adapter/in/web/**` | Added/updated WebMvc contract and validation tests. |
| `WebAdapterHexagonalBoundaryTest`, `InboundPortsContractTest` | Updated architecture and inbound contract guards. |

### TDD Cycle Evidence
| Task | Test File | Layer | Safety Net | RED | GREEN | TRIANGULATE | REFACTOR |
|------|-----------|-------|------------|-----|-------|-------------|----------|
| Controller split + API preservation | `*ControllerWebMvcTest.java` | Web slice | Existing controller behavior from slice 1 | ✅ Tests targeted old fat adapter behavior and failed until controllers/DTOs/mappers existed | ✅ `./mvnw clean test` passed with 77 tests | ✅ Covered success, lookup, 404, and response JSON fields across features | ✅ Extracted controllers, DTOs, and mappers without changing endpoint contracts |
| Customer command/result write boundary | `InboundPortsContractTest`, `CustomerServiceTest` | Contract/unit | Existing customer registration tests | ✅ Contract tests required command/result signatures before implementation was complete | ✅ Focused customer tests and full suite passed | ✅ Covered register and register-card command/result paths | ✅ Removed primitive-heavy write signatures from inbound port |
| Bean Validation | `CustomerControllerWebMvcTest` | Web slice | Existing customer WebMvc tests | ✅ Invalid payload tests failed before DTO constraints/`@Valid` were added | ✅ `CustomerControllerWebMvcTest` passed with 6 tests, full suite passed | ✅ Covered invalid customer and invalid card payloads | ✅ Kept validation at web DTO boundary |
| Card expiration boundary | `CustomerControllerWebMvcTest`, `CustomerServiceTest`, `InboundPortsContractTest` | Web/unit/contract | Existing card registration tests | ✅ Tests updated to require `fechaExpiracion` and `YearMonth` command boundary | ✅ Focused customer tests passed with 14 tests, full suite passed | ✅ Covered valid `MM/yyyy` and invalid format returning 400 | ✅ Kept domain persistence month/year unchanged to avoid broad schema churn |

### Test Summary
- **Focused validation**: `CustomerControllerWebMvcTest` passed with 6 tests.
- **Focused customer/contract validation**: `CustomerControllerWebMvcTest`, `CustomerServiceTest`, `InboundPortsContractTest` passed with 14 tests.
- **Full verification**: `./mvnw clean test` with `JAVA_HOME=C:\Program Files\Java\jdk-25.0.2` passed with 77 tests, 0 failures, 0 errors, 0 skipped.

### Deviations from Design
- Mapper classes are currently instantiated directly by controllers in some cases instead of constructor-injected. This is acceptable for the cleanup but can be improved later.
- Card expiration uses `YearMonth` at the web/application boundary while keeping the existing `Tarjeta` month/year persistence model unchanged to avoid broad schema churn.

### Issues Found
- A stale compiled `CustomerWebAdapter` in `target` caused a false ambiguous mapping failure after source deletion; `./mvnw clean test` resolved it.
- Existing domain entities still contain JPA/Spring annotations; treated as baseline architecture debt outside this cleanup.

### Remaining Tasks
- [ ] Consider constructor-injecting web mappers in a follow-up cleanup.
- [ ] Add customer mapper unit tests if expiration parsing grows more complex.

### Status
All cleanup tasks are completed and runtime verification is GREEN under JDK 25.
