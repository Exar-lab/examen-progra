package com.buses.examen.Progra.sales.application.port.out;

import com.buses.examen.Progra.sales.domain.Ticket;

import java.util.Optional;

/**
 * Puerto de salida para persistencia y búsqueda de tickets.
 */
public interface TicketRepositoryPort {
    /**
     * Persiste un ticket.
     *
     * @param ticket ticket a guardar
     * @return ticket persistido
     */
    Ticket save(Ticket ticket);

    /**
     * Busca un ticket por código único.
     *
     * @param codigoTicket código de ticket
     * @return ticket encontrado, si existe
     */
    Optional<Ticket> findByCodigoTicket(String codigoTicket);
}
