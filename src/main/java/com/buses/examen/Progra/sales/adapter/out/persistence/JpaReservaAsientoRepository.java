package com.buses.examen.Progra.sales.adapter.out.persistence;

import com.buses.examen.Progra.sales.application.port.out.ReservaAsientoRepositoryPort;
import com.buses.examen.Progra.sales.domain.EstadoReservaAsiento;
import com.buses.examen.Progra.sales.domain.ReservaAsiento;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador JPA del puerto {@link ReservaAsientoRepositoryPort}.
 */
@Component
public class JpaReservaAsientoRepository implements ReservaAsientoRepositoryPort {

    private final SpringDataReservaAsientoRepository repository;

    /**
     * Crea el adaptador con su repositorio Spring Data delegado.
     *
     * @param repository repositorio Spring Data delegado
     */
    public JpaReservaAsientoRepository(final SpringDataReservaAsientoRepository repository) {
        this.repository = repository;
    }

    /** {@inheritDoc} */
    @Override
    public ReservaAsiento save(final ReservaAsiento reservaAsiento) {
        return repository.save(reservaAsiento);
    }

    /** {@inheritDoc} */
    @Override
    public boolean existsByServicioIdAndAsientoIdAndEstadoReserva(final Long servicioId, final Long asientoId, final EstadoReservaAsiento estadoReserva) {
        return repository.existsByServicioIdAndAsientoIdAndEstadoReserva(servicioId, asientoId, estadoReserva);
    }

    /** {@inheritDoc} */
    @Override
    public List<ReservaAsiento> findByServicioIdAndEstadoReserva(final Long servicioId, final EstadoReservaAsiento estadoReserva) {
        return repository.findByServicio_IdAndEstadoReserva(servicioId, estadoReserva);
    }

    /** {@inheritDoc} */
    @Override
    public Optional<ReservaAsiento> findById(final Long id) {
        return repository.findById(id);
    }
}
