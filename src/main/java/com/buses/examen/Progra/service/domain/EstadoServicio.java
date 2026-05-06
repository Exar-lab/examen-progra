package com.buses.examen.Progra.service.domain;

/** Estado operativo de un servicio de bus programado. */
public enum EstadoServicio {
    /** Servicio aún no iniciado; venta de tickets habilitada. */
    PROGRAMADO,
    /** Servicio en tránsito; venta cerrada. */
    EN_CURSO,
    /** Servicio completado exitosamente. */
    FINALIZADO,
    /** Servicio cancelado antes de partir. */
    CANCELADO
}
