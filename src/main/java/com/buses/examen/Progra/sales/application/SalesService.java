package com.buses.examen.Progra.sales.application;

import com.buses.examen.Progra.customer.application.port.out.ClienteRepositoryPort;
import com.buses.examen.Progra.customer.application.port.out.TarjetaRepositoryPort;
import com.buses.examen.Progra.customer.domain.Cliente;
import com.buses.examen.Progra.customer.domain.Tarjeta;
import com.buses.examen.Progra.customer.exception.ClienteNoEncontradoException;
import com.buses.examen.Progra.fleet.application.port.out.AsientoRepositoryPort;
import com.buses.examen.Progra.fleet.domain.Asiento;
import com.buses.examen.Progra.loyalty.application.port.out.MovimientoPuntosRepositoryPort;
import com.buses.examen.Progra.loyalty.domain.MovimientoPuntos;
import com.buses.examen.Progra.sales.application.command.PurchaseTicketsCommand;
import com.buses.examen.Progra.sales.application.port.in.PurchaseTicketsUseCase;
import com.buses.examen.Progra.sales.application.port.in.SalesQueryUseCase;
import com.buses.examen.Progra.sales.application.result.ComprobanteJsonResult;
import com.buses.examen.Progra.sales.application.result.ComprobantePdfResult;
import com.buses.examen.Progra.sales.application.port.out.*;
import com.buses.examen.Progra.sales.application.result.PurchaseTicketsResult;
import com.buses.examen.Progra.sales.application.result.TicketViewResult;
import com.buses.examen.Progra.sales.domain.*;
import com.buses.examen.Progra.sales.exception.MaxTicketsExceededException;
import com.buses.examen.Progra.sales.exception.PurchaseWindowExpiredException;
import com.buses.examen.Progra.sales.exception.ComprobanteNoEncontradoException;
import com.buses.examen.Progra.sales.exception.TarjetaNoEncontradaException;
import com.buses.examen.Progra.sales.exception.AsientoNoEncontradoException;
import com.buses.examen.Progra.sales.exception.AsientoReservadoException;
import com.buses.examen.Progra.service.application.port.out.ServicioRepositoryPort;
import com.buses.examen.Progra.service.domain.Servicio;
import com.buses.examen.Progra.service.exception.CapacidadAgotadaException;
import com.buses.examen.Progra.service.exception.ServicioNoEncontradoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio de aplicación para orquestación de compra de tickets.
 */
@Service
public class SalesService implements PurchaseTicketsUseCase, SalesQueryUseCase {
    private static final int MAX_TICKETS_POR_COMPRA = 5;
    private static final int MAX_DIAS_ANTICIPACION_COMPRA = 7;
    private static final String COMPROBANTE_TIPO = "FACTURA";
    private static final String COMPROBANTE_SERIE = "F001";
    private static final String COMPROBANTE_MONEDA = "CRC";

    private final ClienteRepositoryPort clienteRepositoryPort;
    private final TarjetaRepositoryPort tarjetaRepositoryPort;
    private final ServicioRepositoryPort servicioRepositoryPort;
    private final AsientoRepositoryPort asientoRepositoryPort;
    private final ReservaAsientoRepositoryPort reservaAsientoRepositoryPort;
    private final TicketRepositoryPort ticketRepositoryPort;
    private final CompraRepositoryPort compraRepositoryPort;
    private final ComprobanteRepositoryPort comprobanteRepositoryPort;
    private final MovimientoPuntosRepositoryPort movimientoPuntosRepositoryPort;
    private final TicketCodeGeneratorPort ticketCodeGeneratorPort;
    private final ComprobanteNumberGeneratorPort comprobanteNumberGeneratorPort;
    private final ComprobantePdfPort comprobantePdfPort;

    /**
     * Crea el servicio de ventas con todos los puertos necesarios.
     *
     * @param clienteRepositoryPort puerto de clientes
     * @param tarjetaRepositoryPort puerto de tarjetas
     * @param servicioRepositoryPort puerto de servicios
     * @param asientoRepositoryPort puerto de asientos
     * @param reservaAsientoRepositoryPort puerto de reservas de asiento
     * @param ticketRepositoryPort puerto de tickets
     * @param compraRepositoryPort puerto de compras
     * @param comprobanteRepositoryPort puerto de comprobantes
     * @param movimientoPuntosRepositoryPort puerto de movimientos de puntos
     * @param ticketCodeGeneratorPort puerto generador de códigos
     * @param comprobanteNumberGeneratorPort puerto generador de números de comprobante
     * @param comprobantePdfPort puerto de generación PDF
     */
    public SalesService(final ClienteRepositoryPort clienteRepositoryPort,
                        final TarjetaRepositoryPort tarjetaRepositoryPort,
                        final ServicioRepositoryPort servicioRepositoryPort,
                        final AsientoRepositoryPort asientoRepositoryPort,
                        final ReservaAsientoRepositoryPort reservaAsientoRepositoryPort,
                        final TicketRepositoryPort ticketRepositoryPort,
                        final CompraRepositoryPort compraRepositoryPort,
                        final ComprobanteRepositoryPort comprobanteRepositoryPort,
                        final MovimientoPuntosRepositoryPort movimientoPuntosRepositoryPort,
                        final TicketCodeGeneratorPort ticketCodeGeneratorPort,
                        final ComprobanteNumberGeneratorPort comprobanteNumberGeneratorPort,
                        final ComprobantePdfPort comprobantePdfPort) {
        this.clienteRepositoryPort = clienteRepositoryPort;
        this.tarjetaRepositoryPort = tarjetaRepositoryPort;
        this.servicioRepositoryPort = servicioRepositoryPort;
        this.asientoRepositoryPort = asientoRepositoryPort;
        this.reservaAsientoRepositoryPort = reservaAsientoRepositoryPort;
        this.ticketRepositoryPort = ticketRepositoryPort;
        this.compraRepositoryPort = compraRepositoryPort;
        this.comprobanteRepositoryPort = comprobanteRepositoryPort;
        this.movimientoPuntosRepositoryPort = movimientoPuntosRepositoryPort;
        this.ticketCodeGeneratorPort = ticketCodeGeneratorPort;
        this.comprobanteNumberGeneratorPort = comprobanteNumberGeneratorPort;
        this.comprobantePdfPort = comprobantePdfPort;
    }

    /** {@inheritDoc} */
    @Override
    @Transactional
    public PurchaseTicketsResult purchase(final PurchaseTicketsCommand command) {
        validateTicketLimit(command.asientoIds());
        final List<String> ticketCodes = new ArrayList<>();

        final Cliente cliente = loadCliente(command.clienteId());
        final Tarjeta tarjeta = resolveTarjeta(command.tarjetaId());
        final Servicio servicio = loadServicio(command.servicioId());
        final OffsetDateTime fechaCompra = OffsetDateTime.now();

        validatePurchaseWindow(fechaCompra, servicio);
        validateServiceCapacity(servicio, command.asientoIds().size());

        final Compra compra = createCompra(command, cliente, tarjeta, fechaCompra);
        buildTicketsForSeats(command, compra, cliente, servicio, ticketCodes);

        servicioRepositoryPort.save(servicio);
        final Compra compraPersistida = compraRepositoryPort.save(compra);
        final Comprobante comprobante = emitComprobante(compraPersistida);

        recordLoyaltyMovement(cliente, compraPersistida);
        comprobantePdfPort.generateFor(compraPersistida, comprobante);

        return new PurchaseTicketsResult(compraPersistida.getId(), ticketCodes, comprobante.getId());
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public List<TicketViewResult> listTicketsForCustomer(final Long clienteId) {
        return ticketRepositoryPort.findAllByClienteIdOrderByFechaEmisionDesc(clienteId).stream()
                .map(ticket -> new TicketViewResult(
                        ticket.getId(),
                        ticket.getCodigoTicket(),
                        ticket.getPrecioFinal(),
                        ticket.getFechaEmision()))
                .collect(Collectors.toList());
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public ComprobantePdfResult getComprobantePdf(final Long clienteId, final Long comprobanteId) {
        final Comprobante comprobante = comprobanteRepositoryPort.findByIdAndCompraClienteId(comprobanteId, clienteId)
                .orElseThrow(() -> new ComprobanteNoEncontradoException(comprobanteId));
        final byte[] pdf = comprobantePdfPort.renderFor(comprobante.getCompra(), comprobante);
        return new ComprobantePdfResult("comprobante-" + comprobante.getNumero() + ".pdf", pdf);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public ComprobanteJsonResult getComprobanteJson(final Long clienteId, final Long comprobanteId) {
        final Comprobante comprobante = comprobanteRepositoryPort.findByIdAndCompraClienteId(comprobanteId, clienteId)
                .orElseThrow(() -> new ComprobanteNoEncontradoException(comprobanteId));
        final Compra compra = comprobante.getCompra();
        final Cliente cliente = compra.getCliente();
        return new ComprobanteJsonResult(
                comprobante.getId(),
                comprobante.getNumero(),
                comprobante.getSerie(),
                comprobante.getTipo(),
                comprobante.getFechaEmision(),
                comprobante.getMontoTotal(),
                comprobante.getMoneda(),
                cliente.getNombres() + " " + cliente.getApellidos(),
                cliente.getEmail(),
                compra.getTickets().stream()
                        .map(this::toTicketComprobanteResult)
                        .toList(),
                compra.getFechaCompra());
    }

    private ComprobanteJsonResult.TicketComprobanteResult toTicketComprobanteResult(final Ticket ticket) {
        final Servicio servicio = ticket.getServicio();
        return new ComprobanteJsonResult.TicketComprobanteResult(
                ticket.getCodigoTicket(),
                ticket.getPrecioFinal(),
                servicio.getId(),
                servicio.getRutaId(),
                servicio.getSalidaProgramada());
    }

    private Cliente loadCliente(final Long clienteId) {
        return clienteRepositoryPort.findById(clienteId)
                .orElseThrow(() -> new ClienteNoEncontradoException(clienteId));
    }

    private Servicio loadServicio(final Long servicioId) {
        return servicioRepositoryPort.findById(servicioId)
                .orElseThrow(() -> new ServicioNoEncontradoException(servicioId));
    }

    private void validateServiceCapacity(final Servicio servicio, final int requestedSeats) {
        if (servicio.getCapacidadDisponible() < requestedSeats) {
            throw new CapacidadAgotadaException();
        }
    }

    private Compra createCompra(final PurchaseTicketsCommand command,
                                final Cliente cliente,
                                final Tarjeta tarjeta,
                                final OffsetDateTime fechaCompra) {
        return new Compra(
                cliente,
                tarjeta,
                CanalCompra.valueOf(command.canalCompra()),
                command.codigoOperacionExterna(),
                fechaCompra
        );
    }

    private void buildTicketsForSeats(final PurchaseTicketsCommand command,
                                      final Compra compra,
                                      final Cliente cliente,
                                      final Servicio servicio,
                                      final List<String> ticketCodes) {
        for (final Long asientoId : command.asientoIds()) {
            emitTicketForSeat(command.servicioId(), compra, cliente, servicio, asientoId, ticketCodes);
        }
    }

    private void emitTicketForSeat(final Long servicioId,
                                   final Compra compra,
                                   final Cliente cliente,
                                   final Servicio servicio,
                                   final Long asientoId,
                                   final List<String> ticketCodes) {
        final Asiento asiento = asientoRepositoryPort.findById(asientoId)
                .orElseThrow(() -> new AsientoNoEncontradoException(asientoId));
        validateSeatNotReserved(servicioId, asientoId);

        servicio.reservarCupo();
        final String ticketCode = generateUniqueTicketCode();
        final Ticket ticket = ticketRepositoryPort.save(Ticket.emitir(
                compra,
                servicio,
                cliente,
                asiento,
                ticketCode,
                servicio.getPrecioBase()
        ));
        compra.agregarTicket(ticket);
        reservaAsientoRepositoryPort.save(ReservaAsiento.activa(servicio, asiento));
        ticketCodes.add(ticketCode);
    }

    private Comprobante emitComprobante(final Compra compraPersistida) {
        return comprobanteRepositoryPort.save(
                Comprobante.emitirParaCompra(
                        compraPersistida,
                        COMPROBANTE_TIPO,
                        COMPROBANTE_SERIE,
                        comprobanteNumberGeneratorPort.generate(),
                        compraPersistida.getTickets().stream()
                                .map(Ticket::getPrecioFinal)
                                .reduce(BigDecimal.ZERO, BigDecimal::add),
                        COMPROBANTE_MONEDA
                )
        );
    }

    private void recordLoyaltyMovement(final Cliente cliente, final Compra compraPersistida) {
        movimientoPuntosRepositoryPort.save(new MovimientoPuntos(cliente, compraPersistida));
    }

    private void validateTicketLimit(final List<Long> asientoIds) {
        if (asientoIds == null || asientoIds.isEmpty() || asientoIds.size() > MAX_TICKETS_POR_COMPRA) {
            throw new MaxTicketsExceededException(MAX_TICKETS_POR_COMPRA);
        }
    }

    private Tarjeta resolveTarjeta(final Long tarjetaId) {
        if (tarjetaId == null) {
            return null;
        }
        return tarjetaRepositoryPort.findById(tarjetaId)
                .orElseThrow(() -> new TarjetaNoEncontradaException(tarjetaId));
    }

    private void validatePurchaseWindow(final OffsetDateTime fechaCompra, final Servicio servicio) {
        final OffsetDateTime salidaProgramada = servicio.getSalidaProgramada();
        if (salidaProgramada.isBefore(fechaCompra)
                || salidaProgramada.isAfter(fechaCompra.plusDays(MAX_DIAS_ANTICIPACION_COMPRA))) {
            throw new PurchaseWindowExpiredException();
        }
    }

    private void validateSeatNotReserved(final Long servicioId, final Long asientoId) {
        if (reservaAsientoRepositoryPort.existsByServicioIdAndAsientoIdAndEstadoReserva(
                servicioId, asientoId, EstadoReservaAsiento.ACTIVA)) {
            throw new AsientoReservadoException(servicioId, asientoId);
        }
    }

    private String generateUniqueTicketCode() {
        String candidate = ticketCodeGeneratorPort.generate();
        while (ticketRepositoryPort.findByCodigoTicket(candidate).isPresent()) {
            candidate = ticketCodeGeneratorPort.generate();
        }
        return candidate;
    }
}
