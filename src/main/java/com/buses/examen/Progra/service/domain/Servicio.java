package com.buses.examen.Progra.service.domain;

import com.buses.examen.Progra.fleet.domain.Bus;
import com.buses.examen.Progra.route.domain.Ruta;
import com.buses.examen.Progra.service.exception.CapacidadAgotadaException;
import com.buses.examen.Progra.service.exception.CapacidadDisponibleInvalidaException;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * Instancia concreta de una ruta operada por un bus en una fecha y hora determinadas.
 */
@Entity
@Table(name = "servicio")
public class Servicio {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "ruta_id", nullable = false) private Ruta ruta;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "bus_id", nullable = false) private Bus bus;
    @Column(name = "salida_programada", nullable = false) private OffsetDateTime salidaProgramada;
    @Column(name = "llegada_programada", nullable = false) private OffsetDateTime llegadaProgramada;
    @Column(name = "precio_base", nullable = false, precision = 12, scale = 2) private BigDecimal precioBase;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private EstadoServicio estado;
    @Column(name = "capacidad_disponible", nullable = false) private int capacidadDisponible;

    /** Constructor requerido por JPA. */
    protected Servicio() {}

    /**
     * Crea un servicio de bus con todos sus atributos operacionales.
     *
     * @param ruta                ruta que opera este servicio
     * @param bus                 bus asignado
     * @param salidaProgramada    fecha y hora de salida
     * @param llegadaProgramada   fecha y hora de llegada estimada
     * @param precioBase          precio base del pasaje
     * @param estado              estado inicial del servicio
     * @param capacidadDisponible cupos disponibles para venta
     */
    public Servicio(@NonNull final Ruta ruta, @NonNull final Bus bus, @NonNull final OffsetDateTime salidaProgramada,
                    @NonNull final OffsetDateTime llegadaProgramada, @NonNull final BigDecimal precioBase,
                    @NonNull final EstadoServicio estado, final int capacidadDisponible) {
        this.ruta = ruta;
        this.bus = bus;
        this.salidaProgramada = salidaProgramada;
        this.llegadaProgramada = llegadaProgramada;
        this.precioBase = precioBase;
        this.estado = estado;
        this.capacidadDisponible = capacidadDisponible;
    }

    /**
     * Devuelve el identificador del servicio.
     *
     * @return id generado por la base de datos
     */
    public Long getId() { return id; }

    /**
     * Devuelve la fecha y hora de salida programada.
     *
     * @return salida programada
     */
    public OffsetDateTime getSalidaProgramada() { return salidaProgramada; }

    /**
     * Devuelve el bus asignado a este servicio.
     *
     * @return bus
     */
    public Bus getBus() { return bus; }

    /**
     * Devuelve el identificador de la ruta asociada.
     *
     * @return id de la ruta
     */
    public Long getRutaId() { return ruta.getId(); }

    /**
     * Reserva un cupo del servicio, decrementando la capacidad disponible.
     *
     * @throws CapacidadAgotadaException            si no quedan cupos disponibles
     * @throws CapacidadDisponibleInvalidaException  si la capacidad disponible supera la del bus
     */
    public void reservarCupo() {
        if (capacidadDisponible <= 0) {
            throw new CapacidadAgotadaException();
        }
        if (capacidadDisponible > bus.getCapacidadTotal()) {
            throw new CapacidadDisponibleInvalidaException();
        }
        capacidadDisponible--;
    }

    /**
     * Devuelve la cantidad de cupos aún disponibles para venta.
     *
     * @return cupos disponibles
     */
    public int getCapacidadDisponible() { return capacidadDisponible; }

    /**
     * Devuelve el precio base configurado para el servicio.
     *
     * @return precio base
     */
    public BigDecimal getPrecioBase() { return precioBase; }
}
