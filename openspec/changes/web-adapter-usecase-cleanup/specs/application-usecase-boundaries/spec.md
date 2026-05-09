# Spec: Application Use Case Boundaries

## ADDED Requirements

### Requirement: Write orchestration stays in application services
Business write/save orchestration MUST stay in application services implementing inbound use-case ports. Inbound web controllers MUST NOT own save workflows or business rules.

#### Scenario: Customer registration is requested
- **GIVEN** a customer registration HTTP request
- **WHEN** the request reaches the web layer
- **THEN** the web layer MUST map it to an application command
- **AND** call `RegisterCustomerUseCase`
- **AND** `CustomerService` MUST own registration orchestration and outbound repository interaction.

### Requirement: Customer write ports use command/result contracts
Customer write inbound ports SHOULD use command/result records to avoid primitive-heavy method signatures at application boundaries.

#### Scenario: Registering a customer through the application port
- **GIVEN** a caller has customer registration data
- **WHEN** it calls the customer write use case
- **THEN** it SHOULD pass a `RegisterCustomerCommand`
- **AND** receive a `RegisterCustomerResult`.

#### Scenario: Registering a customer card through the application port
- **GIVEN** a caller has card registration data
- **WHEN** it calls the customer write use case
- **THEN** it SHOULD pass a `RegisterCardCommand`
- **AND** receive a `RegisterCardResult`.

### Requirement: Query ports remain behaviorally stable in this slice
Existing query use-case behavior MUST remain stable during this cleanup to keep the refactor low risk.

#### Scenario: Query endpoints are refactored
- **GIVEN** a query endpoint for catalog, geography, fleet, route, service, loyalty, or customer lookup
- **WHEN** the inbound web class is split into controller, DTOs, and mapper
- **THEN** the application query port behavior MUST remain unchanged.

### Requirement: Architecture guards cover the new structure
Tests MUST guard that controllers depend on inbound ports and DTOs/mappers, not concrete persistence adapters or outbound implementation details.

#### Scenario: Architecture tests run
- **GIVEN** the cleanup has renamed `*WebAdapter` classes to `*Controller`
- **WHEN** boundary tests inspect the inbound web layer
- **THEN** they MUST validate the new controller classes and prevent dependencies on persistence adapters, Spring Data repositories, or domain leakage in public web signatures.
