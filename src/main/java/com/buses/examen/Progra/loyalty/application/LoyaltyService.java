package com.buses.examen.Progra.loyalty.application;

import com.buses.examen.Progra.loyalty.application.port.in.LoyaltyQueryUseCase;
import com.buses.examen.Progra.loyalty.application.port.out.MovimientoPuntosRepositoryPort;
import com.buses.examen.Progra.loyalty.domain.MovimientoPuntos;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio de aplicación para consultas de fidelidad.
 */
@Service
public class LoyaltyService implements LoyaltyQueryUseCase {
    private final MovimientoPuntosRepositoryPort movimientoPuntosRepositoryPort;

    /**
     * Crea el servicio con su puerto requerido.
     *
     * @param movimientoPuntosRepositoryPort puerto de movimientos de puntos
     */
    public LoyaltyService(final MovimientoPuntosRepositoryPort movimientoPuntosRepositoryPort) {
        this.movimientoPuntosRepositoryPort = movimientoPuntosRepositoryPort;
    }

    /** {@inheritDoc} */
    @Override
    public List<MovimientoPuntos> listHistory(final Long clienteId) {
        return movimientoPuntosRepositoryPort.findByClienteIdOrderByFechaMovimientoDesc(clienteId);
    }
}
