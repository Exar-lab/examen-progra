package com.buses.examen.Progra.fleet.adapter.out.persistence;

import com.buses.examen.Progra.fleet.domain.Bus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface SpringDataBusRepository extends JpaRepository<Bus, Long> {
    List<Bus> findByCompania_Id(Long companiaId);
}
