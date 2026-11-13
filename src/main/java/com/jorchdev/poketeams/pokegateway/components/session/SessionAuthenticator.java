package com.jorchdev.poketeams.pokegateway.components.session;

import com.jorchdev.poketeams.pokegateway.entities.internal.UserSession;
import com.jorchdev.poketeams.pokegateway.repositories.SessionRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Component
public class SessionAuthenticator {

    private static final String COOKIE_NAME = "SESSION_ID";
    private final SessionRepository sessionRepository;

    public SessionAuthenticator(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public Optional<UUID> authenticate(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return Optional.empty();

        return Arrays.stream(cookies)
                .filter(c -> COOKIE_NAME.equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .flatMap(sessionRepository::findById)
                .filter(s -> s.getSessionExpiresAt().isAfter(Instant.now()))
                .map(UserSession::getTrainerId);
    }
}
