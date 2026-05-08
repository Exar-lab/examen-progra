package com.buses.examen.Progra.fleet.application.port.out;

import com.buses.examen.Progra.fleet.domain.Bus;

import java.util.Optional;

/**
 * Puerto de salida para persistencia de buses.
 */
public interface BusRepositoryPort {
    /**
     * Persiste un bus.
     *
     * @param bus bus a guardar
     * @return bus persistido
     */
    Bus save(Bus bus);

    /**
     * Busca un bus por id.
     *
     * @param id identificador del bus
     * @return bus encontrado, si existe
     */
    Optional<Bus> findById(Long id);
}
