package com.buses.examen.Progra.customer.domain;

import com.buses.examen.Progra.customer.exception.MarcaTarjetaNoSoportadaException;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.time.OffsetDateTime;

/**
 * Tarjeta de crédito o débito tokenizada, nunca almacena datos PCI sensibles directamente.
 */
@Entity
@Table(name = "tarjeta")
public class Tarjeta {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "cliente_id", nullable = false) private Cliente cliente;
    @Column(nullable = false) private String titular;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private MarcaTarjeta marca;
    @Column(name = "ultimo4", nullable = false, length = 4) private String ultimo4;
    @Column(name = "mes_expiracion", nullable = false) private int mesExpiracion;
    @Column(name = "anio_expiracion", nullable = false) private int anioExpiracion;
    @Column(name = "token_referencia", nullable = false) private String tokenReferencia;
    @Column(name = "enmascarada", nullable = false) private String enmascarada;
    @Column(nullable = false) private boolean activa = true;
    @Column(name = "creado_en", nullable = false) private OffsetDateTime creadoEn = OffsetDateTime.now();
    @Transient private String cvv;

    /** Constructor requerido por JPA. */
    protected Tarjeta() {}

    /**
     * Construye una tarjeta a partir del token devuelto por la pasarela de pago.
     * El CVV nunca se persiste — se descarta tras la tokenización.
     *
     * @param cliente          propietario de la tarjeta
     * @param titular          nombre del titular tal como aparece en la tarjeta
     * @param marca            marca de la tarjeta (VISA, MASTERCARD, etc.)
     * @param ultimo4          últimos 4 dígitos del número de tarjeta
     * @param mesExpiracion    mes de expiración (1-12)
     * @param anioExpiracion   año de expiración (ej. 2030)
     * @param tokenReferencia  token opaco de la pasarela de pago
     * @param enmascarada      representación enmascarada del PAN (ej. "4111******1111")
     * @param cvv              CVV de sesión — se descarta y no se persiste
     * @return nueva tarjeta tokenizada
     * @throws MarcaTarjetaNoSoportadaException si {@code marca} no corresponde a ningún valor de {@link MarcaTarjeta}
     */
    public static Tarjeta fromGatewayToken(@NonNull final Cliente cliente, @NonNull final String titular,
                                           @NonNull final String marca, @NonNull final String ultimo4,
                                           final int mesExpiracion, final int anioExpiracion,
                                           @NonNull final String tokenReferencia, @NonNull final String enmascarada,
                                           final String cvv) {
        final MarcaTarjeta marcaEnum;
        try {
            marcaEnum = MarcaTarjeta.valueOf(marca);
        } catch (final IllegalArgumentException e) {
            throw new MarcaTarjetaNoSoportadaException(marca);
        }
        final Tarjeta tarjeta = new Tarjeta();
        tarjeta.cliente = cliente;
        tarjeta.titular = titular;
        tarjeta.marca = marcaEnum;
        tarjeta.ultimo4 = ultimo4;
        tarjeta.mesExpiracion = mesExpiracion;
        tarjeta.anioExpiracion = anioExpiracion;
        tarjeta.tokenReferencia = tokenReferencia;
        tarjeta.enmascarada = enmascarada;
        tarjeta.cvv = null;
        return tarjeta;
    }

    /**
     * Devuelve el CVV de sesión — siempre {@code null} tras la persistencia.
     *
     * @return cvv de sesión o {@code null}
     */
    public String getCvv() { return cvv; }

    /**
     * Devuelve el identificador de la tarjeta.
     *
     * @return id generado por la base de datos
     */
    public Long getId() { return id; }

    /**
     * Devuelve el token opaco de la pasarela de pago.
     *
     * @return token de referencia
     */
    public String getTokenReferencia() { return tokenReferencia; }

    /**
     * Devuelve la representación enmascarada del PAN.
     *
     * @return número de tarjeta enmascarado
     */
    public String getEnmascarada() { return enmascarada; }
}
