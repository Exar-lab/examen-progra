# Design: Customer Auth + Payment Security

## Technical Approach
Extend the existing `customer` feature slice (hexagonal) to register customer profile, credentials, and secure card metadata with explicit security ports. `CustomerService` remains the use-case orchestrator and adds transactional rules: unique username, password hashing, card expiration validation through a clock abstraction, and CVV non-persistence by design. Full login/session flow stays out of scope.

## Architecture Decisions

### Decision: Keep `UserSecurity` in `customer` feature (separate from `Cliente`)
| Option | Tradeoff | Decision |
|---|---|---|
| New `security` feature now | Better separation, higher cross-feature complexity | Rejected for this slice |
| `UserSecurity` in `customer/domain` with 1:1 relation | Fastest secure onboarding path, contained scope | **Chosen** |

### Decision: Hashing/protection/time via outbound ports
| Option | Tradeoff | Decision |
|---|---|---|
| Direct Spring Security/JDK calls in service/domain | Leaks framework/time concerns into core | Rejected |
| `PasswordHasherPort`, `CardDataProtectorPort`, `ClockPort` | More interfaces, better testability and purity | **Chosen** |

### Decision: CVV never persisted
| Option | Tradeoff | Decision |
|---|---|---|
| Persist CVV encrypted | Higher compliance risk footprint | Rejected |
| Use CVV only for protection/tokenization request | Requires adapter contract redesign | **Chosen** |

## Data Flow

`POST /api/customers`
Web DTO -> `CustomerWebMapper` -> `RegisterCustomerCommand` -> `CustomerService.register()`
-> `UserSecurityRepositoryPort.existsByUsername()`
-> `PasswordHasherPort.hash(rawPassword)`
-> persist `Cliente` + `UserSecurity` (same transaction)
-> `RegisterCustomerResult`.

`POST /api/customers/{id}/cards`
Web DTO (raw PAN/CVV/exp) -> mapper -> `RegisterCardCommand` -> `CustomerService.registerCard()`
-> `ClockPort.currentYearMonth()` for expiry rule
-> `CardDataProtectorPort.protect(...)` returns token/masked/last4/brand
-> create `Tarjeta` with protected payload only
-> persist `Tarjeta`.

## File Changes

| File | Action | Description |
|---|---|---|
| `customer/domain/UserSecurity.java` | Create | New auth entity (`username`, `passwordHash`, `enabled`, `locked`) with `@OneToOne` to `Cliente` and unique username. |
| `customer/domain/Cliente.java` | Modify | Add inverse 1:1 association only (no auth fields mixed into profile semantics). |
| `customer/application/command/RegisterCustomerCommand.java` | Modify | Add credential fields (`username`, `rawPassword`). |
| `customer/application/command/RegisterCardCommand.java` | Modify | Replace token-only inputs with raw card inputs needed by protector adapter; keep CVV transient in command lifecycle. |
| `customer/application/port/out/PasswordHasherPort.java` | Create | Hash raw passwords (and optional verify). |
| `customer/application/port/out/CardDataProtectorPort.java` | Create | Convert PAN/CVV input into safe payload record for persistence. |
| `customer/application/port/out/ClockPort.java` | Create | Provides deterministic current `YearMonth`. |
| `customer/application/port/out/UserSecurityRepositoryPort.java` | Create | Save/find user security and username existence checks. |
| `customer/application/CustomerService.java` | Modify | Add `@Transactional`, uniqueness guard, hash/protect/expiry orchestration. |
| `customer/adapter/out/security/BCryptPasswordHasherAdapter.java` | Create | Spring Security BCrypt implementation of `PasswordHasherPort`. |
| `customer/adapter/out/security/SystemClockAdapter.java` | Create | JDK time adapter implementing `ClockPort`. |
| `customer/adapter/out/security/PassThroughCardDataProtectorAdapter.java` | Create | Initial adapter that normalizes and returns safe artifacts; never returns CVV. |
| `customer/adapter/out/persistence/JpaUserSecurityRepository.java` + `SpringDataUserSecurityRepository.java` | Create | Persistence adapter + Spring Data repository with unique username query methods. |
| `customer/adapter/in/web/dto/request/RegisterCustomerRequest.java` | Modify | Add validated username/password fields. |
| `customer/adapter/in/web/dto/request/RegisterCardRequest.java` | Modify | Accept raw card data fields and expiration; remove `tokenReferencia/enmascarada` from public contract. |
| `customer/adapter/in/web/mapper/CustomerWebMapper.java` | Modify | Map new DTO fields to updated commands. |
| `src/test/java/.../customer/application/CustomerServiceTest.java` | Modify | Add tests for username collision, hashing invocation, expiration rule via fake clock, CVV non-persistence. |
| `src/test/java/.../customer/adapter/in/web/CustomerControllerWebMvcTest.java` | Modify | Validate request schema changes and protected flow inputs. |
| `src/test/java/.../persistence/adapter/out/persistence/RepositoryAdaptersDataJpaTest.java` | Modify | Verify 1:1 mapping and username uniqueness constraint behavior. |

## Interfaces / Contracts

```java
public interface PasswordHasherPort { String hash(String rawPassword); }

public interface ClockPort { YearMonth currentYearMonth(); }

public interface CardDataProtectorPort {
    ProtectedCardData protect(RawCardData raw);
}

public record RawCardData(String titular, String numeroTarjeta, YearMonth expiracion, String cvv) {}
public record ProtectedCardData(String marca, String ultimo4, String tokenReferencia, String enmascarada) {}
```

## Testing Strategy

| Layer | What to Test | Approach |
|---|---|---|
| Unit (application) | Username uniqueness, password hashing, expiry rejection, protector invocation | Mockito + captors + fake `ClockPort`. |
| Unit (domain) | `UserSecurity` construction invariants, relationship expectations | Plain JUnit domain tests. |
| Web slice | DTO validation for new username/password/card fields | `@WebMvcTest` with CSRF and mocked use cases. |
| Persistence slice | 1:1 `cliente`-`user_security`, unique username, CVV absent in persisted card | `@DataJpaTest` querying persisted entities/constraints. |

## Migration / Rollout
Use a single preload/migration strategy (prefer `data.sql` already in project if present; otherwise one initializer, not both). Add new `user_security` table with `cliente_id` unique FK and `username` unique index. Backfill only if existing customers require credentials; otherwise allow phased registration from this release onward. No auth filter-chain rollout in this change.

## Open Questions
- [ ] Should username be globally case-insensitive unique (recommended: normalized lower-case at write)?
- [ ] Is `PassThroughCardDataProtectorAdapter` temporary until real gateway tokenization is integrated in the next change?
