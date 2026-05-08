package com.buses.examen.Progra.application.port.in;

import com.buses.examen.Progra.customer.application.port.in.CustomerQueryUseCase;
import com.buses.examen.Progra.customer.application.port.in.RegisterCustomerUseCase;
import com.buses.examen.Progra.customer.application.command.RegisterCardCommand;
import com.buses.examen.Progra.customer.application.command.RegisterCustomerCommand;
import com.buses.examen.Progra.customer.application.result.RegisterCardResult;
import com.buses.examen.Progra.customer.application.result.RegisterCustomerResult;
import com.buses.examen.Progra.fleet.application.port.in.FleetQueryUseCase;
import com.buses.examen.Progra.geography.application.port.in.GeographyQueryUseCase;
import com.buses.examen.Progra.loyalty.application.port.in.LoyaltyQueryUseCase;
import com.buses.examen.Progra.route.application.port.in.RouteQueryUseCase;
import com.buses.examen.Progra.sales.application.port.in.PurchaseTicketsUseCase;
import com.buses.examen.Progra.sales.application.result.PurchaseTicketsResult;
import com.buses.examen.Progra.service.application.port.in.ServiceQueryUseCase;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/** Verifica que los puertos de entrada definidos exponen sus contratos públicos. */
class InboundPortsContractTest {

    /**
     * Verifica contratos de registro y consulta de clientes.
     *
     * @throws NoSuchMethodException si algún método del contrato no existe
     */
    @Test
    void shouldExposeCustomerInboundPorts() throws NoSuchMethodException {
        assertThat(RegisterCustomerUseCase.class.isInterface()).isTrue();
        final Method register = RegisterCustomerUseCase.class.getMethod(
                "register",
                RegisterCustomerCommand.class
        );
        assertThat(register.getReturnType()).isEqualTo(RegisterCustomerResult.class);

        final Method registerCard = RegisterCustomerUseCase.class.getMethod(
                "registerCard",
                RegisterCardCommand.class
        );
        assertThat(registerCard.getReturnType()).isEqualTo(RegisterCardResult.class);

        assertThat(CustomerQueryUseCase.class.isInterface()).isTrue();
        final Method findByPassport = CustomerQueryUseCase.class.getMethod("findByDocumentoIdentidad", String.class);
        assertThat(findByPassport.getReturnType()).isEqualTo(Optional.class);
    }

    /**
     * Verifica contratos de consulta para geografía, flota, rutas, servicios y lealtad.
     *
     * @throws NoSuchMethodException si algún método del contrato no existe
     */
    @Test
    void shouldExposeCatalogQueryPorts() throws NoSuchMethodException {
        assertThat(GeographyQueryUseCase.class.isInterface()).isTrue();
        assertThat(GeographyQueryUseCase.class.getMethod("listCountries").getReturnType()).isEqualTo(List.class);
        assertThat(GeographyQueryUseCase.class.getMethod("listCitiesByCountry", Long.class).getReturnType()).isEqualTo(List.class);
        assertThat(GeographyQueryUseCase.class.getMethod("findCountryById", Long.class).getReturnType()).isEqualTo(Optional.class);
        assertThat(GeographyQueryUseCase.class.getMethod("findCityById", Long.class).getReturnType()).isEqualTo(Optional.class);

        assertThat(FleetQueryUseCase.class.isInterface()).isTrue();
        assertThat(FleetQueryUseCase.class.getMethod("listCompanies").getReturnType()).isEqualTo(List.class);
        assertThat(FleetQueryUseCase.class.getMethod("listBusesByCompany", Long.class).getReturnType()).isEqualTo(List.class);
        assertThat(FleetQueryUseCase.class.getMethod("listSeatsByBus", Long.class).getReturnType()).isEqualTo(List.class);
        assertThat(FleetQueryUseCase.class.getMethod("findBusById", Long.class).getReturnType()).isEqualTo(Optional.class);

        assertThat(RouteQueryUseCase.class.isInterface()).isTrue();
        assertThat(RouteQueryUseCase.class.getMethod("listRoutes").getReturnType()).isEqualTo(List.class);
        assertThat(RouteQueryUseCase.class.getMethod("findRouteById", Long.class).getReturnType()).isEqualTo(Optional.class);
        assertThat(RouteQueryUseCase.class.getMethod("planRoutes", Long.class, Long.class, Instant.class).getReturnType()).isEqualTo(List.class);

        assertThat(ServiceQueryUseCase.class.isInterface()).isTrue();
        assertThat(ServiceQueryUseCase.class.getMethod("listServicesForRoute", Long.class, OffsetDateTime.class, OffsetDateTime.class)
                .getReturnType()).isEqualTo(List.class);
        assertThat(ServiceQueryUseCase.class.getMethod("findServiceById", Long.class).getReturnType()).isEqualTo(Optional.class);

        assertThat(LoyaltyQueryUseCase.class.isInterface()).isTrue();
        assertThat(LoyaltyQueryUseCase.class.getMethod("listHistory", Long.class).getReturnType()).isEqualTo(List.class);
    }

    /**
     * Verifica contrato de compra de tickets.
     *
     * @throws NoSuchMethodException si el método del contrato no existe
     */
    @Test
    void shouldExposePurchaseTicketsUseCase() throws NoSuchMethodException {
        assertThat(PurchaseTicketsUseCase.class.isInterface()).isTrue();
        final Method purchase = PurchaseTicketsUseCase.class.getMethod("purchase",
                com.buses.examen.Progra.sales.application.command.PurchaseTicketsCommand.class);
        assertThat(purchase.getReturnType()).isEqualTo(PurchaseTicketsResult.class);
    }

}
