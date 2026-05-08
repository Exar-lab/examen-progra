package com.buses.examen.Progra.loyalty.adapter.in.web.mapper;

import com.buses.examen.Progra.loyalty.adapter.in.web.dto.response.LoyaltyMovementResponse;
import com.buses.examen.Progra.loyalty.domain.MovimientoPuntos;
import org.springframework.stereotype.Component;

/** Mapper de movimientos de lealtad hacia DTOs web. */
@Component
public class LoyaltyWebMapper {
    /**
     * Convierte un movimiento de puntos a DTO de respuesta.
     *
     * @param movimiento movimiento de puntos de dominio
     * @return respuesta HTTP de movimiento de lealtad
     */
    public LoyaltyMovementResponse toResponse(final MovimientoPuntos movimiento) {
        final Long compraId = movimiento.getCompra() == null ? null : movimiento.getCompra().getId();
        return new LoyaltyMovementResponse(movimiento.getFechaMovimiento(), compraId);
    }
}
