package com.buses.examen.Progra.sales.adapter.in.web.dto.response;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * DTO de comprobante para consumo del frontend.
 *
 * @param comprobanteId identificador del comprobante
 * @param numero número del comprobante
 * @param serie serie del comprobante
 * @param tipo tipo de comprobante (FACTURA, BOLETA)
 * @param fechaEmision fecha/hora de emisión
 * @param montoTotal monto total
 * @param moneda moneda
 * @param clienteNombre nombre del cliente
 * @param clienteEmail email del cliente
 * @param tickets lista de tickets comprados
 * @param fechaCompra fecha de la compra
 */
public record ComprobanteResponse(
        Long comprobanteId,
        String numero,
        String serie,
        String tipo,
        OffsetDateTime fechaEmision,
        BigDecimal montoTotal,
        String moneda,
        String clienteNombre,
        String clienteEmail,
        List<TicketComprobanteResponse> tickets,
        OffsetDateTime fechaCompra) {

    /**
     * DTO de ticket incluido dentro del comprobante.
     *
     * @param codigo código único del ticket
     * @param precio precio final pagado
     * @param servicioId identificador del servicio programado
     * @param rutaId identificador de la ruta
     * @param salidaProgramada fecha/hora de salida del servicio
     */
    public record TicketComprobanteResponse(
            String codigo,
            BigDecimal precio,
            Long servicioId,
            Long rutaId,
            OffsetDateTime salidaProgramada) {}
}
