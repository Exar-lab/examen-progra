package com.buses.examen.Progra.geography.adapter.in.web.mapper;

import com.buses.examen.Progra.geography.adapter.in.web.dto.response.CityResponse;
import com.buses.examen.Progra.geography.adapter.in.web.dto.response.CountryResponse;
import com.buses.examen.Progra.geography.domain.Ciudad;
import com.buses.examen.Progra.geography.domain.Pais;
import org.springframework.stereotype.Component;

/** Mapper de dominio geográfico hacia DTOs web. */
@Component
public class GeographyWebMapper {
    /**
     * Convierte un país a DTO de respuesta.
     *
     * @param pais país de dominio
     * @return respuesta HTTP de país
     */
    public CountryResponse toCountryResponse(final Pais pais) { return new CountryResponse(pais.getId()); }
    /**
     * Convierte una ciudad a DTO de respuesta.
     *
     * @param ciudad ciudad de dominio
     * @return respuesta HTTP de ciudad
     */
    public CityResponse toCityResponse(final Ciudad ciudad) { return new CityResponse(ciudad.getId()); }
}
