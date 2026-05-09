package com.buses.examen.Progra.customer.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Payload HTTP para registrar una tarjeta.
 *
 * @param titular nombre del titular de la tarjeta
 * @param numeroTarjeta número de tarjeta
 * @param fechaExpiracion fecha de expiración en formato MM/yyyy
 * @param cvv código de seguridad
 */
public record RegisterCardRequest(
        @NotBlank
        @Size(max = 100)
        String titular,
        @NotBlank
        @Pattern(regexp = "\\d{13,19}")
        String numeroTarjeta,
        @NotBlank
        @Pattern(regexp = "^(0[1-9]|1[0-2])/\\d{4}$")
        String fechaExpiracion,
        @NotBlank
        @Pattern(regexp = "\\d{3,4}")
        String cvv
) { }
