package com.buses.examen.Progra.loyalty.application.port.out;

import com.buses.examen.Progra.loyalty.domain.MovimientoPuntos;

import java.util.List;

/**
 * Puerto de salida para persistencia de movimientos de puntos.
 */
public interface MovimientoPuntosRepositoryPort {
    /**
     * Persiste un movimiento de puntos.
     *
     * @param movimientoPuntos movimiento a guardar
     * @return movimiento persistido
     */
    MovimientoPuntos save(MovimientoPuntos movimientoPuntos);

    /**
     * Obtiene el historial de movimientos de un cliente en orden descendente de fecha.
     *
     * @param clienteId identificador del cliente
     * @return historial de movimientos
     */
    List<MovimientoPuntos> findByClienteIdOrderByFechaMovimientoDesc(Long clienteId);
}
