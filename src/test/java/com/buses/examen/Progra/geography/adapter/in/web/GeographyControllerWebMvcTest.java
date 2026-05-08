package com.buses.examen.Progra.geography.adapter.in.web;

import com.buses.examen.Progra.geography.application.port.in.GeographyQueryUseCase;
import com.buses.examen.Progra.geography.adapter.in.web.mapper.GeographyWebMapper;
import com.buses.examen.Progra.geography.domain.Ciudad;
import com.buses.examen.Progra.geography.domain.Pais;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GeographyController.class)
@Import(GeographyWebMapper.class)
@WithMockUser
class GeographyControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GeographyQueryUseCase geographyQueryUseCase;

    /**
     * Verifica que el endpoint de países devuelve resultados.
     *
     * @throws Exception si falla la ejecución del request MVC
     */
    @Test
    void shouldListCountries() throws Exception {
        final Pais pais = new Pais("CR", "Costa Rica");
        setId(pais, 1L);
        when(geographyQueryUseCase.listCountries()).thenReturn(List.of(pais));

        mockMvc.perform(get("/api/geography/countries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    /**
     * Verifica que buscar un país inexistente responde con 404.
     *
     * @throws Exception si falla la ejecución del request MVC
     */
    @Test
    void shouldReturn404WhenCountryNotFound() throws Exception {
        when(geographyQueryUseCase.findCountryById(44L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/geography/countries/44"))
                .andExpect(status().isNotFound());
    }

    /**
     * Verifica que el endpoint de ciudades por país devuelve resultados.
     *
     * @throws Exception si falla la ejecución del request MVC
     */
    @Test
    void shouldListCitiesByCountry() throws Exception {
        final Pais pais = new Pais("CR", "Costa Rica");
        final Ciudad ciudad = new Ciudad(pais, "San Jose", "SJO");
        setId(ciudad, 2L);
        when(geographyQueryUseCase.listCitiesByCountry(1L)).thenReturn(List.of(ciudad));

        mockMvc.perform(get("/api/geography/countries/1/cities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2L));
    }

    private static void setId(final Object target, final Long id) {
        try {
            final Field field = target.getClass().getDeclaredField("id");
            field.setAccessible(true);
            field.set(target, id);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException(exception);
        }
    }
}
