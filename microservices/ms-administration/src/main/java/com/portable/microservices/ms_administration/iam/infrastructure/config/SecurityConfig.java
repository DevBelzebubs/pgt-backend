package com.portable.microservices.ms_administration.iam.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Endpoints públicos de autenticación y creación de usuarios
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/api/users/**").permitAll()

                // Endpoints públicos de productos (solo lectura)
                .requestMatchers(HttpMethod.GET, "/api/v1/products/**").permitAll()
                .requestMatchers("/api/v1/products/count").permitAll()

                // Endpoints protegidos de productos (crear, editar, eliminar)
                .requestMatchers(HttpMethod.POST, "/api/v1/products/**").authenticated()
                .requestMatchers(HttpMethod.PUT, "/api/v1/products/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/api/v1/products/**").authenticated()

                // Todo lo demás requiere autenticación
                .anyRequest().authenticated()
            );

        return http.build();
    }
}
