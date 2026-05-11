package com.buses.examen.Progra.customer.adapter.in.web;

import com.buses.examen.Progra.config.WebSecurityConfig;
import com.buses.examen.Progra.customer.adapter.out.security.AuthenticatedCustomerPrincipal;
import com.buses.examen.Progra.customer.adapter.out.security.CustomerUserDetailsServiceAdapter;
import com.buses.examen.Progra.customer.application.port.out.UserSecurityRepositoryPort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Verifica el endpoint REST de login.
 */
@WebMvcTest(AuthenticationController.class)
@Import({WebSecurityConfig.class, CustomerUserDetailsServiceAdapter.class})
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private UserSecurityRepositoryPort userSecurityRepositoryPort;

    /**
     * Verifica que un login válido retorna 200, usuario autenticado y sesión con contexto de seguridad.
     *
     * @throws Exception si falla la ejecución del request MVC
     */
    @Test
    void shouldLoginAndPersistSecurityContextOnValidCredentials() throws Exception {
        final AuthenticatedCustomerPrincipal principal =
                new AuthenticatedCustomerPrincipal(42L, "juanp", "$2a$10$hash", true, true);
        final Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(
                principal,
                null,
                principal.getAuthorities()
        );

        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType("application/json")
                        .content("""
                                {
                                  "username": "juanp",
                                  "password": "password123"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clienteId").value(42L))
                .andExpect(jsonPath("$.username").value("juanp"))
                .andExpect(request().sessionAttribute(
                        HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                        notNullValue()
                ));
    }

    /**
     * Verifica que un login inválido retorna 401.
     *
     * @throws Exception si falla la ejecución del request MVC
     */
    @Test
    void shouldReturn401ForInvalidCredentials() throws Exception {
        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType("application/json")
                        .content("""
                                {
                                  "username": "juanp",
                                  "password": "wrong-password"
                                }
                                """))
                .andExpect(status().isUnauthorized());
    }
}
