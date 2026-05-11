package com.buses.examen.Progra.customer.adapter.in.web.dto.response;

/**
 * Respuesta mínima para validar una sesión autenticada en frontend.
 *
 * @param clienteId identificador del cliente autenticado
 * @param username nombre de usuario autenticado
 */
public record AuthenticatedSessionResponse(Long clienteId, String username) {
}
