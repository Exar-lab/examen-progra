package com.buses.examen.Progra.customer.adapter.out.persistence;

import com.buses.examen.Progra.customer.domain.UserSecurity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface SpringDataUserSecurityRepository extends JpaRepository<UserSecurity, Long> {
    /**
     * Verifica si ya existe un usuario de seguridad con el nombre indicado.
     *
     * @param username nombre de usuario a validar
     * @return {@code true} si existe un registro con ese username; en caso contrario {@code false}
     */
    boolean existsByUsername(String username);

    /**
     * Busca el usuario de seguridad asociado a un nombre de usuario.
     *
     * @param username nombre de usuario a consultar
     * @return un {@link Optional} con el usuario encontrado o vacío si no existe
     */
    Optional<UserSecurity> findByUsername(String username);

    /**
     * Busca el usuario de seguridad vinculado al identificador del cliente.
     *
     * @param clienteId identificador del cliente propietario de las credenciales
     * @return un {@link Optional} con el usuario vinculado o vacío si no existe
     */
    Optional<UserSecurity> findByClienteId(Long clienteId);
}
