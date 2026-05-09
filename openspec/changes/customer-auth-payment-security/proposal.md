# Proposal: Customer Auth + Payment Security

## Intent
Reduce immediate security and compliance risk in customer onboarding/payment registration by introducing credential security boundaries, enforcing card expiration rules, and preventing sensitive card data persistence.

## Scope

### In Scope
- Add a dedicated `UserSecurity` model linked to `Cliente` without polluting customer profile data.
- Enforce business rule: expired cards cannot be registered.
- Introduce password hashing boundary so only `passwordHash` is persisted.
- Introduce card-data protection boundary and guarantee CVV is never persisted.
- Add uniqueness rule for username.
- Define application and outbound ports needed for these capabilities.
- Add tests for domain/application/web/persistence behavior tied to these rules.

### Out of Scope
- Full login UI/endpoints and complete session authentication flow.
- Full Spring Security filter-chain redesign and OAuth2 auth-server setup.
- Replacing session-based security with token-only/stateless auth (explicitly forbidden by project constraints).

## Capabilities

### New Capabilities
- `customer-security-profile`: customer credential record (`UserSecurity`) with hashed password and account status flags.
- `secure-card-registration`: card registration that rejects expired cards and persists only protected card artifacts.

### Modified Capabilities
- `customer-management`: registration flow evolves to include credential creation and username uniqueness validation.

## Approach
Implement this as a **feature-first customer slice**:

1. Keep `UserSecurity` inside `customer/domain` as a dedicated entity, linked 1:1 to `Cliente`.
2. Expand inbound use cases so customer registration can include credentials in the same application transaction.
3. Add outbound security/protection ports (`PasswordHasherPort`, `CardDataProtectorPort`, `ClockPort`, `UserSecurityRepositoryPort`).
4. Keep card and password handling in application/domain boundaries; adapters own concrete hashing/tokenization libraries.
5. Defer full login/session endpoint/filter implementation to a follow-up change, but keep today’s model ready for that step.

## Affected Areas

| Area | Impact | Description |
|------|--------|-------------|
| `src/main/java/com/buses/examen/Progra/customer/domain/` | New/Modified | Add `UserSecurity`; possibly add domain exceptions and expiration validation helpers/value objects. |
| `src/main/java/com/buses/examen/Progra/customer/application/port/in/` | Modified | Add/evolve registration use cases to include credentials and secure-card rules. |
| `src/main/java/com/buses/examen/Progra/customer/application/port/out/` | New/Modified | Add ports for hashing, card protection, clock, and user-security persistence. |
| `src/main/java/com/buses/examen/Progra/customer/application/` | Modified | Update orchestration in service(s) with new invariants and atomic persistence. |
| `src/main/java/com/buses/examen/Progra/customer/adapter/out/persistence/` | Modified | Add JPA adapters/repositories for `UserSecurity` and 1:1 mapping constraints. |
| `src/main/java/com/buses/examen/Progra/customer/adapter/in/web/` | Modified | Evolve request DTOs/mappers for credential input and validation contracts. |
| `src/test/java/com/buses/examen/Progra/customer/**` | Modified | Unit and WebMvc tests for new registration/card-security rules. |
| `src/test/java/com/buses/examen/Progra/persistence/**` | Modified | DataJpa tests for one-to-one mapping and username uniqueness. |

## Risks

| Risk | Likelihood | Mitigation |
|------|------------|------------|
| Scope creep into full authentication system | High | Explicitly defer login/session endpoints and filter-chain changes. |
| Accidental sensitive-data persistence/logging | Medium | Keep raw sensitive fields at boundaries only; persist protected payload only; test CVV non-persistence. |
| Username uniqueness race conditions | Medium | Enforce unique DB constraint + repository pre-check + transactional handling. |

## Rollback Plan
If the change introduces instability, revert customer-security additions (`UserSecurity` model, related ports/adapters, registration-contract changes) and restore prior customer/card registration behavior. Keep DB migration rollback scripts to drop new auth tables/constraints safely.

## Dependencies
- Existing customer registration/card flow from stacked change `business-logic-services/02-sales-orchestration`.
- Existing hexagonal contracts and tests for customer/persistence/web layers.

## Success Criteria
- [ ] `UserSecurity` exists as separate model from `Cliente`, with 1:1 relation and unique username.
- [ ] Password is never persisted in plain text; only hash is stored via `PasswordHasherPort`.
- [ ] Expired cards are rejected by business rule.
- [ ] CVV/CCV is never persisted.
- [ ] Card protection/tokenization is abstracted behind outbound port(s).
- [ ] Tests cover domain/application rules, persistence constraints, and web validation behavior.
