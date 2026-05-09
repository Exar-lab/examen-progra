package com.buses.examen.Progra.sales.adapter.out.persistence;

import com.buses.examen.Progra.sales.domain.Comprobante;
import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataComprobanteRepository extends JpaRepository<Comprobante, Long> {
    java.util.Optional<Comprobante> findByIdAndCompraClienteId(Long id, Long clienteId);
}
