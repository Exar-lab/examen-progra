package com.buses.examen.Progra.route.domain;

import com.buses.examen.Progra.geography.domain.Ciudad;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;

/**
 * Par origen-destino que define una ruta de transporte entre dos ciudades.
 */
@Entity
@Table(name = "ruta")
public class Ruta {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "ciudad_origen_id", nullable = false) private Ciudad ciudadOrigen;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "ciudad_destino_id", nullable = false) private Ciudad ciudadDestino;
    @Column(name = "duracion_minutos", nullable = false) private int duracionMinutos;
    @Column(name = "distancia_km", nullable = false) private double distanciaKm;
    @Column(nullable = false) private boolean activa = true;

    /** Constructor requerido por JPA. */
    protected Ruta() {}

    /**
     * Crea una ruta entre dos ciudades con su distancia y duración estimadas.
     *
     * @param ciudadOrigen    ciudad de partida
     * @param ciudadDestino   ciudad de llegada
     * @param duracionMinutos duración estimada del viaje en minutos
     * @param distanciaKm     distancia aproximada en kilómetros
     */
    public Ruta(@NonNull final Ciudad ciudadOrigen, @NonNull final Ciudad ciudadDestino,
                final int duracionMinutos, final double distanciaKm) {
        this.ciudadOrigen = ciudadOrigen;
        this.ciudadDestino = ciudadDestino;
        this.duracionMinutos = duracionMinutos;
        this.distanciaKm = distanciaKm;
    }

    /**
     * Devuelve el identificador de la ruta.
     *
     * @return id generado por la base de datos
     */
    public Long getId() { return id; }

    /**
     * Devuelve la ciudad de origen.
     *
     * @return ciudad origen
     */
    public Ciudad getCiudadOrigen() { return ciudadOrigen; }

    /**
     * Devuelve la ciudad de destino.
     *
     * @return ciudad destino
     */
    public Ciudad getCiudadDestino() { return ciudadDestino; }

    /**
     * Devuelve la duración estimada en minutos.
     *
     * @return duración en minutos
     */
    public int getDuracionMinutos() { return duracionMinutos; }

    /**
     * Devuelve la distancia aproximada en kilómetros.
     *
     * @return distancia en km
     */
    public double getDistanciaKm() { return distanciaKm; }
}
