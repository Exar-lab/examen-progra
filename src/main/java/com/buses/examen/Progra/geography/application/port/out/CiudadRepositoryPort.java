package com.buses.examen.Progra.geography.application.port.out;

import com.buses.examen.Progra.geography.domain.Ciudad;

import java.util.Optional;

/**
 * Puerto de salida para persistencia de ciudades.
 */
public interface CiudadRepositoryPort {
    /**
     * Persiste una ciudad.
     *
     * @param ciudad ciudad a guardar
     * @return ciudad persistida
     */
    Ciudad save(Ciudad ciudad);

    /**
     * Busca una ciudad por id.
     *
     * @param id identificador de la ciudad
     * @return ciudad encontrada, si existe
     */
    Optional<Ciudad> findById(Long id);
}
