package com.api.muinda_kubika.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final Algorithm algorithm;
    private final JWTVerifier verifier;
    private final String issuer;
    private final long expirationMs;
    private final long refreshExpirationMs;

    public JwtService(
        @Value("${jwt.secret}") String secret,
        @Value("${jwt.issuer:muinda-kubika-api}") String issuer,
        @Value("${jwt.expiration-ms:86400000}") long expirationMs,
        @Value(
            "${jwt.refresh-expiration-ms:2592000000}"
        ) long refreshExpirationMs
    ) {
        this.algorithm = Algorithm.HMAC256(secret);
        this.issuer = issuer;
        this.expirationMs = expirationMs;
        this.refreshExpirationMs = refreshExpirationMs;
        this.verifier = JWT.require(this.algorithm)
            .withIssuer(this.issuer)
            .build();
    }

    public String generateToken(
        UUID userId,
        Set<String> roles,
        Set<String> permissions
    ) {
        Instant now = Instant.now();

        return JWT.create()
            .withIssuer(issuer)
            .withSubject(userId.toString())
            .withClaim("userId", userId.toString())
            .withClaim("roles", List.copyOf(roles))
            .withClaim("permissions", List.copyOf(permissions))
            .withIssuedAt(now)
            .withExpiresAt(now.plusMillis(expirationMs))
            .sign(algorithm);
    }

    public JwtAuthPayload validateToken(String token) {
        try {
            DecodedJWT decodedJWT = verifier.verify(token);

            String userIdClaim = decodedJWT.getClaim("userId").asString();
            if (userIdClaim == null || userIdClaim.isBlank()) {
                userIdClaim = decodedJWT.getSubject();
            }

            UUID userId = UUID.fromString(userIdClaim);
            Set<String> roles = toSet(
                decodedJWT.getClaim("roles").asList(String.class)
            );
            Set<String> permissions = toSet(
                decodedJWT.getClaim("permissions").asList(String.class)
            );

            return new JwtAuthPayload(userId, roles, permissions);
        } catch (JWTVerificationException | IllegalArgumentException ex) {
            return null;
        }
    }

    public long getExpirationMs() {
        return expirationMs;
    }

    public long getRefreshExpirationMs() {
        return refreshExpirationMs;
    }

    private Set<String> toSet(List<String> values) {
        if (values == null) {
            return Collections.emptySet();
        }
        return new HashSet<>(values);
    }
}
