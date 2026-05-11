package com.buses.examen.Progra.customer.exception;

/** Error de dominio cuando un username ya existe. */
public class UsernameDuplicadoException extends RuntimeException {
    /**
     * Crea la excepción para username duplicado.
     *
     * @param username username que ya está registrado
     */
    public UsernameDuplicadoException(final String username) {
        super("El username ya existe: " + username);
    }
}
