package com.buses.examen.Progra.service.application;

import com.buses.examen.Progra.service.application.port.out.ServicioRepositoryPort;
import com.buses.examen.Progra.service.domain.Servicio;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/** Pruebas de servicio de servicios programados. */
class ServiceServiceTest {

    @Test
    void shouldListServicesForRouteAndFindById() {
        final ServicioRepositoryPort servicioRepositoryPort = mock(ServicioRepositoryPort.class);
        final ServiceService service = new ServiceService(servicioRepositoryPort);

        final Servicio servicio = mock(Servicio.class);
        final OffsetDateTime start = OffsetDateTime.now();
        final OffsetDateTime end = start.plusDays(1);
        when(servicioRepositoryPort.findByRutaIdAndSalidaProgramadaBetween(1L, start, end))
                .thenReturn(List.of(servicio));
        when(servicioRepositoryPort.findById(10L)).thenReturn(Optional.of(servicio));

        assertThat(service.listServicesForRoute(1L, start, end)).containsExactly(servicio);
        assertThat(service.findServiceById(10L)).contains(servicio);
    }
}
