package com.buses.examen.Progra.customer.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Payload HTTP para registrar una tarjeta.
 *
 * @param titular nombre del titular de la tarjeta
 * @param marca marca de la tarjeta
 * @param ultimo4 últimos cuatro dígitos
 * @param fechaExpiracion fecha de expiración en formato MM/yyyy
 * @param tokenReferencia token de referencia del proveedor de pagos
 * @param enmascarada representación enmascarada de la tarjeta
 * @param cvv código de seguridad
 */
public record RegisterCardRequest(
        @NotBlank
        @Size(max = 100)
        String titular,
        @NotBlank
        @Size(max = 30)
        String marca,
        @NotBlank
        @Pattern(regexp = "\\d{4}")
        String ultimo4,
        @NotBlank
        @Pattern(regexp = "^(0[1-9]|1[0-2])/\\d{4}$")
        String fechaExpiracion,
        @NotBlank
        @Size(max = 120)
        String tokenReferencia,
        @NotBlank
        @Size(max = 25)
        String enmascarada,
        @NotBlank
        @Pattern(regexp = "\\d{3,4}")
        String cvv
) { }
