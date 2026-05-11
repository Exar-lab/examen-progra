package com.buses.examen.Progra.service.adapter.in.web.mapper;

import com.buses.examen.Progra.service.adapter.in.web.dto.response.ServiceResponse;
import com.buses.examen.Progra.service.domain.Servicio;
import org.springframework.stereotype.Component;

/** Mapper de dominio de servicios hacia DTOs web. */
@Component
public class ServiceWebMapper {
    /**
     * Convierte un servicio programado a DTO de respuesta.
     *
     * @param servicio servicio de dominio
     * @return respuesta HTTP de servicio
     */
    public ServiceResponse toResponse(final Servicio servicio) {
        return new ServiceResponse(servicio.getId(), servicio.getSalidaProgramada(), servicio.getBus().getId(), servicio.getCapacidadDisponible());
    }
}
