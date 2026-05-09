package com.buses.examen.Progra.sales.exception;

/**
 * Lanzada cuando el asiento ya tiene una reserva activa para el servicio.
 */
public class AsientoReservadoException extends RuntimeException {

    /**
     * Construye la excepción indicando servicio y asiento en conflicto.
     *
     * @param servicioId id del servicio
     * @param asientoId id del asiento
     */
    public AsientoReservadoException(final Long servicioId, final Long asientoId) {
        super("Asiento ya reservado para el servicio. servicioId=" + servicioId + ", asientoId=" + asientoId);
    }
}
