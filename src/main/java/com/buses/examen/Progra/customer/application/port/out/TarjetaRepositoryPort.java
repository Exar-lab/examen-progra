package com.buses.examen.Progra.customer.application.port.out;

import com.buses.examen.Progra.customer.domain.Tarjeta;

import java.util.Optional;

/**
 * Puerto de salida para persistencia de tarjetas.
 */
public interface TarjetaRepositoryPort {
    /**
     * Persiste una tarjeta.
     *
     * @param tarjeta tarjeta a guardar
     * @return tarjeta persistida
     */
    Tarjeta save(Tarjeta tarjeta);

    /**
     * Busca una tarjeta por id.
     *
     * @param id identificador de la tarjeta
     * @return tarjeta encontrada, si existe
     */
    Optional<Tarjeta> findById(Long id);
}
