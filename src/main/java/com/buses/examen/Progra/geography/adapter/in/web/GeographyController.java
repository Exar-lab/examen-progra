package com.buses.examen.Progra.geography.adapter.in.web;

import com.buses.examen.Progra.geography.adapter.in.web.dto.response.CityResponse;
import com.buses.examen.Progra.geography.adapter.in.web.dto.response.CountryResponse;
import com.buses.examen.Progra.geography.adapter.in.web.mapper.GeographyWebMapper;
import com.buses.examen.Progra.geography.application.port.in.GeographyQueryUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/** Controlador HTTP para catálogo geográfico. */
@RestController
@RequestMapping("/api/geography")
public class GeographyController {
    private final GeographyQueryUseCase geographyQueryUseCase;
    private final GeographyWebMapper mapper;

    /**
     * Crea el controlador de catálogo geográfico.
     *
     * @param geographyQueryUseCase caso de uso de consulta geográfica
     * @param mapper mapper de dominio a DTO web
     */
    public GeographyController(final GeographyQueryUseCase geographyQueryUseCase, final GeographyWebMapper mapper) {
        this.geographyQueryUseCase = geographyQueryUseCase;
        this.mapper = mapper;
    }

    /**
     * Lista países disponibles.
     *
     * @return lista de países disponibles
     */
    @GetMapping("/countries")
    public List<CountryResponse> listCountries() { return geographyQueryUseCase.listCountries().stream().map(mapper::toCountryResponse).toList(); }

    /**
     * Busca un país por identificador.
     *
     * @param paisId identificador del país
     * @return país encontrado
     */
    @GetMapping("/countries/{paisId}")
    public CountryResponse findCountryById(@PathVariable final Long paisId) {
        return geographyQueryUseCase.findCountryById(paisId).map(mapper::toCountryResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "País no encontrado"));
    }

    /**
     * Lista ciudades por país.
     *
     * @param paisId identificador del país
     * @return lista de ciudades del país
     */
    @GetMapping("/countries/{paisId}/cities")
    public List<CityResponse> listCitiesByCountry(@PathVariable final Long paisId) {
        return geographyQueryUseCase.listCitiesByCountry(paisId).stream().map(mapper::toCityResponse).toList();
    }

    /**
     * Busca una ciudad por identificador.
     *
     * @param ciudadId identificador de la ciudad
     * @return ciudad encontrada
     */
    @GetMapping("/cities/{ciudadId}")
    public CityResponse findCityById(@PathVariable final Long ciudadId) {
        return geographyQueryUseCase.findCityById(ciudadId).map(mapper::toCityResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ciudad no encontrada"));
    }
}
