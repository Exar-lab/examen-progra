package com.buses.examen.Progra.customer.adapter.in.web;

import com.buses.examen.Progra.customer.adapter.in.web.dto.request.LoginRequest;
import com.buses.examen.Progra.customer.adapter.in.web.dto.response.LoginResponse;
import com.buses.examen.Progra.customer.adapter.out.security.AuthenticatedCustomerPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controlador REST para autenticación de clientes.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    /**
     * Crea el controlador de autenticación.
     *
     * @param authenticationManager manager de autenticación de Spring Security
     */
    public AuthenticationController(final AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * Autentica un cliente y persiste su contexto en la sesión HTTP.
     *
     * @param request payload con credenciales
     * @param httpRequest request HTTP actual para obtener/crear sesión
     * @return datos mínimos del usuario autenticado
     */
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody final LoginRequest request,
                               final HttpServletRequest httpRequest) {
        try {
            final Authentication authentication = authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken.unauthenticated(request.username(), request.password())
            );

            final Object principal = authentication.getPrincipal();
            if (!(principal instanceof AuthenticatedCustomerPrincipal customerPrincipal)) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Principal autenticado no soportado");
            }

            final SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authentication);
            SecurityContextHolder.setContext(securityContext);
            httpRequest.getSession(true).setAttribute(
                    HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    securityContext
            );

            return new LoginResponse(customerPrincipal.clienteId(), customerPrincipal.getUsername());
        } catch (final BadCredentialsException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas", ex);
        }
    }
}
