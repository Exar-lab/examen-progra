package com.buses.examen.Progra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Configuración de beans de seguridad compartidos por los adaptadores.
 */
@Configuration
public class SecurityBeansConfig {

    /**
     * Expone un encoder BCrypt reutilizable para hashing de contraseñas.
     *
     * @return instancia singleton de {@link BCryptPasswordEncoder}
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
