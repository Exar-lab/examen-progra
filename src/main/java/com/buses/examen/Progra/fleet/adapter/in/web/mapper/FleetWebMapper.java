package com.buses.examen.Progra.fleet.adapter.in.web.mapper;

import com.buses.examen.Progra.fleet.adapter.in.web.dto.response.*;
import com.buses.examen.Progra.fleet.domain.*;
import org.springframework.stereotype.Component;

/** Mapper de dominio de flota hacia DTOs web. */
@Component
public class FleetWebMapper {
    /**
     * Convierte una compañía en DTO de respuesta.
     *
     * @param compania compañía de dominio
     * @return respuesta HTTP de compañía
     */
    public CompanyResponse toCompanyResponse(final Compania compania) { return new CompanyResponse(compania.getId()); }
    /**
     * Convierte un bus en DTO de respuesta.
     *
     * @param bus bus de dominio
     * @return respuesta HTTP de bus
     */
    public BusResponse toBusResponse(final Bus bus) { return new BusResponse(bus.getId(), bus.getCapacidadTotal()); }
    /**
     * Convierte un asiento en DTO de respuesta.
     *
     * @param asiento asiento de dominio
     * @return respuesta HTTP de asiento
     */
    public SeatResponse toSeatResponse(final Asiento asiento) { return new SeatResponse(asiento.getId(), asiento.getBusId()); }
}
