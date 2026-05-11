package com.buses.examen.Progra.service.application;

import com.buses.examen.Progra.route.adapter.in.web.dto.response.RouteCatalogResponse;
import com.buses.examen.Progra.route.adapter.in.web.mapper.RouteWebMapper;
import com.buses.examen.Progra.service.adapter.in.web.dto.response.ServiceCatalogResponse;
import com.buses.examen.Progra.service.application.port.in.ServiceQueryUseCase;
import com.buses.examen.Progra.service.application.port.out.ServicioRepositoryPort;
import com.buses.examen.Progra.service.domain.Servicio;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Servicio de aplicación para consultas de servicios programados.
 */
@Service
public class ServiceService implements ServiceQueryUseCase {
    private static final Map<String, String> COUNTRY_FLAGS = Map.of(
            "CR", "🇨🇷", "NI", "🇳🇮", "ES", "🇸🇻",
            "GUA", "🇬🇹", "HN", "🇭🇳", "PN", "🇵🇦"
    );

    private final ServicioRepositoryPort servicioRepositoryPort;
    private final RouteWebMapper routeWebMapper;

    /**
     * Crea el servicio con su puerto requerido.
     *
     * @param servicioRepositoryPort puerto de servicios
     * @param routeWebMapper mapper de rutas para el catálogo
     */
    public ServiceService(final ServicioRepositoryPort servicioRepositoryPort, final RouteWebMapper routeWebMapper) {
        this.servicioRepositoryPort = servicioRepositoryPort;
        this.routeWebMapper = routeWebMapper;
    }

    /** {@inheritDoc} */
    @Override
    public List<Servicio> listServicesForRoute(final Long rutaId, final OffsetDateTime start, final OffsetDateTime end) {
        return servicioRepositoryPort.findByRutaIdAndSalidaProgramadaBetween(rutaId, start, end);
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Servicio> findServiceById(final Long servicioId) {
        return servicioRepositoryPort.findById(servicioId);
    }

    /** {@inheritDoc} */
    @Override
    public List<ServiceCatalogResponse> catalog(final Long rutaId, final OffsetDateTime start, final OffsetDateTime end) {
        final List<Servicio> servicios;
        if (rutaId != null) {
            servicios = servicioRepositoryPort.findByRutaIdAndSalidaProgramadaBetween(rutaId, start, end);
        } else {
            servicios = servicioRepositoryPort.findAll().stream()
                    .filter(s -> !s.getSalidaProgramada().isBefore(start) && !s.getSalidaProgramada().isAfter(end))
                    .toList();
        }
        return servicios.stream().map(this::toServiceCatalogResponse).toList();
    }

    private ServiceCatalogResponse toServiceCatalogResponse(final Servicio servicio) {
        final var route = servicio.getRuta();
        final var origin = route.getCiudadOrigen().getPais();
        final var dest = route.getCiudadDestino().getPais();

        final RouteCatalogResponse routeResponse = new RouteCatalogResponse(
                route.getId(),
                origin.getCodigoIso(),
                origin.getNombre(),
                COUNTRY_FLAGS.getOrDefault(origin.getCodigoIso(), "🏳️"),
                dest.getCodigoIso(),
                dest.getNombre(),
                COUNTRY_FLAGS.getOrDefault(dest.getCodigoIso(), "🏳️"),
                route.getDuracionMinutos() / 60,
                servicio.getPrecioBase()
        );

        return new ServiceCatalogResponse(
                servicio.getId(),
                route.getId(),
                servicio.getBus().getId(),
                servicio.getSalidaProgramada(),
                servicio.getCapacidadDisponible(),
                servicio.getPrecioBase(),
                routeResponse
        );
    }
}
