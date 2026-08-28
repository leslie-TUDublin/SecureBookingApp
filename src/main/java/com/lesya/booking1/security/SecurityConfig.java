package com.lesya.booking1.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
// EN: Security configuration.
// UA: Конфігурація безпеки.
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // EN: Constructor injection of the JWT filter.
    // UA: Впровадження залежності JWT-фільтра через конструктор.
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    // EN: Password encoder bean.
    // UA: Bean для хешування паролів.
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    // EN: Configure Spring Security filter chain.
    // UA: Налаштування ланцюжка фільтрів Spring Security.
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // EN: Enable CORS support.
                // UA: Увімкнути підтримку CORS.
                .cors(Customizer.withDefaults())

                // EN: Disable CSRF for REST API.
                // UA: Вимкнути CSRF для REST API.
                .csrf(csrf -> csrf.disable())

                // EN: JWT application must be stateless.
                // UA: JWT застосунок не повинен створювати сесії.
                .sessionManagement(session -> session.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS
                ))

                // EN: Configure endpoint access rules.
                // UA: Налаштування доступу до endpoint-ів.
                .authorizeHttpRequests(auth -> auth
                        // EN: Allow public access to all authentication endpoints.
                        // UA: Дозволити публічний доступ до всіх ендпоінтів авторизації.
                        .requestMatchers("/auth/**").permitAll()
                        // EN: All other requests require authentication.
                        // UA: Усі інші запити вимагають авторизації.
                        .anyRequest().authenticated()
                )

                // EN: Add JWT filter before Spring authentication filter.
                // UA: Додати JWT фільтр перед стандартним фільтром Spring.
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
