package com.buses.examen.Progra.customer.repository;

import com.buses.examen.Progra.customer.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio JPA para la entidad {@link Cliente}.
 */
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    /**
     * Busca un cliente por su número de documento de identidad.
     *
     * @param documentoIdentidad documento único del cliente
     * @return cliente encontrado o vacío si no existe
     */
    Optional<Cliente> findByDocumentoIdentidad(String documentoIdentidad);
}
