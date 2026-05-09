package com.buses.examen.Progra.customer.application.port.out;

import java.time.YearMonth;

/** Puerto de tiempo para reglas de negocio deterministas. */
public interface ClockPort {
    /**
     * Obtiene el año/mes actual para validar reglas temporales de negocio.
     *
     * @return año/mes vigente
     */
    YearMonth currentYearMonth();
}
