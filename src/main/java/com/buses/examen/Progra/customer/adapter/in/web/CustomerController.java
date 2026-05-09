package com.buses.examen.Progra.customer.adapter.in.web;

import com.buses.examen.Progra.customer.adapter.in.web.dto.request.RegisterCardRequest;
import com.buses.examen.Progra.customer.adapter.in.web.dto.request.RegisterCustomerRequest;
import com.buses.examen.Progra.customer.adapter.in.web.dto.response.CardResponse;
import com.buses.examen.Progra.customer.adapter.in.web.dto.response.CustomerResponse;
import com.buses.examen.Progra.customer.adapter.in.web.mapper.CustomerWebMapper;
import com.buses.examen.Progra.customer.application.port.in.CustomerQueryUseCase;
import com.buses.examen.Progra.customer.application.port.in.RegisterCustomerUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/** Controlador HTTP para registro y consulta de clientes. */
@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final RegisterCustomerUseCase registerCustomerUseCase;
    private final CustomerQueryUseCase customerQueryUseCase;
    private final CustomerWebMapper mapper;

    /**
     * Crea el controlador HTTP de clientes.
     *
     * @param registerCustomerUseCase caso de uso de registro de clientes
     * @param customerQueryUseCase caso de uso de consulta de clientes
     * @param mapper mapper de web a aplicación y de dominio a web
     */
    public CustomerController(final RegisterCustomerUseCase registerCustomerUseCase,
                              final CustomerQueryUseCase customerQueryUseCase,
                              final CustomerWebMapper mapper) {
        this.registerCustomerUseCase = registerCustomerUseCase;
        this.customerQueryUseCase = customerQueryUseCase;
        this.mapper = mapper;
    }

    /**
     * Registra un cliente y retorna su identificador.
     *
     * @param request datos del cliente a registrar
     * @return respuesta HTTP con el cliente registrado
     */
    @PostMapping
    public ResponseEntity<CustomerResponse> register(@Valid @RequestBody final RegisterCustomerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                mapper.toResponse(registerCustomerUseCase.register(mapper.toCommand(request)))
        );
    }

    /**
     * Registra una tarjeta para un cliente existente.
     *
     * @param clienteId identificador del cliente
     * @param request datos de la tarjeta a registrar
     * @return respuesta HTTP con la tarjeta registrada
     */
    @PostMapping("/{clienteId}/cards")
    public ResponseEntity<CardResponse> registerCard(@PathVariable final Long clienteId,
                                                      @Valid @RequestBody final RegisterCardRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                mapper.toResponse(registerCustomerUseCase.registerCard(mapper.toCommand(clienteId, request)))
        );
    }

    /**
     * Busca un cliente por documento de identidad.
     *
     * @param documentoIdentidad documento de identidad del cliente
     * @return cliente encontrado
     * @throws ResponseStatusException cuando no existe cliente para el documento indicado
     */
    @GetMapping("/document/{documentoIdentidad}")
    public CustomerResponse findByDocument(@PathVariable final String documentoIdentidad) {
        return customerQueryUseCase.findByDocumentoIdentidad(documentoIdentidad)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));
    }
}
