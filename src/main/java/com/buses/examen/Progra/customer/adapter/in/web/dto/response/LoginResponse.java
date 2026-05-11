package com.buses.examen.Progra.customer.adapter.in.web.dto.response;

/**
 * Respuesta HTTP para autenticación de clientes.
 *
 * @param clienteId identificador del cliente autenticado
 * @param username nombre de usuario autenticado
 */
public record LoginResponse(Long clienteId, String username) { }
