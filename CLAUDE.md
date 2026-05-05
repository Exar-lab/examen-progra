# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

Spring Boot 4.0.6 service (`com.buses.examen.Progra`), Java 25, Maven. Domain: bus/transport management.

Key dependencies: Spring Data JPA + MySQL, Spring Security + OAuth2 Authorization Server, Spring Cloud Load Balancer, Bean Validation, Actuator, Lombok.

## Commands

```bash
# Run the application
./mvnw spring-boot:run

# Compile only
./mvnw compile

# Run all tests
./mvnw test

# Run a single test class
./mvnw test -Dtest=MyServiceTest

# Package (skip tests)
./mvnw package -DskipTests
```

> Do NOT run `./mvnw` (full build) after code changes — see global rules.

## Architecture

Organize by feature/domain, not by layer:

```
src/main/java/com/buses/examen/Progra/
  <feature>/
    controller/   # @RestController — DTOs in/out, no entities exposed
    service/      # @Service — all business logic, @Transactional
    repository/   # JpaRepository extensions
    domain/       # JPA entities, records, enums
    dto/          # Request/response records
    exception/    # Domain-specific unchecked exceptions
  config/         # Security, OAuth2, Spring Cloud config
```

Services must be stateless. Use constructor injection with `private final` fields. Never expose JPA entities through the API — always map to DTOs.

## Security

The service runs as an **OAuth2 Authorization Server**. When adding endpoints, consider whether they require token-based authentication and apply Spring Security rules explicitly rather than relying on defaults.

## Coding Standards (Active Skills)

Skills in `.claude/skills/` are auto-loaded by context. Key rules:

- **Naming**: `PascalCase` classes/records, `camelCase` methods/fields, `UPPER_SNAKE_CASE` constants
- **Immutability**: favor records and `final` fields; no setters on domain objects
- **Optional**: `findBy*` methods return `Optional<T>`; use `.map()/.orElseThrow()`, never `.get()`
- **Exceptions**: domain-specific unchecked exceptions (e.g., `BusNotFoundException`); no broad `catch (Exception e)`
- **Logging**: SLF4J via `LoggerFactory.getLogger(...)`, parameterized messages, never string concat
- **Null**: `@NonNull` by default; use Bean Validation (`@NotNull`, `@NotBlank`) on DTO inputs
- **Javadoc**: public and protected members must have Javadoc; use `@param`, `@return`, `@throws`

## Testing

Stack: JUnit 5 + AssertJ + Mockito. Use test slices:

The `pom.xml` uses Spring Boot 4 focused `*-test` starters (for example `spring-boot-starter-webmvc-test`, `spring-boot-starter-data-jpa-test`, and related modules). These are valid Spring Boot 4 artifacts; do not replace them with Spring Boot 3-style assumptions unless Maven resolution proves a problem.

- `@WebMvcTest` for controllers (no full context)
- `@DataJpaTest` for repositories
- `@SpringBootTest` for integration tests — prefer Testcontainers when a test actually needs a real database

No hidden sleeps, no partial mocks, no randomness without a fixed seed.

## CI/CD

- **Claude PR Review** (`.github/workflows/claude-review.yml`): auto-runs on every non-draft PR; posts inline comments and a summary
- **Release Please** (`.github/workflows/release-please.yml`): creates release PRs and GitHub releases on push to `master` using conventional commits (maven release type)

Use conventional commits (`feat:`, `fix:`, `chore:`, etc.) — Release Please parses them to determine version bumps.
