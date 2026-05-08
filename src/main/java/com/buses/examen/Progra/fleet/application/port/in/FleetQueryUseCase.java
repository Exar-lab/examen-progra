package com.buses.examen.Progra.fleet.application.port.in;

import com.buses.examen.Progra.fleet.domain.Asiento;
import com.buses.examen.Progra.fleet.domain.Bus;
import com.buses.examen.Progra.fleet.domain.Compania;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de entrada para consultas de flota y asientos.
 */
public interface FleetQueryUseCase {
    /**
     * Lista compañías de transporte activas.
     *
     * @return compañías registradas
     */
    List<Compania> listCompanies();

    /**
     * Lista buses de una compañía.
     *
     * @param companiaId identificador de la compañía
     * @return buses asociados
     */
    List<Bus> listBusesByCompany(Long companiaId);

    /**
     * Lista los asientos de un bus específico.
     *
     * @param busId identificador del bus
     * @return asientos del bus
     */
    List<Asiento> listSeatsByBus(Long busId);

    /**
     * Busca un bus por su id.
     *
     * @param busId identificador del bus
     * @return bus encontrado, si existe
     */
    Optional<Bus> findBusById(Long busId);
}
