package com.buses.examen.Progra.customer.adapter.out.persistence;

import com.buses.examen.Progra.customer.domain.Tarjeta;
import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataTarjetaRepository extends JpaRepository<Tarjeta, Long> {
}
