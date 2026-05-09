package com.buses.examen.Progra.sales.application;

import com.buses.examen.Progra.customer.application.port.out.ClienteRepositoryPort;
import com.buses.examen.Progra.customer.application.port.out.TarjetaRepositoryPort;
import com.buses.examen.Progra.customer.domain.Cliente;
import com.buses.examen.Progra.customer.domain.Tarjeta;
import com.buses.examen.Progra.fleet.application.port.out.AsientoRepositoryPort;
import com.buses.examen.Progra.fleet.domain.Asiento;
import com.buses.examen.Progra.loyalty.application.port.out.MovimientoPuntosRepositoryPort;
import com.buses.examen.Progra.sales.application.command.PurchaseTicketsCommand;
import com.buses.examen.Progra.sales.application.port.out.CompraRepositoryPort;
import com.buses.examen.Progra.sales.application.port.out.ComprobanteNumberGeneratorPort;
import com.buses.examen.Progra.sales.application.port.out.ComprobantePdfPort;
import com.buses.examen.Progra.sales.application.port.out.ComprobanteRepositoryPort;
import com.buses.examen.Progra.sales.application.port.out.ReservaAsientoRepositoryPort;
import com.buses.examen.Progra.sales.application.port.out.TicketCodeGeneratorPort;
import com.buses.examen.Progra.sales.application.port.out.TicketRepositoryPort;
import com.buses.examen.Progra.sales.application.result.PurchaseTicketsResult;
import com.buses.examen.Progra.sales.domain.Compra;
import com.buses.examen.Progra.sales.domain.EstadoReservaAsiento;
import com.buses.examen.Progra.sales.domain.ReservaAsiento;
import com.buses.examen.Progra.sales.domain.Ticket;
import com.buses.examen.Progra.sales.exception.MaxTicketsExceededException;
import com.buses.examen.Progra.sales.exception.PurchaseWindowExpiredException;
import com.buses.examen.Progra.service.application.port.out.ServicioRepositoryPort;
import com.buses.examen.Progra.service.domain.Servicio;
import com.buses.examen.Progra.service.exception.CapacidadAgotadaException;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SalesServiceTest {

    @Test
    void shouldPurchaseTicketsWithinRulesAndRequestReceiptPdf() {
        final Fixture fixture = new Fixture();
        final PurchaseTicketsCommand command = new PurchaseTicketsCommand(
                1L, 2L, 3L, List.of(11L, 12L), "WEB", "op-1");

        when(fixture.ticketCodeGeneratorPort.generate()).thenReturn("TK-1", "TK-2");

        final PurchaseTicketsResult result = fixture.service.purchase(command);

        assertThat(result.ticketCodes()).containsExactly("TK-1", "TK-2");
        verify(fixture.reservaAsientoRepositoryPort, times(2))
                .save(any(ReservaAsiento.class));
        verify(fixture.servicio, times(2)).reservarCupo();
        verify(fixture.comprobantePdfPort).generateFor(any(Compra.class), any());
    }

    @Test
    void shouldRejectWhenRequestContainsSixTickets() {
        final Fixture fixture = new Fixture();
        final PurchaseTicketsCommand command = new PurchaseTicketsCommand(
                1L, 2L, 3L, List.of(1L, 2L, 3L, 4L, 5L, 6L), "WEB", "op-2");

        assertThatThrownBy(() -> fixture.service.purchase(command))
                .isInstanceOf(MaxTicketsExceededException.class);
    }

    @Test
    void shouldRejectWhenServiceDepartsAfterEightDays() {
        final Fixture fixture = new Fixture();
        when(fixture.servicio.getSalidaProgramada()).thenReturn(OffsetDateTime.now().plusDays(8));
        final PurchaseTicketsCommand command = new PurchaseTicketsCommand(
                1L, 2L, 3L, List.of(11L), "WEB", "op-3");

        assertThatThrownBy(() -> fixture.service.purchase(command))
                .isInstanceOf(PurchaseWindowExpiredException.class);
    }

    @Test
    void shouldRejectWhenServiceHasNoSeatsAvailable() {
        final Fixture fixture = new Fixture();
        final PurchaseTicketsCommand command = new PurchaseTicketsCommand(
                1L, 2L, 3L, List.of(11L), "WEB", "op-4");
        when(fixture.servicio.getCapacidadDisponible()).thenReturn(0);

        assertThatThrownBy(() -> fixture.service.purchase(command))
                .isInstanceOf(CapacidadAgotadaException.class);
    }

    @Test
    void shouldGenerateUniqueCodesWhenGeneratorReturnsCollision() {
        final Fixture fixture = new Fixture();
        final PurchaseTicketsCommand command = new PurchaseTicketsCommand(
                1L, 2L, 3L, List.of(11L, 12L), "WEB", "op-5");
        when(fixture.ticketCodeGeneratorPort.generate()).thenReturn("DUP", "DUP", "OK-1", "OK-2");
        when(fixture.ticketRepositoryPort.findByCodigoTicket("DUP")).thenReturn(Optional.of(mock(Ticket.class)));

        final PurchaseTicketsResult result = fixture.service.purchase(command);

        assertThat(result.ticketCodes()).containsExactly("OK-1", "OK-2");
    }

    @Test
    void shouldDeclareTransactionalBoundaryOnPurchase() throws NoSuchMethodException {
        assertThat(SalesService.class.getMethod("purchase", PurchaseTicketsCommand.class)
                        .isAnnotationPresent(Transactional.class))
                .isTrue();
    }

    private static final class Fixture {
        private final ClienteRepositoryPort clienteRepositoryPort = mock(ClienteRepositoryPort.class);
        private final TarjetaRepositoryPort tarjetaRepositoryPort = mock(TarjetaRepositoryPort.class);
        private final ServicioRepositoryPort servicioRepositoryPort = mock(ServicioRepositoryPort.class);
        private final AsientoRepositoryPort asientoRepositoryPort = mock(AsientoRepositoryPort.class);
        private final ReservaAsientoRepositoryPort reservaAsientoRepositoryPort = mock(ReservaAsientoRepositoryPort.class);
        private final TicketRepositoryPort ticketRepositoryPort = mock(TicketRepositoryPort.class);
        private final CompraRepositoryPort compraRepositoryPort = mock(CompraRepositoryPort.class);
        private final ComprobanteRepositoryPort comprobanteRepositoryPort = mock(ComprobanteRepositoryPort.class);
        private final MovimientoPuntosRepositoryPort movimientoPuntosRepositoryPort = mock(MovimientoPuntosRepositoryPort.class);
        private final TicketCodeGeneratorPort ticketCodeGeneratorPort = mock(TicketCodeGeneratorPort.class);
        private final ComprobanteNumberGeneratorPort comprobanteNumberGeneratorPort = mock(ComprobanteNumberGeneratorPort.class);
        private final ComprobantePdfPort comprobantePdfPort = mock(ComprobantePdfPort.class);

        private final Cliente cliente = mock(Cliente.class);
        private final Tarjeta tarjeta = mock(Tarjeta.class);
        private final Servicio servicio = mock(Servicio.class);
        private final Asiento asientoOne = mock(Asiento.class);
        private final Asiento asientoTwo = mock(Asiento.class);

        private final SalesService service = new SalesService(
                clienteRepositoryPort,
                tarjetaRepositoryPort,
                servicioRepositoryPort,
                asientoRepositoryPort,
                reservaAsientoRepositoryPort,
                ticketRepositoryPort,
                compraRepositoryPort,
                comprobanteRepositoryPort,
                movimientoPuntosRepositoryPort,
                ticketCodeGeneratorPort,
                comprobanteNumberGeneratorPort,
                comprobantePdfPort
        );

        private Fixture() {
            when(clienteRepositoryPort.findById(1L)).thenReturn(Optional.of(cliente));
            when(tarjetaRepositoryPort.findById(2L)).thenReturn(Optional.of(tarjeta));
            when(servicioRepositoryPort.findById(3L)).thenReturn(Optional.of(servicio));
            when(servicio.getSalidaProgramada()).thenReturn(OffsetDateTime.now().plusDays(2));
            when(servicio.getCapacidadDisponible()).thenReturn(20);
            when(servicio.getPrecioBase()).thenReturn(BigDecimal.valueOf(10.0));
            when(asientoRepositoryPort.findById(11L)).thenReturn(Optional.of(asientoOne));
            when(asientoRepositoryPort.findById(12L)).thenReturn(Optional.of(asientoTwo));
            when(reservaAsientoRepositoryPort.existsByServicioIdAndAsientoIdAndEstadoReserva(
                    any(), any(), any())).thenReturn(false);
            when(ticketCodeGeneratorPort.generate()).thenReturn("CODE-1", "CODE-2");
            when(comprobanteNumberGeneratorPort.generate()).thenReturn("CMP-1");
            when(ticketRepositoryPort.findByCodigoTicket(any())).thenReturn(Optional.empty());
            when(compraRepositoryPort.save(any(Compra.class))).thenAnswer(invocation -> invocation.getArgument(0));
            when(ticketRepositoryPort.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));
            when(comprobanteRepositoryPort.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
            when(reservaAsientoRepositoryPort.save(any(ReservaAsiento.class))).thenAnswer(invocation -> invocation.getArgument(0));
            when(movimientoPuntosRepositoryPort.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        }
    }
}
