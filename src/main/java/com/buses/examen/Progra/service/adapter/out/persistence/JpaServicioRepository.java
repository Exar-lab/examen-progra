package com.buses.examen.Progra.service.adapter.out.persistence;

import com.buses.examen.Progra.service.application.port.out.ServicioRepositoryPort;
import com.buses.examen.Progra.service.domain.Servicio;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Adaptador JPA del puerto {@link ServicioRepositoryPort}.
 */
@Component
public class JpaServicioRepository implements ServicioRepositoryPort {

    private final SpringDataServicioRepository repository;

    /**
     * Crea el adaptador con su repositorio Spring Data delegado.
     *
     * @param repository repositorio Spring Data delegado
     */
    public JpaServicioRepository(final SpringDataServicioRepository repository) {
        this.repository = repository;
    }

    /** {@inheritDoc} */
    @Override
    public Servicio save(final Servicio servicio) {
        return repository.save(servicio);
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Servicio> findById(final Long id) {
        return repository.findById(id);
    }

    /** {@inheritDoc} */
    @Override
    public List<Servicio> findByRutaIdAndSalidaProgramadaBetween(final Long rutaId, final OffsetDateTime start, final OffsetDateTime end) {
        return repository.findByRutaIdAndSalidaProgramadaBetween(rutaId, start, end);
    }

    /** {@inheritDoc} */
    @Override
    public List<Servicio> findAll() {
        return repository.findAll();
    }
}
