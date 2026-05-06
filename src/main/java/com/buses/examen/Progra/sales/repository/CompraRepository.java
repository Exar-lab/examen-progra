package com.buses.examen.Progra.sales.repository;

import com.buses.examen.Progra.sales.domain.Compra;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA para la entidad {@link Compra}.
 */
public interface CompraRepository extends JpaRepository<Compra, Long> {
}
