package com.buses.examen.Progra.fleet.adapter.out.persistence;

import com.buses.examen.Progra.fleet.domain.Asiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface SpringDataAsientoRepository extends JpaRepository<Asiento, Long> {
    List<Asiento> findByBus_Id(Long busId);
}
