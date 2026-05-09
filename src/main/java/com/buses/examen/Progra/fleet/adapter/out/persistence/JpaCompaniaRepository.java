package com.buses.examen.Progra.fleet.adapter.out.persistence;

import com.buses.examen.Progra.fleet.application.port.out.CompaniaRepositoryPort;
import com.buses.examen.Progra.fleet.domain.Compania;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador JPA del puerto {@link CompaniaRepositoryPort}.
 */
@Component
public class JpaCompaniaRepository implements CompaniaRepositoryPort {
    private final SpringDataCompaniaRepository repository;

    /**
     * Crea el adaptador con su repositorio Spring Data delegado.
     *
     * @param repository repositorio Spring Data delegado
     */
    public JpaCompaniaRepository(final SpringDataCompaniaRepository repository) {
        this.repository = repository;
    }

    /** {@inheritDoc} */
    @Override
    public Compania save(final Compania compania) {
        return repository.save(compania);
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Compania> findById(final Long id) {
        return repository.findById(id);
    }

    /** {@inheritDoc} */
    @Override
    public List<Compania> findAll() {
        return repository.findAll();
    }
}
