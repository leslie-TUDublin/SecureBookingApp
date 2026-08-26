package com.lesya.booking1.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
// EN: JWT service responsible for generating and validating JWT tokens.
// UA: JWT сервіс, відповідає за генерацію та перевірку JWT-токенів.
public class JwtService {

    // EN: Secret key is loaded from application.yml.
    // UA: Секретний ключ завантажується з application.yml.
    private final String secretKey;

    // EN: JWT token lifetime in milliseconds.
    // UA: Час життя JWT-токена в мілісекундах.
    private final long jwtExpiration;


    // EN: Constructor receives JWT configuration values from application.yml
    // UA: Конструктор отримує налаштування JWT з application.yml.
    public JwtService(
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

        return Keys.hmacShaKeyFor(
                secretKey.getBytes(StandardCharsets.UTF_8)
        );
    }

    // GENERATE JWT TOKEN
    // EN: Generate a JWT token for the given user's email.
    // UA: Згенерувати JWT-токен для email переданого користувача.
    public String generateToken(String email) {

        return Jwts.builder()

                // EN: Store user's email as JWT subject.
                .subject(email)

                // EN: Store token creation time and token expiration time.
                .issuedAt(new Date())
                .expiration( new Date(  System.currentTimeMillis() + jwtExpiration  )  )

                // EN: Sign JWT using app's secret key.
                // UA: Підписати JWT за допомогою мого секретного ключа.
                .signWith(getSigningKey())

                // EN: Convert JWT object into a compact String.
                .compact();
    }


    // EXTRACT EMAIL
    // EN: Extract user's email from JWT subject.
    public String extractEmail(String token) {

        return getClaims(token).getSubject();
    }


    // VALIDATE TOKEN
    // EN: Check whether Token belongs to the given email and  has not expired.
    public boolean isTokenValid(String token, String email) {

        try {

            // EN: Extract email stored inside the JWT.
            // UA: Витягнути email, який збережений всередині JWT.
            final String extractedEmail = extractEmail(token);

            // EN: Token is valid only when:
            // 1. email from token matches the user's email; 2. token has not expired.

            return extractedEmail.equals(email)
                    && !isTokenExpired(token);

        } catch (Exception e) {

            // EN: Any Parsing, Signature or Expiration error means that the token is invalid.
            // UA:  Будь-яка помилка 1)під час читання/розпізнавання, 2)перевірки підпису 3)терміну дії означає, що токен недійсний.
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

    // Клеймсики
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
