package com.buses.examen.Progra.customer.adapter.out.persistence;

import com.buses.examen.Progra.customer.application.port.out.ClienteRepositoryPort;
import com.buses.examen.Progra.customer.domain.Cliente;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Adaptador JPA del puerto {@link ClienteRepositoryPort}.
 */
@Component
public class JpaClienteRepository implements ClienteRepositoryPort {

    private final SpringDataClienteRepository repository;

    /**
     * Crea el adaptador con su repositorio Spring Data.
     *
     * @param repository repositorio Spring Data delegado
     */
    public JpaClienteRepository(final SpringDataClienteRepository repository) {
        this.repository = repository;
    }

    /** {@inheritDoc} */
    @Override
    public Cliente save(final Cliente cliente) {
        return repository.save(cliente);
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Cliente> findByEmail(final String email) {
        return repository.findByEmail(email);
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Cliente> findByDocumentoIdentidad(final String documentoIdentidad) {
        return repository.findByDocumentoIdentidad(documentoIdentidad);
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Cliente> findById(final Long id) {
        return repository.findById(id);
    }
}
