package com.buses.examen.Progra.customer.application.result;

/**
 * Artefactos protegidos de tarjeta aptos para persistencia.
 *
 * @param marca marca detectada de la tarjeta
 * @param ultimo4 últimos cuatro dígitos del PAN
 * @param tokenReferencia token de referencia del medio de pago
 * @param enmascarada representación enmascarada para mostrar al usuario
 */
public record ProtectedCardData(
        String marca,
        String ultimo4,
        String tokenReferencia,
        String enmascarada
) { }
