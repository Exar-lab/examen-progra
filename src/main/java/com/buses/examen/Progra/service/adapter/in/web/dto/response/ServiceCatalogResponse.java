package com.buses.examen.Progra.service.adapter.in.web.dto.response;

import com.buses.examen.Progra.route.adapter.in.web.dto.response.RouteCatalogResponse;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * Respuesta HTTP para catálogo de servicios consumido por el frontend.
 *
 * @param serviceId identificador del servicio
 * @param routeId identificador de la ruta
 * @param departure fecha y hora de salida
 * @param busId identificador del bus asignado
 * @param availableSeats cupos disponibles
 * @param price precio del servicio
 * @param route datos enriquecidos de la ruta
 */
public record ServiceCatalogResponse(
        Long serviceId,
        Long routeId,
        Long busId,
        OffsetDateTime departure,
        int availableSeats,
        BigDecimal price,
        RouteCatalogResponse route) {
}
