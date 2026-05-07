package com.buses.examen.Progra.customer.adapter.out.persistence;

import com.buses.examen.Progra.customer.application.port.out.TarjetaRepositoryPort;
import com.buses.examen.Progra.customer.domain.Tarjeta;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Adaptador JPA del puerto {@link TarjetaRepositoryPort}.
 */
@Component
public class JpaTarjetaRepository implements TarjetaRepositoryPort {

    private final SpringDataTarjetaRepository repository;

    /**
     * Crea el adaptador con su repositorio Spring Data delegado.
     *
     * @param repository repositorio Spring Data delegado
     */
    public JpaTarjetaRepository(final SpringDataTarjetaRepository repository) {
        this.repository = repository;
    }

    /** {@inheritDoc} */
    @Override
    public Tarjeta save(final Tarjeta tarjeta) {
        return repository.save(tarjeta);
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Tarjeta> findById(final Long id) {
        return repository.findById(id);
    }
}
