package com.buses.examen.Progra.service.exception;

/**
 * Excepción de dominio cuando no existe un servicio para el identificador solicitado.
 */
public class ServicioNoEncontradoException extends RuntimeException {

    /**
     * Crea la excepción con el id del servicio no encontrado.
     *
     * @param servicioId identificador del servicio ausente
     */
    public ServicioNoEncontradoException(final Long servicioId) {
        super("Servicio no encontrado: " + servicioId);
    }
}
