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
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "cliente_id", nullable = false) private Cliente cliente;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "compra_id") private Compra compra;
    @Enumerated(EnumType.STRING) @Column(name = "tipo_movimiento", nullable = false) private TipoMovimientoPuntos tipoMovimiento = TipoMovimientoPuntos.ACUMULACION;
    @Column(nullable = false) private int puntos = 0;
    @Column(name = "saldo_posterior", nullable = false) private int saldoPosterior = 0;
    @Column(name = "fecha_movimiento", nullable = false) private OffsetDateTime fechaMovimiento = OffsetDateTime.now();
    @Column(nullable = false) private String motivo = "COMPRA";

    /** Constructor requerido por JPA. */
    protected MovimientoPuntos() {}

    /**
     * Crea un movimiento de puntos asociado a un cliente y su compra.
     *
     * @param cliente cliente que genera el movimiento
     * @param compra  compra que origina el movimiento, puede ser {@code null} para ajustes manuales
     */
    public MovimientoPuntos(final Cliente cliente, final Compra compra) {
        this.cliente = cliente;
        this.compra = compra;
    }

    /**
     * Devuelve la compra que originó el movimiento.
     *
     * @return compra asociada o {@code null}
     */
    public Compra getCompra() { return compra; }
}
