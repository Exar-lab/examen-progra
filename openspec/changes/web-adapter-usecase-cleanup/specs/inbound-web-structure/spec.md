# Spec: Inbound Web Structure

## ADDED Requirements

### Requirement: Thin HTTP controllers
Inbound HTTP classes MUST be thin controllers responsible only for HTTP concerns: route annotations, request validation, status mapping, and delegation to inbound use-case ports.

#### Scenario: Controller delegates without business orchestration
- **GIVEN** an HTTP request reaches a feature controller
- **WHEN** the controller handles the request
- **THEN** it MUST convert the request through a web mapper when mapping is needed
- **AND** delegate to an inbound port
- **AND** avoid persistence, PDF, seat, pricing, ticket, or route-planning business decisions directly in the controller.

### Requirement: DTOs are top-level web contracts
Request and response DTOs exposed by HTTP controllers MUST be top-level records/classes under the feature's inbound web DTO package, not nested records inside controller classes.

#### Scenario: API contract is visible by file
- **GIVEN** a feature exposes JSON through HTTP
- **WHEN** a request or response payload type is needed
- **THEN** the type MUST live under `adapter/in/web/dto/request` or `adapter/in/web/dto/response`
- **AND** JSON field names MUST remain compatible with the previous API contract unless a spec explicitly changes them.

### Requirement: Mapping is isolated from controllers
Non-trivial conversion between web DTOs and application/domain types MUST be isolated in a web mapper class.

#### Scenario: Controller returns domain data as JSON
- **GIVEN** an application query returns domain objects or application results
- **WHEN** the HTTP response is created
- **THEN** a mapper under `adapter/in/web/mapper` SHOULD transform those values into response DTOs
- **AND** the controller SHOULD not contain inline `from(...)` mapping records or multi-field conversion logic.

### Requirement: Existing HTTP behavior is preserved
The cleanup MUST preserve existing endpoint paths, HTTP methods, status codes, and JSON fields for customer, fleet, geography, route, service, and loyalty endpoints.

#### Scenario: Existing clients call a refactored endpoint
- **GIVEN** a client calls an endpoint that existed before the cleanup
- **WHEN** the endpoint is served by the refactored controller
- **THEN** the client MUST receive the same successful status code shape and JSON field names as before.
