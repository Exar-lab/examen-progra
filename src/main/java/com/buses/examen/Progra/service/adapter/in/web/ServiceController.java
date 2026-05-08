package com.buses.examen.Progra.service.adapter.in.web;

import com.buses.examen.Progra.service.adapter.in.web.dto.response.ServiceResponse;
import com.buses.examen.Progra.service.adapter.in.web.mapper.ServiceWebMapper;
import com.buses.examen.Progra.service.application.port.in.ServiceQueryUseCase;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.List;

/** Controlador HTTP para servicios programados. */
@RestController
@RequestMapping("/api/services")
public class ServiceController {
    private final ServiceQueryUseCase serviceQueryUseCase;
    private final ServiceWebMapper mapper;

    /**
     * Crea el controlador HTTP de servicios.
     *
     * @param serviceQueryUseCase caso de uso de consulta de servicios
     * @param mapper mapper de dominio a DTOs web
     */
    public ServiceController(final ServiceQueryUseCase serviceQueryUseCase, final ServiceWebMapper mapper) {
        this.serviceQueryUseCase = serviceQueryUseCase;
        this.mapper = mapper;
    }

    /**
     * Lista servicios para una ruta en un rango temporal.
     *
     * @param rutaId identificador de la ruta
     * @param start fecha y hora inicial del rango
     * @param end fecha y hora final del rango
     * @return servicios programados para la ruta en el rango indicado
     */
    @GetMapping
    public List<ServiceResponse> listServicesForRoute(@RequestParam final Long rutaId,
                                                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final OffsetDateTime start,
                                                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final OffsetDateTime end) {
        return serviceQueryUseCase.listServicesForRoute(rutaId, start, end).stream().map(mapper::toResponse).toList();
    }

    /**
     * Busca un servicio por identificador.
     *
     * @param servicioId identificador del servicio
     * @return servicio encontrado
     * @throws ResponseStatusException cuando no existe un servicio con el identificador indicado
     */
    @GetMapping("/{servicioId}")
    public ServiceResponse findServiceById(@PathVariable final Long servicioId) {
        return serviceQueryUseCase.findServiceById(servicioId).map(mapper::toResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Servicio no encontrado"));
    }
}
