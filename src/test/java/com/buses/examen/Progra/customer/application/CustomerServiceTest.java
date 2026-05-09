package com.buses.examen.Progra.customer.application;

import com.buses.examen.Progra.customer.application.port.out.ClienteRepositoryPort;
import com.buses.examen.Progra.customer.application.port.out.ClockPort;
import com.buses.examen.Progra.customer.application.port.out.PasswordHasherPort;
import com.buses.examen.Progra.customer.application.port.out.CardDataProtectorPort;
import com.buses.examen.Progra.customer.application.port.out.TarjetaRepositoryPort;
import com.buses.examen.Progra.customer.application.port.out.UserSecurityRepositoryPort;
import com.buses.examen.Progra.customer.application.command.RegisterCardCommand;
import com.buses.examen.Progra.customer.application.command.RegisterCustomerCommand;
import com.buses.examen.Progra.customer.application.command.RawCardData;
import com.buses.examen.Progra.customer.application.result.ProtectedCardData;
import com.buses.examen.Progra.customer.application.result.RegisterCardResult;
import com.buses.examen.Progra.customer.application.result.RegisterCustomerResult;
import com.buses.examen.Progra.customer.domain.Cliente;
import com.buses.examen.Progra.customer.domain.Tarjeta;
import com.buses.examen.Progra.customer.domain.UserSecurity;
import com.buses.examen.Progra.customer.exception.ClienteNoEncontradoException;
import com.buses.examen.Progra.customer.exception.TarjetaExpiradaException;
import com.buses.examen.Progra.customer.exception.UsernameDuplicadoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.YearMonth;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** Pruebas de servicio de clientes. */
@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private ClienteRepositoryPort clienteRepositoryPort;
    @Mock
    private TarjetaRepositoryPort tarjetaRepositoryPort;
    @Mock
    private UserSecurityRepositoryPort userSecurityRepositoryPort;
    @Mock
    private PasswordHasherPort passwordHasherPort;
    @Mock
    private CardDataProtectorPort cardDataProtectorPort;
    @Mock
    private ClockPort clockPort;

    private CustomerService service;

    /** Inicializa el servicio con puertos simulados para cada prueba. */
    @BeforeEach
    void setUp() {
        service = new CustomerService(clienteRepositoryPort, tarjetaRepositoryPort,
                userSecurityRepositoryPort, passwordHasherPort, cardDataProtectorPort, clockPort);
    }

    /** Verifica registro exitoso de cliente y credenciales asociadas. */
    @Test
    void shouldRegisterCustomerAndReturnSavedEntity() {
        final ArgumentCaptor<Cliente> captor = ArgumentCaptor.forClass(Cliente.class);
        final Cliente persisted = new Cliente("Ana", "Perez", "P-123", "CR", "ana@mail.com", "888");
        when(userSecurityRepositoryPort.existsByUsername("ana_user")).thenReturn(false);
        when(passwordHasherPort.hash("ClaveSegura123")).thenReturn("hash-123");
        when(clienteRepositoryPort.save(captor.capture())).thenReturn(persisted);

        final RegisterCustomerResult result = service.register(new RegisterCustomerCommand(
                "Ana", "Perez", "P-123", "CR", "ana@mail.com", "888", "ana_user", "ClaveSegura123"
        ));

        assertThat(result.id()).isEqualTo(persisted.getId());
        assertThat(captor.getValue()).isNotNull();
        final ArgumentCaptor<UserSecurity> securityCaptor = ArgumentCaptor.forClass(UserSecurity.class);
        verify(userSecurityRepositoryPort).save(securityCaptor.capture());
        assertThat(securityCaptor.getValue().getPasswordHash()).isEqualTo("hash-123");
    }

    /** Verifica rechazo de registro cuando el username ya existe. */
    @Test
    void shouldFailRegisterCustomerWhenUsernameAlreadyExists() {
        when(userSecurityRepositoryPort.existsByUsername("ana_user")).thenReturn(true);

        assertThatThrownBy(() -> service.register(new RegisterCustomerCommand(
                "Ana", "Perez", "P-123", "CR", "ana@mail.com", "888", "ana_user", "ClaveSegura123"
        )))
                .isInstanceOf(UsernameDuplicadoException.class);
        verify(clienteRepositoryPort, never()).save(org.mockito.ArgumentMatchers.any());
    }

    /** Verifica registro de tarjeta tokenizada para cliente existente. */
    @Test
    void shouldRegisterCardForExistingCustomer() {
        final Cliente cliente = new Cliente("Ana", "Perez", "P-123", "CR", "ana@mail.com", "888");
        when(clienteRepositoryPort.findById(10L)).thenReturn(Optional.of(cliente));
        final ArgumentCaptor<Tarjeta> captor = ArgumentCaptor.forClass(Tarjeta.class);
        when(clockPort.currentYearMonth()).thenReturn(YearMonth.of(2026, 5));
        when(cardDataProtectorPort.protect(new RawCardData("Ana Perez", "4111111111111111", YearMonth.of(2030, 12), "999")))
                .thenReturn(new ProtectedCardData("VISA", "1111", "tok-1", "4111******1111"));
        when(tarjetaRepositoryPort.save(captor.capture())).thenAnswer(invocation -> captor.getValue());

        final RegisterCardResult result = service.registerCard(new RegisterCardCommand(
                10L, "Ana Perez", "4111111111111111", YearMonth.of(2030, 12), "999"
        ));

        assertThat(result.id()).isEqualTo(captor.getValue().getId());
        assertThat(result.enmascarada()).isEqualTo("4111******1111");
        assertThat(captor.getValue().getCvv()).isNull();
        verify(tarjetaRepositoryPort).save(captor.getValue());
    }

    /** Verifica rechazo de tarjeta vencida usando el reloj de dominio. */
    @Test
    void shouldRejectExpiredCardUsingClockPort() {
        final Cliente cliente = new Cliente("Ana", "Perez", "P-123", "CR", "ana@mail.com", "888");
        when(clienteRepositoryPort.findById(10L)).thenReturn(Optional.of(cliente));
        when(clockPort.currentYearMonth()).thenReturn(YearMonth.of(2026, 5));

        assertThatThrownBy(() -> service.registerCard(new RegisterCardCommand(
                10L, "Ana Perez", "4111111111111111", YearMonth.of(2026, 4), "999"
        )))
                .isInstanceOf(TarjetaExpiradaException.class);
        verify(tarjetaRepositoryPort, never()).save(org.mockito.ArgumentMatchers.any());
    }

    /** Verifica aceptación de tarjeta que vence en el mes actual. */
    @Test
    void shouldAcceptCardExpiringCurrentMonthAndPersistProtectedCard() {
        final Cliente cliente = new Cliente("Ana", "Perez", "P-123", "CR", "ana@mail.com", "888");
        when(clienteRepositoryPort.findById(10L)).thenReturn(Optional.of(cliente));
        when(clockPort.currentYearMonth()).thenReturn(YearMonth.of(2026, 5));
        when(cardDataProtectorPort.protect(new RawCardData("Ana Perez", "4111111111111111", YearMonth.of(2026, 5), "999")))
                .thenReturn(new ProtectedCardData("VISA", "1111", "tok-current", "4111******1111"));
        final ArgumentCaptor<Tarjeta> captor = ArgumentCaptor.forClass(Tarjeta.class);
        when(tarjetaRepositoryPort.save(captor.capture())).thenAnswer(invocation -> captor.getValue());

        final RegisterCardResult result = service.registerCard(new RegisterCardCommand(
                10L, "Ana Perez", "4111111111111111", YearMonth.of(2026, 5), "999"
        ));

        assertThat(result.enmascarada()).isEqualTo("4111******1111");
        verify(cardDataProtectorPort).protect(new RawCardData("Ana Perez", "4111111111111111", YearMonth.of(2026, 5), "999"));
        verify(tarjetaRepositoryPort).save(captor.getValue());
        assertThat(captor.getValue().getCvv()).isNull();
    }

    /** Verifica error cuando se registra tarjeta para cliente inexistente. */
    @Test
    void shouldFailRegisterCardWhenCustomerMissing() {
        when(clienteRepositoryPort.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.registerCard(new RegisterCardCommand(
                99L, "Ana Perez", "4111111111111111", YearMonth.of(2030, 12), "999"
        )))
                .isInstanceOf(ClienteNoEncontradoException.class)
                .hasMessageContaining("Cliente no encontrado");
    }

    /** Verifica búsqueda de cliente por documento de identidad. */
    @Test
    void shouldLookupCustomerByDocumentoIdentidad() {
        final Cliente cliente = new Cliente("Ana", "Perez", "P-123", "CR", "ana@mail.com", "888");
        when(clienteRepositoryPort.findByDocumentoIdentidad("P-123")).thenReturn(Optional.of(cliente));

        final Optional<Cliente> result = service.findByDocumentoIdentidad("P-123");

        assertThat(result).contains(cliente);
    }

    /** Verifica resultado vacío cuando el documento no existe. */
    @Test
    void shouldReturnEmptyWhenCustomerDocumentIsMissing() {
        when(clienteRepositoryPort.findByDocumentoIdentidad("P-404")).thenReturn(Optional.empty());

        final Optional<Cliente> result = service.findByDocumentoIdentidad("P-404");

        assertThat(result).isEmpty();
    }
}
