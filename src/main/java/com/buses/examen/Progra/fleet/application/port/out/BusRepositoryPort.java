package com.buses.examen.Progra.fleet.application.port.out;

import com.buses.examen.Progra.fleet.domain.Bus;

import java.util.List;
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

    /**
     * Lista buses de una compañía específica.
     *
     * @param companiaId identificador de la compañía
     * @return buses asociados
     */
    List<Bus> findByCompaniaId(Long companiaId);
}
