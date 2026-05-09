package com.buses.examen.Progra.sales.application.result;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * Resultado de consulta de datos del comprobante en formato JSON.
 *
 * @param comprobanteId identificador del comprobante
 * @param numero número documental del comprobante
 * @param serie serie documental del comprobante
 * @param tipo tipo del comprobante
 * @param fechaEmision fecha/hora de emisión
 * @param montoTotal monto total emitido
 * @param moneda moneda del comprobante
 * @param clienteNombre nombre completo del cliente
 * @param clienteEmail email del cliente
 * @param tickets tickets incluidos en el comprobante
 * @param fechaCompra fecha/hora de compra
 */
public record ComprobanteJsonResult(
        Long comprobanteId,
        String numero,
        String serie,
        String tipo,
        OffsetDateTime fechaEmision,
        BigDecimal montoTotal,
        String moneda,
        String clienteNombre,
        String clienteEmail,
        List<TicketComprobanteResult> tickets,
        OffsetDateTime fechaCompra) {

    /**
     * Resultado de ticket incluido en el comprobante.
     *
     * @param codigo código único del ticket
     * @param precio precio final pagado
     * @param servicioId identificador del servicio programado
     * @param rutaId identificador de la ruta
     * @param salidaProgramada fecha/hora de salida del servicio
     */
    public record TicketComprobanteResult(
            String codigo,
            BigDecimal precio,
            Long servicioId,
            Long rutaId,
            OffsetDateTime salidaProgramada) {}
}
