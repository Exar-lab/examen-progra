package com.buses.examen.Progra.loyalty.adapter.in.web.dto.response;

import java.time.OffsetDateTime;

/**
 * Respuesta HTTP para movimiento de puntos.
 *
 * @param fechaMovimiento fecha y hora del movimiento
 * @param compraId identificador de la compra asociada, si existe
 */
public record LoyaltyMovementResponse(OffsetDateTime fechaMovimiento, Long compraId) { }
