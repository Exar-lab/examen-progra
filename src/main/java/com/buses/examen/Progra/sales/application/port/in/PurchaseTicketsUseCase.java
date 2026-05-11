package com.buses.examen.Progra.sales.application.port.in;

import com.buses.examen.Progra.sales.application.command.PurchaseTicketsCommand;
import com.buses.examen.Progra.sales.application.result.PurchaseTicketsResult;

/**
 * Puerto de entrada para la compra de tickets.
 */
public interface PurchaseTicketsUseCase {
    /**
     * Ejecuta la compra de tickets para un servicio.
     *
     * @param command comando de compra con cliente, servicio y asientos
     * @return resultado de la compra con ids y códigos de tickets
     */
    PurchaseTicketsResult purchase(PurchaseTicketsCommand command);
}
