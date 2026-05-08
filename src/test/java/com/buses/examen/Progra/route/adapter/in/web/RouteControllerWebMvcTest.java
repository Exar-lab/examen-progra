package com.buses.examen.Progra.route.adapter.in.web;

import com.buses.examen.Progra.route.application.port.in.RouteQueryUseCase;
import com.buses.examen.Progra.route.adapter.in.web.mapper.RouteWebMapper;
import com.buses.examen.Progra.route.application.port.out.RoutePlannerPort;
import com.buses.examen.Progra.route.domain.Ruta;
import com.buses.examen.Progra.geography.domain.Pais;
import com.buses.examen.Progra.geography.domain.Ciudad;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RouteController.class)
@Import(RouteWebMapper.class)
@WithMockUser
class RouteControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RouteQueryUseCase routeQueryUseCase;

    /**
     * Verifica que el endpoint de rutas devuelve rutas disponibles.
     *
     * @throws Exception si falla la ejecución del request MVC
     */
    @Test
    void shouldListRoutes() throws Exception {
        final Pais pais = new Pais("CR", "Costa Rica");
        final Ruta ruta = new Ruta(new Ciudad(pais, "San Jose", "SJO"), new Ciudad(pais, "Liberia", "LIR"), 120, 180.0);
        setId(ruta, 10L);
        when(routeQueryUseCase.listRoutes()).thenReturn(List.of(ruta));

        mockMvc.perform(get("/api/routes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10L));
    }

    /**
     * Verifica que el endpoint de planificación preserva todos los campos de respuesta.
     *
     * @throws Exception si falla la ejecución del request MVC
     */
    @Test
    void shouldPlanRoutesKeepingResponseFields() throws Exception {
        when(routeQueryUseCase.planRoutes(1L, 2L, Instant.parse("2026-01-10T12:30:00Z")))
                .thenReturn(List.of(new RoutePlannerPort.RouteOption(11L, 22L, 99)));

        mockMvc.perform(get("/api/routes/plan")
                        .param("originCityId", "1")
                        .param("destinationCityId", "2")
                        .param("departureAfter", "2026-01-10T12:30:00Z"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].rutaId").value(11L))
                .andExpect(jsonPath("$[0].servicioId").value(22L))
                .andExpect(jsonPath("$[0].score").value(99));
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
