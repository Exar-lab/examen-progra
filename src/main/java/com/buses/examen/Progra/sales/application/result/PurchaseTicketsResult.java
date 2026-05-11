package com.buses.examen.Progra.sales.application.result;

import java.util.List;

/**
 * Resultado de una compra de tickets.
 *
 * @param compraId      identificador de la compra
 * @param ticketCodes   códigos únicos generados
 * @param comprobanteId identificador del comprobante emitido
 */
public record PurchaseTicketsResult(
        Long compraId,
        List<String> ticketCodes,
        Long comprobanteId) {
}
