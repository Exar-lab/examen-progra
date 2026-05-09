package com.buses.examen.Progra.customer.application.port.out;

import com.buses.examen.Progra.customer.application.command.RawCardData;
import com.buses.examen.Progra.customer.application.result.ProtectedCardData;

/** Puerto para proteger datos sensibles de tarjeta. */
public interface CardDataProtectorPort {
    /**
     * Protege datos sensibles de tarjeta para su almacenamiento seguro.
     *
     * @param rawCardData datos de tarjeta en claro
     * @return datos protegidos/enmascarados de tarjeta
     */
    ProtectedCardData protect(RawCardData rawCardData);
}
