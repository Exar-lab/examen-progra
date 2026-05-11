package com.buses.examen.Progra.sales.adapter.in.web.dto.response;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * DTO de ticket para listado de tickets del usuario autenticado.
 *
 * @param ticketId identificador del ticket
 * @param codigoTicket código único de viaje
 * @param precioFinal precio pagado por el ticket
 * @param fechaEmision fecha/hora de emisión
 */
public record TicketResponse(
        Long ticketId,
        String codigoTicket,
        BigDecimal precioFinal,
        OffsetDateTime fechaEmision) {
}
