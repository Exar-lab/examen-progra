package com.buses.examen.Progra.fleet.domain;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

/**
 * Empresa de transporte propietaria de la flota de buses.
 * Se identifica de forma única tanto por nombre comercial como por RUC/NIT.
 */
@Entity
@Table(name = "compania", uniqueConstraints = {
        @UniqueConstraint(name = "uk_compania_nombre_comercial", columnNames = "nombre_comercial"),
        @UniqueConstraint(name = "uk_compania_ruc_nit", columnNames = "ruc_nit")
})
public class Compania {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "nombre_comercial", nullable = false) private String nombreComercial;
    @Column(name = "ruc_nit", nullable = false) private String rucNit;
    @Column(nullable = false) private boolean activa = true;

    /** Constructor requerido por JPA. */
    protected Compania() {}

    /**
     * Crea una compañía de transporte con su nombre comercial y número fiscal.
     *
     * @param nombreComercial razón social o nombre comercial único
     * @param rucNit          número de RUC o NIT único
     */
    public Compania(@NonNull final String nombreComercial, @NonNull final String rucNit) {
        this.nombreComercial = nombreComercial;
        this.rucNit = rucNit;
    }

    /**
     * Devuelve el identificador de la compañía.
     *
     * @return id generado por la base de datos
     */
    public Long getId() { return id; }
}
