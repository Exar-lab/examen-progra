package com.buses.examen.Progra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad web con sesiones HTTP y form login.
 *
 * <p>Controla el acceso a los endpoints protegidos (compras) y públicos
 * (registro, consultas de catálogo, login).</p>
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    /**
     * Define el {@link SecurityFilterChain} con las reglas de acceso y sesiones.
     *
     * @param http constructor de la cadena de seguridad
     * @return la cadena de filtros configurada
     * @throws Exception si la configuración falla
     */
    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        http
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .formLogin(form -> form
                        .loginPage("/login.html")
                        .loginProcessingUrl("/login")
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .permitAll())
                // CSRF habilitado por defecto para sesiones basadas en navegador.
                // Tests con mutating methods deben incluir .with(csrf())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/api/csrf").permitAll()
                        // Endpoints públicos: autenticación
                        .requestMatchers(HttpMethod.GET, "/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/auth/me").authenticated()
                        // Páginas y assets públicos del frontend estático.
                        .requestMatchers(
                                "/",
                                "/index.html",
                                "/login.html",
                                "/registro.html",
                                "/horarios.html",
                                "/contacto.html",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/favicon.ico")
                        .permitAll()
                        // Registro y consulta pública de clientes
                        .requestMatchers(HttpMethod.GET, "/api/customers/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/customers/document/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/customers", "/api/customers/register").permitAll()
                        // Catálogo público
                        .requestMatchers(HttpMethod.GET, "/api/routes/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/services/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/geography/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/fleet/**").permitAll()
                        // Todo lo demás: requiere autenticación
                        // La autorización granular se controla con @PreAuthorize en cada controller
                        .anyRequest().authenticated());

        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
