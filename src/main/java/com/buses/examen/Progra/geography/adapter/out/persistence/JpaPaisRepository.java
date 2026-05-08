package com.buses.examen.Progra.geography.adapter.out.persistence;

import com.buses.examen.Progra.geography.application.port.out.PaisRepositoryPort;
import com.buses.examen.Progra.geography.domain.Pais;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Adaptador JPA del puerto {@link PaisRepositoryPort}.
 */
@Component
public class JpaPaisRepository implements PaisRepositoryPort {
    private final SpringDataPaisRepository repository;

    /**
     * Crea el adaptador con su repositorio Spring Data delegado.
     *
     * @param repository repositorio Spring Data delegado
     */
    public JpaPaisRepository(final SpringDataPaisRepository repository) {
        this.repository = repository;
    }

    /** {@inheritDoc} */
    @Override
    public Pais save(final Pais pais) {
        return repository.save(pais);
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Pais> findById(final Long id) {
        return repository.findById(id);
    }
}
