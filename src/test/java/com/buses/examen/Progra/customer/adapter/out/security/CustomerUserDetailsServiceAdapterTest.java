package com.buses.examen.Progra.customer.adapter.out.security;

import com.buses.examen.Progra.customer.application.port.out.UserSecurityRepositoryPort;
import com.buses.examen.Progra.customer.domain.Cliente;
import com.buses.examen.Progra.customer.domain.UserSecurity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Verifica el adaptador de UserDetailsService que carga clientes desde el repositorio.
 */
@ExtendWith(MockitoExtension.class)
class CustomerUserDetailsServiceAdapterTest {

    @Mock
    private UserSecurityRepositoryPort userSecurityRepositoryPort;

    private CustomerUserDetailsServiceAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new CustomerUserDetailsServiceAdapter(userSecurityRepositoryPort);
    }

    /**
     * Verifica que el adaptador mapea correctamente un UserSecurity a AuthenticatedCustomerPrincipal.
     */
    @Test
    void shouldMapUserSecurityToAuthenticatedPrincipal() {
        final Cliente cliente = mock(Cliente.class);
        when(cliente.getId()).thenReturn(5L);

        final UserSecurity userSecurity = new UserSecurity(cliente, "juanp", "$2a$10$hash", true, false);
        when(userSecurityRepositoryPort.findByUsername("juanp")).thenReturn(Optional.of(userSecurity));

        final UserDetails result = adapter.loadUserByUsername("juanp");

        assertThat(result).isInstanceOf(AuthenticatedCustomerPrincipal.class);
        final AuthenticatedCustomerPrincipal principal = (AuthenticatedCustomerPrincipal) result;
        assertThat(principal.clienteId()).isEqualTo(5L);
        assertThat(principal.getUsername()).isEqualTo("juanp");
        assertThat(principal.getPassword()).isEqualTo("$2a$10$hash");
        assertThat(principal.isEnabled()).isTrue();
        assertThat(principal.isAccountNonLocked()).isTrue();
    }

    /**
     * Verifica que se lanza UsernameNotFoundException cuando el usuario no existe.
     */
    @Test
    void shouldThrowUsernameNotFoundExceptionWhenUserMissing() {
        when(userSecurityRepositoryPort.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adapter.loadUserByUsername("unknown"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("unknown");
    }

    /**
     * Verifica que una cuenta bloqueada se refleja como isAccountNonLocked = false.
     */
    @Test
    void shouldMapLockedAccountToNonLockedFalse() {
        final Cliente cliente = mock(Cliente.class);
        when(cliente.getId()).thenReturn(10L);

        final UserSecurity locked = new UserSecurity(cliente, "marial", "$2a$10$hashLocked", true, true);
        when(userSecurityRepositoryPort.findByUsername("marial")).thenReturn(Optional.of(locked));

        final AuthenticatedCustomerPrincipal principal =
                (AuthenticatedCustomerPrincipal) adapter.loadUserByUsername("marial");

        assertThat(principal.isAccountNonLocked()).isFalse();
    }

}
