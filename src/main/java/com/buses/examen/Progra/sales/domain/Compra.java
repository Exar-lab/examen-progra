package com.buses.examen.Progra.sales.domain;

import com.buses.examen.Progra.customer.domain.Cliente;
import com.buses.examen.Progra.customer.domain.Tarjeta;
import com.buses.examen.Progra.sales.exception.MaxTicketsExceededException;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Agrupa uno o más tickets adquiridos en una sola transacción de compra.
 */
@Entity
@Table(name = "compra")
public class Compra {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "cliente_id", nullable = false) private Cliente cliente;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "tarjeta_id") private Tarjeta tarjeta;
    @Column(name = "fecha_compra", nullable = false) private OffsetDateTime fechaCompra;
    @Column(nullable = false) private double total = 0;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private EstadoCompra estado = EstadoCompra.PENDIENTE;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private CanalCompra canal;
    @Column(name = "codigo_operacion_externa") private String codigoOperacionExterna;
    @OneToMany(mappedBy = "compra", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true) private final List<Ticket> tickets = new ArrayList<>();
    @OneToOne(mappedBy = "compra", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true) private Comprobante comprobante;

    /** Constructor requerido por JPA. */
    protected Compra() {}

    /**
     * Crea una compra para el cliente indicado.
     *
     * @param cliente                 cliente que realiza la compra
     * @param tarjeta                 tarjeta de pago utilizada, puede ser {@code null}
     * @param canal                   canal por el que se realizó la compra
     * @param codigoOperacionExterna  referencia de la pasarela de pago
     * @param fechaCompra             momento de la compra
     */
    public Compra(final Cliente cliente, final Tarjeta tarjeta, final CanalCompra canal,
                  final String codigoOperacionExterna, final OffsetDateTime fechaCompra) {
        this.cliente = cliente;
        this.tarjeta = tarjeta;
        this.canal = canal;
        this.codigoOperacionExterna = codigoOperacionExterna;
        this.fechaCompra = fechaCompra;
    }

    /**
     * Agrega un ticket a la compra, acumulando su precio al total.
     *
     * @param ticket ticket a agregar
     * @throws MaxTicketsExceededException si ya se alcanzó el límite de 5 tickets
     */
    public void agregarTicket(final Ticket ticket) {
        if (tickets.size() >= 5) throw new MaxTicketsExceededException(5);
        tickets.add(ticket);
        total += ticket.getPrecioFinal();
    }

    /**
     * Devuelve la lista de tickets como vista inmutable.
     *
     * @return tickets asociados a la compra
     */
    public List<Ticket> getTickets() { return Collections.unmodifiableList(tickets); }

    /**
     * Devuelve la fecha y hora de la compra.
     *
     * @return fecha de compra
     */
    public OffsetDateTime getFechaCompra() { return fechaCompra; }

    /**
     * Devuelve el comprobante emitido para esta compra.
     *
     * @return comprobante o {@code null} si aún no fue emitido
     */
    public Comprobante getComprobante() { return comprobante; }

    /**
     * Vincula el comprobante a esta compra de forma bidireccional.
     *
     * @param comprobante comprobante a asociar
     */
    void vincularComprobante(final Comprobante comprobante) {
        this.comprobante = comprobante;
        if (comprobante != null) {
            comprobante.vincularCompra(this);
        }
    }
}
