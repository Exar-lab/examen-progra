package com.buses.examen.Progra.customer.exception;

/**
 * Lanzada cuando no existe un cliente para el identificador solicitado.
 */
public class ClienteNoEncontradoException extends RuntimeException {

    /**
     * Construye la excepción con el id que no fue encontrado.
     *
     * @param clienteId identificador de cliente inexistente
     */
    public ClienteNoEncontradoException(final Long clienteId) {
        super("Cliente no encontrado: " + clienteId);
    }
}
