package com.buses.examen.Progra.service.adapter.in.web.dto.response;

import java.time.OffsetDateTime;

/**
 * Respuesta HTTP para servicio programado.
 *
 * @param id identificador del servicio
 * @param salidaProgramada fecha y hora de salida
 * @param busId identificador del bus asignado
 * @param capacidadDisponible asientos disponibles
 */
public record ServiceResponse(Long id, OffsetDateTime salidaProgramada, Long busId, int capacidadDisponible) { }
