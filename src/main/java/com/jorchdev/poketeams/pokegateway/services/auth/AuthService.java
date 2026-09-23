package com.jorchdev.poketeams.pokegateway.services.auth;

import com.jorchdev.poketeams.pokegateway.client.Auth0Client;
import com.jorchdev.poketeams.pokegateway.dtos.responses.internal.UserInfoResponse;
import com.jorchdev.poketeams.pokegateway.entities.Trainer;
import com.jorchdev.poketeams.pokegateway.entities.internal.UserSession;
import com.jorchdev.poketeams.pokegateway.repositories.SessionRepository;
import com.jorchdev.poketeams.pokegateway.repositories.TrainerRepository;
import com.jorchdev.poketeams.pokegateway.dtos.responses.internal.TokenResponse;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class AuthService {

    private final Auth0Client auth0Client;
    private final TrainerRepository trainerRepository;
    private final SessionRepository sessionRepository;

    public AuthService(
            Auth0Client auth0Client,
            TrainerRepository trainerRepository,
            SessionRepository sessionRepository)
    {
        this.auth0Client = auth0Client;
        this.trainerRepository = trainerRepository;
        this.sessionRepository = sessionRepository;
    }

    public String handleCallback(String code, String redirectUri) {
        TokenResponse tokens = auth0Client.exchangeCodeForTokens(code, redirectUri);
        UserInfoResponse userInfo = auth0Client.getUserInfo(tokens.accessToken());

        Trainer trainer = trainerRepository.findByAuth0Sub(userInfo.sub())
                .orElseGet(() -> createTrainer(userInfo));

        UserSession session = new UserSession(
                trainer.getId(),
                tokens.accessToken(),
                tokens.refreshToken(),
                Instant.now().plusSeconds(tokens.expiresIn()),
                Instant.now().plus(Duration.ofDays(7))
                );


        sessionRepository.save(session);
        return session.getSessionId();
    }

    private Trainer createTrainer(UserInfoResponse userInfo) {
        Trainer trainer = new Trainer();
        trainer.setAuth0Sub(userInfo.sub());
        trainer.setEmail(userInfo.email());
        trainer.setName(userInfo.name());
        return trainerRepository.save(trainer);
    }

    public void logout(String sessionId) {
        sessionRepository.findById(sessionId).ifPresent(session -> {
            auth0Client.revokeRefreshToken(session.getAuth0RefreshToken());
            sessionRepository.deleteById(sessionId);
        });
    }
}
