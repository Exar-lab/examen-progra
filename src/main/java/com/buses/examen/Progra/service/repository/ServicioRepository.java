package com.buses.examen.Progra.service.repository;

import com.buses.examen.Progra.service.domain.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA para la entidad {@link Servicio}.
 */
public interface ServicioRepository extends JpaRepository<Servicio, Long> {
}
