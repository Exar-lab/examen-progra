package com.buses.examen.Progra.sales.service;

import com.buses.examen.Progra.sales.dto.CompraStatusResponse;
import org.springframework.stereotype.Service;

/**
 * Servicio de orquestación del flujo de compra de tickets.
 */
@Service
public class CompraService {

    /**
     * Inicia una nueva compra y devuelve su estado inicial.
     *
     * @return estado de la operación de creación
     */
    public CompraStatusResponse iniciarCompra() {
        return new CompraStatusResponse("ok");
    }
}
