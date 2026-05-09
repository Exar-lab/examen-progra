package com.buses.examen.Progra.route.adapter.in.web.mapper;

import com.buses.examen.Progra.route.application.port.out.RoutePlannerPort;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RouteWebMapperTest {
    /** Verifica que el mapper transforma una opción planificada en respuesta web. */
    @Test
    void shouldMapRouteOptionToResponse() {
        final RouteWebMapper mapper = new RouteWebMapper();
        final var response = mapper.toRouteOptionResponse(new RoutePlannerPort.RouteOption(1L, 2L, 77));
        assertThat(response.rutaId()).isEqualTo(1L);
        assertThat(response.servicioId()).isEqualTo(2L);
        assertThat(response.score()).isEqualTo(77);
    }
}
