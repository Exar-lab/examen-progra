package com.buses.examen.Progra.customer.adapter.out.security;

import com.buses.examen.Progra.customer.application.AuthenticatedCustomer;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Principal autenticado que expone el {@code clienteId} para derivar la identidad
 * del cliente en operaciones protegidas como la compra de tickets.
 *
 * <p>No lleva anotaciones de Spring Bean — es creado por
 * {@link CustomerUserDetailsServiceAdapter} en tiempo de carga.</p>
 */
public final class AuthenticatedCustomerPrincipal implements UserDetails, AuthenticatedCustomer {

    private static final String ROLE_USER = "ROLE_USER";

    private final Long clienteId;
    private final String username;
    private final String password;
    private final boolean enabled;
    private final boolean accountNonLocked;

    /**
     * Crea un principal autenticado con los datos del cliente.
     *
     * @param clienteId       identificador del cliente en el dominio
     * @param username        nombre de usuario para autenticación
     * @param password        hash de contraseña
     * @param enabled         indica si la cuenta está habilitada
     * @param accountNonLocked indica si la cuenta no está bloqueada
     */
    public AuthenticatedCustomerPrincipal(@NonNull final Long clienteId, @NonNull final String username,
                                          @NonNull final String password, final boolean enabled,
                                          final boolean accountNonLocked) {
        this.clienteId = clienteId;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.accountNonLocked = accountNonLocked;
    }

    /**
     * Retorna el identificador del cliente en el dominio.
     *
     * @return id del cliente
     */
    public Long clienteId() {
        return clienteId;
    }

    /** {@inheritDoc} */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(ROLE_USER));
    }

    /** {@inheritDoc} */
    @Override
    public String getPassword() {
        return password;
    }

    /** {@inheritDoc} */
    @Override
    public String getUsername() {
        return username;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
