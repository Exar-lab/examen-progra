# Customer Management Specification

## Purpose
Manage registration and lookup of customers and their payment cards to enable ticket purchasing.

## Requirements

### Requirement: Register Customer
The system MUST allow registering a new customer with first name, last name, passport, nationality, email, and phone.

#### Scenario: Valid registration
- GIVEN valid customer details
- WHEN the register customer command is executed
- THEN the customer MUST be saved via the outbound port
- AND the created customer details MUST be returned

### Requirement: Register Card
The system MUST allow registering a payment card for an existing customer with card number, CCV, and expiration date.

#### Scenario: Valid card registration
- GIVEN an existing customer ID and valid card details
- WHEN the register card command is executed
- THEN the card MUST be saved and linked to the customer via the outbound port

### Requirement: Lookup Customer
The system MUST provide lookup of customer details.

#### Scenario: Lookup by passport
- GIVEN an existing customer with a specific passport
- WHEN the lookup is requested by that passport
- THEN the system MUST return the customer details

#### Scenario: Customer not found
- GIVEN a non-existent passport
- WHEN the lookup is requested
- THEN the system MUST return an empty result or domain exception
