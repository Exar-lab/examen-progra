package com.buses.examen.Progra.sales.domain;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * Comprobante fiscal (factura o boleta) emitido para una compra.
 * Existe exactamente uno por {@link Compra}.
 */
@Entity
@Table(name = "comprobante", uniqueConstraints = @UniqueConstraint(name = "uk_comprobante_compra", columnNames = "compra_id"))
public class Comprobante {
    private static final String DEFAULT_TIPO = "FACTURA";
    private static final String DEFAULT_SERIE = "F001";
    private static final String DEFAULT_NUMERO = "000001";
    private static final String DEFAULT_MONEDA = "CRC";

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @OneToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "compra_id", nullable = false) private Compra compra;
    @Column(nullable = false) private String tipo = DEFAULT_TIPO;
    @Column(nullable = false) private String serie = DEFAULT_SERIE;
    @Column(nullable = false) private String numero = DEFAULT_NUMERO;
    @Column(name = "fecha_emision", nullable = false) private OffsetDateTime fechaEmision = OffsetDateTime.now();
    @Column(name = "monto_total", nullable = false, precision = 10, scale = 2) private BigDecimal montoTotal = BigDecimal.ZERO;
    @Column(nullable = false) private String moneda = DEFAULT_MONEDA;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false) private EstadoComprobante estado = EstadoComprobante.EMITIDO;

    /** Constructor requerido por JPA. */
    protected Comprobante() {}

    /**
     * Emite un comprobante y lo vincula bidireccionalmente con la compra.
     *
     * @param compra compra propietaria del comprobante
     * @param tipo tipo de comprobante (p.ej. FACTURA)
     * @param serie serie documental
     * @param numero numeración correlativa
     * @param montoTotal monto total emitido
     * @param moneda moneda de facturación
     * @return comprobante emitido y asociado
     */
    public static Comprobante emitirParaCompra(@NonNull final Compra compra, @NonNull final String tipo,
                                               @NonNull final String serie, @NonNull final String numero,
                                               @NonNull final BigDecimal montoTotal, @NonNull final String moneda) {
        final Comprobante comprobante = new Comprobante();
        comprobante.tipo = tipo;
        comprobante.serie = serie;
        comprobante.numero = numero;
        comprobante.montoTotal = montoTotal;
        comprobante.moneda = moneda;
        compra.vincularComprobante(comprobante);
        return comprobante;
    }

    /**
     * Asocia este comprobante a su compra propietaria.
     * Método de enlace bidireccional — usar {@link Compra#vincularComprobante} en su lugar.
     *
     * @param compra compra a la que pertenece este comprobante
     */
    void vincularCompra(final Compra compra) {
        this.compra = compra;
    }

    /**
     * Devuelve el identificador persistido del comprobante.
     *
     * @return id del comprobante o {@code null} si no fue persistido
     */
    public Long getId() { return id; }

    /**
     * Devuelve la numeración documental del comprobante.
     *
     * @return número del comprobante
     */
    public String getNumero() { return numero; }

    /**
     * Devuelve el tipo documental del comprobante.
     *
     * @return tipo de comprobante
     */
    public String getTipo() { return tipo; }

    /**
     * Devuelve la serie documental del comprobante.
     *
     * @return serie documental
     */
    public String getSerie() { return serie; }

    /**
     * Devuelve la fecha y hora de emisión del comprobante.
     *
     * @return fecha de emisión
     */
    public OffsetDateTime getFechaEmision() { return fechaEmision; }

    /**
     * Devuelve el monto total facturado.
     *
     * @return monto total
     */
    public BigDecimal getMontoTotal() { return montoTotal; }

    /**
     * Devuelve la moneda del comprobante.
     *
     * @return moneda
     */
    public String getMoneda() { return moneda; }

    /**
     * Devuelve la compra asociada al comprobante.
     *
     * @return compra propietaria
     */
    public Compra getCompra() { return compra; }
}
