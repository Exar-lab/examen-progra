package com.buses.examen.Progra.loyalty.application.port.in;

import com.buses.examen.Progra.loyalty.domain.MovimientoPuntos;

import java.util.List;

/**
 * Puerto de entrada para consultas de puntos de fidelidad.
 */
public interface LoyaltyQueryUseCase {
    /**
     * Lista el historial de puntos para un cliente.
     *
     * @param clienteId identificador del cliente
     * @return movimientos de puntos en orden descendente por fecha
     */
    List<MovimientoPuntos> listHistory(Long clienteId);
}
