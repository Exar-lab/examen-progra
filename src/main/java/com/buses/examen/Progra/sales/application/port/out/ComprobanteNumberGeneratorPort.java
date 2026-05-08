package com.buses.examen.Progra.sales.application.port.out;

/**
 * Puerto de salida para generar números de comprobante.
 */
public interface ComprobanteNumberGeneratorPort {

    /**
     * Genera un número de comprobante.
     *
     * @return número generado
     */
    String generate();
}
