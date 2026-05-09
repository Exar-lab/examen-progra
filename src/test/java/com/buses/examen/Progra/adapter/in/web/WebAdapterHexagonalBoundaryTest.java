package com.buses.examen.Progra.adapter.in.web;

import com.buses.examen.Progra.customer.adapter.in.web.CustomerController;
import com.buses.examen.Progra.customer.application.CustomerService;
import com.buses.examen.Progra.customer.domain.Cliente;
import com.buses.examen.Progra.fleet.adapter.in.web.FleetController;
import com.buses.examen.Progra.fleet.application.FleetService;
import com.buses.examen.Progra.fleet.domain.Bus;
import com.buses.examen.Progra.geography.adapter.in.web.GeographyController;
import com.buses.examen.Progra.geography.application.GeographyService;
import com.buses.examen.Progra.geography.domain.Pais;
import com.buses.examen.Progra.loyalty.adapter.in.web.LoyaltyController;
import com.buses.examen.Progra.loyalty.application.LoyaltyService;
import com.buses.examen.Progra.route.adapter.in.web.RouteController;
import com.buses.examen.Progra.route.application.RouteService;
import com.buses.examen.Progra.route.domain.Ruta;
import com.buses.examen.Progra.service.adapter.in.web.ServiceController;
import com.buses.examen.Progra.service.application.ServiceService;
import com.buses.examen.Progra.service.domain.Servicio;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifica que los adaptadores web preserven las fronteras hexagonales.
 */
class WebAdapterHexagonalBoundaryTest {
    private static final List<Class<?>> WEB_ADAPTERS = List.of(
            CustomerController.class,
            GeographyController.class,
            FleetController.class,
            RouteController.class,
            ServiceController.class,
            LoyaltyController.class
    );

    private static final List<Class<?>> APPLICATION_SERVICES = List.of(
            CustomerService.class,
            GeographyService.class,
            FleetService.class,
            RouteService.class,
            ServiceService.class,
            LoyaltyService.class
    );

    private static final List<Class<?>> DOMAIN_TYPES = List.of(
            Cliente.class,
            Pais.class,
            Bus.class,
            Ruta.class,
            Servicio.class
    );

    @Test
    void webAdaptersDependOnInboundPortsInsteadOfApplicationServices() {
        WEB_ADAPTERS.forEach(adapter -> Arrays.stream(adapter.getDeclaredConstructors())
                .flatMap(constructor -> Arrays.stream(constructor.getParameterTypes()))
                .forEach(parameterType -> assertThat(APPLICATION_SERVICES)
                        .as("%s must not depend on concrete application service %s", adapter.getName(), parameterType.getName())
                        .doesNotContain(parameterType)));
    }

    @Test
    void webAdapterPublicMethodsDoNotExposeDomainTypes() {
        WEB_ADAPTERS.forEach(adapter -> Stream.of(adapter.getDeclaredMethods())
                .filter(method -> !method.isSynthetic())
                .forEach(this::assertMethodDoesNotExposeDomainTypes));
    }

    private void assertMethodDoesNotExposeDomainTypes(final Method method) {
        assertThat(DOMAIN_TYPES)
                .as("%s must not return domain type directly", method.getName())
                .doesNotContain(method.getReturnType());
        Arrays.stream(method.getParameterTypes())
                .forEach(parameterType -> assertThat(DOMAIN_TYPES)
                        .as("%s must not accept domain type directly", method.getName())
                        .doesNotContain(parameterType));
    }
}
