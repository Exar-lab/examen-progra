package com.buses.examen.Progra.sales.adapter.in.web.mapper;

import com.buses.examen.Progra.customer.application.AuthenticatedCustomer;
import com.buses.examen.Progra.sales.adapter.in.web.dto.request.PurchaseRequest;
import com.buses.examen.Progra.sales.adapter.in.web.dto.response.ComprobanteResponse;
import com.buses.examen.Progra.sales.adapter.in.web.dto.response.PurchaseResponse;
import com.buses.examen.Progra.sales.adapter.in.web.dto.response.TicketResponse;
import com.buses.examen.Progra.sales.application.command.PurchaseTicketsCommand;
import com.buses.examen.Progra.sales.application.result.ComprobanteJsonResult;
import com.buses.examen.Progra.sales.application.result.PurchaseTicketsResult;
import com.buses.examen.Progra.sales.application.result.TicketViewResult;
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

    /**
     * Convierte una vista de ticket en DTO web.
     *
     * @param result ticket del caso de uso
     * @return DTO de ticket para respuesta HTTP
     */
    public TicketResponse toTicketResponse(@NonNull final TicketViewResult result) {
        return new TicketResponse(
                result.ticketId(),
                result.codigoTicket(),
                result.precioFinal(),
                result.fechaEmision());
    }

    /**
     * Convierte los datos del comprobante en DTO web para que el frontend genere el PDF.
     *
     * @param result comprobante del caso de uso
     * @return DTO de comprobante para respuesta HTTP
     */
    public ComprobanteResponse toComprobanteResponse(@NonNull final ComprobanteJsonResult result) {
        return new ComprobanteResponse(
                result.comprobanteId(),
                result.numero(),
                result.serie(),
                result.tipo(),
                result.fechaEmision(),
                result.montoTotal(),
                result.moneda(),
                result.clienteNombre(),
                result.clienteEmail(),
                result.tickets().stream()
                        .map(this::toTicketComprobanteResponse)
                        .toList(),
                result.fechaCompra());
    }

    private ComprobanteResponse.TicketComprobanteResponse toTicketComprobanteResponse(
            final ComprobanteJsonResult.TicketComprobanteResult result) {
        return new ComprobanteResponse.TicketComprobanteResponse(
                result.codigo(),
                result.precio(),
                result.servicioId(),
                result.rutaId(),
                result.salidaProgramada());
    }
}
