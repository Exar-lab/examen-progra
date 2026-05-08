package com.buses.examen.Progra.sales.application.port.out;

import com.buses.examen.Progra.sales.domain.Comprobante;

import java.util.Optional;

/**
 * Puerto de salida para persistencia de comprobantes.
 */
public interface ComprobanteRepositoryPort {
    /**
     * Persiste un comprobante.
     *
     * @param comprobante comprobante a guardar
     * @return comprobante persistido
     */
    Comprobante save(Comprobante comprobante);

    /**
     * Busca un comprobante por id.
     *
     * @param id identificador del comprobante
     * @return comprobante encontrado, si existe
     */
    Optional<Comprobante> findById(Long id);
}
