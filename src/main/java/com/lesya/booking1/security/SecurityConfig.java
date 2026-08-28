package com.lesya.booking1.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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
    private final CustomUserDetailsService userDetailsService;

    // EN: Constructor injection of the JWT filter and user details service.
    // UA: Впровадження залежностей JWT-фільтра та сервісу користувачів через конструктор.
    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            CustomUserDetailsService userDetailsService
    ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    // EN: Password encoder bean.
    // UA: Bean для хешування паролів.
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    // EN: AuthenticationProvider connects our UserDetailsService and PasswordEncoder.
    // UA: Provider, який пов'язує наш UserDetailsService та кодувальник паролів.
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    // EN: AuthenticationManager bean to handle login requests in AuthController.
    // UA: Bean менеджера автентифікаці для обробки логіну в AuthController.
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
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

                // EN: Link our custom authentication provider.
                // UA: Підключаємо наш кастомний провайдер автентифікації.
                .authenticationProvider(authenticationProvider())

                // EN: Add JWT filter before Spring authentication filter.
                // UA: Додати JWT фільтр перед стандартним фільтром Spring.
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
