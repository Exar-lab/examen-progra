package com.buses.examen.Progra.route.adapter.in.web.dto.response;

import java.math.BigDecimal;

/**
 * Respuesta HTTP para catálogo de rutas consumido por el frontend.
 *
 * @param routeId identificador de la ruta
 * @param originCode código ISO del país de origen
 * @param originName nombre del país de origen
 * @param originFlag emoji de bandera del país de origen
 * @param destinationCode código ISO del país de destino
 * @param destinationName nombre del país de destino
 * @param destinationFlag emoji de bandera del país de destino
 * @param durationHours duración estimada del viaje en horas
 * @param basePrice precio base del servicio asociado
 */
public record RouteCatalogResponse(
        Long routeId,
        String originCode,
        String originName,
        String originFlag,
        String destinationCode,
        String destinationName,
        String destinationFlag,
        int durationHours,
        BigDecimal basePrice) {
}
