package com.buses.examen.Progra.route.adapter.out.planning;

import com.buses.examen.Progra.fleet.domain.Bus;
import com.buses.examen.Progra.fleet.domain.Compania;
import com.buses.examen.Progra.geography.domain.Ciudad;
import com.buses.examen.Progra.geography.domain.Pais;
import com.buses.examen.Progra.route.domain.Ruta;
import com.buses.examen.Progra.service.application.port.out.ServicioRepositoryPort;
import com.buses.examen.Progra.service.domain.EstadoServicio;
import com.buses.examen.Progra.service.domain.Servicio;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas del adaptador básico de planificación de rutas.
 */
class SimpleRoutePlannerAdapterTest {

    private static long nextId = 1L;

    @Test
    void shouldReturnFutureServicesOrderedByCapacityScore() {
        final Servicio lowCapacity = service(OffsetDateTime.parse("2026-05-08T10:00:00Z"), 4);
        final Servicio highCapacity = service(OffsetDateTime.parse("2026-05-08T11:00:00Z"), 20);
        final Servicio past = service(OffsetDateTime.parse("2026-05-07T10:00:00Z"), 50);
        final SimpleRoutePlannerAdapter adapter = new SimpleRoutePlannerAdapter(repositoryWith(highCapacity, past, lowCapacity));

        assertThat(adapter.findBestRoutes(1L, 2L, Instant.parse("2026-05-08T00:00:00Z")))
                .extracting(option -> option.score())
                .containsExactly(1_020, 1_004);
    }

    @Test
    void shouldReturnEmptyOptionsWhenNoServiceDepartsAfterRequestedInstant() {
        final Servicio past = service(OffsetDateTime.parse("2026-05-07T10:00:00Z"), 50);
        final SimpleRoutePlannerAdapter adapter = new SimpleRoutePlannerAdapter(repositoryWith(past));

        assertThat(adapter.findBestRoutes(1L, 2L, Instant.parse("2026-05-08T00:00:00Z"))).isEmpty();
    }

    private Servicio service(final OffsetDateTime departure, final int capacity) {
        final Pais originCountry = new Pais("CR", "Costa Rica");
        final Pais destinationCountry = new Pais("PA", "Panamá");
        final Ciudad origin = new Ciudad(originCountry, "San José", "SJO");
        final Ciudad destination = new Ciudad(destinationCountry, "Ciudad de Panamá", "PTY");
        final Ruta route = new Ruta(origin, destination, 480, 850);
        final Compania company = new Compania("Central Bus", "CB-1");
        final Bus bus = new Bus(company, "BUS-1", "Volvo", 60);
        final Servicio service = new Servicio(route, bus, departure, departure.plusHours(8), BigDecimal.valueOf(100), EstadoServicio.PROGRAMADO, capacity);
        ReflectionTestUtils.setField(route, "id", nextId++);
        ReflectionTestUtils.setField(service, "id", nextId++);
        return service;
    }

    private ServicioRepositoryPort repositoryWith(final Servicio... services) {
        return new ServicioRepositoryPort() {
            @Override
            public Servicio save(final Servicio servicio) {
                return servicio;
            }

            @Override
            public Optional<Servicio> findById(final Long id) {
                return Optional.empty();
            }

            @Override
            public List<Servicio> findByRutaIdAndSalidaProgramadaBetween(final Long rutaId,
                                                                         final OffsetDateTime start,
                                                                         final OffsetDateTime end) {
                return List.of();
            }

            @Override
            public List<Servicio> findAll() {
                return List.of(services);
            }
        };
    }
}
