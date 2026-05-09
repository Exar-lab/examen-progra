package com.buses.examen.Progra.fleet.application.port.out;

import com.buses.examen.Progra.fleet.domain.Compania;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para persistencia de compañías de buses.
 */
public interface CompaniaRepositoryPort {
    /**
     * Persiste una compañía.
     *
     * @param compania compañía a guardar
     * @return compañía persistida
     */
    Compania save(Compania compania);

    /**
     * Busca una compañía por id.
     *
     * @param id identificador de la compañía
     * @return compañía encontrada, si existe
     */
    Optional<Compania> findById(Long id);

    /**
     * Lista todas las compañías registradas.
     *
     * @return compañías disponibles
     */
    List<Compania> findAll();
}
