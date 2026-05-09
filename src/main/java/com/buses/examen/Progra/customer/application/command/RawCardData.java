package com.buses.examen.Progra.customer.application.command;

import java.time.YearMonth;

/**
 * Datos sensibles de tarjeta usados solo en frontera de aplicación.
 *
 * @param titular nombre del titular de la tarjeta
 * @param numeroTarjeta número completo de tarjeta (PAN)
 * @param expiracion fecha de expiración año/mes
 * @param cvv código de verificación de la tarjeta
 */
public record RawCardData(
        String titular,
        String numeroTarjeta,
        YearMonth expiracion,
        String cvv
) { }
