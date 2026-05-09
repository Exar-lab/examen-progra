package com.buses.examen.Progra.service.adapter.in.web.mapper;

import com.buses.examen.Progra.fleet.domain.Bus;
import com.buses.examen.Progra.fleet.domain.Compania;
import com.buses.examen.Progra.geography.domain.Ciudad;
import com.buses.examen.Progra.geography.domain.Pais;
import com.buses.examen.Progra.route.domain.Ruta;
import com.buses.examen.Progra.service.domain.EstadoServicio;
import com.buses.examen.Progra.service.domain.Servicio;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ServiceWebMapperTest {
    /** Verifica que el mapper transforma un servicio de dominio en respuesta web. */
    @Test
    void shouldMapServiceToResponse() {
        final Pais pais = new Pais("CR", "Costa Rica");
        final Ruta ruta = new Ruta(new Ciudad(pais, "San Jose", "SJO"), new Ciudad(pais, "Liberia", "LIR"), 120, 180.0);
        final Bus bus = new Bus(new Compania("Tica Bus", "123"), "ABC123", "Volvo", 40);
        setId(bus, 5L);
        final Servicio servicio = new Servicio(ruta, bus, OffsetDateTime.parse("2026-06-05T10:00:00Z"), OffsetDateTime.parse("2026-06-05T12:00:00Z"), BigDecimal.valueOf(10.0), EstadoServicio.PROGRAMADO, 20);
        setId(servicio, 7L);

        final var response = new ServiceWebMapper().toResponse(servicio);

        assertThat(response.id()).isEqualTo(7L);
        assertThat(response.busId()).isEqualTo(5L);
        assertThat(response.capacidadDisponible()).isEqualTo(20);
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
