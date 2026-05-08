package com.buses.examen.Progra.geography.application;

import com.buses.examen.Progra.geography.application.port.in.GeographyQueryUseCase;
import com.buses.examen.Progra.geography.application.port.out.CiudadRepositoryPort;
import com.buses.examen.Progra.geography.application.port.out.PaisRepositoryPort;
import com.buses.examen.Progra.geography.domain.Ciudad;
import com.buses.examen.Progra.geography.domain.Pais;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de aplicación para consultas geográficas.
 */
@Service
public class GeographyService implements GeographyQueryUseCase {
    private final PaisRepositoryPort paisRepositoryPort;
    private final CiudadRepositoryPort ciudadRepositoryPort;

    /**
     * Crea el servicio con sus puertos requeridos.
     *
     * @param paisRepositoryPort   puerto de países
     * @param ciudadRepositoryPort puerto de ciudades
     */
    public GeographyService(final PaisRepositoryPort paisRepositoryPort,
                            final CiudadRepositoryPort ciudadRepositoryPort) {
        this.paisRepositoryPort = paisRepositoryPort;
        this.ciudadRepositoryPort = ciudadRepositoryPort;
    }

    /** {@inheritDoc} */
    @Override
    public List<Pais> listCountries() {
        return paisRepositoryPort.findAll();
    }

    /** {@inheritDoc} */
    @Override
    public List<Ciudad> listCitiesByCountry(final Long paisId) {
        return ciudadRepositoryPort.findByPaisId(paisId);
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Pais> findCountryById(final Long paisId) {
        return paisRepositoryPort.findById(paisId);
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Ciudad> findCityById(final Long ciudadId) {
        return ciudadRepositoryPort.findById(ciudadId);
    }
}
