package com.buses.examen.Progra.route.service;

import java.time.Instant;
import java.util.List;

/** Puerto para planificación de rutas, extensible a motores de grafos externos. */
public interface RoutePlannerPort {

    /**
     * Busca las mejores rutas entre dos ciudades a partir de una fecha de salida.
     *
     * @param originCityId      id de la ciudad de origen
     * @param destinationCityId id de la ciudad de destino
     * @param departureAfter    instante a partir del cual buscar salidas
     * @return lista de opciones de ruta ordenadas por puntuación descendente
     */
    List<RouteOption> findBestRoutes(Long originCityId, Long destinationCityId, Instant departureAfter);

    /**
     * Representa una opción de ruta calculada con su puntuación relativa.
     *
     * @param rutaId     id de la ruta
     * @param servicioId id del servicio específico
     * @param score      puntuación calculada (mayor es mejor)
     */
    record RouteOption(Long rutaId, Long servicioId, int score) { }
}
