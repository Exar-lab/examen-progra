package com.buses.examen.Progra.customer.adapter.out.security;

import com.buses.examen.Progra.customer.application.command.RawCardData;
import com.buses.examen.Progra.customer.application.result.ProtectedCardData;
import org.junit.jupiter.api.Test;

import java.time.YearMonth;

import static org.assertj.core.api.Assertions.assertThat;

class PassThroughCardDataProtectorAdapterTest {

    /** Verifica que el PAN se enmascara con formato 4+6 asteriscos+4. */
    @Test
    void shouldMaskPanAsFirst4SixAsterisksAndLast4() {
        final PassThroughCardDataProtectorAdapter adapter = new PassThroughCardDataProtectorAdapter();

        final ProtectedCardData result = adapter.protect(new RawCardData(
                "Ana Perez", "4111111111111111", YearMonth.of(2030, 12), "999"
        ));

        assertThat(result.enmascarada()).isEqualTo("4111******1111");
    }
}
