package com.buses.examen.Progra.customer.adapter.out.persistence;

import com.buses.examen.Progra.customer.application.port.out.UserSecurityRepositoryPort;
import com.buses.examen.Progra.customer.domain.UserSecurity;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Adaptador JPA para persistencia de credenciales de cliente.
 */
@Component
public class JpaUserSecurityRepository implements UserSecurityRepositoryPort {

    private final SpringDataUserSecurityRepository repository;

    /**
     * Crea el adaptador con el repositorio Spring Data subyacente.
     *
     * @param repository repositorio JPA de credenciales
     */
    public JpaUserSecurityRepository(final SpringDataUserSecurityRepository repository) {
        this.repository = repository;
    }

    /** {@inheritDoc} */
    @Override
    public boolean existsByUsername(final String username) {
        return repository.existsByUsername(username);
    }

    /** {@inheritDoc} */
    @Override
    public Optional<UserSecurity> findByUsername(final String username) {
        return repository.findByUsername(username);
    }

    /** {@inheritDoc} */
    @Override
    public Optional<UserSecurity> findByClienteId(final Long clienteId) {
        return repository.findByClienteId(clienteId);
    }

    /** {@inheritDoc} */
    @Override
    public UserSecurity save(final UserSecurity userSecurity) {
        return repository.save(userSecurity);
    }
}
