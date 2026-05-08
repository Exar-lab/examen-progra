package com.buses.examen.Progra.customer.adapter.in.web.mapper;

import com.buses.examen.Progra.customer.adapter.in.web.dto.request.RegisterCardRequest;
import com.buses.examen.Progra.customer.adapter.in.web.dto.request.RegisterCustomerRequest;
import com.buses.examen.Progra.customer.adapter.in.web.dto.response.CardResponse;
import com.buses.examen.Progra.customer.adapter.in.web.dto.response.CustomerResponse;
import com.buses.examen.Progra.customer.application.command.RegisterCardCommand;
import com.buses.examen.Progra.customer.application.command.RegisterCustomerCommand;
import com.buses.examen.Progra.customer.application.result.RegisterCardResult;
import com.buses.examen.Progra.customer.application.result.RegisterCustomerResult;
import com.buses.examen.Progra.customer.domain.Cliente;
import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

/** Mapper entre DTO web y comandos/resultados del caso de uso de clientes. */
@Component
public class CustomerWebMapper {
    private static final DateTimeFormatter CARD_EXPIRATION_FORMATTER = DateTimeFormatter.ofPattern("MM/yyyy");

    /**
     * Convierte un request de registro de cliente en comando de aplicación.
     *
     * @param request payload HTTP de registro
     * @return comando para registrar el cliente
     */
    public RegisterCustomerCommand toCommand(final RegisterCustomerRequest request) {
        return new RegisterCustomerCommand(request.nombres(), request.apellidos(), request.documentoIdentidad(),
                request.nacionalidad(), request.email(), request.telefono());
    }

    /**
     * Convierte un request de tarjeta en comando de aplicación.
     *
     * @param clienteId identificador del cliente
     * @param request payload HTTP de tarjeta
     * @return comando para registrar la tarjeta
     */
    public RegisterCardCommand toCommand(final Long clienteId, final RegisterCardRequest request) {
        return new RegisterCardCommand(clienteId, request.titular(), request.marca(), request.ultimo4(),
                YearMonth.parse(request.fechaExpiracion(), CARD_EXPIRATION_FORMATTER),
                request.tokenReferencia(), request.enmascarada(), request.cvv());
    }

    /**
     * Convierte un cliente de dominio a DTO web.
     *
     * @param cliente cliente de dominio
     * @return respuesta HTTP de cliente
     */
    public CustomerResponse toResponse(final Cliente cliente) { return new CustomerResponse(cliente.getId()); }
    /**
     * Convierte un resultado de registro de cliente a DTO web.
     *
     * @param result resultado del caso de uso
     * @return respuesta HTTP de cliente
     */
    public CustomerResponse toResponse(final RegisterCustomerResult result) { return new CustomerResponse(result.id()); }
    /**
     * Convierte un resultado de registro de tarjeta a DTO web.
     *
     * @param result resultado del caso de uso
     * @return respuesta HTTP de tarjeta
     */
    public CardResponse toResponse(final RegisterCardResult result) { return new CardResponse(result.id(), result.enmascarada()); }
}
