package com.buses.examen.Progra.service.exception;

/**
 * Lanzada cuando la capacidad disponible de un servicio supera la capacidad total del bus.
 */
public class CapacidadDisponibleInvalidaException extends RuntimeException {

    /** Construye la excepción indicando el estado inválido de capacidad. */
    public CapacidadDisponibleInvalidaException() {
        super("Capacidad disponible no puede superar la capacidad total del bus");
    }
}
