package com.buses.examen.Progra.customer.domain;

import jakarta.persistence.*;

/** Credenciales de autenticación de un cliente. */
@Entity
@Table(name = "user_security", uniqueConstraints = {
        @UniqueConstraint(name = "uk_user_security_username", columnNames = "username"),
        @UniqueConstraint(name = "uk_user_security_cliente", columnNames = "cliente_id")
})
public class UserSecurity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(nullable = false)
    private String username;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private boolean enabled;

    @Column(nullable = false)
    private boolean locked;

    /** Constructor requerido por JPA. */
    protected UserSecurity() {
    }

    /**
     * Crea credenciales de autenticación para un cliente.
     *
     * @param cliente cliente dueño de las credenciales
     * @param username nombre de usuario único para login
     * @param passwordHash hash no reversible de la contraseña
     * @param enabled indica si la cuenta está habilitada
     * @param locked indica si la cuenta está bloqueada
     */
    public UserSecurity(final Cliente cliente, final String username, final String passwordHash,
                         final boolean enabled, final boolean locked) {
        this.cliente = cliente;
        this.username = username;
        this.passwordHash = passwordHash;
        this.enabled = enabled;
        this.locked = locked;
    }

    /**
     * Retorna el hash de contraseña persistido.
     *
     * @return hash de contraseña del usuario
     */
    public String getPasswordHash() {
        return passwordHash;
    }
}
