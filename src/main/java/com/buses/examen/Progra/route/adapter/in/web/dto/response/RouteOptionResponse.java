package com.buses.examen.Progra.route.adapter.in.web.dto.response;

/**
 * Respuesta HTTP para una opción de viaje planificada.
 *
 * @param rutaId identificador de la ruta
 * @param servicioId identificador del servicio sugerido
 * @param score puntaje de relevancia de la opción
 */
public record RouteOptionResponse(Long rutaId, Long servicioId, int score) { }
