package com.buses.examen.Progra.route.application.port.in;

import com.buses.examen.Progra.route.application.port.out.RoutePlannerPort;
import com.buses.examen.Progra.route.domain.Ruta;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de entrada para consultas de rutas y planificación.
 */
public interface RouteQueryUseCase {
    /**
     * Lista rutas disponibles.
     *
     * @return rutas registradas
     */
    List<Ruta> listRoutes();

    /**
     * Busca una ruta por su id.
     *
     * @param rutaId identificador de la ruta
     * @return ruta encontrada, si existe
     */
    Optional<Ruta> findRouteById(Long rutaId);

    /**
     * Planifica rutas recomendadas usando el motor de planificación.
     *
     * @param originCityId      id de la ciudad origen
     * @param destinationCityId id de la ciudad destino
     * @param departureAfter    instante desde el cual buscar salidas
     * @return opciones de ruta recomendadas
     */
    List<RoutePlannerPort.RouteOption> planRoutes(Long originCityId, Long destinationCityId, Instant departureAfter);
}
