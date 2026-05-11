package com.buses.examen.Progra.config;

import org.springframework.http.HttpStatus;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Expone el token CSRF para clientes SPA basados en sesión.
 */
@RestController
@RequestMapping("/api/csrf")
public class CsrfController {

    /**
     * Devuelve el token CSRF actual y el header requerido.
     *
     * @param csrfToken token resuelto por Spring Security
     * @return representación del token para el frontend
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CsrfTokenResponse getCsrf(final CsrfToken csrfToken) {
        return new CsrfTokenResponse(csrfToken.getHeaderName(), csrfToken.getToken());
    }

    /**
     * DTO de respuesta con el token CSRF.
     *
     * @param headerName nombre del header esperado
     * @param token valor del token
     */
    public record CsrfTokenResponse(String headerName, String token) {
    }
}
