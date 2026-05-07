package com.buses.examen.Progra.fleet.adapter.out.persistence;

import com.buses.examen.Progra.fleet.domain.Bus;
import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataBusRepository extends JpaRepository<Bus, Long> {
}
