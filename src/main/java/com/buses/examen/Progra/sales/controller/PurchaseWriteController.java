package com.buses.examen.Progra.sales.controller;

import com.buses.examen.Progra.sales.dto.CompraStatusResponse;
import com.buses.examen.Progra.sales.service.CompraService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Expone endpoint mínimo de escritura protegido para el flujo de compra. */
@RestController
@RequestMapping("/api/ventas")
public class PurchaseWriteController {

    private final CompraService compraService;

    /**
     * Construye el controller con su servicio de compra.
     *
     * @param compraService servicio de orquestación de compras
     */
    public PurchaseWriteController(final CompraService compraService) {
        this.compraService = compraService;
    }

    /**
     * Inicia la creación de una compra validando la frontera de autenticación.
     *
     * @return respuesta de estado de la operación
     */
    @PostMapping("/compra")
    public ResponseEntity<CompraStatusResponse> crearCompra() {
        return ResponseEntity.ok(compraService.iniciarCompra());
    }
}
