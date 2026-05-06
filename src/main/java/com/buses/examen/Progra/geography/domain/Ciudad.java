package com.buses.examen.Progra.geography.domain;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

/**
 * Ciudad que actúa como origen o destino de rutas de bus.
 * El código de la ciudad es único dentro de su país.
 */
@Entity
@Table(name = "ciudad", uniqueConstraints = @UniqueConstraint(name = "uk_ciudad_pais_codigo", columnNames = {"pais_id", "codigo"}))
public class Ciudad {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "pais_id", nullable = false) private Pais pais;
    @Column(nullable = false, length = 120) private String nombre;
    @Column(nullable = false, length = 10) private String codigo;

    /** Constructor requerido por JPA. */
    protected Ciudad() {}

    /**
     * Crea una ciudad dentro del país indicado.
     *
     * @param pais   país al que pertenece la ciudad
     * @param nombre nombre legible de la ciudad
     * @param codigo código único de la ciudad dentro del país (ej. "LIM")
     */
    public Ciudad(@NonNull final Pais pais, @NonNull final String nombre, @NonNull final String codigo) {
        this.pais = pais;
        this.nombre = nombre;
        this.codigo = codigo;
    }

    /**
     * Devuelve el identificador de la ciudad.
     *
     * @return id generado por la base de datos
     */
    public Long getId() { return id; }
}
