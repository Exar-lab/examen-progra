package com.buses.examen.Progra.sales.adapter.out.persistence;

import com.buses.examen.Progra.sales.application.port.out.CompraRepositoryPort;
import com.buses.examen.Progra.sales.domain.Compra;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Adaptador JPA del puerto {@link CompraRepositoryPort}.
 */
@Component
public class JpaCompraRepository implements CompraRepositoryPort {

    private final SpringDataCompraRepository repository;

    /**
     * Crea el adaptador con su repositorio Spring Data delegado.
     *
     * @param repository repositorio Spring Data delegado
     */
    public JpaCompraRepository(final SpringDataCompraRepository repository) {
        this.repository = repository;
    }

    /** {@inheritDoc} */
    @Override
    public Compra save(final Compra compra) {
        return repository.save(compra);
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Compra> findById(final Long id) {
        return repository.findById(id);
    }
}
