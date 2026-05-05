package com.buses.examen.Progra.geography.controller;

import com.buses.examen.Progra.geography.dto.PaisResponse;
import com.buses.examen.Progra.geography.service.CatalogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Expone lectura pública mínima del catálogo geográfico. */
@RestController
@RequestMapping("/api/catalogo")
public class CatalogController {

    private final CatalogService catalogService;

    /**
     * Construye el controller con su servicio de catálogo.
     *
     * @param catalogService servicio de consulta del catálogo
     */
    public CatalogController(final CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    /**
     * Devuelve el catálogo básico de países disponibles.
     *
     * @return lista de países
     */
    @GetMapping("/paises")
    public ResponseEntity<List<PaisResponse>> paises() {
        return ResponseEntity.ok(catalogService.listarPaises());
    }
}
