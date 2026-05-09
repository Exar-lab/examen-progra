package com.buses.examen.Progra.sales.application.port.out;

/**
 * Puerto de salida para generar candidatos de código único de ticket.
 */
public interface TicketCodeGeneratorPort {
    /**
     * Genera un código candidato para ticket.
     *
     * @return código candidato
     */
    String generate();
}
