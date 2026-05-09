package com.buses.examen.Progra.geography.application.port.in;

import com.buses.examen.Progra.geography.domain.Ciudad;
import com.buses.examen.Progra.geography.domain.Pais;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de entrada para consultas geográficas.
 */
public interface GeographyQueryUseCase {
    /**
     * Lista países disponibles.
     *
     * @return países registrados
     */
    List<Pais> listCountries();

    /**
     * Lista ciudades pertenecientes a un país.
     *
     * @param paisId identificador del país
     * @return ciudades del país
     */
    List<Ciudad> listCitiesByCountry(Long paisId);

    /**
     * Busca un país por su id.
     *
     * @param paisId identificador del país
     * @return país encontrado, si existe
     */
    Optional<Pais> findCountryById(Long paisId);

    /**
     * Busca una ciudad por su id.
     *
     * @param ciudadId identificador de la ciudad
     * @return ciudad encontrada, si existe
     */
    Optional<Ciudad> findCityById(Long ciudadId);
}
