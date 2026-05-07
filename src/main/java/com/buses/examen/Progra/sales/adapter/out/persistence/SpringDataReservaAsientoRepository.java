package com.buses.examen.Progra.sales.adapter.out.persistence;

import com.buses.examen.Progra.sales.domain.EstadoReservaAsiento;
import com.buses.examen.Progra.sales.domain.ReservaAsiento;
import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataReservaAsientoRepository extends JpaRepository<ReservaAsiento, Long> {
    boolean existsByServicioIdAndAsientoIdAndEstadoReserva(Long servicioId, Long asientoId, EstadoReservaAsiento estadoReserva);
}
