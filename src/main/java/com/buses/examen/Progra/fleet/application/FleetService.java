package com.buses.examen.Progra.fleet.application;

import com.buses.examen.Progra.fleet.application.port.in.FleetQueryUseCase;
import com.buses.examen.Progra.fleet.application.port.out.AsientoRepositoryPort;
import com.buses.examen.Progra.fleet.application.port.out.BusRepositoryPort;
import com.buses.examen.Progra.fleet.application.port.out.CompaniaRepositoryPort;
import com.buses.examen.Progra.fleet.domain.Asiento;
import com.buses.examen.Progra.fleet.domain.Bus;
import com.buses.examen.Progra.fleet.domain.Compania;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de aplicación para consultas de flota y asientos.
 */
@Service
public class FleetService implements FleetQueryUseCase {
    private final CompaniaRepositoryPort companiaRepositoryPort;
    private final BusRepositoryPort busRepositoryPort;
    private final AsientoRepositoryPort asientoRepositoryPort;

    /**
     * Crea el servicio con sus puertos requeridos.
     *
     * @param companiaRepositoryPort puerto de compañías
     * @param busRepositoryPort      puerto de buses
     * @param asientoRepositoryPort  puerto de asientos
     */
    public FleetService(final CompaniaRepositoryPort companiaRepositoryPort,
                        final BusRepositoryPort busRepositoryPort,
                        final AsientoRepositoryPort asientoRepositoryPort) {
        this.companiaRepositoryPort = companiaRepositoryPort;
        this.busRepositoryPort = busRepositoryPort;
        this.asientoRepositoryPort = asientoRepositoryPort;
    }

    /** {@inheritDoc} */
    @Override
    public List<Compania> listCompanies() {
        return companiaRepositoryPort.findAll();
    }

    /** {@inheritDoc} */
    @Override
    public List<Bus> listBusesByCompany(final Long companiaId) {
        return busRepositoryPort.findByCompaniaId(companiaId);
    }

    /** {@inheritDoc} */
    @Override
    public List<Asiento> listSeatsByBus(final Long busId) {
        return asientoRepositoryPort.findByBusId(busId);
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Bus> findBusById(final Long busId) {
        return busRepositoryPort.findById(busId);
    }
}
