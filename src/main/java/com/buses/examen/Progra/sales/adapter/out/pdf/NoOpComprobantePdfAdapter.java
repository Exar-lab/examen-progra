package com.buses.examen.Progra.sales.adapter.out.pdf;

import com.buses.examen.Progra.sales.application.port.out.ComprobantePdfPort;
import com.buses.examen.Progra.sales.domain.Compra;
import com.buses.examen.Progra.sales.domain.Comprobante;
import org.springframework.stereotype.Component;

/**
 * Adaptador temporal que representa la solicitud de generación PDF.
 */
@Component
public class NoOpComprobantePdfAdapter implements ComprobantePdfPort {

    /** {@inheritDoc} */
    @Override
    public void generateFor(final Compra compra, final Comprobante comprobante) {
        // Adaptador intencionalmente vacío para mantener desacoplado el puerto de PDF
        // cuando la infraestructura de generación electrónica no está instalada.
    }
}
