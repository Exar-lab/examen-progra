package com.buses.examen.Progra.fleet.adapter.in.web.dto.response;

/**
 * Respuesta HTTP para bus.
 *
 * @param id identificador del bus
 * @param capacidadTotal capacidad total de asientos
 */
public record BusResponse(Long id, int capacidadTotal) { }
