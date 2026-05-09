package com.buses.examen.Progra.route.application;

import com.buses.examen.Progra.route.application.port.in.RouteQueryUseCase;
import com.buses.examen.Progra.route.application.port.out.RoutePlannerPort;
import com.buses.examen.Progra.route.application.port.out.RutaRepositoryPort;
import com.buses.examen.Progra.route.domain.Ruta;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Servicio de aplicación para consultas y planificación de rutas.
 */
@Service
public class RouteService implements RouteQueryUseCase {
    private final RutaRepositoryPort rutaRepositoryPort;
    private final RoutePlannerPort routePlannerPort;

    /**
     * Crea el servicio con sus puertos requeridos.
     *
     * @param rutaRepositoryPort puerto de rutas
     * @param routePlannerPort   puerto de planificación
     */
    public RouteService(final RutaRepositoryPort rutaRepositoryPort, final RoutePlannerPort routePlannerPort) {
        this.rutaRepositoryPort = rutaRepositoryPort;
        this.routePlannerPort = routePlannerPort;
    }

    /** {@inheritDoc} */
    @Override
    public List<Ruta> listRoutes() {
        return rutaRepositoryPort.findAll();
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Ruta> findRouteById(final Long rutaId) {
        return rutaRepositoryPort.findById(rutaId);
    }

    /** {@inheritDoc} */
    @Override
    public List<RoutePlannerPort.RouteOption> planRoutes(final Long originCityId,
                                                         final Long destinationCityId,
                                                         final Instant departureAfter) {
        return routePlannerPort.findBestRoutes(originCityId, destinationCityId, departureAfter);
    }
}
