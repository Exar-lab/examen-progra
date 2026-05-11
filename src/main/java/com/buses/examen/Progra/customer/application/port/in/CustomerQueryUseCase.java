package com.buses.examen.Progra.customer.application.port.in;

import com.buses.examen.Progra.customer.domain.Cliente;

import java.util.Optional;

/**
 * Puerto de entrada para consultas de clientes registrados.
 */
public interface CustomerQueryUseCase {
    /**
     * Busca un cliente por su documento de identidad o pasaporte.
     *
     * @param documentoIdentidad documento de identidad
     * @return cliente encontrado, si existe
     */
    Optional<Cliente> findByDocumentoIdentidad(String documentoIdentidad);
}
