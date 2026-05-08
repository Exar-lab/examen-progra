package com.buses.examen.Progra.loyalty.domain;

import com.buses.examen.Progra.customer.domain.Cliente;
import com.buses.examen.Progra.sales.domain.Compra;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.time.OffsetDateTime;

/**
 * Registra cada acumulación o canje de puntos de fidelidad de un cliente.
 */
@Entity
@Table(name = "movimiento_puntos")
public class MovimientoPuntos {
    private static final int PUNTOS_INICIALES = 0;
    private static final String MOTIVO_COMPRA = "COMPRA";

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "cliente_id", nullable = false) private Cliente cliente;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "compra_id") private Compra compra;
    @Enumerated(EnumType.STRING) @Column(name = "tipo_movimiento", nullable = false) private TipoMovimientoPuntos tipoMovimiento = TipoMovimientoPuntos.ACUMULACION;
    @Column(nullable = false) private int puntos = PUNTOS_INICIALES;
    @Column(name = "saldo_posterior", nullable = false) private int saldoPosterior = PUNTOS_INICIALES;
    @Column(name = "fecha_movimiento", nullable = false) private OffsetDateTime fechaMovimiento = OffsetDateTime.now();
    @Column(nullable = false) private String motivo = MOTIVO_COMPRA;

    /** Constructor requerido por JPA. */
    protected MovimientoPuntos() {}

    /**
     * Crea un movimiento de puntos asociado a un cliente y su compra.
     *
     * @param cliente cliente que genera el movimiento
     * @param compra  compra que origina el movimiento, puede ser {@code null} para ajustes manuales
     */
    public MovimientoPuntos(@NonNull final Cliente cliente, final Compra compra) {
        this.cliente = cliente;
        this.compra = compra;
    }

    /**
     * Devuelve la compra que originó el movimiento.
     *
     * @return compra asociada o {@code null}
     */
    public Compra getCompra() { return compra; }

    /**
     * Devuelve la fecha y hora registrada del movimiento.
     *
     * @return fecha y hora del movimiento
     */
    public OffsetDateTime getFechaMovimiento() { return fechaMovimiento; }
}
