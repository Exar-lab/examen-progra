package com.buses.examen.Progra.customer.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Payload HTTP para autenticación de clientes.
 *
 * @param username nombre de usuario registrado
 * @param password contraseña en texto plano
 */
public record LoginRequest(
        @NotBlank
        @Size(max = 60)
        String username,
        @NotBlank
        @Size(max = 120)
        String password
) { }
