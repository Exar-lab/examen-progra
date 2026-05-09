package com.buses.examen.Progra.sales.adapter.out.persistence;

import com.buses.examen.Progra.sales.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

interface SpringDataTicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByCodigoTicket(String codigoTicket);
    List<Ticket> findAllByClienteIdOrderByFechaEmisionDesc(Long clienteId);
}
