package com.buses.examen.Progra.customer.application.port.out;

import com.buses.examen.Progra.customer.domain.UserSecurity;

import java.util.Optional;

/** Puerto de persistencia para credenciales de cliente. */
public interface UserSecurityRepositoryPort {
    /**
     * Verifica si ya existe un usuario con el username indicado.
     *
     * @param username identificador de login normalizado
     * @return {@code true} si el username ya está registrado
     */
    boolean existsByUsername(String username);

    /**
     * Busca credenciales por username.
     *
     * @param username identificador de login normalizado
     * @return credenciales encontradas, si existen
     */
    Optional<UserSecurity> findByUsername(String username);

    /**
     * Busca credenciales asociadas a un cliente.
     *
     * @param clienteId identificador del cliente
     * @return credenciales asociadas al cliente, si existen
     */
    Optional<UserSecurity> findByClienteId(Long clienteId);

    /**
     * Persiste o actualiza credenciales de un cliente.
     *
     * @param userSecurity credenciales a persistir
     * @return entidad persistida
     */
    UserSecurity save(UserSecurity userSecurity);
}
