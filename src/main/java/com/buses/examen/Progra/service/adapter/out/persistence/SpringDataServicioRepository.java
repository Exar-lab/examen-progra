package com.buses.examen.Progra.service.adapter.out.persistence;

import com.buses.examen.Progra.service.domain.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;

interface SpringDataServicioRepository extends JpaRepository<Servicio, Long> {
    List<Servicio> findByRutaIdAndSalidaProgramadaBetween(Long rutaId, OffsetDateTime start, OffsetDateTime end);
}
