package com.buses.examen.Progra.fleet.adapter.in.web.dto.response;

/**
 * Respuesta HTTP para asiento de bus.
 *
 * @param id identificador del asiento
 * @param busId identificador del bus al que pertenece
 */
public record SeatResponse(Long id, Long busId) { }
