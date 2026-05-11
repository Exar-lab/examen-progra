package com.buses.examen.Progra.sales.adapter.in.web.dto.response;

import java.util.List;

/**
 * Respuesta de la compra de tickets devuelta al cliente HTTP.
 *
 * @param compraId      identificador de la compra generada
 * @param ticketCodes   códigos únicos asignados a cada ticket
 * @param comprobanteId identificador del comprobante electrónico emitido
 */
public record PurchaseResponse(
        Long compraId,
        List<String> ticketCodes,
        Long comprobanteId) {
}
