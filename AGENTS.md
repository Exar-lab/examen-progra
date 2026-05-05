# AGENTS.md

## Project facts

- Spring Boot `4.0.6`, Java `25`, Maven wrapper project; entrypoint is `src/main/java/com/buses/examen/Progra/PrograApplication.java`.
- Main package is `com.buses.examen.Progra` (capital `Progra` is real; do not silently rename packages).
- Current runtime config is minimal: `src/main/resources/application.properties` only sets `spring.application.name=Progra`.
- Product domain: bus ticket sales for a Central American bus company headquartered in Costa Rica.

## Assignment statement

- Customers buy bus tickets and receive a unique invented ticket code used for travel.
- The company offers routes and scheduled services between countries, each with different prices.
- Tickets can only be bought for services up to one week ahead: `purchaseDate <= departure <= purchaseDate + 7 days`.
- A person cannot buy more than 5 tickets.
- Buyers must be registered or register with: first name, last name, passport, nationality, email, phone, card, CCV, and expiration date.
- Sessions must restrict protected access to registered users.
- After purchase, the system must generate an electronic PDF receipt with the ticket code, route, schedule/time, and ticket data.
- The database must be preloaded with initial data.

## Commands

- Run app: `./mvnw spring-boot:run`
- Compile only: `./mvnw compile`
- Run all tests: `./mvnw test`
- Run one test class: `./mvnw test -Dtest=MyServiceTest`
- Package without tests: `./mvnw package -DskipTests`
- Do **not** run bare `./mvnw`; use a specific Maven goal.

## Architecture conventions

- Use Hexagonal Architecture (Ports and Adapters), organized by feature/domain under `src/main/java/com/buses/examen/Progra/<feature>/`, not by global layers.
- Dependency rule: `domain` is the center and must not depend on web, Spring Security, PDF, persistence adapters, or controllers.
- Expected feature folders: `domain/`, `application/port/in/`, `application/port/out/`, `application/`, `adapter/in/web/`, `adapter/out/persistence/`, `adapter/out/pdf/`, `exception/`; shared wiring belongs in `config/`.
- Model-only changes may stay limited to `domain/` plus focused tests; do not introduce controllers/services/security/PDF when the scope is only the model.
- Controllers are inbound web adapters and should expose DTOs, not JPA entities. Application services own use-case orchestration and transaction boundaries.
- Repositories exposed to application logic should be ports; Spring Data/JPA repositories are outbound adapter details.
- This service includes Spring Security and OAuth2 Authorization Server dependencies; new endpoints need explicit security consideration.
- The assignment requires sessions for registered users. Do not disable CSRF globally for browser/session flows; if using stateless API security, document and enforce it explicitly.
- PDF generation must be behind an outbound port; concrete PDF libraries belong in an adapter.
- Choose one preload strategy per change (`data.sql`, migration tool, or initializer). Do not keep duplicate seed sources.

## Layer model and clean code

- Layer responsibilities must stay explicit:
  - `domain`: business concepts, invariants, entities/value objects, domain exceptions; no Spring/web/persistence/PDF details.
  - `application`: use cases, transactions, commands/results, inbound and outbound ports; no concrete adapter details.
  - `adapter/in/web`: controllers and web DTOs; input validation only, not business rule ownership.
  - `adapter/out/persistence`: Spring Data/JPA adapters and mapping details.
  - `adapter/out/pdf`: concrete PDF receipt generation.
  - `config`: Spring wiring and security/session/OAuth2 configuration.
- Keep code clean: small cohesive classes, short intention-revealing methods, clear domain names, named constants for business limits, and no duplicated business rules.
- Avoid generic `Manager`/`Helper`/`Processor` names unless they describe a real domain concept. Prefer assignment language such as `Compra`, `Ticket`, `Servicio`, `Ruta`, and `Cliente`.
- If a class crosses multiple layers, split it; do not hide architecture violations behind convenience.
- Traditional Spring layer names must map to hexagonal boundaries:
  - `model`/entity belongs in `domain/` and owns invariants only.
  - `repository` interfaces/ports belong in `application/port/out/`; Spring Data/JPA implementations belong in `adapter/out/persistence/`.
  - `service` means application use-case orchestration in `application/`; it should depend on interfaces/ports, not concrete adapters.
  - `dto` belongs at adapter boundaries (`adapter/in/web/`) or as application command/result records; never leak web DTOs into domain.
  - `controller` belongs in `adapter/in/web/` and calls inbound use-case interfaces.
- Use interfaces for boundaries: controllers depend on inbound use-case interfaces; application services depend on outbound ports. Avoid depending on concrete classes across layers.

## Testing notes

- Test dependencies are Spring Boot test starters for actuator, JPA, authorization server, security, validation, and WebMVC.
- Current test suite only has a `@SpringBootTest` context load test; add focused slices when adding real features (`@WebMvcTest`, `@DataJpaTest`) instead of defaulting to full context.
- MySQL connector is runtime-only; prefer Testcontainers or a test slice over assuming a local database exists.

## CI and automation

- Release Please runs on pushes to `master` via `.github/workflows/release-please.yml` with `release-type: maven`; commit messages must be Conventional Commits for releases.
- Claude automation is split across `.github/workflows/claude.yml` for `@claude` interactions and `.github/workflows/claude-code-review.yml` for automatic PR review.
- GGA config lives in `.gga`, reviews Java/Maven/YAML/XML/properties files, and currently reads rules from `CLAUDE.md`.

## Existing instruction source

- Preserve `CLAUDE.md`; it contains the repo-specific Java/Spring conventions used by GGA and Claude tooling.
