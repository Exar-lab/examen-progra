package com.buses.examen.Progra.fleet.domain;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

/**
 * Asiento físico de un bus, identificado de forma única por número y piso dentro del mismo bus.
 */
@Entity
@Table(name = "asiento", uniqueConstraints = @UniqueConstraint(name = "uk_asiento_bus_numero_piso", columnNames = {"bus_id", "numero", "piso"}))
public class Asiento {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "bus_id", nullable = false) private Bus bus;
    @Column(nullable = false, length = 8) private String numero;
    @Column(nullable = false) private int piso;
    @Column(nullable = false, length = 30) private String categoria;
    @Column(nullable = false) private boolean activo = true;

    /** Constructor requerido por JPA. */
    protected Asiento() {}

    /**
     * Crea un asiento para el bus indicado.
     *
     * @param bus       bus al que pertenece el asiento
     * @param numero    identificador del asiento dentro del bus (ej. "1A")
     * @param piso      piso del bus donde se ubica el asiento
     * @param categoria categoría del asiento (ej. "REGULAR", "VIP")
     */
    public Asiento(@NonNull final Bus bus, @NonNull final String numero,
                   final int piso, @NonNull final String categoria) {
        this.bus = bus;
        this.numero = numero;
        this.piso = piso;
        this.categoria = categoria;
    }

    /**
     * Devuelve el identificador del asiento.
     *
     * @return id generado por la base de datos
     */
    public Long getId() { return id; }

    /**
     * Devuelve el identificador del bus propietario del asiento.
     *
     * @return id del bus asociado
     */
    public Long getBusId() { return bus.getId(); }
}
