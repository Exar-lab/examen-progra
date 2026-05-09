package com.buses.examen.Progra.service.application.port.in;

import com.buses.examen.Progra.service.domain.Servicio;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de entrada para consultas de servicios programados.
 */
public interface ServiceQueryUseCase {
    /**
     * Lista servicios para una ruta dentro de una ventana temporal.
     *
     * @param rutaId id de la ruta
     * @param start  fecha inicial inclusiva
     * @param end    fecha final inclusiva
     * @return servicios programados
     */
    List<Servicio> listServicesForRoute(Long rutaId, OffsetDateTime start, OffsetDateTime end);

    /**
     * Busca un servicio por su id.
     *
     * @param servicioId identificador del servicio
     * @return servicio encontrado, si existe
     */
    Optional<Servicio> findServiceById(Long servicioId);
}
