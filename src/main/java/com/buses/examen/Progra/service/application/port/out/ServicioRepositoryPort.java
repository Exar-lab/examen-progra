package com.buses.examen.Progra.service.application.port.out;

import com.buses.examen.Progra.service.domain.Servicio;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para persistencia y búsqueda de servicios programados.
 */
public interface ServicioRepositoryPort {
    /**
     * Persiste un servicio.
     *
     * @param servicio servicio a guardar
     * @return servicio persistido
     */
    Servicio save(Servicio servicio);

    /**
     * Busca un servicio por id.
     *
     * @param id identificador del servicio
     * @return servicio encontrado, si existe
     */
    Optional<Servicio> findById(Long id);

    /**
     * Lista servicios de una ruta dentro de una ventana de salida.
     *
     * @param rutaId identificador de la ruta
     * @param start fecha inicial inclusiva
     * @param end fecha final inclusiva
     * @return servicios que cumplen el criterio
     */
    List<Servicio> findByRutaIdAndSalidaProgramadaBetween(Long rutaId, OffsetDateTime start, OffsetDateTime end);

    /**
     * Lista todos los servicios programados.
     *
     * @return servicios registrados
     */
    List<Servicio> findAll();
}
