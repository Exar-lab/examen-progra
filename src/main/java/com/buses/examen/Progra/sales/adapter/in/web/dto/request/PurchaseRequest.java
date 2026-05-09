package com.buses.examen.Progra.sales.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Datos de entrada para la compra de tickets enviados por el cliente HTTP.
 *
 * <p>NO incluye {@code clienteId} — la identidad del comprador se extrae
 * del principal autenticado en la sesión, nunca del cuerpo de la solicitud.</p>
 *
 * @param tarjetaId               identificador de la tarjeta de pago (puede ser {@code null})
 * @param servicioId              identificador del servicio programado
 * @param asientoIds              asientos solicitados en el servicio
 * @param canalCompra             canal por el que se realiza la compra
 * @param codigoOperacionExterna  código externo de la pasarela de pago
 */
public record PurchaseRequest(
        Long tarjetaId,
        @NotNull Long servicioId,
        @NotEmpty List<Long> asientoIds,
        @NotBlank String canalCompra,
        String codigoOperacionExterna) {
}
