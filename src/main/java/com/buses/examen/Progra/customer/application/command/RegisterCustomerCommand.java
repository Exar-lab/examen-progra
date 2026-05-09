package com.buses.examen.Progra.customer.application.command;

/**
 * Comando de entrada para registrar un cliente.
 *
 * @param nombres nombres del cliente
 * @param apellidos apellidos del cliente
 * @param documentoIdentidad documento de identidad del cliente
 * @param nacionalidad nacionalidad del cliente
 * @param email correo electrónico del cliente
 * @param telefono teléfono del cliente
 * @param username nombre de usuario para autenticación
 * @param rawPassword contraseña sin procesar para aplicar hash
 */
public record RegisterCustomerCommand(
        String nombres,
        String apellidos,
        String documentoIdentidad,
        String nacionalidad,
        String email,
        String telefono,
        String username,
        String rawPassword
) { }
