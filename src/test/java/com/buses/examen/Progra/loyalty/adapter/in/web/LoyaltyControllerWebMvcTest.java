package com.buses.examen.Progra.loyalty.adapter.in.web;

import com.buses.examen.Progra.customer.domain.Cliente;
import com.buses.examen.Progra.loyalty.application.port.in.LoyaltyQueryUseCase;
import com.buses.examen.Progra.loyalty.adapter.in.web.mapper.LoyaltyWebMapper;
import com.buses.examen.Progra.loyalty.domain.MovimientoPuntos;
import com.buses.examen.Progra.sales.domain.CanalCompra;
import com.buses.examen.Progra.sales.domain.Compra;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Field;
import java.time.OffsetDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoyaltyController.class)
@Import(LoyaltyWebMapper.class)
@WithMockUser
class LoyaltyControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LoyaltyQueryUseCase loyaltyQueryUseCase;

    /**
     * Verifica que la respuesta conserva compraId nulo cuando no hay compra asociada.
     *
     * @throws Exception si falla la ejecución del request MVC
     */
    @Test
    void shouldKeepNullCompraIdInResponse() throws Exception {
        final Cliente cliente = new Cliente("Ana", "Perez", "P-1", "CR", "ana@mail.com", "111");
        final MovimientoPuntos movimiento = new MovimientoPuntos(cliente, null);
        setFechaMovimiento(movimiento, OffsetDateTime.parse("2026-05-08T10:00:00Z"));
        when(loyaltyQueryUseCase.listHistory(1L)).thenReturn(List.of(movimiento));

        mockMvc.perform(get("/api/loyalty/customers/1/points"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fechaMovimiento").value("2026-05-08T10:00:00Z"))
                .andExpect(jsonPath("$[0].compraId").isEmpty());
    }

    /**
     * Verifica que la respuesta expone compraId cuando existe una compra asociada.
     *
     * @throws Exception si falla la ejecución del request MVC
     */
    @Test
    void shouldReturnCompraIdWhenPresent() throws Exception {
        final Cliente cliente = new Cliente("Ana", "Perez", "P-1", "CR", "ana@mail.com", "111");
        final Compra compra = new Compra(cliente, null, CanalCompra.WEB, "op-1", OffsetDateTime.parse("2026-05-08T09:30:00Z"));
        setId(compra, 99L);
        final MovimientoPuntos movimiento = new MovimientoPuntos(cliente, compra);
        setFechaMovimiento(movimiento, OffsetDateTime.parse("2026-05-08T10:00:00Z"));
        when(loyaltyQueryUseCase.listHistory(1L)).thenReturn(List.of(movimiento));

        mockMvc.perform(get("/api/loyalty/customers/1/points"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].compraId").value(99L));
    }

    private static void setId(final Object target, final Long id) {
        try {
            final Field field = target.getClass().getDeclaredField("id");
            field.setAccessible(true);
            field.set(target, id);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException(exception);
        }
    }

    private static void setFechaMovimiento(final MovimientoPuntos movimiento, final OffsetDateTime fechaMovimiento) {
        try {
            final Field field = movimiento.getClass().getDeclaredField("fechaMovimiento");
            field.setAccessible(true);
            field.set(movimiento, fechaMovimiento);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException(exception);
        }
    }
}
