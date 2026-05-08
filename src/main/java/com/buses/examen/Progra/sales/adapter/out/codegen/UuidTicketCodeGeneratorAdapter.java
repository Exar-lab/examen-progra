package com.buses.examen.Progra.sales.adapter.out.codegen;

import com.buses.examen.Progra.sales.application.port.out.TicketCodeGeneratorPort;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Generador técnico de códigos de ticket basado en UUID.
 */
@Component
public class UuidTicketCodeGeneratorAdapter implements TicketCodeGeneratorPort {

    /** {@inheritDoc} */
    @Override
    public String generate() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }
}
