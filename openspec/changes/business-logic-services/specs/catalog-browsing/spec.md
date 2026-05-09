# Catalog Browsing Specification

## Purpose
Provide read-only queries for routes, scheduled services, geography, and fleet to support ticket purchasing and availability checks.

## Requirements

### Requirement: Query Routes
The system MUST return available routes including origins, destinations, and pricing.

#### Scenario: List routes
- GIVEN existing routes in the system
- WHEN the query routes use case is invoked
- THEN the system MUST return a list of routes with their respective details

### Requirement: Query Scheduled Services
The system MUST return scheduled services for a given route and date range.

#### Scenario: List services for a route
- GIVEN an existing route and upcoming scheduled services
- WHEN the query scheduled services use case is invoked for that route
- THEN the system MUST return the available services including departure times and seat capacities
