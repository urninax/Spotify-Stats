package me.urninax.spotifystats.security.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JWTUtil{
    @Value("${jwtSecret}")
    private String secret;

    @Value("${jwtExpirationMs}")
    private long expirationMs;

    @Value("${jwtRefreshExpirationMs}")
    private long refreshExpirationMs;

    public String generateToken(String username){
        Instant expirationDateTime = Instant.now().plus(expirationMs, ChronoUnit.MILLIS);

        return JWT.create()
                .withSubject("User Details")
                .withClaim("username", username)
                .withIssuedAt(Instant.now())
                .withExpiresAt(expirationDateTime)
                .withIssuer("developer")
                .sign(Algorithm.HMAC256(secret));
    }

    public String validateAndRetrieveClaim(String token) throws JWTVerificationException{
        JWTVerifier jwtVerifier =JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User Details")
                .withIssuer("developer")
                .build();

        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        return decodedJWT.getClaim("username").asString();
    }
}
