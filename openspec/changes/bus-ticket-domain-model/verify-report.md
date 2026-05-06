## Verification Report

**Change**: bus-ticket-domain-model
**Version**: N/A
**Mode**: Strict TDD
**Scope**: Model-only override applied

---

### Completeness
| Metric | Value |
|--------|-------|
| Tasks total | 9 |
| Tasks complete | 9 |
| Tasks incomplete | 0 |

No incomplete model-only tasks found in `openspec/changes/bus-ticket-domain-model/tasks.md`.

---

### Build & Tests Execution

**Build**: ✅ Passed
```powershell
$env:JAVA_HOME="C:\Program Files\Java\jdk-25.0.2"; $env:Path="$env:JAVA_HOME\bin;$env:Path"; .\mvnw.cmd compile
BUILD SUCCESS, Java 25.0.2, exit code 0
```

**Tests**: ✅ 12 passed / ❌ 0 failed / ⚠️ 0 skipped
```powershell
$env:JAVA_HOME="C:\Program Files\Java\jdk-25.0.2"; $env:Path="$env:JAVA_HOME\bin;$env:Path"; .\mvnw.cmd test
Tests run: 12, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS, Java 25.0.2, exit code 0
```

**Coverage**: ➖ Not available — no coverage tool/threshold configured for this Maven project.

---

### TDD Compliance
| Check | Result | Details |
|-------|--------|---------|
| TDD Evidence reported | ✅ | `sdd/bus-ticket-domain-model/apply-progress` contains a TDD Cycle Evidence table. |
| All tasks have tests | ✅ | Model tasks are covered by `BusTicketDomainModelContractTest` and `BusTicketJpaMappingTest`; no model-only task remains incomplete. |
| RED confirmed (tests exist) | ✅ | Reported test files exist. |
| GREEN confirmed (tests pass) | ✅ | Reported test classes pass in the full `./mvnw.cmd test` run. |
| Triangulation adequate | ✅ | Domain invariants, mapping persistence, duplicate ticket code, and duplicate active seat reservation paths are tested. |
| Safety Net for modified files | ✅ | Apply progress reports baseline/safety-net execution before fixes. |

**TDD Compliance**: 6/6 checks passed

---

### Test Layer Distribution
| Layer | Tests | Files | Tools |
|-------|-------|-------|-------|
| Unit/domain contract | 8 | 1 | JUnit 5 + AssertJ |
| Integration (`@DataJpaTest`) | 3 | 1 | Spring Boot Data JPA Test + H2 |
| E2E | 0 | 0 | Not applicable for model-only scope |
| **Total model-focused** | **11** | **2** | |

Note: the full suite also includes 1 existing `@SpringBootTest` context-load test; it is not required for model-only compliance.

---

### Changed File Coverage

Coverage analysis skipped — no coverage tool detected.

---

### Assertion Quality

**Assertion quality**: ✅ All assertions in model-focused tests verify entity metadata, persistence behavior, invariant outcomes, or explicit exception paths. No tautologies, ghost loops, or smoke-test-only assertions found.

---

### Quality Metrics
**Linter**: ➖ Not available
**Type Checker**: ✅ Maven compile passed

---

### Spec Compliance Matrix
| Requirement | Scenario | Test | Result |
|-------------|----------|------|--------|
| Requirement 1 — Feature/Domain Hierarchy | Scenario 1.1 — Feature model roots exist | `BusTicketDomainModelContractTest > shouldExposeExactMandatoryEntitySetAndCoreFields` + source tree inspection | ✅ COMPLIANT |
| Requirement 2 — Mandatory Entity Set | Scenario 2.1 — Entity presence and PK mapping | `BusTicketDomainModelContractTest > shouldExposeExactMandatoryEntitySetAndCoreFields` | ✅ COMPLIANT |
| Requirement 3 — Required Enums | Scenario 3.1 — Enum persistence strategy | `BusTicketJpaMappingTest` schema generation under `@DataJpaTest`; source uses `@Enumerated(EnumType.STRING)` | ✅ COMPLIANT |
| Requirement 4 — Relationship Cardinalities | Scenario 4.1 — FK mapping integrity | `BusTicketJpaMappingTest > shouldPersistOptionalLinksAndUniqueTicketCode` and Hibernate FK DDL in test execution | ✅ COMPLIANT |
| Requirement 5 — Constraints and Indexes | Scenario 5.1 — Duplicate rejection | `BusTicketJpaMappingTest > shouldRejectDuplicateTicketCodeAtDatabaseLevel`; `shouldRejectDuplicateActiveSeatReservationForSameServiceAndSeat` | ⚠️ PARTIAL |
| Requirement 6 — Entity-Level Invariant Helpers | Scenario 6.1 — Invariant enforcement | `BusTicketDomainModelContractTest > shouldRejectSixthTicketForCompra`; `shouldBlockTicketCodeMutation`; `shouldCreateCompraWithAtMostFiveTicketsAndWithoutPersistingCvv` | ✅ COMPLIANT |
| Requirement 7 — Model-Focused Tests | Scenario 7.1 — Mapping and invariant proof | `BusTicketDomainModelContractTest` and `BusTicketJpaMappingTest`; full suite passed | ✅ COMPLIANT |
| Requirement 8 — Scope Guardrails | Scenario 8.1 — Out-of-scope enforcement | Artifact/source review under model-only override | ✅ COMPLIANT |

**Compliance summary**: 7/8 scenarios compliant, 1/8 partial.

---

### Correctness (Static — Structural Evidence)
| Requirement | Status | Notes |
|------------|--------|-------|
| Feature/domain hierarchy | ✅ Implemented | Model packages exist under `customer`, `geography`, `fleet`, `route`, `service`, `sales`, and `loyalty` feature roots. |
| 14 JPA entities | ✅ Implemented | All required entities are annotated with `@Entity` and stable `Long` identity PKs. |
| Required enums | ✅ Implemented | Enum fields use `@Enumerated(EnumType.STRING)`. |
| Relationship cardinalities | ✅ Implemented | Required FKs are represented through JPA associations, including optional `ReservaAsiento.ticket` and `MovimientoPuntos.compra`. |
| Constraints/indexes | ⚠️ Partial test proof | Static unique constraints exist for required identities and active reservation strategy; runtime duplicate tests currently prove ticket code and active seat reservation only. |
| Entity invariant helpers | ✅ Implemented | `Compra.agregarTicket`, `Ticket.actualizarCodigoTicket`, and `Tarjeta.fromGatewayToken`/`@Transient cvv` cover required helpers. |
| Model-focused tests | ✅ Implemented | Uses one domain/reflection test class and one `@DataJpaTest` class. |
| Scope guardrails | ✅ Implemented | Verification did not require controllers, DTOs, auth/security, purchase orchestration, PDF, or payment gateway work. |

---

### Coherence (Design)
| Decision | Followed? | Notes |
|----------|-----------|-------|
| Model-only scope | ✅ Yes | Verification only evaluated model/entity scope. |
| `Long` + `IDENTITY` persistence identity | ✅ Yes | Entities use `Long` IDs with `GenerationType.IDENTITY`. |
| Entity helpers + DB constraints | ✅ Yes | Invariants are entity-level; DB unique constraints are defined and partially runtime-tested. |
| Unique constraint strategy for reservation conflicts | ✅ Yes | `ReservaAsiento` uses unique `(servicio_id, asiento_id, estado_reserva)`. |
| Token/masked-only card storage, CVV excluded | ✅ Yes | `Tarjeta` stores token/masked metadata and marks `cvv` as `@Transient`; tests assert no CVV retention. |

---

### Issues Found

**CRITICAL** (must fix before archive):
None.

**WARNING** (should fix):
- Requirement 5.1 duplicate rejection is only partially proven at runtime: tests cover `ticket.codigoTicket` and active `ReservaAsiento` uniqueness, while other required unique constraints are present statically but not individually duplicate-tested (`bus.placa`, `cliente.documentoIdentidad`, `cliente.email`, `pais.codigoIso`, `comprobante.compraId`, `ciudad(pais,codigo)`, `asiento(bus,numero,piso)`).

**SUGGESTION** (nice to have):
None.

---

### Verdict
PASS WITH WARNINGS

The amended model-only implementation compiles and passes all tests with Java 25. The only model-scope gap is incomplete runtime duplicate-rejection coverage for all specified unique constraints; mappings and constraints are otherwise structurally present.
