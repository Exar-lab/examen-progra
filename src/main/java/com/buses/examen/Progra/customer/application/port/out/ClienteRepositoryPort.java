package com.buses.examen.Progra.customer.application.port.out;

import com.buses.examen.Progra.customer.domain.Cliente;

import java.util.Optional;

/**
 * Puerto de salida para persistencia y consultas de clientes.
 */
public interface ClienteRepositoryPort {
    /**
     * Persiste un cliente.
     *
     * @param cliente cliente a guardar
     * @return cliente persistido
     */
    Cliente save(Cliente cliente);

    /**
     * Busca un cliente por correo electrónico.
     *
     * @param email correo del cliente
     * @return cliente encontrado, si existe
     */
    Optional<Cliente> findByEmail(String email);

    /**
     * Busca un cliente por documento/pasaporte.
     *
     * @param documentoIdentidad documento de identidad
     * @return cliente encontrado, si existe
     */
    Optional<Cliente> findByDocumentoIdentidad(String documentoIdentidad);

    /**
     * Busca un cliente por id.
     *
     * @param id identificador del cliente
     * @return cliente encontrado, si existe
     */
    Optional<Cliente> findById(Long id);
}
