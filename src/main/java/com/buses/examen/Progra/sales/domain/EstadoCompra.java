package com.buses.examen.Progra.sales.domain;

/** Estado del ciclo de vida de una compra. */
public enum EstadoCompra {
    /** Compra iniciada pero pago aún no confirmado. */
    PENDIENTE,
    /** Pago confirmado y tickets emitidos. */
    PAGADA,
    /** Compra anulada antes o después del pago. */
    CANCELADA
}
