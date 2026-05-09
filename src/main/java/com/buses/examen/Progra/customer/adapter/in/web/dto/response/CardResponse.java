package com.buses.examen.Progra.customer.adapter.in.web.dto.response;

/**
 * Respuesta HTTP para tarjeta registrada.
 *
 * @param id identificador de la tarjeta registrada
 * @param enmascarada representación enmascarada de la tarjeta
 */
public record CardResponse(Long id, String enmascarada) { }
