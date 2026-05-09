# Spec: Customer Credential Security

## ADDED Requirements

### Requirement: Customer registration includes credentials
Customer registration MUST accept username and password input in addition to customer profile data.

#### Scenario: Registering a customer with credentials
- **GIVEN** a registration request contains profile data, username, and password
- **WHEN** the customer registration use case succeeds
- **THEN** a `Cliente` MUST be persisted for profile data
- **AND** a separate `UserSecurity` record MUST be persisted for authentication data
- **AND** `UserSecurity` MUST be linked 1:1 to the `Cliente`.

### Requirement: Username is unique
The system MUST reject customer registration when the requested username already exists.

#### Scenario: Duplicate username
- **GIVEN** a `UserSecurity` record already exists with username `user@example.com`
- **WHEN** another registration attempts to use `user@example.com`
- **THEN** the registration MUST fail with a domain/application-specific duplicate-username error
- **AND** no second `Cliente`/`UserSecurity` pair MUST be persisted for that request.

### Requirement: Password is hashed before persistence
Plain passwords MUST never be persisted. Password hashing MUST happen through an outbound application port.

#### Scenario: Password registration
- **GIVEN** a registration request contains a raw password
- **WHEN** the application service handles registration
- **THEN** it MUST call `PasswordHasherPort` with the raw password
- **AND** persist only the returned password hash in `UserSecurity`
- **AND** the raw password MUST NOT be exposed in persisted entities or application result records.

### Requirement: Login/session implementation remains out of scope
This change MUST prepare credential data for future session authentication without implementing the full login/session filter chain.

#### Scenario: Credentials are registered
- **GIVEN** a customer has a `UserSecurity` record
- **WHEN** this change completes
- **THEN** no token-only/stateless authentication flow MUST be introduced
- **AND** full session login behavior MUST remain deferred to a later change.
