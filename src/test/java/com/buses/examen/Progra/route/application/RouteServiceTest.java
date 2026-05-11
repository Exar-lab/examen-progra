package com.buses.examen.Progra.route.application;

import com.buses.examen.Progra.route.adapter.in.web.mapper.RouteWebMapper;
import com.buses.examen.Progra.route.application.port.out.RoutePlannerPort;
import com.buses.examen.Progra.route.application.port.out.RutaRepositoryPort;
import com.buses.examen.Progra.route.domain.Ruta;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/** Pruebas de servicio de rutas. */
class RouteServiceTest {

    @Test
    void shouldListRoutesAndFindById() {
        final RutaRepositoryPort rutaRepositoryPort = mock(RutaRepositoryPort.class);
        final RoutePlannerPort plannerPort = mock(RoutePlannerPort.class);
        final RouteWebMapper mapper = mock(RouteWebMapper.class);
        final RouteService service = new RouteService(rutaRepositoryPort, plannerPort, mapper);

        final Ruta ruta = mock(Ruta.class);
        when(rutaRepositoryPort.findAll()).thenReturn(List.of(ruta));
        when(rutaRepositoryPort.findById(11L)).thenReturn(Optional.of(ruta));

        assertThat(service.listRoutes()).containsExactly(ruta);
        assertThat(service.findRouteById(11L)).contains(ruta);
    }

    @Test
    void shouldDelegateRoutePlanning() {
        final RutaRepositoryPort rutaRepositoryPort = mock(RutaRepositoryPort.class);
        final RoutePlannerPort plannerPort = mock(RoutePlannerPort.class);
        final RouteWebMapper mapper = mock(RouteWebMapper.class);
        final RouteService service = new RouteService(rutaRepositoryPort, plannerPort, mapper);

        final RoutePlannerPort.RouteOption option = new RoutePlannerPort.RouteOption(1L, 2L, 95);
        when(plannerPort.findBestRoutes(10L, 20L, Instant.EPOCH)).thenReturn(List.of(option));

        assertThat(service.planRoutes(10L, 20L, Instant.EPOCH)).containsExactly(option);
    }
}
