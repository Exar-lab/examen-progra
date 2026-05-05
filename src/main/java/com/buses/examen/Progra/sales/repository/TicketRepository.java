package com.buses.examen.Progra.sales.repository;

import com.buses.examen.Progra.sales.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio JPA para la entidad {@link Ticket}.
 */
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    /**
     * Busca un ticket por su código único.
     *
     * @param codigoTicket código único del ticket
     * @return ticket encontrado o vacío si no existe
     */
    Optional<Ticket> findByCodigoTicket(String codigoTicket);
}
