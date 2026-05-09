package com.buses.examen.Progra.sales.adapter.in.web.mapper;

import com.buses.examen.Progra.customer.application.AuthenticatedCustomer;
import com.buses.examen.Progra.sales.adapter.in.web.dto.request.PurchaseRequest;
import com.buses.examen.Progra.sales.adapter.in.web.dto.response.PurchaseResponse;
import com.buses.examen.Progra.sales.application.command.PurchaseTicketsCommand;
import com.buses.examen.Progra.sales.application.result.PurchaseTicketsResult;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Mapper entre los DTOs de la capa web y los objetos del caso de uso de compra.
 *
 * <p>La identidad del comprador ({@code clienteId}) se extrae del principal
 * autenticado en sesión — nunca del cuerpo de la solicitud HTTP.</p>
 */
@Component
public class PurchaseWebMapper {

    /**
     * Construye un comando de compra a partir del request web y el principal autenticado.
     *
     * @param request   datos de la compra enviados por el cliente
     * @param principal identidad del cliente autenticado portador del {@code clienteId}
     * @return comando de compra listo para el caso de uso
     */
    public PurchaseTicketsCommand toCommand(@NonNull final PurchaseRequest request,
                                            @NonNull final AuthenticatedCustomer principal) {
        return new PurchaseTicketsCommand(
                principal.clienteId(),
                request.tarjetaId(),
                request.servicioId(),
                request.asientoIds(),
                request.canalCompra(),
                request.codigoOperacionExterna());
    }

    /**
     * Convierte el resultado del caso de uso en la respuesta HTTP.
     *
     * @param result resultado de la compra
     * @return DTO de respuesta para el cliente HTTP
     */
    public PurchaseResponse toResponse(@NonNull final PurchaseTicketsResult result) {
        return new PurchaseResponse(
                result.compraId(),
                result.ticketCodes(),
                result.comprobanteId());
    }
}
