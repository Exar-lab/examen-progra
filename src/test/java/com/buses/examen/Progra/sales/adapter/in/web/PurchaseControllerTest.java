package com.buses.examen.Progra.sales.adapter.in.web;

import com.buses.examen.Progra.config.WebSecurityConfig;
import com.buses.examen.Progra.customer.adapter.out.security.AuthenticatedCustomerPrincipal;
import com.buses.examen.Progra.customer.adapter.out.security.CustomerUserDetailsServiceAdapter;
import com.buses.examen.Progra.customer.application.port.out.UserSecurityRepositoryPort;
import com.buses.examen.Progra.sales.adapter.in.web.mapper.PurchaseWebMapper;
import com.buses.examen.Progra.sales.application.command.PurchaseTicketsCommand;
import com.buses.examen.Progra.sales.application.port.in.PurchaseTicketsUseCase;
import com.buses.examen.Progra.sales.application.port.in.SalesQueryUseCase;
import com.buses.examen.Progra.sales.application.result.ComprobantePdfResult;
import com.buses.examen.Progra.sales.application.result.PurchaseTicketsResult;
import com.buses.examen.Progra.sales.application.result.TicketViewResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.time.OffsetDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Verifica el controlador de compras con reglas de seguridad aplicadas.
 */
@WebMvcTest(PurchaseController.class)
@Import({WebSecurityConfig.class, CustomerUserDetailsServiceAdapter.class, PurchaseWebMapper.class})
class PurchaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PurchaseTicketsUseCase purchaseTicketsUseCase;

    @MockitoBean
    private SalesQueryUseCase salesQueryUseCase;

    @MockitoBean
    private UserSecurityRepositoryPort userSecurityRepositoryPort;

    /**
     * Verifica que POST /api/purchases sin autenticación retorna 401 o 302.
     *
     * @throws Exception si falla la ejecución del request MVC
     */
    @Test
    void shouldRejectPurchaseWithoutAuth() throws Exception {
        mockMvc.perform(post("/api/purchases")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "tarjetaId": 10,
                                  "servicioId": 5,
                                  "asientoIds": [21, 22],
                                  "canalCompra": "WEB",
                                  "codigoOperacionExterna": "GW-123"
                                }
                                """))
                .andExpect(result -> {
                    final int status = result.getResponse().getStatus();
                    if (status != 401 && status != 302) {
                        throw new AssertionError("Expected 401 or 302 for unauthenticated purchase, but got: " + status);
                    }
                });
    }

    /**
     * Verifica que POST /api/purchases con usuario autenticado y CSRF válido retorna 201.
     *
     * @throws Exception si falla la ejecución del request MVC
     */
    @Test
    void shouldReturn201ForAuthenticatedPurchaseWithCsrf() throws Exception {
        final AuthenticatedCustomerPrincipal principal =
                new AuthenticatedCustomerPrincipal(42L, "juanp", "$2a$10$hash", true, true);

        when(purchaseTicketsUseCase.purchase(any(PurchaseTicketsCommand.class)))
                .thenReturn(new PurchaseTicketsResult(1L, List.of("CODE-001", "CODE-002"), 100L));

        mockMvc.perform(post("/api/purchases")
                        .with(user(principal))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "tarjetaId": 10,
                                  "servicioId": 5,
                                  "asientoIds": [21, 22],
                                  "canalCompra": "WEB",
                                  "codigoOperacionExterna": "GW-123"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.compraId").value(1L))
                .andExpect(jsonPath("$.comprobanteId").value(100L));
    }

    @Test
    void shouldReturnAuthenticatedUserTickets() throws Exception {
        final AuthenticatedCustomerPrincipal principal =
                new AuthenticatedCustomerPrincipal(42L, "juanp", "$2a$10$hash", true, true);
        when(salesQueryUseCase.listTicketsForCustomer(42L)).thenReturn(List.of(
                new TicketViewResult(11L, "TK-001", java.math.BigDecimal.valueOf(20), OffsetDateTime.now())));

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/api/purchases/tickets")
                        .with(user(principal)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].ticketId").value(11L))
                .andExpect(jsonPath("$[0].codigoTicket").value("TK-001"));
    }

    @Test
    void shouldDownloadPdfForAuthenticatedUser() throws Exception {
        final AuthenticatedCustomerPrincipal principal =
                new AuthenticatedCustomerPrincipal(42L, "juanp", "$2a$10$hash", true, true);
        when(salesQueryUseCase.getComprobantePdf(42L, 99L))
                .thenReturn(new ComprobantePdfResult("comprobante-CMP-1.pdf", new byte[]{1, 2, 3}));

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/api/purchases/receipts/99/pdf")
                        .with(user(principal)))
                .andExpect(status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.header()
                        .string("Content-Disposition", "attachment; filename=\"comprobante-CMP-1.pdf\""))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_PDF));
    }
}
