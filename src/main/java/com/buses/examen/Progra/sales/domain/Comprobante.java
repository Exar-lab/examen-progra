package com.buses.examen.Progra.sales.domain;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

/**
 * Comprobante fiscal (factura o boleta) emitido para una compra.
 * Existe exactamente uno por {@link Compra}.
 */
@Entity
@Table(name = "comprobante", uniqueConstraints = @UniqueConstraint(name = "uk_comprobante_compra", columnNames = "compra_id"))
public class Comprobante {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @OneToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "compra_id", nullable = false) private Compra compra;
    @Column(nullable = false) private String tipo = "FACTURA";
    @Column(nullable = false) private String serie = "F001";
    @Column(nullable = false) private String numero = "000001";
    @Column(name = "fecha_emision", nullable = false) private OffsetDateTime fechaEmision = OffsetDateTime.now();
    @Column(name = "monto_total", nullable = false) private double montoTotal = 0;
    @Column(nullable = false) private String moneda = "PEN";
    @Column(nullable = false) private String estado = "EMITIDO";

    /** Constructor requerido por JPA. */
    protected Comprobante() {}

    /**
     * Asocia este comprobante a su compra propietaria.
     * Método de enlace bidireccional — usar {@link Compra#vincularComprobante} en su lugar.
     *
     * @param compra compra a la que pertenece este comprobante
     */
    void vincularCompra(final Compra compra) {
        this.compra = compra;
    }
}
