package com.buses.examen.Progra.sales.exception;

/**
 * Lanzada cuando la tarjeta de pago indicada no existe.
 */
public class TarjetaNoEncontradaException extends RuntimeException {

    /**
     * Construye la excepción con el id de tarjeta no encontrado.
     *
     * @param tarjetaId identificador de tarjeta inexistente
     */
    public TarjetaNoEncontradaException(final Long tarjetaId) {
        super("Tarjeta no encontrada: " + tarjetaId);
    }
}
