package com.buses.examen.Progra.customer.adapter.out.persistence;

import com.buses.examen.Progra.customer.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface SpringDataClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByEmail(String email);
    Optional<Cliente> findByDocumentoIdentidad(String documentoIdentidad);
}
