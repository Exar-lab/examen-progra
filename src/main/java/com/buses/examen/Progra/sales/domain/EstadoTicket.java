package com.buses.examen.Progra.sales.domain;

/** Estado de emisión del ticket de viaje. */
public enum EstadoTicket {
    /** Ticket emitido y válido para el viaje. */
    EMITIDO,
    /** Ticket anulado; no permite embarque. */
    ANULADO
}
