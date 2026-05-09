package com.buses.examen.Progra.customer.application.command;

import java.time.YearMonth;

/**
 * Comando de entrada para registrar una tarjeta de cliente.
 *
 * @param clienteId identificador del cliente dueño de la tarjeta
 * @param titular nombre del titular de la tarjeta
 * @param numeroTarjeta número de tarjeta sin enmascarar
 * @param fechaExpiracion fecha de expiración de la tarjeta
 * @param cvv código de seguridad
 */
public record RegisterCardCommand(
        Long clienteId,
        String titular,
        String numeroTarjeta,
        YearMonth fechaExpiracion,
        String cvv
) { }
