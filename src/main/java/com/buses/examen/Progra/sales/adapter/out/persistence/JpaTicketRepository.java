package com.buses.examen.Progra.sales.adapter.out.persistence;

import com.buses.examen.Progra.sales.application.port.out.TicketRepositoryPort;
import com.buses.examen.Progra.sales.domain.Ticket;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador JPA del puerto {@link TicketRepositoryPort}.
 */
@Component
public class JpaTicketRepository implements TicketRepositoryPort {

    private final SpringDataTicketRepository repository;

    /**
     * Crea el adaptador con su repositorio Spring Data delegado.
     *
     * @param repository repositorio Spring Data delegado
     */
    public JpaTicketRepository(final SpringDataTicketRepository repository) {
        this.repository = repository;
    }

    /** {@inheritDoc} */
    @Override
    public Ticket save(final Ticket ticket) {
        return repository.save(ticket);
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Ticket> findByCodigoTicket(final String codigoTicket) {
        return repository.findByCodigoTicket(codigoTicket);
    }

    /** {@inheritDoc} */
    @Override
    public List<Ticket> findAllByClienteIdOrderByFechaEmisionDesc(final Long clienteId) {
        return repository.findAllByClienteIdOrderByFechaEmisionDesc(clienteId);
    }
}
