package com.buses.examen.Progra.customer.application;

import com.buses.examen.Progra.customer.application.port.in.CustomerQueryUseCase;
import com.buses.examen.Progra.customer.application.port.in.RegisterCustomerUseCase;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.Optional;

/**
 * Servicio de aplicación para operaciones de clientes.
 */
@Service
public class CustomerService implements RegisterCustomerUseCase, CustomerQueryUseCase {
    private final ClienteRepositoryPort clienteRepositoryPort;
    private final TarjetaRepositoryPort tarjetaRepositoryPort;
    private final UserSecurityRepositoryPort userSecurityRepositoryPort;
    private final PasswordHasherPort passwordHasherPort;
    private final CardDataProtectorPort cardDataProtectorPort;
    private final ClockPort clockPort;

    /**
     * Crea el servicio con sus puertos requeridos.
     *
     * @param clienteRepositoryPort puerto de clientes
     * @param tarjetaRepositoryPort puerto de tarjetas
     * @param userSecurityRepositoryPort puerto de credenciales de usuario
     * @param passwordHasherPort puerto para hash de contraseñas
     * @param cardDataProtectorPort puerto para protección de datos de tarjeta
     * @param clockPort puerto de tiempo para reglas temporales
     */
    public CustomerService(final ClienteRepositoryPort clienteRepositoryPort,
                           final TarjetaRepositoryPort tarjetaRepositoryPort,
                           final UserSecurityRepositoryPort userSecurityRepositoryPort,
                           final PasswordHasherPort passwordHasherPort,
                           final CardDataProtectorPort cardDataProtectorPort,
                           final ClockPort clockPort) {
        this.clienteRepositoryPort = clienteRepositoryPort;
        this.tarjetaRepositoryPort = tarjetaRepositoryPort;
        this.userSecurityRepositoryPort = userSecurityRepositoryPort;
        this.passwordHasherPort = passwordHasherPort;
        this.cardDataProtectorPort = cardDataProtectorPort;
        this.clockPort = clockPort;
    }

    /** {@inheritDoc} */
    @Override
    @Transactional
    public RegisterCustomerResult register(final RegisterCustomerCommand command) {
        final String username = command.username().trim().toLowerCase();
        if (userSecurityRepositoryPort.existsByUsername(username)) {
            throw new UsernameDuplicadoException(username);
        }
        final Cliente cliente = new Cliente(command.nombres(), command.apellidos(), command.documentoIdentidad(),
                command.nacionalidad(), command.email(), command.telefono());
        final Cliente persisted = clienteRepositoryPort.save(cliente);
        final String passwordHash = passwordHasherPort.hash(command.rawPassword());
        userSecurityRepositoryPort.save(new UserSecurity(persisted, username, passwordHash, true, false));
        return new RegisterCustomerResult(persisted.getId());
    }

    /** {@inheritDoc} */
    @Override
    @Transactional
    public RegisterCardResult registerCard(final RegisterCardCommand command) {
        final Cliente cliente = clienteRepositoryPort.findById(command.clienteId()).orElseThrow(
                () -> new ClienteNoEncontradoException(command.clienteId()));
        final YearMonth currentYearMonth = clockPort.currentYearMonth();
        if (command.fechaExpiracion().isBefore(currentYearMonth)) {
            throw new TarjetaExpiradaException(command.fechaExpiracion(), currentYearMonth);
        }
        final ProtectedCardData protectedCardData = cardDataProtectorPort.protect(new RawCardData(
                command.titular(), command.numeroTarjeta(), command.fechaExpiracion(), command.cvv()
        ));
        final Tarjeta tarjeta = Tarjeta.fromGatewayToken(cliente, command.titular(), protectedCardData.marca(), protectedCardData.ultimo4(),
                command.fechaExpiracion().getMonthValue(), command.fechaExpiracion().getYear(),
                protectedCardData.tokenReferencia(), protectedCardData.enmascarada());
        final Tarjeta persisted = tarjetaRepositoryPort.save(tarjeta);
        return new RegisterCardResult(persisted.getId(), persisted.getEnmascarada());
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Cliente> findByDocumentoIdentidad(final String documentoIdentidad) {
        return clienteRepositoryPort.findByDocumentoIdentidad(documentoIdentidad);
    }
}
