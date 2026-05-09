package com.buses.examen.Progra.fleet.adapter.in.web;

import com.buses.examen.Progra.fleet.application.port.in.FleetQueryUseCase;
import com.buses.examen.Progra.fleet.adapter.in.web.mapper.FleetWebMapper;
import com.buses.examen.Progra.fleet.domain.Asiento;
import com.buses.examen.Progra.fleet.domain.Bus;
import com.buses.examen.Progra.fleet.domain.Compania;
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

@WebMvcTest(FleetController.class)
@Import(FleetWebMapper.class)
@WithMockUser
class FleetControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FleetQueryUseCase fleetQueryUseCase;

    /**
     * Verifica que el endpoint de compañías devuelve resultados.
     *
     * @throws Exception si falla la ejecución del request MVC
     */
    @Test
    void shouldListCompanies() throws Exception {
        final Compania compania = new Compania("Tica Bus", "123");
        setId(compania, 3L);
        when(fleetQueryUseCase.listCompanies()).thenReturn(List.of(compania));

        mockMvc.perform(get("/api/fleet/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(3L));
    }

    /**
     * Verifica que buscar un bus inexistente responde con 404.
     *
     * @throws Exception si falla la ejecución del request MVC
     */
    @Test
    void shouldReturn404WhenBusNotFound() throws Exception {
        when(fleetQueryUseCase.findBusById(88L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/fleet/buses/88"))
                .andExpect(status().isNotFound());
    }

    /**
     * Verifica que el endpoint de asientos devuelve los asientos del bus.
     *
     * @throws Exception si falla la ejecución del request MVC
     */
    @Test
    void shouldListBusSeats() throws Exception {
        final Compania compania = new Compania("Tica Bus", "123");
        final Bus bus = new Bus(compania, "ABC123", "Volvo", 40);
        setId(bus, 4L);
        final Asiento asiento = new Asiento(bus, "A1", 1, "VENTANA");
        setId(asiento, 5L);
        when(fleetQueryUseCase.listSeatsByBus(4L)).thenReturn(List.of(asiento));

        mockMvc.perform(get("/api/fleet/buses/4/seats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(5L))
                .andExpect(jsonPath("$[0].busId").value(4L));
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
