# Spec: Bus Ticket Domain Model (Model-Only Scope)

## Overview
This spec is intentionally limited to domain modeling and persistence proof. It defines feature/domain folder hierarchy, 14 JPA entities, required enums, JPA mappings, constraints/indexes, and entity-level invariant helpers.

Explicitly out of scope: controllers, DTOs, web endpoints, auth/session/security implementation, purchase orchestration services, PDF generation, payment gateway integration, and full runtime bootstrap guarantees beyond model persistence verification.

## Requirement 1 — Feature/Domain Hierarchy for Models
Code **MUST** organize model classes under `src/main/java/com/buses/examen/Progra/<feature>/domain` using feature folders.

### Acceptance Scenario 1.1 — Feature model roots exist
**Given** the change is implemented
**When** the Java source tree is inspected
**Then** model classes exist under feature folders that include: `customer`, `geography`, `fleet`, `route`, `service`, `sales`, and `loyalty`.

## Requirement 2 — Mandatory Entity Set
The model **MUST** include these 14 JPA entities:
`Cliente`, `Tarjeta`, `Pais`, `Ciudad`, `Compania`, `Bus`, `Ruta`, `Servicio`, `Compra`, `Ticket`, `Comprobante`, `MovimientoPuntos`, `Asiento`, `ReservaAsiento`.

### Acceptance Scenario 2.1 — Entity presence and PK mapping
**Given** the project compiles
**When** JPA entities are enumerated
**Then** all 14 entities exist
**And** each has a stable primary key mapping.

## Requirement 3 — Required Enums
The model **MUST** provide enums needed by these entities (for status/channel/type/card brand fields) and persist them as string values.

### Acceptance Scenario 3.1 — Enum persistence strategy
**Given** enum-backed entity fields
**When** mappings are reviewed
**Then** enum values are stored with `EnumType.STRING`.

## Requirement 4 — Relationship Cardinalities
The model **MUST** implement these relationships:
- `Pais (1) -> (N) Ciudad`
- `Compania (1) -> (N) Bus`
- `Bus (1) -> (N) Asiento`
- `Ruta (N) -> (1) Ciudad origen` and `Ruta (N) -> (1) Ciudad destino`
- `Servicio (N) -> (1) Ruta`, `Servicio (N) -> (1) Bus`
- `Cliente (1) -> (N) Tarjeta`
- `Compra (N) -> (1) Cliente`
- `Compra (1) -> (N) Ticket`
- `Compra (1) -> (1) Comprobante`
- `Ticket (N) -> (1) Servicio`
- `ReservaAsiento (N) -> (1) Servicio`
- `ReservaAsiento (N) -> (1) Asiento`
- `ReservaAsiento (N) -> (0..1) Ticket`
- `MovimientoPuntos (N) -> (1) Cliente`
- `MovimientoPuntos (N) -> (0..1) Compra`

### Acceptance Scenario 4.1 — FK mapping integrity
**Given** schema metadata from JPA mappings
**When** FK definitions are reviewed
**Then** all required cardinalities are represented.

## Requirement 5 — Constraints and Indexes
The model **MUST** define unique constraints/indexes required by domain identity and seat conflict prevention.

Minimum constraints:
- unique: `ticket.codigoTicket`, `bus.placa`, `cliente.documentoIdentidad`, `cliente.email`, `pais.codigoIso`, `comprobante.compraId`
- composite unique: `ciudad(pais,codigo)`, `asiento(bus,numero,piso)`
- reservation conflict protection via uniqueness strategy for `(servicio, asiento[, estado])` aligned to active reservation rule.

### Acceptance Scenario 5.1 — Duplicate rejection
**Given** existing rows with those unique keys
**When** duplicates are persisted
**Then** persistence rejects duplicates.

## Requirement 6 — Entity-Level Invariant Helpers
Entities/value objects **MUST** expose invariant helpers where behavior naturally belongs to the model.

Required invariant helpers:
- `Compra` enforces max 5 tickets.
- `Ticket` code is immutable after creation.
- `Tarjeta` model excludes CVV persistence.

### Acceptance Scenario 6.1 — Invariant enforcement
**Given** invalid model operations violating these rules
**When** helper methods are invoked
**Then** domain validation exceptions are raised.

## Requirement 7 — Model-Focused Tests
Tests **MUST** verify model presence, mappings, constraints, and invariants using focused persistence/domain tests.

### Acceptance Scenario 7.1 — Mapping and invariant proof
**Given** test execution
**When** `@DataJpaTest` and model tests run
**Then** mapping integrity and model invariants pass
**And** no controller/security/web flow tests are required for this change.

## Requirement 8 — Scope Guardrails
This change **MUST NOT** introduce or expand non-model layers.

### Acceptance Scenario 8.1 — Out-of-scope enforcement
**Given** changed files for this change
**When** artifacts are reviewed
**Then** they do not require implementation of controllers, auth/session security, purchase orchestration, PDF generation, or payment gateway integration.
