package com.buses.examen.Progra.customer.adapter.in.web;

import com.buses.examen.Progra.customer.application.port.in.CustomerQueryUseCase;
import com.buses.examen.Progra.customer.application.port.in.RegisterCustomerUseCase;
import com.buses.examen.Progra.customer.domain.Cliente;
import com.buses.examen.Progra.customer.domain.Tarjeta;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Adaptador web de entrada para casos de uso de clientes.
 */
@RestController
@RequestMapping("/api/customers")
public class CustomerWebAdapter {
    private final RegisterCustomerUseCase registerCustomerUseCase;
    private final CustomerQueryUseCase customerQueryUseCase;

    /**
     * Crea el adaptador con los puertos de entrada requeridos.
     *
     * @param registerCustomerUseCase puerto de registro de clientes
     * @param customerQueryUseCase    puerto de consulta de clientes
     */
    public CustomerWebAdapter(final RegisterCustomerUseCase registerCustomerUseCase,
                              final CustomerQueryUseCase customerQueryUseCase) {
        this.registerCustomerUseCase = registerCustomerUseCase;
        this.customerQueryUseCase = customerQueryUseCase;
    }

    /**
     * Registra un cliente.
     *
     * @param request datos de registro
     * @return cliente registrado
     */
    @PostMapping
    public ResponseEntity<CustomerResponse> register(@RequestBody final RegisterCustomerRequest request) {
        final Cliente cliente = registerCustomerUseCase.register(request.nombres(), request.apellidos(),
                request.documentoIdentidad(), request.nacionalidad(), request.email(), request.telefono());
        return ResponseEntity.status(HttpStatus.CREATED).body(CustomerResponse.from(cliente));
    }

    /**
     * Registra una tarjeta para un cliente.
     *
     * @param clienteId identificador del cliente
     * @param request   datos de tarjeta tokenizada
     * @return tarjeta registrada
     */
    @PostMapping("/{clienteId}/cards")
    public ResponseEntity<CardResponse> registerCard(@PathVariable final Long clienteId,
                                                      @RequestBody final RegisterCardRequest request) {
        final Tarjeta tarjeta = registerCustomerUseCase.registerCard(clienteId, request.titular(), request.marca(),
                request.ultimo4(), request.mesExpiracion(), request.anioExpiracion(), request.tokenReferencia(),
                request.enmascarada(), request.cvv());
        return ResponseEntity.status(HttpStatus.CREATED).body(CardResponse.from(tarjeta));
    }

    /**
     * Busca un cliente por documento de identidad.
     *
     * @param documentoIdentidad documento o pasaporte
     * @return cliente encontrado
     */
    @GetMapping("/document/{documentoIdentidad}")
    public CustomerResponse findByDocument(@PathVariable final String documentoIdentidad) {
        return customerQueryUseCase.findByDocumentoIdentidad(documentoIdentidad)
                .map(CustomerResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));
    }

    /** Datos de entrada para registrar cliente. */
    public record RegisterCustomerRequest(String nombres, String apellidos, String documentoIdentidad,
                                          String nacionalidad, String email, String telefono) { }

    /** Datos de entrada para registrar tarjeta. */
    public record RegisterCardRequest(String titular, String marca, String ultimo4, int mesExpiracion,
                                      int anioExpiracion, String tokenReferencia, String enmascarada,
                                      String cvv) { }

    /** Respuesta pública de cliente sin exponer la entidad de dominio. */
    public record CustomerResponse(Long id) {
        static CustomerResponse from(final Cliente cliente) {
            return new CustomerResponse(cliente.getId());
        }
    }

    /** Respuesta pública de tarjeta sin exponer datos sensibles ni la entidad de dominio. */
    public record CardResponse(Long id, String enmascarada) {
        static CardResponse from(final Tarjeta tarjeta) {
            return new CardResponse(tarjeta.getId(), tarjeta.getEnmascarada());
        }
    }
}
