package com.buses.examen.Progra.sales.application.result;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * Vista de ticket para consultas del cliente autenticado.
 *
 * @param ticketId identificador del ticket
 * @param codigoTicket código único del ticket
 * @param precioFinal precio pagado por el ticket
 * @param fechaEmision fecha/hora de emisión
 */
public record TicketViewResult(
        Long ticketId,
        String codigoTicket,
        BigDecimal precioFinal,
        OffsetDateTime fechaEmision) {
}
