package com.buses.examen.Progra.route.adapter.in.web.mapper;

import com.buses.examen.Progra.geography.domain.Ciudad;
import com.buses.examen.Progra.geography.domain.Pais;
import com.buses.examen.Progra.route.application.port.out.RoutePlannerPort;
import com.buses.examen.Progra.route.domain.Ruta;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    /** Verifica que el catálogo expone duración positiva y precio recibido desde servicios. */
    @Test
    void shouldMapCatalogResponseWithPositiveDurationAndBasePrice() {
        final RouteWebMapper mapper = new RouteWebMapper();
        final Ruta ruta = mock(Ruta.class);
        final Ciudad originCity = mock(Ciudad.class);
        final Ciudad destinationCity = mock(Ciudad.class);
        final Pais originCountry = mock(Pais.class);
        final Pais destinationCountry = mock(Pais.class);

        when(ruta.getId()).thenReturn(1L);
        when(ruta.getCiudadOrigen()).thenReturn(originCity);
        when(ruta.getCiudadDestino()).thenReturn(destinationCity);
        when(ruta.getDuracionMinutos()).thenReturn(-480);
        when(originCity.getPais()).thenReturn(originCountry);
        when(destinationCity.getPais()).thenReturn(destinationCountry);
        when(originCountry.getCodigoIso()).thenReturn("CR");
        when(originCountry.getNombre()).thenReturn("Costa Rica");
        when(destinationCountry.getCodigoIso()).thenReturn("NI");
        when(destinationCountry.getNombre()).thenReturn("Nicaragua");

        final var response = mapper.toCatalogResponse(ruta, BigDecimal.valueOf(80));

        assertThat(response.durationHours()).isEqualTo(8);
        assertThat(response.basePrice()).isEqualByComparingTo(BigDecimal.valueOf(80));
    }
}
