package com.buses.examen.Progra.customer.application.port.out;

/** Puerto para hashing de contraseñas. */
public interface PasswordHasherPort {
    /**
     * Genera un hash no reversible de una contraseña en texto plano.
     *
     * @param rawPassword contraseña en claro
     * @return hash seguro de la contraseña
     */
    String hash(String rawPassword);
}
