package com.buses.examen.Progra.route.adapter.in.web.mapper;

import com.buses.examen.Progra.route.adapter.in.web.dto.response.RouteCatalogResponse;
import com.buses.examen.Progra.route.adapter.in.web.dto.response.RouteResponse;
import com.buses.examen.Progra.route.adapter.in.web.dto.response.RouteOptionResponse;
import com.buses.examen.Progra.route.application.port.out.RoutePlannerPort;
import com.buses.examen.Progra.route.domain.Ruta;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

/** Mapper de dominio de rutas hacia DTOs web. */
@Component
public class RouteWebMapper {
    private static final Map<String, String> COUNTRY_FLAGS = Map.of(
            "CR", "🇨🇷", "NI", "🇳🇮", "ES", "🇸🇻",
            "GUA", "🇬🇹", "HN", "🇭🇳", "PN", "🇵🇦"
    );

    /**
     * Convierte una ruta a DTO de respuesta.
     *
     * @param ruta ruta de dominio
     * @return respuesta HTTP de ruta
     */
    public RouteResponse toRouteResponse(final Ruta ruta) { return new RouteResponse(ruta.getId()); }

    /**
     * Convierte una opción planificada a DTO web.
     *
     * @param option opción de ruta planificada
     * @return respuesta HTTP de opción de ruta
     */
    public RouteOptionResponse toRouteOptionResponse(final RoutePlannerPort.RouteOption option) {
        return new RouteOptionResponse(option.rutaId(), option.servicioId(), option.score());
    }

    /**
     * Convierte una ruta de dominio a DTO de catálogo enriquecido para el frontend.
     *
     * @param ruta ruta de dominio
     * @return respuesta HTTP de catálogo de ruta
     */
    public RouteCatalogResponse toCatalogResponse(final Ruta ruta) {
        return toCatalogResponse(ruta, null);
    }

    /**
     * Convierte una ruta de dominio a DTO de catálogo enriquecido para el frontend.
     *
     * @param ruta ruta de dominio
     * @param basePrice precio base del servicio asociado
     * @return respuesta HTTP de catálogo de ruta
     */
    public RouteCatalogResponse toCatalogResponse(final Ruta ruta, final BigDecimal basePrice) {
        final var origin = ruta.getCiudadOrigen().getPais();
        final var dest = ruta.getCiudadDestino().getPais();
        return new RouteCatalogResponse(
                ruta.getId(),
                origin.getCodigoIso(),
                origin.getNombre(),
                COUNTRY_FLAGS.getOrDefault(origin.getCodigoIso(), "🏳️"),
                dest.getCodigoIso(),
                dest.getNombre(),
                COUNTRY_FLAGS.getOrDefault(dest.getCodigoIso(), "🏳️"),
                Math.abs(ruta.getDuracionMinutos()) / 60,
                basePrice
        );
    }
}
