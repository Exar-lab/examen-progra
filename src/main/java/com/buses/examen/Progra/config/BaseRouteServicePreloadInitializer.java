package com.buses.examen.Progra.config;

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

    private final EntityManager entityManager;

    public BaseRouteServicePreloadInitializer(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void run(final ApplicationArguments args) {
        final Bus busBase = ensureBaseBus();

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

    private <T> TypedQuery<T> singleResult(final String query, final Class<T> clazz) {
        return entityManager.createQuery(query, clazz).setMaxResults(1);
    }
}
