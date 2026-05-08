package com.buses.examen.Progra.customer.application;

import com.buses.examen.Progra.customer.application.port.in.CustomerQueryUseCase;
import com.buses.examen.Progra.customer.application.port.in.RegisterCustomerUseCase;
import com.buses.examen.Progra.customer.application.port.out.ClienteRepositoryPort;
import com.buses.examen.Progra.customer.application.port.out.TarjetaRepositoryPort;
import com.buses.examen.Progra.customer.domain.Cliente;
import com.buses.examen.Progra.customer.domain.Tarjeta;
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
    public Cliente register(final String nombres, final String apellidos, final String documentoIdentidad,
                            final String nacionalidad, final String email, final String telefono) {
        final Cliente cliente = new Cliente(nombres, apellidos, documentoIdentidad, email, telefono);
        return clienteRepositoryPort.save(cliente);
    }

    /** {@inheritDoc} */
    @Override
    public Tarjeta registerCard(final Long clienteId, final String titular, final String marca, final String ultimo4,
                                final int mesExpiracion, final int anioExpiracion, final String tokenReferencia,
                                final String enmascarada, final String cvv) {
        final Cliente cliente = clienteRepositoryPort.findById(clienteId).orElseThrow(
                () -> new IllegalArgumentException("Cliente no encontrado"));
        final Tarjeta tarjeta = Tarjeta.fromGatewayToken(cliente, titular, marca, ultimo4,
                mesExpiracion, anioExpiracion, tokenReferencia, enmascarada, cvv);
        return tarjetaRepositoryPort.save(tarjeta);
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Cliente> findByDocumentoIdentidad(final String documentoIdentidad) {
        return clienteRepositoryPort.findByDocumentoIdentidad(documentoIdentidad);
    }
}
