package com.buses.examen.Progra.loyalty.adapter.in.web;

import com.buses.examen.Progra.loyalty.adapter.in.web.dto.response.LoyaltyMovementResponse;
import com.buses.examen.Progra.loyalty.adapter.in.web.mapper.LoyaltyWebMapper;
import com.buses.examen.Progra.loyalty.application.port.in.LoyaltyQueryUseCase;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Controlador HTTP para historial de puntos de lealtad. */
@RestController
@RequestMapping("/api/loyalty")
public class LoyaltyController {
    private final LoyaltyQueryUseCase loyaltyQueryUseCase;
    private final LoyaltyWebMapper mapper;

    /**
     * Crea el controlador HTTP de historial de lealtad.
     *
     * @param loyaltyQueryUseCase caso de uso de consulta de movimientos
     * @param mapper mapper de dominio a DTO web
     */
    public LoyaltyController(final LoyaltyQueryUseCase loyaltyQueryUseCase, final LoyaltyWebMapper mapper) {
        this.loyaltyQueryUseCase = loyaltyQueryUseCase;
        this.mapper = mapper;
    }
    /**
     * Lista movimientos de puntos para un cliente.
     *
     * @param clienteId identificador del cliente
     * @return historial de movimientos de puntos del cliente
     */
    @GetMapping("/customers/{clienteId}/points")
    public List<LoyaltyMovementResponse> listHistory(@PathVariable final Long clienteId) {
        return loyaltyQueryUseCase.listHistory(clienteId).stream().map(mapper::toResponse).toList();
    }
}
