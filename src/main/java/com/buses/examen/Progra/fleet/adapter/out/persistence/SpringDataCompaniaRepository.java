package com.buses.examen.Progra.fleet.adapter.out.persistence;

import com.buses.examen.Progra.fleet.domain.Compania;
import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataCompaniaRepository extends JpaRepository<Compania, Long> {
}
