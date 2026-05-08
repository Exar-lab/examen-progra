package com.buses.examen.Progra.customer.application.port.in;

import com.buses.examen.Progra.customer.application.command.RegisterCardCommand;
import com.buses.examen.Progra.customer.application.command.RegisterCustomerCommand;
import com.buses.examen.Progra.customer.application.result.RegisterCardResult;
import com.buses.examen.Progra.customer.application.result.RegisterCustomerResult;
import com.buses.examen.Progra.customer.exception.ClienteNoEncontradoException;

/**
 * Puerto de entrada para registrar clientes y sus medios de pago.
 */
public interface RegisterCustomerUseCase {
    /**
     * Registra un nuevo cliente con sus datos básicos.
     *
     * @param command datos de registro de cliente
     * @return cliente registrado
     */
    RegisterCustomerResult register(RegisterCustomerCommand command);

    /**
     * Registra una tarjeta para un cliente existente.
     *
     * @param command datos de registro de tarjeta
     * @return tarjeta registrada
     * @throws ClienteNoEncontradoException cuando el cliente indicado no existe
     */
    RegisterCardResult registerCard(RegisterCardCommand command);
}
