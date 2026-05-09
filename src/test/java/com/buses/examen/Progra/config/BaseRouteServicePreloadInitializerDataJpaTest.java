package com.buses.examen.Progra.config;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(BaseRouteServicePreloadInitializer.class)
class BaseRouteServicePreloadInitializerDataJpaTest {

    @Autowired
    private BaseRouteServicePreloadInitializer initializer;

    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldPreloadReadmeServicesWithExpectedRouteScheduleAndPrice() throws Exception {
        initializer.run(new DefaultApplicationArguments(new String[0]));
        entityManager.flush();

        final List<PreloadedServiceView> services = fetchPreloadedServices();

        assertThat(services)
                .hasSize(13)
                .containsExactlyInAnyOrder(
                        new PreloadedServiceView("CR", "NI", 3, money(80)),
                        new PreloadedServiceView("CR", "ES", 6, money(120)),
                        new PreloadedServiceView("CR", "GUA", 6, money(140)),
                        new PreloadedServiceView("CR", "NI", 6, money(80)),
                        new PreloadedServiceView("CR", "HN", 6, money(110)),
                        new PreloadedServiceView("CR", "PN", 5, money(80)),
                        new PreloadedServiceView("PN", "CR", 8, money(80)),
                        new PreloadedServiceView("GUA", "CR", 5, money(140)),
                        new PreloadedServiceView("GUA", "ES", 5, money(80)),
                        new PreloadedServiceView("GUA", "HN", 5, money(110)),
                        new PreloadedServiceView("GUA", "NI", 5, money(120)),
                        new PreloadedServiceView("NI", "CR", 3, money(80)),
                        new PreloadedServiceView("NI", "CR", 6, money(80))
                );
    }

    @Test
    void shouldBeIdempotentWhenInitializerRunsMoreThanOnce() throws Exception {
        initializer.run(new DefaultApplicationArguments(new String[0]));
        initializer.run(new DefaultApplicationArguments(new String[0]));
        entityManager.flush();

        final Long countries = entityManager.createQuery("select count(p) from Pais p", Long.class).getSingleResult();
        final Long routes = entityManager.createQuery("select count(r) from Ruta r", Long.class).getSingleResult();
        final Long services = entityManager.createQuery("select count(s) from Servicio s", Long.class).getSingleResult();

        assertThat(countries).isEqualTo(6L);
        assertThat(routes).isEqualTo(11L);
        assertThat(services).isEqualTo(13L);
    }

    private List<PreloadedServiceView> fetchPreloadedServices() {
        final List<Object[]> rows = entityManager.createQuery("""
                select cOrigin.codigo,
                       cDestination.codigo,
                       function('hour', s.salidaProgramada),
                       s.precioBase
                from Servicio s
                join s.ruta r
                join r.ciudadOrigen cOrigin
                join r.ciudadDestino cDestination
                """, Object[].class)
                .getResultList();

        return rows.stream()
                .map(row -> new PreloadedServiceView(
                        (String) row[0],
                        (String) row[1],
                        ((Number) row[2]).intValue(),
                        (BigDecimal) row[3]
                ))
                .toList();
    }

    private static BigDecimal money(final int value) {
        return BigDecimal.valueOf(value).setScale(2);
    }

    private record PreloadedServiceView(String originCode, String destinationCode, Integer departureHour,
                                        BigDecimal price) {
    }
}
