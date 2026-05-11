package com.buses.examen.Progra.fleet.application.port.out;

import com.buses.examen.Progra.fleet.domain.Asiento;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para persistir y consultar asientos.
 */
public interface AsientoRepositoryPort {
    /**
     * Persiste un asiento.
     *
     * @param asiento asiento a persistir
     * @return asiento persistido
     */
    Asiento save(Asiento asiento);

    /**
     * Busca un asiento por su identificador.
     *
     * @param id identificador del asiento
     * @return asiento encontrado, si existe
     */
    Optional<Asiento> findById(Long id);

    /**
     * Lista los asientos pertenecientes a un bus.
     *
     * @param busId identificador del bus
     * @return asientos asociados al bus
     */
    List<Asiento> findByBusId(Long busId);
}
