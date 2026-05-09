# Spec: Secure Card Registration

## ADDED Requirements

### Requirement: Expired cards are rejected
Card registration MUST reject expired cards using deterministic application time from `ClockPort`.

#### Scenario: Registering an expired card
- **GIVEN** the current month from `ClockPort` is `2026-05`
- **AND** a card expiration value is `2026-04`
- **WHEN** the customer registers the card
- **THEN** the use case MUST reject the card as expired
- **AND** no card record MUST be persisted.

#### Scenario: Registering a card expiring this month
- **GIVEN** the current month from `ClockPort` is `2026-05`
- **AND** a card expiration value is `2026-05`
- **WHEN** the customer registers the card
- **THEN** the use case MUST accept the card expiration window.

### Requirement: CVV is never persisted
CVV/CCV MAY be accepted at the request/application boundary only for card protection, but MUST NOT be persisted.

#### Scenario: Card protection uses CVV
- **GIVEN** a card registration request contains a CVV
- **WHEN** the application service protects and stores the card
- **THEN** the CVV MAY be passed to `CardDataProtectorPort`
- **BUT** the persisted `Tarjeta` MUST NOT contain the CVV.

### Requirement: Card data is protected before persistence
Raw card data MUST be converted to protected card artifacts through `CardDataProtectorPort` before persistence.

#### Scenario: Persisting a protected card
- **GIVEN** a card registration request contains raw card data
- **WHEN** the customer card use case succeeds
- **THEN** the application service MUST call `CardDataProtectorPort`
- **AND** persist only protected card artifacts such as brand, last four digits, token/reference, and masked display value.

### Requirement: Web DTO validates card input shape
The inbound web request MUST validate required card fields before application orchestration.

#### Scenario: Invalid card request payload
- **GIVEN** a card registration request is missing required card fields or contains invalid expiration format
- **WHEN** the web controller handles the request
- **THEN** it MUST return HTTP 400 without invoking the application use case.
