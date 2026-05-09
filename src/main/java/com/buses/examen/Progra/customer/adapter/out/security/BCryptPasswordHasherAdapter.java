package com.buses.examen.Progra.customer.adapter.out.security;

import com.buses.examen.Progra.customer.application.port.out.PasswordHasherPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/** Adaptador BCrypt para hashing de contraseña. */
@Component
public class BCryptPasswordHasherAdapter implements PasswordHasherPort {

    private final BCryptPasswordEncoder encoder;

    /**
     * Crea el adaptador con el encoder BCrypt inyectado por Spring.
     *
     * @param encoder encoder BCrypt reutilizable para hashing
     */
    public BCryptPasswordHasherAdapter(final BCryptPasswordEncoder encoder) {
        this.encoder = encoder;
    }

    /**
     * {@inheritDoc}
     *
     * @param rawPassword contraseña en texto plano
     * @return hash BCrypt no reversible de la contraseña
     */
    @Override
    public String hash(final String rawPassword) {
        return encoder.encode(rawPassword);
    }
}
