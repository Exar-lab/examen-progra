# Ticket Purchasing Specification

## Purpose
Orchestrate ticket purchases for scheduled bus services, enforcing business constraints, reserving seats, generating ticket codes, and requesting PDF receipts.

## Requirements

### Requirement: Purchase Constraints
The system MUST enforce business rules for purchasing tickets.

#### Scenario: Successful purchase
- GIVEN a registered customer
- AND a request for 1 to 5 tickets
- AND a scheduled service departing within 7 days from today
- AND sufficient available seats on the service
- WHEN the purchase tickets command is executed
- THEN the system MUST complete the purchase
- AND update the seat availability via the outbound port

#### Scenario: Exceeds ticket limit
- GIVEN a registered customer
- AND a request for 6 or more tickets
- WHEN the purchase tickets command is executed
- THEN the system MUST reject the purchase with a domain exception

#### Scenario: Purchase too far in advance
- GIVEN a registered customer
- AND a scheduled service departing in 8 or more days
- WHEN the purchase tickets command is executed
- THEN the system MUST reject the purchase with a domain exception

#### Scenario: Insufficient seats
- GIVEN a registered customer
- AND a scheduled service with fewer available seats than requested
- WHEN the purchase tickets command is executed
- THEN the system MUST reject the purchase with a domain exception

### Requirement: Ticket Generation
The system MUST generate a unique electronic ticket code for each purchased ticket.

#### Scenario: Generating ticket codes
- GIVEN a successful purchase of multiple tickets
- WHEN the tickets are created
- THEN each ticket MUST have a distinct, generated unique code

### Requirement: Receipt Generation Request
The system MUST request the generation of an electronic PDF receipt after a successful purchase.

#### Scenario: Requesting PDF receipt
- GIVEN a successful ticket purchase
- WHEN the purchase transaction completes
- THEN the system MUST invoke the outbound port to generate a PDF receipt including ticket code, route, schedule/time, and ticket data
