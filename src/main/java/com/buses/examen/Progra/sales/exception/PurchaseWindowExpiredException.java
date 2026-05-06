package com.buses.examen.Progra.sales.exception;

/**
 * Lanzada cuando el servicio está fuera de la ventana de compra de 7 días.
 */
public class PurchaseWindowExpiredException extends RuntimeException {

    /** Construye la excepción con el mensaje estándar de ventana expirada. */
    public PurchaseWindowExpiredException() {
        super("Fuera de ventana de 7 dias");
    }
}
