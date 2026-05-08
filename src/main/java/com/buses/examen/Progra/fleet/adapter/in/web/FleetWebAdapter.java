package com.buses.examen.Progra.fleet.adapter.in.web;

import com.buses.examen.Progra.fleet.application.port.in.FleetQueryUseCase;
import com.buses.examen.Progra.fleet.domain.Asiento;
import com.buses.examen.Progra.fleet.domain.Bus;
import com.buses.examen.Progra.fleet.domain.Compania;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Adaptador web de entrada para consultas de flota.
 */
@RestController
@RequestMapping("/api/fleet")
public class FleetWebAdapter {
    private final FleetQueryUseCase fleetQueryUseCase;

    /**
     * Crea el adaptador con el puerto de consulta de flota.
     *
     * @param fleetQueryUseCase puerto de entrada
     */
    public FleetWebAdapter(final FleetQueryUseCase fleetQueryUseCase) {
        this.fleetQueryUseCase = fleetQueryUseCase;
    }

    /** @return compañías disponibles */
    @GetMapping("/companies")
    public List<CompanyResponse> listCompanies() {
        return fleetQueryUseCase.listCompanies().stream().map(CompanyResponse::from).toList();
    }

    /**
     * Lista buses por compañía.
     *
     * @param companiaId identificador de compañía
     * @return buses asociados
     */
    @GetMapping("/companies/{companiaId}/buses")
    public List<BusResponse> listBusesByCompany(@PathVariable final Long companiaId) {
        return fleetQueryUseCase.listBusesByCompany(companiaId).stream().map(BusResponse::from).toList();
    }

    /**
     * Busca un bus por id.
     *
     * @param busId identificador del bus
     * @return bus encontrado
     */
    @GetMapping("/buses/{busId}")
    public BusResponse findBusById(@PathVariable final Long busId) {
        return fleetQueryUseCase.findBusById(busId)
                .map(BusResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bus no encontrado"));
    }

    /**
     * Lista asientos por bus.
     *
     * @param busId identificador del bus
     * @return asientos del bus
     */
    @GetMapping("/buses/{busId}/seats")
    public List<SeatResponse> listSeatsByBus(@PathVariable final Long busId) {
        return fleetQueryUseCase.listSeatsByBus(busId).stream().map(SeatResponse::from).toList();
    }

    /** Respuesta pública de compañía. */
    public record CompanyResponse(Long id) {
        static CompanyResponse from(final Compania compania) {
            return new CompanyResponse(compania.getId());
        }
    }

    /** Respuesta pública de bus. */
    public record BusResponse(Long id, int capacidadTotal) {
        static BusResponse from(final Bus bus) {
            return new BusResponse(bus.getId(), bus.getCapacidadTotal());
        }
    }

    /** Respuesta pública de asiento. */
    public record SeatResponse(Long id, Long busId) {
        static SeatResponse from(final Asiento asiento) {
            return new SeatResponse(asiento.getId(), asiento.getBusId());
        }
    }
}
