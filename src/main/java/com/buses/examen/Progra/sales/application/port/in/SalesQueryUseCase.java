package com.buses.examen.Progra.sales.application.port.in;

import com.buses.examen.Progra.sales.application.result.ComprobanteJsonResult;
import com.buses.examen.Progra.sales.application.result.ComprobantePdfResult;
import com.buses.examen.Progra.sales.application.result.TicketViewResult;

import java.util.List;

/**
 * Puerto de entrada para consultas de tickets y comprobantes del cliente autenticado.
 */
public interface SalesQueryUseCase {

    /**
     * Lista los tickets que pertenecen al cliente autenticado.
     *
     * @param clienteId identificador del cliente autenticado
     * @return lista de tickets del cliente
     */
    List<TicketViewResult> listTicketsForCustomer(Long clienteId);

    /**
     * Obtiene el PDF del comprobante de una compra perteneciente al cliente autenticado.
     *
     * @param clienteId identificador del cliente autenticado
     * @param comprobanteId identificador del comprobante
     * @return contenido PDF y metadatos de nombre de archivo
     */
    ComprobantePdfResult getComprobantePdf(Long clienteId, Long comprobanteId);

    /**
     * Obtiene los datos del comprobante en formato JSON para que el frontend construya el PDF.
     *
     * @param clienteId identificador del cliente autenticado
     * @param comprobanteId identificador del comprobante
     * @return datos del comprobante para construcción en cliente
     */
    ComprobanteJsonResult getComprobanteJson(Long clienteId, Long comprobanteId);
}
