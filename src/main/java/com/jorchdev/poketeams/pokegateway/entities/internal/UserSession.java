package com.jorchdev.poketeams.pokegateway.entities.internal;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
public class UserSession implements Persistable<String> {
    @Id
    private String sessionId;

    private UUID trainerId;

    @Setter
    @Column(length = 2000)
    private String auth0AccessToken;

    @Setter
    @Column(length = 2000)
    private String auth0RefreshToken;

    @Transient
    private boolean isNew = true;

    @Override
    public String getId() {
        return sessionId;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PostPersist
    @PostLoad
    void markNotNew() {
        this.isNew = false;
    }

    @Setter
    private Instant accessTokenExpiresAt;

    @Setter
    private Instant sessionExpiresAt;

    public UserSession(){};

    public UserSession(
            String sessionId,
            UUID trainerId,
            String auth0AccessToken,
            String auth0RefreshToken,
            Instant accessTokenExpiresAt,
            Instant sessionExpiresAt)
    {
        this.sessionId = sessionId;

        this.trainerId = trainerId;
        this.auth0AccessToken = auth0AccessToken;
        this.auth0RefreshToken = auth0RefreshToken;

        this.accessTokenExpiresAt = accessTokenExpiresAt;
        this.sessionExpiresAt = sessionExpiresAt;
    }
}