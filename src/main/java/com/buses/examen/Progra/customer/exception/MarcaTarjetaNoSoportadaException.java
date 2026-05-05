package com.buses.examen.Progra.customer.exception;

/**
 * Lanzada cuando la marca de tarjeta proporcionada no es reconocida por el sistema.
 */
public class MarcaTarjetaNoSoportadaException extends RuntimeException {

    /**
     * Construye la excepción con el valor de marca que causó el error.
     *
     * @param marca valor de marca no reconocido
     */
    public MarcaTarjetaNoSoportadaException(final String marca) {
        super("Marca de tarjeta no soportada: " + marca);
    }
}
