package com.buses.examen.Progra.route.adapter.in.web.mapper;

import com.buses.examen.Progra.route.adapter.in.web.dto.response.RouteOptionResponse;
import com.buses.examen.Progra.route.adapter.in.web.dto.response.RouteResponse;
import com.buses.examen.Progra.route.application.port.out.RoutePlannerPort;
import com.buses.examen.Progra.route.domain.Ruta;
import org.springframework.stereotype.Component;

/** Mapper de dominio de rutas hacia DTOs web. */
@Component
public class RouteWebMapper {
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
}
