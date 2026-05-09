package com.buses.examen.Progra.customer.adapter.out.security;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifica el contrato de UserDetails de AuthenticatedCustomerPrincipal.
 */
class AuthenticatedCustomerPrincipalTest {

    /**
     * Verifica que el principal expone todos los campos esperados.
     */
    @Test
    void shouldExposeAllFieldsCorrectly() {
        final AuthenticatedCustomerPrincipal principal =
                new AuthenticatedCustomerPrincipal(42L, "juanp", "$2a$10$hash", true, true);

        assertThat(principal.clienteId()).isEqualTo(42L);
        assertThat(principal.getUsername()).isEqualTo("juanp");
        assertThat(principal.getPassword()).isEqualTo("$2a$10$hash");
        assertThat(principal.isEnabled()).isTrue();
        assertThat(principal.isAccountNonLocked()).isTrue();
    }

    /**
     * Verifica que isEnabled y isAccountNonLocked reflejan los valores correctos cuando son false.
     */
    @Test
    void shouldReflectDisabledAndLockedState() {
        final AuthenticatedCustomerPrincipal principal =
                new AuthenticatedCustomerPrincipal(7L, "locked_user", "hash", false, false);

        assertThat(principal.isEnabled()).isFalse();
        assertThat(principal.isAccountNonLocked()).isFalse();
    }

    /**
     * Verifica que getAuthorities retorna ROLE_USER y nunca es null.
     */
    @Test
    void shouldReturnRoleUserAuthority() {
        final AuthenticatedCustomerPrincipal principal =
                new AuthenticatedCustomerPrincipal(1L, "user", "hash", true, true);

        assertThat(principal.getAuthorities()).isNotNull();
        assertThat(principal.getAuthorities())
                .hasSize(1)
                .extracting(a -> a.getAuthority())
                .containsExactly("ROLE_USER");
    }

    /**
     * Verifica que isAccountNonExpired e isCredentialsNonExpired retornan true por defecto.
     */
    @Test
    void shouldReturnTrueForExpiredChecks() {
        final AuthenticatedCustomerPrincipal principal =
                new AuthenticatedCustomerPrincipal(1L, "user", "hash", true, true);

        assertThat(principal.isAccountNonExpired()).isTrue();
        assertThat(principal.isCredentialsNonExpired()).isTrue();
    }
}
