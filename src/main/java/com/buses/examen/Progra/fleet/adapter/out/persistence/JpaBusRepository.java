package com.buses.examen.Progra.fleet.adapter.out.persistence;

import com.buses.examen.Progra.fleet.application.port.out.BusRepositoryPort;
import com.buses.examen.Progra.fleet.domain.Bus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador JPA del puerto {@link BusRepositoryPort}.
 */
@Component
public class JpaBusRepository implements BusRepositoryPort {
    private final SpringDataBusRepository repository;

    /**
     * Crea el adaptador con su repositorio Spring Data delegado.
     *
     * @param repository repositorio Spring Data delegado
     */
    public JpaBusRepository(final SpringDataBusRepository repository) {
        this.repository = repository;
    }

    /** {@inheritDoc} */
    @Override
    public Bus save(final Bus bus) {
        return repository.save(bus);
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Bus> findById(final Long id) {
        return repository.findById(id);
    }

    /** {@inheritDoc} */
    @Override
    public List<Bus> findByCompaniaId(final Long companiaId) {
        return repository.findByCompania_Id(companiaId);
    }
}
