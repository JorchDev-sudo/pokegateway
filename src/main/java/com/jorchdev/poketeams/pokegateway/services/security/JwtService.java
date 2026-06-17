package com.jorchdev.poketeams.pokegateway.services.security;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jorchdev.poketeams.pokegateway.entities.internal.CustomUserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    @Value("${jwt_secret}")
    private String secret;

    public String generateToken (UUID userId) throws
            IllegalArgumentException, JWTCreationException {
        return JWT.create()
                .withSubject(userId.toString())
                .withIssuedAt(new Date())
                .withIssuer("pokegateway")
                .sign(Algorithm.HMAC256(secret));

    }

    public boolean validateToken(
            String token,
            CustomUserDetails userDetails) {

        try {
            String subject =
                    extractSubject(token);

            return subject.equals(
                    userDetails.getId().toString());

        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public String extractSubject(String token) {
        JWTVerifier verifier = JWT.require(
                        Algorithm.HMAC256(secret))
                .withIssuer("pokegateway")
                .build();

        return verifier
                .verify(token)
                .getSubject();
    }
}