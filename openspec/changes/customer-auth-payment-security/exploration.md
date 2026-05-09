## Exploration: customer-auth-payment-security

### Current State
The `customer` feature currently handles registration and card storage through `CustomerService` using `ClienteRepositoryPort` and `TarjetaRepositoryPort`. `Tarjeta` already avoids CVV persistence by keeping `cvv` as `@Transient` and nulling it in `fromGatewayToken`, but card expiration is not validated as a business rule. There is no dedicated authentication model (no username/password entity, no password hash flow), and there is no security configuration package yet (`config/` currently absent).

### Affected Areas
- `src/main/java/com/buses/examen/Progra/customer/domain/Cliente.java` — currently only personal/contact fields; candidate aggregate root relation for auth.
- `src/main/java/com/buses/examen/Progra/customer/domain/Tarjeta.java` — stores tokenized card metadata; needs expiration rule enforcement point.
- `src/main/java/com/buses/examen/Progra/customer/application/CustomerService.java` — current orchestration point for customer/card registration; place to split or add auth-aware use cases.
- `src/main/java/com/buses/examen/Progra/customer/application/port/in/RegisterCustomerUseCase.java` — currently only customer+card commands; should evolve to credential-aware inbound contracts.
- `src/main/java/com/buses/examen/Progra/customer/application/port/out/*.java` — needs expansion with security/protection ports (`PasswordHasherPort`, card protector/tokenizer, clock).
- `src/main/java/com/buses/examen/Progra/customer/adapter/out/persistence/*.java` — must map new one-to-one auth relation and username uniqueness.
- `src/main/java/com/buses/examen/Progra/customer/adapter/in/web/dto/request/*.java` — registration DTO contracts must include credentials (and keep CVV non-persisted flow).
- `src/test/java/com/buses/examen/Progra/customer/**` and `src/test/java/com/buses/examen/Progra/persistence/**` — expand unit/web/persistence tests for new invariants and mappings.

### Approaches
1. **Keep authentication model inside `customer` feature (recommended)** — add `UserSecurity` under `customer/domain` with dedicated ports/adapters under `customer`.
   - Pros: Preserves feature-first packaging, avoids premature new feature split, keeps customer-registration + credential provisioning in one transactional use case, simplest migration from current `CustomerService`.
   - Cons: `customer` feature grows in scope; requires discipline to avoid mixing auth concerns into `Cliente` entity itself.
   - Effort: Medium

2. **Create dedicated `security` feature with its own domain** — `security/domain/UserSecurity` plus cross-feature reference to `Cliente`.
   - Pros: Strong conceptual separation for future login/session expansion.
   - Cons: Higher complexity now (new feature boundaries, cross-feature persistence wiring, more ports), overkill for this slice where only credential + card protection rules are needed.
   - Effort: Medium/High

### Recommendation
Use **Approach 1** now: keep `UserSecurity` in `customer/domain` as a separate entity related 1:1 with `Cliente` (shared lifecycle but isolated fields).

Recommended model and rules for this change:
- `UserSecurity` relation: one-to-one with `Cliente` (`cliente_id` unique), plus unique `username`.
- `UserSecurity` fields: `username`, `passwordHash`, `enabled` (default true), `locked` (default false), timestamps as needed.
- `Cliente` remains clean (personal profile only); auth fields MUST NOT be added to `Cliente`.
- Inbound use cases to define now:
  - `RegisterCustomerWithCredentialsUseCase` (or evolve register command) to create `Cliente` + `UserSecurity` atomically.
  - `RegisterCardUseCase` (or existing equivalent) with explicit expired-card rule.
  - Do not implement full login/session flow in this slice; only prepare boundaries needed by registration and card protection.
- Outbound ports to add:
  - `PasswordHasherPort` (hash + optional verify contract)
  - `CardDataProtectorPort` (tokenize/protect raw PAN/CVV input into persistable safe payload)
  - `ClockPort` (deterministic expiration checks; avoid hard-coding `YearMonth.now()` in domain/application)
  - `UserSecurityRepositoryPort` (find by username / by cliente id / save)
- Mandatory rules in domain/application:
  - No expired cards (`fechaExpiracion >= current month`)
  - CVV/CCV never persisted
  - Only password hash persisted (never plain password)
  - Username unique

### Risks
- Adding full Spring Security/session login in same slice can explode scope and delay business-risk fixes.
- If card protection responsibility is unclear between controller and application, sensitive data can leak to logs or persistence.
- One-to-one mapping migration may break existing seed/tests if DB schema updates are incomplete.

### Ready for Proposal
Yes — boundaries, ports, invariants, and scope limits are clear enough to produce a focused proposal without implementing login/session internals yet.
