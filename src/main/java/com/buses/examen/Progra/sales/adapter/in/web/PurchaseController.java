package com.buses.examen.Progra.sales.adapter.in.web;

import com.buses.examen.Progra.customer.application.AuthenticatedCustomer;
import com.buses.examen.Progra.sales.adapter.in.web.dto.request.PurchaseRequest;
import com.buses.examen.Progra.sales.adapter.in.web.dto.response.PurchaseResponse;
import com.buses.examen.Progra.sales.adapter.in.web.mapper.PurchaseWebMapper;
import com.buses.examen.Progra.sales.application.port.in.PurchaseTicketsUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private final PurchaseWebMapper mapper;

    /**
     * Crea el controlador de compras.
     *
     * @param purchaseTicketsUseCase caso de uso de compra de tickets
     * @param mapper                 mapper de DTOs web a comandos y resultados
     */
    public PurchaseController(@NonNull final PurchaseTicketsUseCase purchaseTicketsUseCase,
                              @NonNull final PurchaseWebMapper mapper) {
        this.purchaseTicketsUseCase = purchaseTicketsUseCase;
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
}
