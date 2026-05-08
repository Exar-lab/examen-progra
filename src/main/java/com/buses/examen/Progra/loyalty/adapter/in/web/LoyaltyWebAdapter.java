package com.buses.examen.Progra.loyalty.adapter.in.web;

import com.buses.examen.Progra.loyalty.application.port.in.LoyaltyQueryUseCase;
import com.buses.examen.Progra.loyalty.domain.MovimientoPuntos;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Adaptador web de entrada para consultas de fidelidad.
 */
@RestController
@RequestMapping("/api/loyalty")
public class LoyaltyWebAdapter {
    private final LoyaltyQueryUseCase loyaltyQueryUseCase;

    /**
     * Crea el adaptador con el puerto de consulta de fidelidad.
     *
     * @param loyaltyQueryUseCase puerto de entrada
     */
    public LoyaltyWebAdapter(final LoyaltyQueryUseCase loyaltyQueryUseCase) {
        this.loyaltyQueryUseCase = loyaltyQueryUseCase;
    }

    /**
     * Lista el historial de puntos de un cliente.
     *
     * @param clienteId identificador del cliente
     * @return movimientos de fidelidad
     */
    @GetMapping("/customers/{clienteId}/points")
    public List<LoyaltyMovementResponse> listHistory(@PathVariable final Long clienteId) {
        return loyaltyQueryUseCase.listHistory(clienteId).stream().map(LoyaltyMovementResponse::from).toList();
    }

    /** Respuesta pública de movimiento de puntos. */
    public record LoyaltyMovementResponse(OffsetDateTime fechaMovimiento, Long compraId) {
        static LoyaltyMovementResponse from(final MovimientoPuntos movimiento) {
            final Long compraId = movimiento.getCompra() == null ? null : movimiento.getCompra().getId();
            return new LoyaltyMovementResponse(movimiento.getFechaMovimiento(), compraId);
        }
    }
}
