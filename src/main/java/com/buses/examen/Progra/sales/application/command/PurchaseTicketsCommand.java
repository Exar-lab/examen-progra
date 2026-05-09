package com.buses.examen.Progra.sales.application.command;

import java.util.List;

/**
 * Datos de entrada para ejecutar una compra de tickets.
 *
 * @param clienteId              identificador del cliente
 * @param tarjetaId              identificador de la tarjeta (puede ser {@code null})
 * @param servicioId             identificador del servicio programado
 * @param asientoIds             asientos solicitados
 * @param canalCompra            canal de compra utilizado
 * @param codigoOperacionExterna código externo de la pasarela
 */
public record PurchaseTicketsCommand(
        Long clienteId,
        Long tarjetaId,
        Long servicioId,
        List<Long> asientoIds,
        String canalCompra,
        String codigoOperacionExterna) {
}
