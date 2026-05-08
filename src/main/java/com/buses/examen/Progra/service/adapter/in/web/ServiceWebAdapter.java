package com.buses.examen.Progra.service.adapter.in.web;

import com.buses.examen.Progra.service.application.port.in.ServiceQueryUseCase;
import com.buses.examen.Progra.service.domain.Servicio;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Adaptador web de entrada para consultas de servicios programados.
 */
@RestController
@RequestMapping("/api/services")
public class ServiceWebAdapter {
    private final ServiceQueryUseCase serviceQueryUseCase;

    /**
     * Crea el adaptador con el puerto de consulta de servicios.
     *
     * @param serviceQueryUseCase puerto de entrada
     */
    public ServiceWebAdapter(final ServiceQueryUseCase serviceQueryUseCase) {
        this.serviceQueryUseCase = serviceQueryUseCase;
    }

    /**
     * Lista servicios de una ruta dentro de una ventana temporal.
     *
     * @param rutaId id de la ruta
     * @param start  inicio inclusivo
     * @param end    fin inclusivo
     * @return servicios programados
     */
    @GetMapping
    public List<ServiceResponse> listServicesForRoute(@RequestParam final Long rutaId,
                                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                      final OffsetDateTime start,
                                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                      final OffsetDateTime end) {
        return serviceQueryUseCase.listServicesForRoute(rutaId, start, end)
                .stream()
                .map(ServiceResponse::from)
                .toList();
    }

    /**
     * Busca un servicio por id.
     *
     * @param servicioId identificador del servicio
     * @return servicio encontrado
     */
    @GetMapping("/{servicioId}")
    public ServiceResponse findServiceById(@PathVariable final Long servicioId) {
        return serviceQueryUseCase.findServiceById(servicioId)
                .map(ServiceResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Servicio no encontrado"));
    }

    /** Respuesta pública de servicio programado. */
    public record ServiceResponse(Long id, OffsetDateTime salidaProgramada, Long busId, int capacidadDisponible) {
        static ServiceResponse from(final Servicio servicio) {
            return new ServiceResponse(servicio.getId(), servicio.getSalidaProgramada(),
                    servicio.getBus().getId(), servicio.getCapacidadDisponible());
        }
    }
}
