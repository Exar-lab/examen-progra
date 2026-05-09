package com.buses.examen.Progra.route.adapter.in.web;

import com.buses.examen.Progra.route.adapter.in.web.dto.response.RouteOptionResponse;
import com.buses.examen.Progra.route.adapter.in.web.dto.response.RouteResponse;
import com.buses.examen.Progra.route.adapter.in.web.mapper.RouteWebMapper;
import com.buses.examen.Progra.route.application.port.in.RouteQueryUseCase;
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

/** Controlador HTTP para rutas y planeación. */
@RestController
@RequestMapping("/api/routes")
public class RouteController {
    private final RouteQueryUseCase routeQueryUseCase;
    private final RouteWebMapper mapper;

    /**
     * Crea el controlador HTTP de rutas.
     *
     * @param routeQueryUseCase caso de uso de consulta de rutas
     * @param mapper mapper de dominio a DTOs web
     */
    public RouteController(final RouteQueryUseCase routeQueryUseCase, final RouteWebMapper mapper) {
        this.routeQueryUseCase = routeQueryUseCase;
        this.mapper = mapper;
    }

    /**
     * Lista rutas disponibles.
     *
     * @return lista de rutas disponibles
     */
    @GetMapping
    public List<RouteResponse> listRoutes() { return routeQueryUseCase.listRoutes().stream().map(mapper::toRouteResponse).toList(); }

    /**
     * Busca una ruta por identificador.
     *
     * @param rutaId identificador de la ruta
     * @return ruta encontrada
     * @throws ResponseStatusException cuando no existe una ruta con el identificador indicado
     */
    @GetMapping("/{rutaId}")
    public RouteResponse findRouteById(@PathVariable final Long rutaId) {
        return routeQueryUseCase.findRouteById(rutaId).map(mapper::toRouteResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ruta no encontrada"));
    }

    /**
     * Planea opciones de viaje según origen, destino y salida mínima.
     *
     * @param originCityId identificador de la ciudad de origen
     * @param destinationCityId identificador de la ciudad de destino
     * @param departureAfter fecha y hora mínima de salida
     * @return opciones de viaje ordenadas por score
     */
    @GetMapping("/plan")
    public List<RouteOptionResponse> planRoutes(@RequestParam final Long originCityId,
                                                 @RequestParam final Long destinationCityId,
                                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final Instant departureAfter) {
        return routeQueryUseCase.planRoutes(originCityId, destinationCityId, departureAfter).stream().map(mapper::toRouteOptionResponse).toList();
    }
}
