package com.buses.examen.Progra.config;

import com.buses.examen.Progra.customer.adapter.in.web.CustomerController;
import com.buses.examen.Progra.customer.adapter.in.web.mapper.CustomerWebMapper;
import com.buses.examen.Progra.customer.adapter.out.security.CustomerUserDetailsServiceAdapter;
import com.buses.examen.Progra.customer.application.port.in.CustomerQueryUseCase;
import com.buses.examen.Progra.customer.application.port.in.RegisterCustomerUseCase;
import com.buses.examen.Progra.customer.application.port.out.UserSecurityRepositoryPort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Verifica las reglas de acceso definidas en {@link WebSecurityConfig}.
 *
 * <p>Usa {@link CustomerController} para verificar rutas públicas de registro y catálogo.
 * La autorización de compras se verifica en {@code PurchaseControllerTest} via {@code @PreAuthorize}.</p>
 */
@WebMvcTest(CustomerController.class)
@Import({WebSecurityConfig.class, CustomerUserDetailsServiceAdapter.class, CustomerWebMapper.class})
class WebSecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RegisterCustomerUseCase registerCustomerUseCase;

    @MockitoBean
    private CustomerQueryUseCase customerQueryUseCase;

    @MockitoBean
    private UserSecurityRepositoryPort userSecurityRepositoryPort;

    /**
     * Verifica que GET /api/customers/document/X (consulta pública de cliente por documento) es accesible sin autenticación.
     * La respuesta puede ser 404 (cliente no encontrado) — no 401/403 porque la seguridad lo permite.
     *
     * @throws Exception si falla la ejecución del request MVC
     */
    @Test
    void shouldAllowPublicCustomerLookupWithoutAuth() throws Exception {
        mockMvc.perform(get("/api/customers/document/P-999"))
                .andExpect(result -> {
                    final int status = result.getResponse().getStatus();
                    if (status == 401 || status == 403) {
                        throw new AssertionError(
                                "Expected public customer query allowed (not 401/403) but got: " + status);
                    }
                });
    }

    /**
     * Verifica que GET /api/routes/** es accesible sin autenticación.
     *
     * @throws Exception si falla la ejecución del request MVC
     */
    @Test
    void shouldAllowPublicCatalogAccessWithoutAuth() throws Exception {
        mockMvc.perform(get("/api/routes/1"))
                .andExpect(result -> {
                    final int status = result.getResponse().getStatus();
                    if (status == 401 || status == 403) {
                        throw new AssertionError(
                                "Expected public catalog access allowed but got: " + status);
                    }
                });
    }

    /**
     * Verifica que POST /api/auth/login está permitido por seguridad sin autenticación previa.
     *
     * @throws Exception si falla la ejecución del request MVC
     */
    @Test
    void shouldAllowPublicRestLoginWithoutAuth() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType("application/json")
                        .content("""
                                {
                                  "username": "u",
                                  "password": "p"
                                }
                                """))
                .andExpect(result -> {
                    final int status = result.getResponse().getStatus();
                    if (status == 401 || status == 403) {
                        throw new AssertionError(
                                "Expected public REST login allowed (not 401/403) but got: " + status);
                    }
                });
    }

    /**
     * Verifica que las páginas públicas del frontend estático no quedan protegidas por seguridad.
     *
     * @throws Exception si falla la ejecución del request MVC
     */
    @Test
    void shouldAllowPublicStaticFrontendPagesWithoutAuth() throws Exception {
        mockMvc.perform(get("/login.html"))
                .andExpect(result -> {
                    final int status = result.getResponse().getStatus();
                    if (status == 401 || status == 403) {
                        throw new AssertionError(
                                "Expected public static login page allowed (not 401/403) but got: " + status);
                    }
                });
    }

    /**
     * Verifica que recursos protegidos redirigen al login personalizado, no al formulario default de Spring Security.
     *
     * @throws Exception si falla la ejecución del request MVC
     */
    @Test
    void shouldRedirectProtectedPagesToCustomLoginPage() throws Exception {
        mockMvc.perform(get("/compra.html"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login.html"));
    }
}
