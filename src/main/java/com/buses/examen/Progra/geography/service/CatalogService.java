package com.buses.examen.Progra.geography.service;

import com.buses.examen.Progra.geography.dto.PaisResponse;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio de consulta del catálogo geográfico.
 */
@Service
public class CatalogService {

    /**
     * Devuelve el catálogo de países disponibles para rutas.
     *
     * @return lista de países
     */
    public List<PaisResponse> listarPaises() {
        return List.of(new PaisResponse("PE", "Peru"));
    }
}
