package com.buses.examen.Progra.sales.domain;

import com.buses.examen.Progra.customer.domain.Cliente;
import com.buses.examen.Progra.fleet.domain.Asiento;
import com.buses.examen.Progra.sales.exception.PurchaseWindowExpiredException;
import com.buses.examen.Progra.service.domain.Servicio;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * Representa el derecho de viaje de un pasajero en un asiento específico de un servicio.
 */
@Entity
@Table(name = "ticket", uniqueConstraints = @UniqueConstraint(name = "uk_ticket_codigo", columnNames = "codigo_ticket"))
public class Ticket {
    private static final int MAX_DIAS_ANTICIPACION_COMPRA = 7;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "compra_id", nullable = false) private Compra compra;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "servicio_id", nullable = false) private Servicio servicio;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "cliente_id", nullable = false) private Cliente cliente;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "asiento_id", nullable = false) private Asiento asiento;
    @Column(name = "codigo_ticket", nullable = false, updatable = false) private String codigoTicket;
    @Column(name = "precio_final", nullable = false, precision = 10, scale = 2) private BigDecimal precioFinal = BigDecimal.ZERO;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private EstadoTicket estado = EstadoTicket.EMITIDO;
    @Column(name = "fecha_emision", nullable = false) private OffsetDateTime fechaEmision = OffsetDateTime.now();

    /** Constructor requerido por JPA. */
    protected Ticket() {}

    /**
     * Emite un ticket para el pasajero, validando que el servicio esté dentro de la ventana de
     * {@value #MAX_DIAS_ANTICIPACION_COMPRA} días.
     *
     * @param compra        compra a la que pertenece este ticket
     * @param servicio      servicio de bus a tomar
     * @param cliente       pasajero
     * @param asiento       asiento asignado
     * @param codigoTicket  código único del ticket
     * @param precioFinal   precio cobrado al pasajero
     * @return ticket emitido
     * @throws PurchaseWindowExpiredException si el servicio está fuera de la ventana
     *                                        [fechaCompra, fechaCompra+{@value #MAX_DIAS_ANTICIPACION_COMPRA}d]
     */
    public static Ticket emitir(@NonNull final Compra compra, @NonNull final Servicio servicio,
                                 @NonNull final Cliente cliente, @NonNull final Asiento asiento,
                                 @NonNull final String codigoTicket, @NonNull final BigDecimal precioFinal) {
        if (servicio.getSalidaProgramada().isBefore(compra.getFechaCompra())
                || servicio.getSalidaProgramada().isAfter(compra.getFechaCompra().plusDays(MAX_DIAS_ANTICIPACION_COMPRA))) {
            throw new PurchaseWindowExpiredException();
        }
        final Ticket ticket = new Ticket();
        ticket.compra = compra;
        ticket.servicio = servicio;
        ticket.cliente = cliente;
        ticket.asiento = asiento;
        ticket.codigoTicket = codigoTicket;
        ticket.precioFinal = precioFinal;
        return ticket;
    }

    /**
     * Devuelve el precio final cobrado al pasajero.
     *
     * @return precio final
     */
    public BigDecimal getPrecioFinal() { return precioFinal; }

    /**
     * Devuelve el identificador persistido del ticket.
     *
     * @return identificador del ticket o {@code null} si aún no fue persistido
     */
    public Long getId() { return id; }

    /**
     * Devuelve el código único asignado al ticket.
     *
     * @return código de ticket
     */
    public String getCodigoTicket() { return codigoTicket; }

}
