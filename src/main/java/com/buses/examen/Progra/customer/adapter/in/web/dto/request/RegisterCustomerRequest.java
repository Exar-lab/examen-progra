package com.buses.examen.Progra.customer.adapter.in.web.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Payload HTTP para registrar un cliente.
 *
 * @param nombres nombres del cliente
 * @param apellidos apellidos del cliente
 * @param documentoIdentidad documento de identidad del cliente
 * @param nacionalidad nacionalidad del cliente
 * @param email correo electrónico del cliente
 * @param telefono teléfono del cliente
 * @param username nombre de usuario
 * @param password contraseña sin procesar
 */
public record RegisterCustomerRequest(
        @NotBlank
        @Size(max = 100)
        String nombres,
        @NotBlank
        @Size(max = 100)
        String apellidos,
        @NotBlank
        @Size(max = 30)
        String documentoIdentidad,
        @NotBlank
        @Size(max = 60)
        String nacionalidad,
        @NotBlank
        @Email
        @Size(max = 254)
        String email,
        @NotBlank
        @Size(max = 25)
        String telefono,
        @NotBlank
        @Size(min = 4, max = 60)
        String username,
        @NotBlank
        @Size(min = 8, max = 120)
        String password
) { }
