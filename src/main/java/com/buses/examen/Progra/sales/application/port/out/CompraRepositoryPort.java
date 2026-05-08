package com.buses.examen.Progra.sales.application.port.out;

import com.buses.examen.Progra.sales.domain.Compra;

import java.util.Optional;

/**
 * Puerto de salida para persistencia de compras.
 */
public interface CompraRepositoryPort {
    /**
     * Persiste una compra.
     *
     * @param compra compra a guardar
     * @return compra persistida
     */
    Compra save(Compra compra);

    /**
     * Busca una compra por id.
     *
     * @param id identificador de la compra
     * @return compra encontrada, si existe
     */
    Optional<Compra> findById(Long id);
}
