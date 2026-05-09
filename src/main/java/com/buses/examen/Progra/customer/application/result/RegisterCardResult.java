package com.buses.examen.Progra.customer.application.result;

/**
 * Resultado de registro de tarjeta.
 *
 * @param id identificador de la tarjeta registrada
 * @param enmascarada representación enmascarada de la tarjeta
 */
public record RegisterCardResult(Long id, String enmascarada) { }
