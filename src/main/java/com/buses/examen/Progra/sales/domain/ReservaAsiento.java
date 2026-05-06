package com.buses.examen.Progra.sales.domain;

import com.buses.examen.Progra.fleet.domain.Asiento;
import com.buses.examen.Progra.service.domain.Servicio;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.time.OffsetDateTime;

/**
 * Bloquea temporalmente un asiento de un servicio mientras se procesa la compra.
 */
@Entity
@Table(name = "reserva_asiento", uniqueConstraints = @UniqueConstraint(name = "uk_reserva_servicio_asiento_estado", columnNames = {"servicio_id", "asiento_id", "estado_reserva"}))
public class ReservaAsiento {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "servicio_id", nullable = false) private Servicio servicio;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "asiento_id", nullable = false) private Asiento asiento;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "ticket_id") private Ticket ticket;
    @Enumerated(EnumType.STRING) @Column(name = "estado_reserva", nullable = false) private EstadoReservaAsiento estadoReserva = EstadoReservaAsiento.ACTIVA;
    @Column(name = "expira_en", nullable = false) private OffsetDateTime expiraEn = OffsetDateTime.now().plusMinutes(15);
    @Column(name = "creado_en", nullable = false) private OffsetDateTime creadoEn = OffsetDateTime.now();

    /** Constructor requerido por JPA. */
    protected ReservaAsiento() {}

    /**
     * Crea una reserva activa para el asiento dado dentro del servicio indicado.
     *
     * @param servicio servicio al que pertenece el asiento
     * @param asiento  asiento a reservar
     * @return nueva reserva en estado {@link EstadoReservaAsiento#ACTIVA}
     */
    public static ReservaAsiento activa(final Servicio servicio, final Asiento asiento) {
        final ReservaAsiento reserva = new ReservaAsiento();
        reserva.servicio = servicio;
        reserva.asiento = asiento;
        reserva.estadoReserva = EstadoReservaAsiento.ACTIVA;
        return reserva;
    }

    /**
     * Devuelve el ticket confirmado asociado a esta reserva.
     *
     * @return ticket o {@code null} si aún no fue confirmado
     */
    public Ticket getTicket() { return ticket; }

    /**
     * Vincula el ticket confirmado a esta reserva.
     *
     * @param ticket ticket que confirma la reserva
     */
    void vincularTicket(final Ticket ticket) { this.ticket = ticket; }
}
