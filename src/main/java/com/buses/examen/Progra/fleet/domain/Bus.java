package com.buses.examen.Progra.fleet.domain;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

/**
 * Vehículo de transporte identificado de forma única por su placa.
 */
@Entity
@Table(name = "bus", uniqueConstraints = @UniqueConstraint(name = "uk_bus_placa", columnNames = "placa"))
public class Bus {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "compania_id", nullable = false) private Compania compania;
    @Column(nullable = false) private String placa;
    @Column(nullable = false) private String modelo;
    @Column(name = "capacidad_total", nullable = false) private int capacidadTotal;
    @Column(nullable = false) private boolean activo = true;

    /** Constructor requerido por JPA. */
    protected Bus() {}

    /**
     * Crea un bus con su matrícula, modelo y capacidad.
     *
     * @param compania       compañía propietaria del bus
     * @param placa          placa única del vehículo
     * @param modelo         modelo del vehículo (ej. "Volvo 9700")
     * @param capacidadTotal número máximo de asientos disponibles
     */
    public Bus(@NonNull final Compania compania, @NonNull final String placa,
               @NonNull final String modelo, final int capacidadTotal) {
        this.compania = compania;
        this.placa = placa;
        this.modelo = modelo;
        this.capacidadTotal = capacidadTotal;
    }

    /**
     * Devuelve el identificador del bus.
     *
     * @return id generado por la base de datos
     */
    public Long getId() { return id; }

    /**
     * Devuelve el número total de asientos del bus.
     *
     * @return capacidad total
     */
    public int getCapacidadTotal() { return capacidadTotal; }
}
