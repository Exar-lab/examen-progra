package com.buses.examen.Progra.service.exception;

/**
 * Lanzada cuando no quedan cupos disponibles en un servicio.
 */
public class CapacidadAgotadaException extends RuntimeException {

    /** Construye la excepción indicando que la capacidad del servicio está agotada. */
    public CapacidadAgotadaException() {
        super("Capacidad agotada para este servicio");
    }
}
