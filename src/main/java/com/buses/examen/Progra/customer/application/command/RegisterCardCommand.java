package com.buses.examen.Progra.customer.application.command;

import java.time.YearMonth;

/**
 * Comando de entrada para registrar una tarjeta de cliente.
 *
 * @param clienteId identificador del cliente dueño de la tarjeta
 * @param titular nombre del titular de la tarjeta
 * @param marca marca de la tarjeta
 * @param ultimo4 últimos cuatro dígitos
 * @param fechaExpiracion fecha de expiración de la tarjeta
 * @param tokenReferencia token de referencia del proveedor de pagos
 * @param enmascarada valor enmascarado de la tarjeta
 * @param cvv código de seguridad
 */
public record RegisterCardCommand(
        Long clienteId,
        String titular,
        String marca,
        String ultimo4,
        YearMonth fechaExpiracion,
        String tokenReferencia,
        String enmascarada,
        String cvv
) { }
