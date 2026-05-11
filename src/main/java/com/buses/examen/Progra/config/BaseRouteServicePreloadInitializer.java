package com.buses.examen.Progra.config;

import com.buses.examen.Progra.customer.domain.Cliente;
import com.buses.examen.Progra.customer.domain.UserSecurity;
import com.buses.examen.Progra.fleet.domain.Asiento;
import com.buses.examen.Progra.fleet.domain.Bus;
import com.buses.examen.Progra.fleet.domain.Compania;
import com.buses.examen.Progra.geography.domain.Ciudad;
import com.buses.examen.Progra.geography.domain.Pais;
import com.buses.examen.Progra.route.domain.Ruta;
import com.buses.examen.Progra.service.domain.EstadoServicio;
import com.buses.examen.Progra.service.domain.Servicio;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

/**
 * Precarga catálogo base de países/ciudades/rutas/servicios según README.
 */
@Component
public class BaseRouteServicePreloadInitializer implements ApplicationRunner {

    private static final String BASE_COMPANY_NAME = "Progra Bus Tickets";
    private static final String BASE_COMPANY_TAX_ID = "CR-PROGRA-001";
    private static final String BASE_BUS_PLATE = "CR-BASE-001";
    private static final String BASE_BUS_MODEL = "Marcopolo G7";
    private static final int BASE_BUS_CAPACITY = 50;
    private static final ZoneOffset CENTRAL_AMERICA_OFFSET = ZoneOffset.of("-06:00");
    private static final LocalDate BASE_SERVICE_DATE = LocalDate.now(ZoneOffset.UTC);
    private static final int SERVICE_WINDOW_DAYS = 7;
    private static final int DEFAULT_ROUTE_DURATION_MINUTES = 480;
    private static final double DEFAULT_ROUTE_DISTANCE_KM = 500.0d;
    private static final int DEFAULT_TRIP_DURATION_HOURS = 8;
    private static final String DEFAULT_SEAT_CATEGORY = "REGULAR";

    private static final String PRELOAD_PASSWORD = "password";

    private static final record PreloadCliente(String nombres, String apellidos, String documento,
                                                String nacionalidad, String email, String telefono, String username) {}

    private final EntityManager entityManager;
    private final PasswordEncoder passwordEncoder;

    public BaseRouteServicePreloadInitializer(final EntityManager entityManager, final PasswordEncoder passwordEncoder) {
        this.entityManager = entityManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(final ApplicationArguments args) {
        final Bus busBase = ensureBaseBus();
        ensureSeatsForBus(busBase);

        ensureService("CR", "Costa Rica", "NI", "Nicaragua", 3, 80, busBase);
        ensureService("CR", "Costa Rica", "ES", "El Salvador", 6, 120, busBase);
        ensureService("CR", "Costa Rica", "GUA", "Guatemala", 6, 140, busBase);
        ensureService("CR", "Costa Rica", "NI", "Nicaragua", 6, 80, busBase);
        ensureService("CR", "Costa Rica", "HN", "Honduras", 6, 110, busBase);
        ensureService("CR", "Costa Rica", "PN", "Panamá", 5, 80, busBase);
        ensureService("PN", "Panamá", "CR", "Costa Rica", 8, 80, busBase);
        ensureService("GUA", "Guatemala", "CR", "Costa Rica", 5, 140, busBase);
        ensureService("GUA", "Guatemala", "ES", "El Salvador", 5, 80, busBase);
        ensureService("GUA", "Guatemala", "HN", "Honduras", 5, 110, busBase);
        ensureService("GUA", "Guatemala", "NI", "Nicaragua", 5, 120, busBase);
        ensureService("NI", "Nicaragua", "CR", "Costa Rica", 3, 80, busBase);
        ensureService("NI", "Nicaragua", "CR", "Costa Rica", 6, 80, busBase);

        ensurePreloadClientes();
    }

    private void ensureService(final String originCode, final String originName,
                                final String destinationCode, final String destinationName,
                                final int departureHour, final int price,
                                final Bus bus) {
        final Pais originCountry = ensureCountry(originCode, originName);
        final Pais destinationCountry = ensureCountry(destinationCode, destinationName);
        final Ciudad originCity = ensureCity(originCountry, originName, originCode);
        final Ciudad destinationCity = ensureCity(destinationCountry, destinationName, destinationCode);
        final Ruta route = ensureRoute(originCity, destinationCity);

        for (int dayOffset = 0; dayOffset < SERVICE_WINDOW_DAYS; dayOffset++) {
            final LocalDate serviceDate = BASE_SERVICE_DATE.plusDays(dayOffset);
            final OffsetDateTime departure = serviceDate.atTime(departureHour, 0).atOffset(CENTRAL_AMERICA_OFFSET);
            final OffsetDateTime arrival = departure.plusHours(DEFAULT_TRIP_DURATION_HOURS);

            if (findService(route.getId(), departure).isEmpty()) {
                final Servicio service = new Servicio(
                        route,
                        bus,
                        departure,
                        arrival,
                        BigDecimal.valueOf(price),
                        EstadoServicio.PROGRAMADO,
                        BASE_BUS_CAPACITY
                );
                entityManager.persist(service);
            }
        }
    }

    private Bus ensureBaseBus() {
        final Compania company = findCompanyByName(BASE_COMPANY_NAME)
                .orElseGet(() -> {
                    final Compania newCompany = new Compania(BASE_COMPANY_NAME, BASE_COMPANY_TAX_ID);
                    entityManager.persist(newCompany);
                    return newCompany;
                });

        return findBusByPlate(BASE_BUS_PLATE)
                .orElseGet(() -> {
                    final Bus newBus = new Bus(company, BASE_BUS_PLATE, BASE_BUS_MODEL, BASE_BUS_CAPACITY);
                    entityManager.persist(newBus);
                    return newBus;
                });
    }

    private void ensureSeatsForBus(final Bus bus) {
        for (int seatNumber = 1; seatNumber <= BASE_BUS_CAPACITY; seatNumber++) {
            final String seatLabel = String.valueOf(seatNumber);
            if (findSeat(bus.getId(), seatLabel, 1).isEmpty()) {
                entityManager.persist(new Asiento(bus, seatLabel, 1, DEFAULT_SEAT_CATEGORY));
            }
        }
    }

    private Pais ensureCountry(final String isoCode, final String countryName) {
        return findCountryByIso(isoCode)
                .orElseGet(() -> {
                    final Pais country = new Pais(isoCode, countryName);
                    entityManager.persist(country);
                    return country;
                });
    }

    private Ciudad ensureCity(final Pais country, final String cityName, final String cityCode) {
        return findCityByCountryAndCode(country.getId(), cityCode)
                .orElseGet(() -> {
                    final Ciudad city = new Ciudad(country, cityName, cityCode);
                    entityManager.persist(city);
                    return city;
                });
    }

    private Ruta ensureRoute(final Ciudad origin, final Ciudad destination) {
        return findRoute(origin.getId(), destination.getId())
                .orElseGet(() -> {
                    final Ruta route = new Ruta(origin, destination, DEFAULT_ROUTE_DURATION_MINUTES, DEFAULT_ROUTE_DISTANCE_KM);
                    entityManager.persist(route);
                    return route;
                });
    }

    private Optional<Pais> findCountryByIso(final String isoCode) {
        return singleResult("""
                select p
                from Pais p
                where p.codigoIso = :isoCode
                """, Pais.class)
                .setParameter("isoCode", isoCode)
                .getResultList()
                .stream()
                .findFirst();
    }

    private Optional<Ciudad> findCityByCountryAndCode(final Long countryId, final String cityCode) {
        return singleResult("""
                select c
                from Ciudad c
                where c.pais.id = :countryId
                  and c.codigo = :cityCode
                """, Ciudad.class)
                .setParameter("countryId", countryId)
                .setParameter("cityCode", cityCode)
                .getResultList()
                .stream()
                .findFirst();
    }

    private Optional<Ruta> findRoute(final Long originId, final Long destinationId) {
        return singleResult("""
                select r
                from Ruta r
                where r.ciudadOrigen.id = :originId
                  and r.ciudadDestino.id = :destinationId
                """, Ruta.class)
                .setParameter("originId", originId)
                .setParameter("destinationId", destinationId)
                .getResultList()
                .stream()
                .findFirst();
    }

    private Optional<Servicio> findService(final Long routeId, final OffsetDateTime departure) {
        return singleResult("""
                select s
                from Servicio s
                where s.ruta.id = :routeId
                  and s.salidaProgramada = :departure
                """, Servicio.class)
                .setParameter("routeId", routeId)
                .setParameter("departure", departure)
                .getResultList()
                .stream()
                .findFirst();
    }

    private Optional<Compania> findCompanyByName(final String companyName) {
        return singleResult("""
                select c
                from Compania c
                where c.nombreComercial = :companyName
                """, Compania.class)
                .setParameter("companyName", companyName)
                .getResultList()
                .stream()
                .findFirst();
    }

    private Optional<Bus> findBusByPlate(final String plate) {
        return singleResult("""
                select b
                from Bus b
                where b.placa = :plate
                """, Bus.class)
                .setParameter("plate", plate)
                .getResultList()
                .stream()
                .findFirst();
    }

    private Optional<Asiento> findSeat(final Long busId, final String seatNumber, final int floor) {
        return singleResult("""
                select a
                from Asiento a
                where a.bus.id = :busId
                  and a.numero = :seatNumber
                  and a.piso = :floor
                """, Asiento.class)
                .setParameter("busId", busId)
                .setParameter("seatNumber", seatNumber)
                .setParameter("floor", floor)
                .getResultList()
                .stream()
                .findFirst();
    }

    private void ensurePreloadClientes() {
        final var clientesPrecargados = new PreloadCliente[] {
            new PreloadCliente("Juan", "Pérez", "155012345678", "Costa Rica", "juan.perez@correo.com", "8888-1111", "juanperez"),
            new PreloadCliente("María", "González", "155987654321", "Costa Rica", "maria.gonzalez@correo.com", "8888-2222", "mariagonzalez"),
            new PreloadCliente("Carlos", "Rodríguez", "155456789123", "Nicaragua", "carlos.rodriguez@correo.com", "8888-3333", "carlosrodriguez"),
            new PreloadCliente("Ana", "López", "155789123456", "Guatemala", "ana.lopez@correo.com", "8888-4444", "analopez"),
            new PreloadCliente("Luis", "Martínez", "155321654987", "Panamá", "luis.martinez@correo.com", "8888-5555", "luismartinez")
        };

        for (final PreloadCliente c : clientesPrecargados) {
            final Cliente cliente = findClientByDocument(c.documento())
                    .orElseGet(() -> createPreloadCliente(c));
            ensurePreloadUserSecurity(cliente, c.username());
        }
    }

    private Cliente createPreloadCliente(final PreloadCliente c) {
        final Cliente cliente = new Cliente(c.nombres(), c.apellidos(), c.documento(),
                c.nacionalidad(), c.email(), c.telefono());
        entityManager.persist(cliente);
        return cliente;
    }

    private void ensurePreloadUserSecurity(final Cliente cliente, final String username) {
        final String passwordHash = passwordEncoder.encode(PRELOAD_PASSWORD);
        final Optional<UserSecurity> existingUserSecurity = findUserSecurityByUsername(username);

        if (existingUserSecurity.isEmpty()) {
            final UserSecurity userSecurity = new UserSecurity(cliente, username, passwordHash, true, false);
            entityManager.persist(userSecurity);
            return;
        }

        if (!passwordEncoder.matches(PRELOAD_PASSWORD, existingUserSecurity.get().getPasswordHash())) {
            entityManager.createQuery("""
                    update UserSecurity u
                    set u.passwordHash = :passwordHash,
                        u.enabled = true,
                        u.locked = false
                    where u.username = :username
                    """)
                    .setParameter("passwordHash", passwordHash)
                    .setParameter("username", username)
                    .executeUpdate();
        }
    }

    private Optional<Cliente> findClientByDocument(final String documento) {
        return singleResult("""
                select c
                from Cliente c
                where c.documentoIdentidad = :documento
                """, Cliente.class)
                .setParameter("documento", documento)
                .getResultList()
                .stream()
                .findFirst();
    }

    private Optional<UserSecurity> findUserSecurityByUsername(final String username) {
        return singleResult("""
                select u
                from UserSecurity u
                where u.username = :username
                """, UserSecurity.class)
                .setParameter("username", username)
                .getResultList()
                .stream()
                .findFirst();
    }

    private <T> TypedQuery<T> singleResult(final String query, final Class<T> clazz) {
        return entityManager.createQuery(query, clazz).setMaxResults(1);
    }
}
