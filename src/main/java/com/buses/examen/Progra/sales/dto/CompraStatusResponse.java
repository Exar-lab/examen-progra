package com.buses.examen.Progra.sales.dto;

/**
 * DTO de respuesta mínimo para operaciones sobre compras.
 *
 * @param status estado de la operación (ej. "ok", "error")
 */
public record CompraStatusResponse(String status) {
}
