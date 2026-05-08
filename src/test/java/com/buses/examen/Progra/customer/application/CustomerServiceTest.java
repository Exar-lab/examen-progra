package com.buses.examen.Progra.customer.application;

import com.buses.examen.Progra.customer.application.port.out.ClienteRepositoryPort;
import com.buses.examen.Progra.customer.application.port.out.TarjetaRepositoryPort;
import com.buses.examen.Progra.customer.application.command.RegisterCardCommand;
import com.buses.examen.Progra.customer.application.command.RegisterCustomerCommand;
import com.buses.examen.Progra.customer.application.result.RegisterCardResult;
import com.buses.examen.Progra.customer.application.result.RegisterCustomerResult;
import com.buses.examen.Progra.customer.domain.Cliente;
import com.buses.examen.Progra.customer.domain.Tarjeta;
import com.buses.examen.Progra.customer.exception.ClienteNoEncontradoException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.YearMonth;
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
        final Cliente persisted = new Cliente("Ana", "Perez", "P-123", "CR", "ana@mail.com", "888");
        when(clienteRepositoryPort.save(captor.capture())).thenReturn(persisted);

        final RegisterCustomerResult result = service.register(new RegisterCustomerCommand(
                "Ana", "Perez", "P-123", "CR", "ana@mail.com", "888"
        ));

        assertThat(result.id()).isEqualTo(persisted.getId());
        assertThat(captor.getValue()).isNotNull();
    }

    @Test
    void shouldRegisterCardForExistingCustomer() {
        final ClienteRepositoryPort clienteRepositoryPort = mock(ClienteRepositoryPort.class);
        final TarjetaRepositoryPort tarjetaRepositoryPort = mock(TarjetaRepositoryPort.class);
        final CustomerService service = new CustomerService(clienteRepositoryPort, tarjetaRepositoryPort);

        final Cliente cliente = new Cliente("Ana", "Perez", "P-123", "CR", "ana@mail.com", "888");
        when(clienteRepositoryPort.findById(10L)).thenReturn(Optional.of(cliente));
        final ArgumentCaptor<Tarjeta> captor = ArgumentCaptor.forClass(Tarjeta.class);
        when(tarjetaRepositoryPort.save(captor.capture())).thenAnswer(invocation -> captor.getValue());

        final RegisterCardResult result = service.registerCard(new RegisterCardCommand(
                10L, "Ana Perez", "VISA", "1111", YearMonth.of(2030, 12), "tok-1", "4111******1111", "999"
        ));

        assertThat(result.id()).isEqualTo(captor.getValue().getId());
        assertThat(result.enmascarada()).isEqualTo("4111******1111");
        verify(tarjetaRepositoryPort).save(captor.getValue());
    }

    @Test
    void shouldFailRegisterCardWhenCustomerMissing() {
        final ClienteRepositoryPort clienteRepositoryPort = mock(ClienteRepositoryPort.class);
        final TarjetaRepositoryPort tarjetaRepositoryPort = mock(TarjetaRepositoryPort.class);
        final CustomerService service = new CustomerService(clienteRepositoryPort, tarjetaRepositoryPort);

        when(clienteRepositoryPort.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.registerCard(new RegisterCardCommand(
                99L, "Ana Perez", "VISA", "1111", YearMonth.of(2030, 12), "tok-1", "4111******1111", "999"
        )))
                .isInstanceOf(ClienteNoEncontradoException.class)
                .hasMessageContaining("Cliente no encontrado");
    }

    @Test
    void shouldLookupCustomerByDocumentoIdentidad() {
        final ClienteRepositoryPort clienteRepositoryPort = mock(ClienteRepositoryPort.class);
        final TarjetaRepositoryPort tarjetaRepositoryPort = mock(TarjetaRepositoryPort.class);
        final CustomerService service = new CustomerService(clienteRepositoryPort, tarjetaRepositoryPort);

        final Cliente cliente = new Cliente("Ana", "Perez", "P-123", "CR", "ana@mail.com", "888");
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
