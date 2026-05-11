package com.buses.examen.Progra.route.adapter.out.persistence;

import com.buses.examen.Progra.route.application.port.out.RutaRepositoryPort;
import com.buses.examen.Progra.route.domain.Ruta;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador JPA del puerto {@link RutaRepositoryPort}.
 */
@Component
public class JpaRutaRepository implements RutaRepositoryPort {
    private final SpringDataRutaRepository repository;

    /**
     * Crea el adaptador con su repositorio Spring Data delegado.
     *
     * @param repository repositorio Spring Data delegado
     */
    public JpaRutaRepository(final SpringDataRutaRepository repository) {
        this.repository = repository;
    }

    /** {@inheritDoc} */
    @Override
    public Ruta save(final Ruta ruta) {
        return repository.save(ruta);
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Ruta> findById(final Long id) {
        return repository.findById(id);
    }

    /** {@inheritDoc} */
    @Override
    public List<Ruta> findAll() {
        return repository.findAll();
    }
}
