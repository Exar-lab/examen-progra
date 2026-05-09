package com.buses.examen.Progra.geography.application;

import com.buses.examen.Progra.geography.application.port.out.CiudadRepositoryPort;
import com.buses.examen.Progra.geography.application.port.out.PaisRepositoryPort;
import com.buses.examen.Progra.geography.domain.Ciudad;
import com.buses.examen.Progra.geography.domain.Pais;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/** Pruebas de servicio de geografía. */
class GeographyServiceTest {

    @Test
    void shouldListCountriesAndCities() {
        final PaisRepositoryPort paisRepositoryPort = mock(PaisRepositoryPort.class);
        final CiudadRepositoryPort ciudadRepositoryPort = mock(CiudadRepositoryPort.class);
        final GeographyService service = new GeographyService(paisRepositoryPort, ciudadRepositoryPort);

        final Pais costaRica = new Pais("CR", "Costa Rica");
        final Ciudad sanJose = new Ciudad(costaRica, "San Jose", "SJO");
        when(paisRepositoryPort.findAll()).thenReturn(List.of(costaRica));
        when(ciudadRepositoryPort.findByPaisId(1L)).thenReturn(List.of(sanJose));

        assertThat(service.listCountries()).containsExactly(costaRica);
        assertThat(service.listCitiesByCountry(1L)).containsExactly(sanJose);
    }

    @Test
    void shouldFindCountryAndCityById() {
        final PaisRepositoryPort paisRepositoryPort = mock(PaisRepositoryPort.class);
        final CiudadRepositoryPort ciudadRepositoryPort = mock(CiudadRepositoryPort.class);
        final GeographyService service = new GeographyService(paisRepositoryPort, ciudadRepositoryPort);

        final Pais costaRica = new Pais("CR", "Costa Rica");
        final Ciudad sanJose = new Ciudad(costaRica, "San Jose", "SJO");
        when(paisRepositoryPort.findById(10L)).thenReturn(Optional.of(costaRica));
        when(ciudadRepositoryPort.findById(20L)).thenReturn(Optional.of(sanJose));

        assertThat(service.findCountryById(10L)).contains(costaRica);
        assertThat(service.findCityById(20L)).contains(sanJose);
    }
}
