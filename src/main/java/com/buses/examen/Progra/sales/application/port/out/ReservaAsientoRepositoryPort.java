package com.buses.examen.Progra.sales.application.port.out;

import com.buses.examen.Progra.sales.domain.EstadoReservaAsiento;
import com.buses.examen.Progra.sales.domain.ReservaAsiento;

import java.util.Optional;

/**
 * Puerto de salida para persistencia y validación de reservas de asiento.
 */
public interface ReservaAsientoRepositoryPort {
    /**
     * Persiste una reserva de asiento.
     *
     * @param reservaAsiento reserva a guardar
     * @return reserva persistida
     */
    ReservaAsiento save(ReservaAsiento reservaAsiento);

    /**
     * Verifica si existe una reserva para combinación servicio/asiento/estado.
     *
     * @param servicioId identificador del servicio
     * @param asientoId identificador del asiento
     * @param estadoReserva estado de reserva
     * @return {@code true} si existe al menos una reserva
     */
    boolean existsByServicioIdAndAsientoIdAndEstadoReserva(Long servicioId, Long asientoId, EstadoReservaAsiento estadoReserva);

    /**
     * Busca una reserva por id.
     *
     * @param id identificador de la reserva
     * @return reserva encontrada, si existe
     */
    Optional<ReservaAsiento> findById(Long id);
}
