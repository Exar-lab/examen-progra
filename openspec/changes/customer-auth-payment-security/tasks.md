# Tasks: Customer Auth + Payment Security

## Phase 1: Test-first safety net

- [x] 1.1 Extend `src/test/java/com/buses/examen/Progra/customer/application/CustomerServiceTest.java` with RED cases for duplicate username, password hashing, expired-card rejection, and CVV non-persistence.
- [x] 1.2 Extend `src/test/java/com/buses/examen/Progra/customer/adapter/in/web/CustomerControllerWebMvcTest.java` to fail on missing/invalid username, password, and card fields with HTTP 400.
- [x] 1.3 Extend `src/test/java/com/buses/examen/Progra/persistence/adapter/out/persistence/RepositoryAdaptersDataJpaTest.java` to fail until 1:1 `Cliente`↔`UserSecurity`, unique username, and card persistence rules are enforced.

## Phase 2: Domain model

- [x] 2.1 Create `src/main/java/com/buses/examen/Progra/customer/domain/UserSecurity.java` with `username`, `passwordHash`, status flags, and a 1:1 link to `Cliente`.
- [x] 2.2 Update `src/main/java/com/buses/examen/Progra/customer/domain/Cliente.java` to add the inverse security relation only, and add domain exceptions for duplicate username / expired card in `customer/exception/`.

## Phase 3: Application ports/commands

- [x] 3.1 Create `PasswordHasherPort`, `CardDataProtectorPort`, `ClockPort`, and `UserSecurityRepositoryPort` under `customer/application/port/out/`.
- [x] 3.2 Add `RawCardData` and `ProtectedCardData` records, then update `RegisterCustomerCommand` and `RegisterCardCommand` to carry raw username/password/card inputs only.

## Phase 4: Application orchestration

- [x] 4.1 Refactor `src/main/java/com/buses/examen/Progra/customer/application/CustomerService.java` to be transactional, normalize/uniquify username, hash passwords, and persist `Cliente` + `UserSecurity` atomically.
- [x] 4.2 Add card-flow validation in `CustomerService.registerCard()` using `ClockPort`, reject expired cards, call `CardDataProtectorPort`, and ensure the persisted `Tarjeta` never keeps CVV.

## Phase 5: Outbound adapters

- [x] 5.1 Add `BCryptPasswordHasherAdapter`, `SystemClockAdapter`, and `PassThroughCardDataProtectorAdapter` in `customer/adapter/out/security/`.
- [x] 5.2 Add `JpaUserSecurityRepository` and `SpringDataUserSecurityRepository` in `customer/adapter/out/persistence/` with unique-username lookup and 1:1 persistence mapping.

## Phase 6: Web DTO/mapper updates

- [x] 6.1 Update `RegisterCustomerRequest` and `RegisterCardRequest` to accept validated username/password and raw card inputs; remove tokenized card fields from the public contract.
- [x] 6.2 Update `CustomerWebMapper` (and controller wiring if needed) to map the new request shape into the updated commands and `YearMonth` expiration parsing.

## Phase 7: Verification

- [x] 7.1 Run `./mvnw.cmd clean test` and fix any failing `CustomerServiceTest`, WebMvc, or `@DataJpaTest` scenarios until all security rules pass.
