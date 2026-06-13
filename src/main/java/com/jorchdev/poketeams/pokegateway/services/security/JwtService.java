package com.jorchdev.poketeams.pokegateway.services.security;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt_secret}")
    private String secret;

    public String generateToken (String userEmail) throws
            IllegalArgumentException, JWTCreationException {
        return JWT.create()
                .withSubject("User Details")
                .withClaim("userEmail", userEmail)
                .withIssuedAt(new Date())
                .withIssuer("pokegateway")
                .sign(Algorithm.HMAC256(secret));

    }

    public boolean validateToken (String token, UserDetails userDetails) throws JWTVerificationException {
        try{
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                    .withSubject("User Details")
                    .withClaim("userEmail", userDetails.getUsername())
                    .withIssuer("pokegateway")
                    .build();

            DecodedJWT jwt = verifier.verify(token);
            return true;

        }catch (JWTVerificationException e){
            return false;
        }
    }

    public String validateTokenAndRetrieveSubject (String token) throws
            JWTVerificationException{
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User Details")
                .withIssuer("pokegateway")
                .build();

        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("userEmail").asString();
    }
}