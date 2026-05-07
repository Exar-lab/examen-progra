package com.buses.examen.Progra.route.adapter.out.persistence;

import com.buses.examen.Progra.route.domain.Ruta;
import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataRutaRepository extends JpaRepository<Ruta, Long> {
}
