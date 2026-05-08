package com.buses.examen.Progra.route.adapter.out.planning;

import com.buses.examen.Progra.route.application.port.out.RoutePlannerPort;
import com.buses.examen.Progra.service.application.port.out.ServicioRepositoryPort;
import com.buses.examen.Progra.service.domain.Servicio;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Adaptador básico de planificación de rutas.
 *
 * <p>Esta implementación inicial mantiene disponible el puerto de planificación
 * para el slice de consultas. La lógica avanzada de recomendación puede
 * reemplazarse por otro adaptador sin cambiar el puerto ni el servicio de aplicación.</p>
 */
@Component
public class SimpleRoutePlannerAdapter implements RoutePlannerPort {
    private static final int BASE_SCORE = 1_000;

    private final ServicioRepositoryPort servicioRepositoryPort;

    /**
     * Crea el adaptador con el puerto de servicios programados.
     *
     * @param servicioRepositoryPort puerto de servicios
     */
    public SimpleRoutePlannerAdapter(final ServicioRepositoryPort servicioRepositoryPort) {
        this.servicioRepositoryPort = servicioRepositoryPort;
    }

    /** {@inheritDoc} */
    @Override
    public List<RouteOption> findBestRoutes(final Long originCityId, final Long destinationCityId,
                                            final Instant departureAfter) {
        final OffsetDateTime minimumDeparture = OffsetDateTime.ofInstant(departureAfter, ZoneOffset.UTC);
        return servicioRepositoryPort.findAll().stream()
                .filter(servicio -> !servicio.getSalidaProgramada().isBefore(minimumDeparture))
                .map(this::toRouteOption)
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingInt(RouteOption::score).reversed())
                .toList();
    }

    private RouteOption toRouteOption(final Servicio servicio) {
        if (servicio.getRutaId() == null) {
            return null;
        }
        return new RouteOption(servicio.getRutaId(), servicio.getId(), BASE_SCORE + servicio.getCapacidadDisponible());
    }
}
