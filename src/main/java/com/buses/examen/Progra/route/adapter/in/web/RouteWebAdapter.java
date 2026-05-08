package com.buses.examen.Progra.route.adapter.in.web;

import com.buses.examen.Progra.route.application.port.in.RouteQueryUseCase;
import com.buses.examen.Progra.route.application.port.out.RoutePlannerPort;
import com.buses.examen.Progra.route.domain.Ruta;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

/**
 * Adaptador web de entrada para consultas de rutas.
 */
@RestController
@RequestMapping("/api/routes")
public class RouteWebAdapter {
    private final RouteQueryUseCase routeQueryUseCase;

    /**
     * Crea el adaptador con el puerto de consulta de rutas.
     *
     * @param routeQueryUseCase puerto de entrada
     */
    public RouteWebAdapter(final RouteQueryUseCase routeQueryUseCase) {
        this.routeQueryUseCase = routeQueryUseCase;
    }

    /** @return rutas disponibles */
    @GetMapping
    public List<RouteResponse> listRoutes() {
        return routeQueryUseCase.listRoutes().stream().map(RouteResponse::from).toList();
    }

    /**
     * Busca una ruta por id.
     *
     * @param rutaId identificador de ruta
     * @return ruta encontrada
     */
    @GetMapping("/{rutaId}")
    public RouteResponse findRouteById(@PathVariable final Long rutaId) {
        return routeQueryUseCase.findRouteById(rutaId)
                .map(RouteResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ruta no encontrada"));
    }

    /**
     * Planifica rutas recomendadas.
     *
     * @param originCityId      ciudad de origen
     * @param destinationCityId ciudad de destino
     * @param departureAfter    instante mínimo de salida
     * @return opciones recomendadas
     */
    @GetMapping("/plan")
    public List<RouteOptionResponse> planRoutes(@RequestParam final Long originCityId,
                                                @RequestParam final Long destinationCityId,
                                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                final Instant departureAfter) {
        return routeQueryUseCase.planRoutes(originCityId, destinationCityId, departureAfter)
                .stream()
                .map(RouteOptionResponse::from)
                .toList();
    }

    /** Respuesta pública de ruta. */
    public record RouteResponse(Long id) {
        static RouteResponse from(final Ruta ruta) {
            return new RouteResponse(ruta.getId());
        }
    }

    /** Respuesta pública de opción planificada. */
    public record RouteOptionResponse(Long rutaId, Long servicioId, int score) {
        static RouteOptionResponse from(final RoutePlannerPort.RouteOption option) {
            return new RouteOptionResponse(option.rutaId(), option.servicioId(), option.score());
        }
    }
}
