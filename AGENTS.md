# AGENTS.md

## Project facts

- Spring Boot `4.0.6`, Java `25`, Maven wrapper project; entrypoint is `src/main/java/com/buses/examen/Progra/PrograApplication.java`.
- Main package is `com.buses.examen.Progra` (capital `Progra` is real; do not silently rename packages).
- Current runtime config is minimal: `src/main/resources/application.properties` only sets `spring.application.name=Progra`.

## Commands

- Run app: `./mvnw spring-boot:run`
- Compile only: `./mvnw compile`
- Run all tests: `./mvnw test`
- Run one test class: `./mvnw test -Dtest=MyServiceTest`
- Package without tests: `./mvnw package -DskipTests`
- Do **not** run bare `./mvnw`; use a specific Maven goal.

## Architecture conventions

- Organize new code by feature/domain under `src/main/java/com/buses/examen/Progra/<feature>/`, not by global layers.
- Expected feature folders: `controller/`, `service/`, `repository/`, `domain/`, `dto/`, `exception/`; shared wiring belongs in `config/`.
- Controllers should expose DTOs, not JPA entities. Services own business logic and transaction boundaries.
- This service includes Spring Security and OAuth2 Authorization Server dependencies; new endpoints need explicit security consideration.

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
