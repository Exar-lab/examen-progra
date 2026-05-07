package com.buses.examen.Progra.sales.adapter.out.persistence;

import com.buses.examen.Progra.sales.domain.Compra;
import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataCompraRepository extends JpaRepository<Compra, Long> {
}
