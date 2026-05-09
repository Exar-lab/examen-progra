package com.buses.examen.Progra.service.application;

import com.buses.examen.Progra.service.application.port.in.ServiceQueryUseCase;
import com.buses.examen.Progra.service.application.port.out.ServicioRepositoryPort;
import com.buses.examen.Progra.service.domain.Servicio;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio de aplicación para consultas de servicios programados.
 */
@Service
public class ServiceService implements ServiceQueryUseCase {
    private final ServicioRepositoryPort servicioRepositoryPort;

    /**
     * Crea el servicio con su puerto requerido.
     *
     * @param servicioRepositoryPort puerto de servicios
     */
    public ServiceService(final ServicioRepositoryPort servicioRepositoryPort) {
        this.servicioRepositoryPort = servicioRepositoryPort;
    }

    /** {@inheritDoc} */
    @Override
    public List<Servicio> listServicesForRoute(final Long rutaId, final OffsetDateTime start, final OffsetDateTime end) {
        return servicioRepositoryPort.findByRutaIdAndSalidaProgramadaBetween(rutaId, start, end);
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Servicio> findServiceById(final Long servicioId) {
        return servicioRepositoryPort.findById(servicioId);
    }
}
