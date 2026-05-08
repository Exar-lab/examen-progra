package com.buses.examen.Progra.customer.application;

import com.buses.examen.Progra.customer.application.port.in.CustomerQueryUseCase;
import com.buses.examen.Progra.customer.application.port.in.RegisterCustomerUseCase;
import com.buses.examen.Progra.customer.application.port.out.ClienteRepositoryPort;
import com.buses.examen.Progra.customer.application.port.out.TarjetaRepositoryPort;
import com.buses.examen.Progra.customer.application.command.RegisterCardCommand;
import com.buses.examen.Progra.customer.application.command.RegisterCustomerCommand;
import com.buses.examen.Progra.customer.application.result.RegisterCardResult;
import com.buses.examen.Progra.customer.application.result.RegisterCustomerResult;
import com.buses.examen.Progra.customer.domain.Cliente;
import com.buses.examen.Progra.customer.domain.Tarjeta;
import com.buses.examen.Progra.customer.exception.ClienteNoEncontradoException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Servicio de aplicación para operaciones de clientes.
 */
@Service
public class CustomerService implements RegisterCustomerUseCase, CustomerQueryUseCase {
    private final ClienteRepositoryPort clienteRepositoryPort;
    private final TarjetaRepositoryPort tarjetaRepositoryPort;

    /**
     * Crea el servicio con sus puertos requeridos.
     *
     * @param clienteRepositoryPort puerto de clientes
     * @param tarjetaRepositoryPort puerto de tarjetas
     */
    public CustomerService(final ClienteRepositoryPort clienteRepositoryPort,
                           final TarjetaRepositoryPort tarjetaRepositoryPort) {
        this.clienteRepositoryPort = clienteRepositoryPort;
        this.tarjetaRepositoryPort = tarjetaRepositoryPort;
    }

    /** {@inheritDoc} */
    @Override
    public RegisterCustomerResult register(final RegisterCustomerCommand command) {
        final Cliente cliente = new Cliente(command.nombres(), command.apellidos(), command.documentoIdentidad(),
                command.nacionalidad(), command.email(), command.telefono());
        final Cliente persisted = clienteRepositoryPort.save(cliente);
        return new RegisterCustomerResult(persisted.getId());
    }

    /** {@inheritDoc} */
    @Override
    public RegisterCardResult registerCard(final RegisterCardCommand command) {
        final Cliente cliente = clienteRepositoryPort.findById(command.clienteId()).orElseThrow(
                () -> new ClienteNoEncontradoException(command.clienteId()));
        final Tarjeta tarjeta = Tarjeta.fromGatewayToken(cliente, command.titular(), command.marca(), command.ultimo4(),
                command.fechaExpiracion().getMonthValue(), command.fechaExpiracion().getYear(),
                command.tokenReferencia(), command.enmascarada(), command.cvv());
        final Tarjeta persisted = tarjetaRepositoryPort.save(tarjeta);
        return new RegisterCardResult(persisted.getId(), persisted.getEnmascarada());
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Cliente> findByDocumentoIdentidad(final String documentoIdentidad) {
        return clienteRepositoryPort.findByDocumentoIdentidad(documentoIdentidad);
    }
}
