package com.buses.examen.Progra.fleet.adapter.out.persistence;

import com.buses.examen.Progra.fleet.application.port.out.AsientoRepositoryPort;
import com.buses.examen.Progra.fleet.domain.Asiento;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador JPA del puerto {@link AsientoRepositoryPort}.
 */
@Component
public class JpaAsientoRepository implements AsientoRepositoryPort {
    private final SpringDataAsientoRepository repository;

    /**
     * Crea el adaptador con su repositorio Spring Data.
     *
     * @param repository repositorio Spring Data delegado
     */
    public JpaAsientoRepository(final SpringDataAsientoRepository repository) {
        this.repository = repository;
    }

    /** {@inheritDoc} */
    @Override
    public Asiento save(final Asiento asiento) {
        return repository.save(asiento);
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Asiento> findById(final Long id) {
        return repository.findById(id);
    }

    /** {@inheritDoc} */
    @Override
    public List<Asiento> findByBusId(final Long busId) {
        return repository.findByBus_Id(busId);
    }
}
