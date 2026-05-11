package com.buses.examen.Progra.sales.adapter.in.web;

import com.buses.examen.Progra.customer.application.AuthenticatedCustomer;
import com.buses.examen.Progra.sales.adapter.in.web.dto.request.PurchaseRequest;
import com.buses.examen.Progra.sales.adapter.in.web.dto.response.ComprobanteResponse;
import com.buses.examen.Progra.sales.adapter.in.web.dto.response.PurchaseResponse;
import com.buses.examen.Progra.sales.adapter.in.web.dto.response.TicketResponse;
import com.buses.examen.Progra.sales.adapter.in.web.mapper.PurchaseWebMapper;
import com.buses.examen.Progra.sales.application.port.in.PurchaseTicketsUseCase;
import com.buses.examen.Progra.sales.application.port.in.SalesQueryUseCase;
import com.buses.examen.Progra.sales.application.result.OccupiedSeatResult;
import com.buses.examen.Progra.sales.application.result.ComprobantePdfResult;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador HTTP para la compra de tickets.
 *
 * <p>La identidad del comprador se extrae del principal autenticado en sesión
 * a través de {@link AuthenticatedCustomer} — nunca del cuerpo del request.</p>
 */
@RestController
@RequestMapping("/api/purchases")
@PreAuthorize("isAuthenticated()")
public class PurchaseController {

    private final PurchaseTicketsUseCase purchaseTicketsUseCase;
    private final SalesQueryUseCase salesQueryUseCase;
    private final PurchaseWebMapper mapper;

    /**
     * Crea el controlador de compras.
     *
     * @param purchaseTicketsUseCase caso de uso de compra de tickets
     * @param salesQueryUseCase      caso de uso de consulta de ventas
     * @param mapper                 mapper de DTOs web a comandos y resultados
     */
    public PurchaseController(@NonNull final PurchaseTicketsUseCase purchaseTicketsUseCase,
                              @NonNull final SalesQueryUseCase salesQueryUseCase,
                              @NonNull final PurchaseWebMapper mapper) {
        this.purchaseTicketsUseCase = purchaseTicketsUseCase;
        this.salesQueryUseCase = salesQueryUseCase;
        this.mapper = mapper;
    }

    /**
     * Ejecuta la compra de tickets para el cliente autenticado.
     *
     * @param request   datos de la compra (sin clienteId — se toma del principal)
     * @param principal principal autenticado con el {@code clienteId} del comprador
     * @return respuesta HTTP 201 con los datos de la compra realizada
     */
    @PostMapping
    public ResponseEntity<PurchaseResponse> purchase(
            @Valid @RequestBody final PurchaseRequest request,
            @AuthenticationPrincipal final AuthenticatedCustomer principal) {
        final PurchaseResponse response = mapper.toResponse(
                purchaseTicketsUseCase.purchase(mapper.toCommand(request, principal)));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Lista tickets del cliente autenticado.
     *
     * @param principal principal autenticado
     * @return tickets del cliente en orden descendente por fecha de emisión
     */
    @GetMapping("/tickets")
    public ResponseEntity<List<TicketResponse>> listTickets(
            @AuthenticationPrincipal final AuthenticatedCustomer principal) {
        final List<TicketResponse> response = salesQueryUseCase.listTicketsForCustomer(principal.clienteId()).stream()
                .map(mapper::toTicketResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    /**
     * Lista los asientos ocupados de un servicio.
     *
     * @param servicioId identificador del servicio
     * @return ids de asientos ocupados
     */
    @GetMapping("/services/{servicioId}/occupied-seats")
    public ResponseEntity<List<OccupiedSeatResult>> listOccupiedSeats(@PathVariable final Long servicioId) {
        return ResponseEntity.ok(salesQueryUseCase.listOccupiedSeatsForService(servicioId));
    }

    /**
     * Obtiene los datos del comprobante para que el frontend construya el PDF.
     *
     * @param comprobanteId identificador del comprobante
     * @param principal     principal autenticado
     * @return datos del comprobante en JSON
     */
    @GetMapping("/receipts/{comprobanteId}")
    public ResponseEntity<ComprobanteResponse> getReceipt(
            @PathVariable final Long comprobanteId,
            @AuthenticationPrincipal final AuthenticatedCustomer principal) {
        final ComprobanteResponse response = mapper.toComprobanteResponse(
                salesQueryUseCase.getComprobanteJson(principal.clienteId(), comprobanteId));
        return ResponseEntity.ok(response);
    }

    /**
     * Descarga el comprobante PDF de una compra del cliente autenticado.
     *
     * @param comprobanteId identificador del comprobante
     * @param principal     principal autenticado
     * @return documento PDF como attachment
     */
    @GetMapping(value = "/receipts/{comprobanteId}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> downloadReceiptPdf(
            @PathVariable final Long comprobanteId,
            @AuthenticationPrincipal final AuthenticatedCustomer principal) {
        final ComprobantePdfResult result = salesQueryUseCase.getComprobantePdf(principal.clienteId(), comprobanteId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + result.fileName() + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(result.contentBytes());
    }
}
