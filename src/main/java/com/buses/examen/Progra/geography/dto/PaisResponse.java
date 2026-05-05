package com.buses.examen.Progra.geography.dto;

/**
 * DTO de respuesta para un país del catálogo.
 *
 * @param codigoIso código ISO-3166 de dos o tres letras
 * @param nombre    nombre legible del país
 */
public record PaisResponse(String codigoIso, String nombre) {
}
