package com.buses.examen.Progra.customer.adapter.out.clock;

import com.buses.examen.Progra.customer.application.port.out.ClockPort;
import org.springframework.stereotype.Component;

import java.time.YearMonth;

/** Adaptador de reloj del sistema. */
@Component
public class SystemClockAdapter implements ClockPort {
    /**
     * {@inheritDoc}
     *
     * @return año/mes actual del reloj del sistema
     */
    @Override
    public YearMonth currentYearMonth() {
        return YearMonth.now();
    }
}
