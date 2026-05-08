package com.buses.examen.Progra.customer.application.port.in;

import com.buses.examen.Progra.customer.domain.Cliente;
import com.buses.examen.Progra.customer.domain.Tarjeta;

/**
 * Puerto de entrada para registrar clientes y sus medios de pago.
 */
public interface RegisterCustomerUseCase {
    /**
     * Registra un nuevo cliente con sus datos básicos.
     *
     * @param nombres            nombres del cliente
     * @param apellidos          apellidos del cliente
     * @param documentoIdentidad documento de identidad o pasaporte
     * @param nacionalidad       nacionalidad del cliente
     * @param email              correo electrónico del cliente
     * @param telefono           teléfono de contacto
     * @return cliente registrado
     */
    Cliente register(String nombres, String apellidos, String documentoIdentidad,
                     String nacionalidad, String email, String telefono);

    /**
     * Registra una tarjeta para un cliente existente.
     *
     * @param clienteId       identificador del cliente
     * @param titular         nombre del titular en la tarjeta
     * @param marca           marca de la tarjeta (VISA, MASTERCARD, etc.)
     * @param ultimo4         últimos cuatro dígitos
     * @param mesExpiracion   mes de expiración
     * @param anioExpiracion  año de expiración
     * @param tokenReferencia token opaco de la pasarela
     * @param enmascarada     representación enmascarada del PAN
     * @param cvv             CVV de sesión
     * @return tarjeta registrada
     */
    Tarjeta registerCard(Long clienteId, String titular, String marca, String ultimo4,
                         int mesExpiracion, int anioExpiracion, String tokenReferencia,
                         String enmascarada, String cvv);
}
