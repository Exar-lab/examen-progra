package com.buses.examen.Progra.loyalty.adapter.out.persistence;

import com.buses.examen.Progra.loyalty.domain.MovimientoPuntos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface SpringDataMovimientoPuntosRepository extends JpaRepository<MovimientoPuntos, Long> {
    List<MovimientoPuntos> findByClienteIdOrderByFechaMovimientoDesc(Long clienteId);
}
