package com.buses.examen.Progra.service.adapter.in.web;

import com.buses.examen.Progra.fleet.domain.Bus;
import com.buses.examen.Progra.fleet.domain.Compania;
import com.buses.examen.Progra.geography.domain.Ciudad;
import com.buses.examen.Progra.geography.domain.Pais;
import com.buses.examen.Progra.route.domain.Ruta;
import com.buses.examen.Progra.service.application.port.in.ServiceQueryUseCase;
import com.buses.examen.Progra.service.adapter.in.web.mapper.ServiceWebMapper;
import com.buses.examen.Progra.service.domain.EstadoServicio;
import com.buses.examen.Progra.service.domain.Servicio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ServiceController.class)
@Import(ServiceWebMapper.class)
@WithMockUser
class ServiceControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ServiceQueryUseCase serviceQueryUseCase;

    /**
     * Verifica que el endpoint de servicios por ruta devuelve servicios mapeados.
     *
     * @throws Exception si falla la ejecución del request MVC
     */
    @Test
    void shouldListServicesForRoute() throws Exception {
        final Servicio servicio = sampleServicio();
        setId(servicio, 9L);
        setId(servicio.getBus(), 4L);
        when(serviceQueryUseCase.listServicesForRoute(1L,
                OffsetDateTime.parse("2026-06-01T00:00:00Z"),
                OffsetDateTime.parse("2026-06-10T00:00:00Z")))
                .thenReturn(List.of(servicio));

        mockMvc.perform(get("/api/services")
                        .param("rutaId", "1")
                        .param("start", "2026-06-01T00:00:00Z")
                        .param("end", "2026-06-10T00:00:00Z"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(9L))
                .andExpect(jsonPath("$[0].busId").value(4L))
                .andExpect(jsonPath("$[0].capacidadDisponible").value(20));
    }

    /**
     * Verifica que buscar un servicio inexistente responde con 404.
     *
     * @throws Exception si falla la ejecución del request MVC
     */
    @Test
    void shouldReturn404WhenServiceNotFound() throws Exception {
        when(serviceQueryUseCase.findServiceById(901L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/services/901"))
                .andExpect(status().isNotFound());
    }

    private static Servicio sampleServicio() {
        final Pais pais = new Pais("CR", "Costa Rica");
        final Ciudad origen = new Ciudad(pais, "San Jose", "SJO");
        final Ciudad destino = new Ciudad(pais, "Liberia", "LIR");
        final Ruta ruta = new Ruta(origen, destino, 120, 180.0);
        final Compania compania = new Compania("Tica Bus", "123");
        final Bus bus = new Bus(compania, "ABC123", "Volvo", 40);
        return new Servicio(ruta, bus,
                OffsetDateTime.parse("2026-06-05T10:00:00Z"),
                OffsetDateTime.parse("2026-06-05T12:00:00Z"),
                BigDecimal.valueOf(10.0),
                EstadoServicio.PROGRAMADO,
                20);
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
