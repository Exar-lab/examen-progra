package com.buses.examen.Progra.customer.application;

/**
 * Identidad del cliente autenticado, disponible durante operaciones protegidas.
 *
 * <p>Esta interfaz permite que los adaptadores de entrada (web) accedan a la
 * identidad del comprador sin depender directamente del adaptador de seguridad
 * ({@code customer.adapter.out.security}).</p>
 */
public interface AuthenticatedCustomer {

    /**
     * Retorna el identificador del cliente en el dominio.
     *
     * @return id del cliente autenticado
     */
    Long clienteId();
}
