package com.buses.examen.Progra.loyalty.adapter.in.web.mapper;

import com.buses.examen.Progra.customer.domain.Cliente;
import com.buses.examen.Progra.loyalty.domain.MovimientoPuntos;
import com.buses.examen.Progra.sales.domain.CanalCompra;
import com.buses.examen.Progra.sales.domain.Compra;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class LoyaltyWebMapperTest {
    /** Verifica que el mapper deja compraId en null cuando no hay compra asociada. */
    @Test
    void shouldMapNullCompraId() {
        final MovimientoPuntos movimiento = new MovimientoPuntos(new Cliente("Ana", "P", "P1", "CR", "a@a", "1"), null);
        final var response = new LoyaltyWebMapper().toResponse(movimiento);
        assertThat(response.compraId()).isNull();
    }

    /** Verifica que el mapper copia compraId cuando la compra existe. */
    @Test
    void shouldMapCompraIdWhenPresent() {
        final Cliente cliente = new Cliente("Ana", "P", "P1", "CR", "a@a", "1");
        final Compra compra = new Compra(cliente, null, CanalCompra.WEB, "op-1", OffsetDateTime.parse("2026-05-08T09:30:00Z"));
        setId(compra, 30L);
        final MovimientoPuntos movimiento = new MovimientoPuntos(cliente, compra);
        final var response = new LoyaltyWebMapper().toResponse(movimiento);
        assertThat(response.compraId()).isEqualTo(30L);
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
}
