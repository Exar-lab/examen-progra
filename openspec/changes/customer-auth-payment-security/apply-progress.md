# Apply Progress — customer-auth-payment-security

## Mode
Strict TDD

## Completed Tasks
- [x] 1.1 to 7.1

## TDD Cycle Evidence
| Task | Test File | Layer | Safety Net | RED | GREEN | TRIANGULATE | REFACTOR |
|------|-----------|-------|------------|-----|-------|-------------|----------|
| 1.1 | `customer/application/CustomerServiceTest.java` | Unit | ✅ 5/5 baseline | ✅ compile-fail first on new ports/types | ✅ 7/7 passing | ✅ duplicate username + hashing + expiry + CVV null | ✅ constructor/fixtures cleanup |
| 1.2 | `customer/adapter/in/web/CustomerControllerWebMvcTest.java` | WebMvc | ✅ 6/6 baseline | ✅ payloads updated to fail on missing username/password/card | ✅ 6/6 passing | ✅ valid payload + invalid payload paths | ➖ minimal |
| 1.3 | `persistence/adapter/out/persistence/RepositoryAdaptersDataJpaTest.java` | DataJpa | ✅ 6/6 baseline | ✅ tests added for 1:1 + unique username + CVV absence | ✅ 9/9 passing | ✅ positive + uniqueness violation + reload CVV null | ➖ minimal |
| 2.1-6.2 | Same files above | Mixed | N/A | ✅ guided by failing tests | ✅ focused suites pass | ✅ covered by unit/web/jpa combinations | ✅ small targeted refactors only |
| 7.1 | Full suite | Integration | N/A | N/A | ✅ `clean test` 82/82 | N/A | N/A |

## Test Summary
- Focused runs:
  - `./mvnw.cmd test -Dtest=CustomerServiceTest` ✅
  - `./mvnw.cmd test -Dtest=CustomerControllerWebMvcTest` ✅
  - `./mvnw.cmd test -Dtest=RepositoryAdaptersDataJpaTest` ✅
- Final run:
  - `./mvnw.cmd clean test` ✅ (82 tests, 0 failures)

## Notes
- Username normalization/uniqueness enforced at application + DB unique constraint.
- Expired card validation uses `ClockPort` at application layer.
- Added direct unit evidence for scenario `ClockPort=2026-05` + expiration `2026-05` accepted path, including protector invocation and persistence.
- CVV accepted at boundary/protector flow and never persisted in `Tarjeta`.
