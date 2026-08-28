package com.lesya.booking1.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
// EN: Filter that checks JWT token on every request with DOS protection.
// UA: Фільтр, який перевіряє JWT токен на кожному запиті із захистом від DOS-атак.
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // EN: Logger for recording security workflow events.
    // UA: Логер для запису подій процесу авторизації.
    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;


    //EN: Constructor
    public JwtAuthenticationFilter(
            JwtService jwtService,
            CustomUserDetailsService customUserDetailsService
    ) {
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // EN: Get Authorization header from incoming HTTP request.
        // UA: Отримуємо Authorization header з HTTP-запиту.
        String authHeader = request.getHeader("Authorization");

        // EN: If header is missing or does not start with Bearer → skip filter.
        // UA: Якщо header відсутній або не Bearer → пропускаємо запит далі.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // EN: Extract JWT token from header (cut off "Bearer " prefix).
        // UA: Витягуємо JWT токен (відрізаємо перші 7 символів префіксу).
        String token = authHeader.substring(7);

        try {
            // ANTI-DOS STEP
            // EN: Step 1: Validate token signature and expiration strictly in memory.
            // EN: If the token is fake or expired, we NEVER call PostgreSQL.
            // UA: Крок 1: Валідуємо підпис і ліміт часу токена суворо в оперативній пам'яті.
            // UA: Якщо токен підроблений чи застарів, ми НІКОЛИ не робимо запит у PostgreSQL.
            if (jwtService.validateStructureAndExpiration(token)) {

                // EN: Step 2: Extract email only after confirming the token is untampered.
                // UA: Крок 2: Витягуємо email лише після того, як переконалися, що токен цілісний.
                String email = jwtService.extractEmail(token);

                // EN: Check if user is not authenticated in Spring Security context yet.
                // UA: Перевіряємо, чи користувач ще не авторизований у поточному потоці запиту.
                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                    // EN: Step 3: Safe to call database now since the token is verified.
                    // UA: Крок 3: Тепер безпечно викликати БД, оскільки токен пройшов криптографічну перевірку.
                    UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

                    // EN: Final cross-check of token details against loaded user.
                    // UA: Фінальна перевірка відповідності токена завантаженому користувачу.
                    if (jwtService.isTokenValid(token, email)) {

                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                        // EN: Attach request details (IP, session info).
                        // UA: Додаємо інформацію про запит (IP, сесія).
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        // EN: Set authentication in SecurityContext.
                        // UA: Зберігаємо авторизацію в SecurityContext для Spring Security.
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                        log.debug("Successfully authenticated user: {}", email);
                    }
                }
            }
        } catch (Exception e) {
            // EN: Catch-all block to prevent filter chain break if unpredicted error occurs.
            // UA: Загальний catch-блок для захисту від неочікуваних помилок, щоб не зламати ланцюжок фільтрів.
            log.error("Unexpected error in JwtAuthenticationFilter: {}", e.getMessage(), e);
        }

        // EN: Continue filter chain execution.
        // UA: Продовжуємо обробку запиту іншими фільтрами.
        filterChain.doFilter(request, response);
    }
}
