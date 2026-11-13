package com.jorchdev.poketeams.pokegateway.components;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Component
public class InternalJwtIssuer {
    private final JwtEncoder encoder;

    public InternalJwtIssuer(
            @Value("${internal-jwt.public-key}") RSAPublicKey publicKey,
            @Value("${internal-jwt.private-key}") RSAPrivateKey privateKey)
    {
        JWK jwk = new RSAKey.Builder(publicKey).privateKey(privateKey).keyID("gateway-1").build();
        this.encoder = new NimbusJwtEncoder(new ImmutableJWKSet<>(new JWKSet(jwk)));
    }

    public String issue(UUID trainerId) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("poketeams-gateway")
                .audience(List.of("poketeams-internal"))
                .subject(trainerId.toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(120))
                .build();
        JwsHeader header = JwsHeader.with(SignatureAlgorithm.RS256).keyId("gateway-1").build();
        return encoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

    public String issueService() {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("poketeams-gateway")
                .audience(List.of("poketeams-internal"))
                .subject("service:gateway")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(120))
                .build();
        JwsHeader header = JwsHeader.with(SignatureAlgorithm.RS256).keyId("gateway-1").build();
        return encoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }
}