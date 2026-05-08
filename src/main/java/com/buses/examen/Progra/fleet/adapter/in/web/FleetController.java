package com.buses.examen.Progra.fleet.adapter.in.web;

import com.buses.examen.Progra.fleet.adapter.in.web.dto.response.BusResponse;
import com.buses.examen.Progra.fleet.adapter.in.web.dto.response.CompanyResponse;
import com.buses.examen.Progra.fleet.adapter.in.web.dto.response.SeatResponse;
import com.buses.examen.Progra.fleet.adapter.in.web.mapper.FleetWebMapper;
import com.buses.examen.Progra.fleet.application.port.in.FleetQueryUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/** Controlador HTTP para consultas de flota. */
@RestController
@RequestMapping("/api/fleet")
public class FleetController {
    private final FleetQueryUseCase fleetQueryUseCase;
    private final FleetWebMapper mapper;

    /**
     * Crea el controlador HTTP de flota.
     *
     * @param fleetQueryUseCase caso de uso de consulta de flota
     * @param mapper mapper de dominio a DTOs web
     */
    public FleetController(final FleetQueryUseCase fleetQueryUseCase, final FleetWebMapper mapper) {
        this.fleetQueryUseCase = fleetQueryUseCase;
        this.mapper = mapper;
    }
    /**
     * Lista compañías de transporte.
     *
     * @return lista de compañías disponibles
     */
    @GetMapping("/companies")
    public List<CompanyResponse> listCompanies() { return fleetQueryUseCase.listCompanies().stream().map(mapper::toCompanyResponse).toList(); }
    /**
     * Lista buses de una compañía.
     *
     * @param companiaId identificador de la compañía
     * @return lista de buses de la compañía
     */
    @GetMapping("/companies/{companiaId}/buses")
    public List<BusResponse> listBusesByCompany(@PathVariable final Long companiaId) { return fleetQueryUseCase.listBusesByCompany(companiaId).stream().map(mapper::toBusResponse).toList(); }
    /**
     * Busca un bus por identificador.
     *
     * @param busId identificador del bus
     * @return bus encontrado
     * @throws ResponseStatusException cuando no existe un bus con el identificador indicado
     */
    @GetMapping("/buses/{busId}")
    public BusResponse findBusById(@PathVariable final Long busId) {
        return fleetQueryUseCase.findBusById(busId).map(mapper::toBusResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bus no encontrado"));
    }
    /**
     * Lista asientos de un bus.
     *
     * @param busId identificador del bus
     * @return lista de asientos del bus
     */
    @GetMapping("/buses/{busId}/seats")
    public List<SeatResponse> listSeatsByBus(@PathVariable final Long busId) { return fleetQueryUseCase.listSeatsByBus(busId).stream().map(mapper::toSeatResponse).toList(); }
}
