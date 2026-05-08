package com.buses.examen.Progra.geography.application.port.out;

import com.buses.examen.Progra.geography.domain.Pais;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para persistencia de países.
 */
public interface PaisRepositoryPort {
    /**
     * Persiste un país.
     *
     * @param pais país a guardar
     * @return país persistido
     */
    Pais save(Pais pais);

    /**
     * Busca un país por id.
     *
     * @param id identificador del país
     * @return país encontrado, si existe
     */
    Optional<Pais> findById(Long id);

    /**
     * Lista todos los países registrados.
     *
     * @return países disponibles
     */
    List<Pais> findAll();
}
