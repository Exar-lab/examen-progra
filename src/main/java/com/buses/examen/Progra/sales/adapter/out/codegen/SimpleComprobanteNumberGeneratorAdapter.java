package com.buses.examen.Progra.sales.adapter.out.codegen;

import com.buses.examen.Progra.sales.application.port.out.ComprobanteNumberGeneratorPort;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * Generador técnico de números de comprobante basado en epoch millis.
 */
@Component
public class SimpleComprobanteNumberGeneratorAdapter implements ComprobanteNumberGeneratorPort {

    /** {@inheritDoc} */
    @Override
    public String generate() {
        return String.valueOf(Instant.now().toEpochMilli());
    }
}
