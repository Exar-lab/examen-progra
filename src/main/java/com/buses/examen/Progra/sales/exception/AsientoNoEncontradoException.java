package com.buses.examen.Progra.sales.exception;

/**
 * Lanzada cuando el asiento solicitado no existe.
 */
public class AsientoNoEncontradoException extends RuntimeException {

    /**
     * Construye la excepción con el id de asiento no encontrado.
     *
     * @param asientoId identificador de asiento inexistente
     */
    public AsientoNoEncontradoException(final Long asientoId) {
        super("Asiento no encontrado: " + asientoId);
    }
}
