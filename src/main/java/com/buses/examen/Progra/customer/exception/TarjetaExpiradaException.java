package com.buses.examen.Progra.customer.exception;

import java.time.YearMonth;

/** Error de dominio cuando la tarjeta está vencida. */
public class TarjetaExpiradaException extends RuntimeException {
    /**
     * Crea la excepción cuando la fecha de expiración ya quedó atrás.
     *
     * @param expiracion fecha de expiración de la tarjeta
     * @param actual año/mes actual usado para validación
     */
    public TarjetaExpiradaException(final YearMonth expiracion, final YearMonth actual) {
        super("Tarjeta expirada: " + expiracion + " < " + actual);
    }
}
