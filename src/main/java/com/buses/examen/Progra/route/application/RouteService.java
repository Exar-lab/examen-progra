package com.buses.examen.Progra.route.application;

import com.buses.examen.Progra.route.adapter.in.web.dto.response.RouteCatalogResponse;
import com.buses.examen.Progra.route.adapter.in.web.mapper.RouteWebMapper;
import com.buses.examen.Progra.route.application.port.in.RouteQueryUseCase;
import com.buses.examen.Progra.route.application.port.out.RoutePlannerPort;
import com.buses.examen.Progra.route.application.port.out.RutaRepositoryPort;
import com.buses.examen.Progra.route.domain.Ruta;
import com.buses.examen.Progra.service.application.port.out.ServicioRepositoryPort;
import com.buses.examen.Progra.service.domain.Servicio;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    private final ServicioRepositoryPort servicioRepositoryPort;
    private final RouteWebMapper mapper;

    /**
     * Crea el servicio con sus puertos requeridos.
     *
     * @param rutaRepositoryPort puerto de rutas
     * @param routePlannerPort   puerto de planificación
     * @param servicioRepositoryPort puerto de servicios programados
     * @param mapper mapper de dominio a DTOs web
     */
    public RouteService(final RutaRepositoryPort rutaRepositoryPort,
                        final RoutePlannerPort routePlannerPort,
                        final ServicioRepositoryPort servicioRepositoryPort,
                        final RouteWebMapper mapper) {
        this.rutaRepositoryPort = rutaRepositoryPort;
        this.routePlannerPort = routePlannerPort;
        this.servicioRepositoryPort = servicioRepositoryPort;
        this.mapper = mapper;
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

    /** {@inheritDoc} */
    @Override
    public List<RouteCatalogResponse> catalog() {
        final List<Servicio> servicios = servicioRepositoryPort.findAll();
        return rutaRepositoryPort.findAll().stream()
                .map(ruta -> mapper.toCatalogResponse(ruta, minimumPriceFor(ruta, servicios)))
                .toList();
    }

    private BigDecimal minimumPriceFor(final Ruta ruta, final List<Servicio> servicios) {
        return servicios.stream()
                .filter(servicio -> ruta.getId().equals(servicio.getRuta().getId()))
                .map(Servicio::getPrecioBase)
                .min(BigDecimal::compareTo)
                .orElse(null);
    }
}
