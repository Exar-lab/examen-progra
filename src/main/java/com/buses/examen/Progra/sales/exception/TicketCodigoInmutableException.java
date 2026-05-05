package com.buses.examen.Progra.sales.exception;

/**
 * Lanzada al intentar modificar el código de un ticket ya emitido.
 */
public class TicketCodigoInmutableException extends RuntimeException {

    /** Construye la excepción indicando que el código de ticket es inmutable. */
    public TicketCodigoInmutableException() {
        super("codigoTicket es inmutable una vez emitido");
    }
}
