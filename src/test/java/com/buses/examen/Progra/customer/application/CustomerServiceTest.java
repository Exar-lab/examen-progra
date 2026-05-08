package com.buses.examen.Progra.customer.application;

import com.buses.examen.Progra.customer.application.port.out.ClienteRepositoryPort;
import com.buses.examen.Progra.customer.application.port.out.TarjetaRepositoryPort;
import com.buses.examen.Progra.customer.domain.Cliente;
import com.buses.examen.Progra.customer.domain.Tarjeta;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** Pruebas de servicio de clientes. */
class CustomerServiceTest {

    @Test
    void shouldRegisterCustomerAndReturnSavedEntity() {
        final ClienteRepositoryPort clienteRepositoryPort = mock(ClienteRepositoryPort.class);
        final TarjetaRepositoryPort tarjetaRepositoryPort = mock(TarjetaRepositoryPort.class);
        final CustomerService service = new CustomerService(clienteRepositoryPort, tarjetaRepositoryPort);

        final ArgumentCaptor<Cliente> captor = ArgumentCaptor.forClass(Cliente.class);
        final Cliente persisted = new Cliente("Ana", "Perez", "P-123", "ana@mail.com", "888");
        when(clienteRepositoryPort.save(captor.capture())).thenReturn(persisted);

        final Cliente result = service.register("Ana", "Perez", "P-123", "CR", "ana@mail.com", "888");

        assertThat(result).isEqualTo(persisted);
        assertThat(captor.getValue()).isNotNull();
    }

    @Test
    void shouldRegisterCardForExistingCustomer() {
        final ClienteRepositoryPort clienteRepositoryPort = mock(ClienteRepositoryPort.class);
        final TarjetaRepositoryPort tarjetaRepositoryPort = mock(TarjetaRepositoryPort.class);
        final CustomerService service = new CustomerService(clienteRepositoryPort, tarjetaRepositoryPort);

        final Cliente cliente = new Cliente("Ana", "Perez", "P-123", "ana@mail.com", "888");
        when(clienteRepositoryPort.findById(10L)).thenReturn(Optional.of(cliente));
        final ArgumentCaptor<Tarjeta> captor = ArgumentCaptor.forClass(Tarjeta.class);
        when(tarjetaRepositoryPort.save(captor.capture())).thenAnswer(invocation -> captor.getValue());

        final Tarjeta result = service.registerCard(10L, "Ana Perez", "VISA", "1111", 12, 2030,
                "tok-1", "4111******1111", "999");

        assertThat(result.getTokenReferencia()).isEqualTo("tok-1");
        assertThat(result.getEnmascarada()).isEqualTo("4111******1111");
        verify(tarjetaRepositoryPort).save(captor.getValue());
    }

    @Test
    void shouldFailRegisterCardWhenCustomerMissing() {
        final ClienteRepositoryPort clienteRepositoryPort = mock(ClienteRepositoryPort.class);
        final TarjetaRepositoryPort tarjetaRepositoryPort = mock(TarjetaRepositoryPort.class);
        final CustomerService service = new CustomerService(clienteRepositoryPort, tarjetaRepositoryPort);

        when(clienteRepositoryPort.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.registerCard(99L, "Ana Perez", "VISA", "1111", 12, 2030,
                "tok-1", "4111******1111", "999"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Cliente no encontrado");
    }

    @Test
    void shouldLookupCustomerByDocumentoIdentidad() {
        final ClienteRepositoryPort clienteRepositoryPort = mock(ClienteRepositoryPort.class);
        final TarjetaRepositoryPort tarjetaRepositoryPort = mock(TarjetaRepositoryPort.class);
        final CustomerService service = new CustomerService(clienteRepositoryPort, tarjetaRepositoryPort);

        final Cliente cliente = new Cliente("Ana", "Perez", "P-123", "ana@mail.com", "888");
        when(clienteRepositoryPort.findByDocumentoIdentidad("P-123")).thenReturn(Optional.of(cliente));

        final Optional<Cliente> result = service.findByDocumentoIdentidad("P-123");

        assertThat(result).contains(cliente);
    }

    @Test
    void shouldReturnEmptyWhenCustomerDocumentIsMissing() {
        final ClienteRepositoryPort clienteRepositoryPort = mock(ClienteRepositoryPort.class);
        final TarjetaRepositoryPort tarjetaRepositoryPort = mock(TarjetaRepositoryPort.class);
        final CustomerService service = new CustomerService(clienteRepositoryPort, tarjetaRepositoryPort);

        when(clienteRepositoryPort.findByDocumentoIdentidad("P-404")).thenReturn(Optional.empty());

        final Optional<Cliente> result = service.findByDocumentoIdentidad("P-404");

        assertThat(result).isEmpty();
    }
}
