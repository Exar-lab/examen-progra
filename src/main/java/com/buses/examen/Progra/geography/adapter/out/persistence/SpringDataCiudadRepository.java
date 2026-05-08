package com.buses.examen.Progra.geography.adapter.out.persistence;

import com.buses.examen.Progra.geography.domain.Ciudad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface SpringDataCiudadRepository extends JpaRepository<Ciudad, Long> {
    List<Ciudad> findByPais_Id(Long paisId);
}
