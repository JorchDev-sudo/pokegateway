package com.jorchdev.poketeams.pokegateway.entities.internal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
public class UserSession {
    @Id
    private String sessionId;

    private UUID trainerId;

    @Setter
    @Column(length = 2000)
    private String auth0AccessToken;

    @Setter
    @Column(length = 2000)
    private String auth0RefreshToken;

    @Setter
    private Instant accessTokenExpiresAt;

    @Setter
    private Instant sessionExpiresAt;

    public UserSession(
            UUID trainerId,
            String auth0AccessToken,
            String auth0RefreshToken,
            Instant accessTokenExpiresAt,
            Instant sessionExpiresAt)
    {
        this.trainerId = trainerId;
        this.auth0AccessToken = auth0AccessToken;
        this.auth0RefreshToken = auth0RefreshToken;

        this.accessTokenExpiresAt = accessTokenExpiresAt;
        this.sessionExpiresAt = sessionExpiresAt;
    }
}