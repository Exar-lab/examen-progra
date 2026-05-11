package com.buses.examen.Progra.sales.application.port.out;

import com.buses.examen.Progra.sales.domain.Compra;
import com.buses.examen.Progra.sales.domain.Comprobante;

/**
 * Puerto de salida para solicitar generación de comprobante PDF.
 */
public interface ComprobantePdfPort {
    /**
     * Solicita la generación del comprobante PDF para una compra.
     *
     * @param compra compra de origen
     * @param comprobante comprobante emitido
     */
    void generateFor(Compra compra, Comprobante comprobante);

    /**
     * Renderiza el comprobante como bytes PDF para descarga.
     *
     * @param compra compra asociada
     * @param comprobante comprobante emitido
     * @return bytes del documento PDF
     */
    byte[] renderFor(Compra compra, Comprobante comprobante);
}
