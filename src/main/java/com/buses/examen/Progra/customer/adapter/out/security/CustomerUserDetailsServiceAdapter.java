package com.buses.examen.Progra.customer.adapter.out.security;

import com.buses.examen.Progra.customer.application.port.out.UserSecurityRepositoryPort;
import com.buses.examen.Progra.customer.domain.UserSecurity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Adaptador de seguridad que implementa {@link UserDetailsService} delegando
 * la carga de credenciales al puerto {@link UserSecurityRepositoryPort}.
 *
 * <p>Mapea {@link UserSecurity} a {@link AuthenticatedCustomerPrincipal} para
 * exponer el {@code clienteId} durante la sesión autenticada.</p>
 */
@Service
public class CustomerUserDetailsServiceAdapter implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(CustomerUserDetailsServiceAdapter.class);

    private final UserSecurityRepositoryPort userSecurityRepositoryPort;

    /**
     * Crea el adaptador con el puerto de persistencia de credenciales.
     *
     * @param userSecurityRepositoryPort puerto de acceso a credenciales de usuario
     */
    public CustomerUserDetailsServiceAdapter(@NonNull final UserSecurityRepositoryPort userSecurityRepositoryPort) {
        this.userSecurityRepositoryPort = userSecurityRepositoryPort;
    }

    /**
     * Carga las credenciales del usuario por nombre de usuario.
     *
     * @param username nombre de usuario a buscar
     * @return principal autenticado con datos del cliente
     * @throws UsernameNotFoundException si no existe usuario con ese nombre
     */
    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        log.debug("Cargando credenciales para usuario: {}", username);
        final UserSecurity userSecurity = userSecurityRepositoryPort.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Usuario no encontrado: {}", username);
                    return new UsernameNotFoundException("Usuario no encontrado: " + username);
                });
        return toAuthenticatedPrincipal(userSecurity);
    }

    private AuthenticatedCustomerPrincipal toAuthenticatedPrincipal(final UserSecurity userSecurity) {
        return new AuthenticatedCustomerPrincipal(
                userSecurity.getCliente().getId(),
                userSecurity.getUsername(),
                userSecurity.getPasswordHash(),
                userSecurity.isEnabled(),
                !userSecurity.isLocked()
        );
    }
}
