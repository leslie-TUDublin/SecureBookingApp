package com.lesya.booking1.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
// EN: JWT service responsible for generating and validating JWT tokens.
// UA: JWT сервіс, відповідає за генерацію та перевірку JWT-токенів.
public class JwtService {

    // EN: Logger for tracking token validation errors.
    // UA: Логер для відстеження помилок валідації токенів.
    private static final Logger log = LoggerFactory.getLogger(JwtService.class);

    // EN: Secret key is loaded from application.yml.
    // UA: Секретний ключ завантажується з application.yml.
    private final String secretKey;

    // EN: JWT token lifetime in milliseconds.
    // UA: Час життя JWT-токена в мілісекундах.
    private final long jwtExpiration;



    public JwtService(
            // EN: Constructor receives JWT configuration values from application.yml
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.expiration}") long jwtExpiration
    ) {
        this.secretKey = secretKey;
        this.jwtExpiration = jwtExpiration;
    }


    // CREATE SIGNING KEY
    // EN: Convert the secret string into a cryptographic SecretKey object.
    // UA: Перетворити секретний рядок у криптографічний об'єкт SecretKey.
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    // GENERATE JWT TOKEN for the given user's email
    public String generateToken(String email) {
        return Jwts.builder()
                // EN: Store user's email as JWT subject.
                .subject(email)
                // EN: Store token creation time and token expiration time.
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))

                // EN: Sign JWT using app's secret key.
                // UA: Підписати JWT за допомогою мого секретного ключа.
                .signWith(getSigningKey())

                // EN: Convert JWT object into a compact String.
                .compact();


    }

    // EXTRACT EMAIL from JWT subject
    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    // PRE-VALIDATE WITHOUT DB (ANTI-DOS)
    // EN: Validates JWT signature and expiration strictly in-memory before calling DB.
    // UA: Перевіряє підпис та термін дії JWT суворо в пам'яті ДО викликання бази даних.
    public boolean validateStructureAndExpiration(String token) {
        try {
            // EN: If parsing succeeds, the token signature and expiration date are valid.
            // UA: Якщо парсинг успішний, значить підпис і термін дії токена в порядку.
            getClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("JWT token is expired: {}", e.getMessage());
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token structure: {}", e.getMessage());
        } catch (Exception e) {
            log.error("JWT token validation failed: {}", e.getMessage());
        }
        return false;
    }

    // VALIDATE TOKEN WITH EMAIL
    // EN: Check whether Token belongs to the given email and  has not expired.
    public boolean isTokenValid(String token, String email) {
        try {
            // EN: Extract email stored inside the JWT.
            // UA: Витягнути email ( from JWT).
            final String extractedEmail = extractEmail(token);

            return extractedEmail
                    .equals(email)
                    &&
                    !isTokenExpired(token);
        }
        catch (Exception e) {
            return false;
        }
    }

    // CHECK TOKEN EXPIRATION
    // EN: Check whether Token expiration date is before the current time. (чи ВЖЕ НАСТАЛА СТАНОМ НА СЬОГОДНІ)
    private boolean isTokenExpired(String token) {
        return getClaims(token)
                .getExpiration()
                .before(new Date());
    }

    // ВІзьми Клеймсики
    // GET CLAIMS
    // EN: Parse the signed JWT and return its claims.
    // UA: Розпарсити підписаний JWT та повернути його Клеймсики.
    private Claims getClaims(String token) {
        return Jwts.parser()

                // EN: Use app's secret key to verify the JWT signature.
                // UA: Використати наш секретний ключ для перевірки підпису JWT.
                .verifyWith(getSigningKey())

                // EN: Build the JWT parser.
                .build()

                // EN: Parse a signed JWT containing claims.
                // UA: Розпарсити підписаний JWT, який містить Клеймсики.
                .parseSignedClaims(token)

                // EN: Return the claims stored in the JWT payload.
                // UA: Повернути Клеймсики які в Пейлоаді сидять.
                .getPayload();


    }
}