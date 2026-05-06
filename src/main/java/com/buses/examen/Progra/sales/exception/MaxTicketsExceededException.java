package com.buses.examen.Progra.sales.exception;

/**
 * Lanzada cuando una compra intenta superar el límite de 5 tickets permitidos.
 */
public class MaxTicketsExceededException extends RuntimeException {

    /**
     * Construye la excepción indicando el límite superado.
     *
     * @param max límite máximo de tickets por compra
     */
    public MaxTicketsExceededException(final int max) {
        super("Maximo " + max + " tickets por compra");
    }
}
