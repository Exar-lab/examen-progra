package com.buses.examen.Progra.geography.domain;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

/**
 * País identificado por su código ISO-3166 único.
 */
@Entity
@Table(name = "pais", uniqueConstraints = @UniqueConstraint(name = "uk_pais_codigo_iso", columnNames = "codigo_iso"))
public class Pais {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "codigo_iso", nullable = false, length = 3) private String codigoIso;
    @Column(nullable = false, length = 100) private String nombre;

    /** Constructor requerido por JPA. */
    protected Pais() {}

    /**
     * Crea un país con su código ISO y nombre.
     *
     * @param codigoIso código ISO-3166 de dos o tres letras (ej. "PE")
     * @param nombre    nombre legible del país
     */
    public Pais(@NonNull final String codigoIso, @NonNull final String nombre) {
        this.codigoIso = codigoIso;
        this.nombre = nombre;
    }

    /**
     * Devuelve el identificador del país.
     *
     * @return id generado por la base de datos
     */
    public Long getId() { return id; }
}
