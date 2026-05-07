package com.buses.examen.Progra.loyalty.adapter.out.persistence;

import com.buses.examen.Progra.loyalty.application.port.out.MovimientoPuntosRepositoryPort;
import com.buses.examen.Progra.loyalty.domain.MovimientoPuntos;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Adaptador JPA del puerto {@link MovimientoPuntosRepositoryPort}.
 */
@Component
public class JpaMovimientoPuntosRepository implements MovimientoPuntosRepositoryPort {

    private final SpringDataMovimientoPuntosRepository repository;

    /**
     * Crea el adaptador con su repositorio Spring Data delegado.
     *
     * @param repository repositorio Spring Data delegado
     */
    public JpaMovimientoPuntosRepository(final SpringDataMovimientoPuntosRepository repository) {
        this.repository = repository;
    }

    /** {@inheritDoc} */
    @Override
    public MovimientoPuntos save(final MovimientoPuntos movimientoPuntos) {
        return repository.save(movimientoPuntos);
    }

    /** {@inheritDoc} */
    @Override
    public List<MovimientoPuntos> findByClienteIdOrderByFechaMovimientoDesc(final Long clienteId) {
        return repository.findByClienteIdOrderByFechaMovimientoDesc(clienteId);
    }
}
