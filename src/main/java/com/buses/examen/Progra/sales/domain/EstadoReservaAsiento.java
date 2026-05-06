package com.buses.examen.Progra.sales.domain;

/** Estado del ciclo de vida de una reserva de asiento. */
public enum EstadoReservaAsiento {
    /** Reserva vigente; el asiento está bloqueado para el cliente. */
    ACTIVA,
    /** Reserva confirmada mediante ticket emitido. */
    CONFIRMADA,
    /** Reserva anulada por el cliente o el sistema. */
    CANCELADA,
    /** Reserva expirada por inactividad tras el tiempo límite. */
    EXPIRADA
}
