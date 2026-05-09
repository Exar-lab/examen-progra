package com.buses.examen.Progra.sales.adapter.in.web.mapper;

import com.buses.examen.Progra.customer.adapter.out.security.AuthenticatedCustomerPrincipal;
import com.buses.examen.Progra.sales.adapter.in.web.dto.request.PurchaseRequest;
import com.buses.examen.Progra.sales.application.command.PurchaseTicketsCommand;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifica que el mapper de compra extrae el clienteId del principal, no del request.
 */
class PurchaseWebMapperTest {

    private final PurchaseWebMapper mapper = new PurchaseWebMapper();

    /**
     * Verifica que clienteId en el comando proviene del principal autenticado, nunca del request.
     */
    @Test
    void shouldReadClienteIdFromPrincipalNotFromRequest() {
        final AuthenticatedCustomerPrincipal principal =
                new AuthenticatedCustomerPrincipal(99L, "juanp", "hash", true, true);
        final PurchaseRequest request = new PurchaseRequest(10L, 5L, List.of(21L, 22L), "WEB", "GW-123");

        final PurchaseTicketsCommand command = mapper.toCommand(request, principal);

        assertThat(command.clienteId()).isEqualTo(99L);
    }

    /**
     * Verifica que todos los campos del request se copian al comando correctamente.
     */
    @Test
    void shouldMapAllRequestFieldsToCommand() {
        final AuthenticatedCustomerPrincipal principal =
                new AuthenticatedCustomerPrincipal(5L, "marial", "hash", true, true);
        final PurchaseRequest request = new PurchaseRequest(10L, 7L, List.of(31L, 32L, 33L), "COUNTER", "OP-456");

        final PurchaseTicketsCommand command = mapper.toCommand(request, principal);

        assertThat(command.clienteId()).isEqualTo(5L);
        assertThat(command.tarjetaId()).isEqualTo(10L);
        assertThat(command.servicioId()).isEqualTo(7L);
        assertThat(command.asientoIds()).containsExactly(31L, 32L, 33L);
        assertThat(command.canalCompra()).isEqualTo("COUNTER");
        assertThat(command.codigoOperacionExterna()).isEqualTo("OP-456");
    }
}
