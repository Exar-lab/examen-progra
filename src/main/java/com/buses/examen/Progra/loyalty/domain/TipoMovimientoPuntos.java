package com.buses.examen.Progra.loyalty.domain;

/** Tipo de movimiento de puntos de fidelidad que afecta el saldo del cliente. */
public enum TipoMovimientoPuntos {
    /** Puntos ganados por una compra realizada. */
    ACUMULACION,
    /** Puntos canjeados por un beneficio o descuento. */
    REDENCION,
    /** Corrección manual del saldo de puntos. */
    AJUSTE
}
