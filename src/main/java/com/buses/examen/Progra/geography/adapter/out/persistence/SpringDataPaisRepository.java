package com.buses.examen.Progra.geography.adapter.out.persistence;

import com.buses.examen.Progra.geography.domain.Pais;
import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataPaisRepository extends JpaRepository<Pais, Long> {
}
