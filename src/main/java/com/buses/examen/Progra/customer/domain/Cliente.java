package com.buses.examen.Progra.customer.domain;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

/**
 * Pasajero registrado en el sistema, identificado de forma única por documento e email.
 */
@Entity
@Table(name = "cliente", uniqueConstraints = {
        @UniqueConstraint(name = "uk_cliente_documento", columnNames = "documento_identidad"),
        @UniqueConstraint(name = "uk_cliente_email", columnNames = "email")
})
public class Cliente {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false) private String nombres;
    @Column(nullable = false) private String apellidos;
    @Column(name = "documento_identidad", nullable = false) private String documentoIdentidad;
    @Column(nullable = false) private String email;
    @Column(nullable = false) private String telefono;
    @Column(name = "puntos_acumulados", nullable = false) private int puntosAcumulados = 0;
    @Column(nullable = false) private boolean activo = true;

    /** Constructor requerido por JPA. */
    protected Cliente() {}

    /**
     * Crea un cliente con sus datos personales y de contacto.
     *
     * @param nombres             primer nombre y segundo nombre del pasajero
     * @param apellidos           apellidos del pasajero
     * @param documentoIdentidad  número único de documento (DNI, pasaporte, etc.)
     * @param email               correo electrónico de contacto
     * @param telefono            número de teléfono de contacto
     */
    public Cliente(@NonNull final String nombres, @NonNull final String apellidos,
                   @NonNull final String documentoIdentidad, @NonNull final String email,
                   @NonNull final String telefono) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.documentoIdentidad = documentoIdentidad;
        this.email = email;
        this.telefono = telefono;
    }

    /**
     * Devuelve el identificador del cliente.
     *
     * @return id generado por la base de datos
     */
    public Long getId() { return id; }
}
