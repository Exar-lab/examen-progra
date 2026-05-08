package com.buses.examen.Progra.geography.adapter.out.persistence;

import com.buses.examen.Progra.geography.application.port.out.CiudadRepositoryPort;
import com.buses.examen.Progra.geography.domain.Ciudad;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Adaptador JPA del puerto {@link CiudadRepositoryPort}.
 */
@Component
public class JpaCiudadRepository implements CiudadRepositoryPort {
    private final SpringDataCiudadRepository repository;

    /**
     * Crea el adaptador con su repositorio Spring Data delegado.
     *
     * @param repository repositorio Spring Data delegado
     */
    public JpaCiudadRepository(final SpringDataCiudadRepository repository) {
        this.repository = repository;
    }

    /** {@inheritDoc} */
    @Override
    public Ciudad save(final Ciudad ciudad) {
        return repository.save(ciudad);
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Ciudad> findById(final Long id) {
        return repository.findById(id);
    }
}
