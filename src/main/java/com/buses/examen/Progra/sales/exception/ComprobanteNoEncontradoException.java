package com.buses.examen.Progra.sales.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción lanzada cuando no existe un comprobante visible para el cliente autenticado.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ComprobanteNoEncontradoException extends RuntimeException {

    /**
     * Crea la excepción con el id consultado.
     *
     * @param comprobanteId identificador del comprobante solicitado
     */
    public ComprobanteNoEncontradoException(final Long comprobanteId) {
        super("Comprobante no encontrado: " + comprobanteId);
    }
}
