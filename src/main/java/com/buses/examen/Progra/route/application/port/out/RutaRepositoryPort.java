package com.buses.examen.Progra.route.application.port.out;

import com.buses.examen.Progra.route.domain.Ruta;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para persistencia de rutas.
 */
public interface RutaRepositoryPort {
    /**
     * Persiste una ruta.
     *
     * @param ruta ruta a guardar
     * @return ruta persistida
     */
    Ruta save(Ruta ruta);

    /**
     * Busca una ruta por id.
     *
     * @param id identificador de la ruta
     * @return ruta encontrada, si existe
     */
    Optional<Ruta> findById(Long id);

    /**
     * Lista todas las rutas disponibles.
     *
     * @return rutas registradas
     */
    List<Ruta> findAll();
}
