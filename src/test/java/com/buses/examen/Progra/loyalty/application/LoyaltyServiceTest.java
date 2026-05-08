package com.buses.examen.Progra.loyalty.application;

import com.buses.examen.Progra.loyalty.application.port.out.MovimientoPuntosRepositoryPort;
import com.buses.examen.Progra.loyalty.domain.MovimientoPuntos;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/** Pruebas de servicio de fidelidad. */
class LoyaltyServiceTest {

    @Test
    void shouldReturnLoyaltyHistory() {
        final MovimientoPuntosRepositoryPort movimientoPuntosRepositoryPort = mock(MovimientoPuntosRepositoryPort.class);
        final LoyaltyService service = new LoyaltyService(movimientoPuntosRepositoryPort);

        final MovimientoPuntos movimiento = mock(MovimientoPuntos.class);
        when(movimientoPuntosRepositoryPort.findByClienteIdOrderByFechaMovimientoDesc(5L))
                .thenReturn(List.of(movimiento));

        assertThat(service.listHistory(5L)).containsExactly(movimiento);
    }
}
