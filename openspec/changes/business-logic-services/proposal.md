# Proposal: Business Logic Services

## Intent
Implement the application layer (use cases and services) following Hexagonal Architecture. This coordinates domain logic (invariants) and outbound ports (repositories) to fulfill the business requirements (purchasing, scheduling, customer registration) without leaking technical details.

## Scope

### In Scope
- Define inbound ports (`application/port/in/*UseCase`) for `customer`, `geography`, `fleet`, `route`, `service`, `sales`, and `loyalty`.
- Implement stateless application services (`application/*Service`) implementing those ports.
- Establish `@Transactional` boundaries on mutative use cases.
- Orchestrate business rules: limit to 5 tickets per purchase, max 7 days in advance, seat capacity.

### Out of Scope
- Web controllers or REST endpoints (API layer).
- Security configuration (authentication/authorization).
- Actual PDF receipt generation logic (will be an outbound adapter).

## Capabilities

### New Capabilities
- `customer-management`: Register and lookup customers and cards.
- `catalog-browsing`: Query geography, fleet, route, and available services.
- `ticket-purchasing`: Orchestrate ticket purchases, seat reservations, and loyalty points.

### Modified Capabilities
- None

## Approach
Adopt feature-first use-case slices. Each feature directory will contain its specific inbound ports and their implementations.
The `sales` feature will act as the cross-feature orchestrator for purchases, invoking its outbound ports and interacting with repositories to enforce constraints. Domain entities will retain strict invariants.

## Affected Areas

| Area | Impact | Description |
|------|--------|-------------|
| `src/main/java/.../*/application/port/in/` | New | Inbound ports for use cases. |
| `src/main/java/.../*/application/` | New | Application service implementations. |

## Risks

| Risk | Likelihood | Mitigation |
|------|------------|------------|
| Concurrency on seat reservation | Medium | Rely on unique constraints or optimistic locking in repositories; domain entity validates limits. |
| Missing repository queries | High | Extend outbound ports and repository adapters as needed during implementation. |

## Rollback Plan
Delete the newly created `application/` folders and files in each feature package. No existing behavior is broken as they are not wired to inbound adapters yet.

## Dependencies
- Pre-existing domain entities and repository ports from `entity-repositories`.

## Success Criteria
- [ ] All use cases are defined as inbound ports.
- [ ] Application services implement these ports and are correctly annotated.
- [ ] Transactions are correctly scoped on mutative methods.
- [ ] Tests verify application-level orchestration (e.g., enforcing the 5-ticket rule).