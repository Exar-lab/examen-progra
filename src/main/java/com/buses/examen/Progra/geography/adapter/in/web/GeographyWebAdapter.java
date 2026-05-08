package com.buses.examen.Progra.geography.adapter.in.web;

import com.buses.examen.Progra.geography.application.port.in.GeographyQueryUseCase;
import com.buses.examen.Progra.geography.domain.Ciudad;
import com.buses.examen.Progra.geography.domain.Pais;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Adaptador web de entrada para consultas geográficas.
 */
@RestController
@RequestMapping("/api/geography")
public class GeographyWebAdapter {
    private final GeographyQueryUseCase geographyQueryUseCase;

    /**
     * Crea el adaptador con el puerto de consulta geográfica.
     *
     * @param geographyQueryUseCase puerto de entrada
     */
    public GeographyWebAdapter(final GeographyQueryUseCase geographyQueryUseCase) {
        this.geographyQueryUseCase = geographyQueryUseCase;
    }

    /** @return países disponibles */
    @GetMapping("/countries")
    public List<CountryResponse> listCountries() {
        return geographyQueryUseCase.listCountries().stream().map(CountryResponse::from).toList();
    }

    /**
     * Busca un país por id.
     *
     * @param paisId identificador del país
     * @return país encontrado
     */
    @GetMapping("/countries/{paisId}")
    public CountryResponse findCountryById(@PathVariable final Long paisId) {
        return geographyQueryUseCase.findCountryById(paisId)
                .map(CountryResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "País no encontrado"));
    }

    /**
     * Lista ciudades por país.
     *
     * @param paisId identificador del país
     * @return ciudades del país
     */
    @GetMapping("/countries/{paisId}/cities")
    public List<CityResponse> listCitiesByCountry(@PathVariable final Long paisId) {
        return geographyQueryUseCase.listCitiesByCountry(paisId).stream().map(CityResponse::from).toList();
    }

    /**
     * Busca una ciudad por id.
     *
     * @param ciudadId identificador de la ciudad
     * @return ciudad encontrada
     */
    @GetMapping("/cities/{ciudadId}")
    public CityResponse findCityById(@PathVariable final Long ciudadId) {
        return geographyQueryUseCase.findCityById(ciudadId)
                .map(CityResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ciudad no encontrada"));
    }

    /** Respuesta pública de país. */
    public record CountryResponse(Long id) {
        static CountryResponse from(final Pais pais) {
            return new CountryResponse(pais.getId());
        }
    }

    /** Respuesta pública de ciudad. */
    public record CityResponse(Long id) {
        static CityResponse from(final Ciudad ciudad) {
            return new CityResponse(ciudad.getId());
        }
    }
}
