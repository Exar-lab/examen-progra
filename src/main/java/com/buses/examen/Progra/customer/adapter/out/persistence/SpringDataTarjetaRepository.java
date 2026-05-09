package com.buses.examen.Progra.customer.adapter.out.persistence;

import com.buses.examen.Progra.customer.domain.Tarjeta;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio Spring Data para la entidad {@link Tarjeta}.
 */
public interface SpringDataTarjetaRepository extends JpaRepository<Tarjeta, Long> {
}
