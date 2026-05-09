package com.buses.examen.Progra.customer.domain;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

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
    public UserSecurity(@NonNull final Cliente cliente, @NonNull final String username,
                        @NonNull final String passwordHash, final boolean enabled, final boolean locked) {
        this.cliente = cliente;
        this.username = username;
        this.passwordHash = passwordHash;
        this.enabled = enabled;
        this.locked = locked;
    }

    /**
     * Retorna el cliente dueño de estas credenciales.
     *
     * @return cliente asociado
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * Retorna el nombre de usuario para autenticación.
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Retorna el hash de contraseña persistido.
     *
     * @return hash de contraseña del usuario
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Indica si la cuenta está habilitada.
     *
     * @return {@code true} si la cuenta está habilitada
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Indica si la cuenta está bloqueada.
     *
     * @return {@code true} si la cuenta está bloqueada
     */
    public boolean isLocked() {
        return locked;
    }
}
