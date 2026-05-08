package com.buses.examen.Progra.sales.application;

import com.buses.examen.Progra.sales.application.port.in.PurchaseTicketsUseCase;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/** Prueba contractual del servicio de ventas aún no implementado. */
class SalesServiceContractTest {

    @Test
    void shouldDeclarePurchaseUseCaseInterface() {
        assertThat(PurchaseTicketsUseCase.class.isInterface()).isTrue();
    }
}
