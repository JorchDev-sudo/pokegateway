package com.jorchdev.poketeams.pokegateway.client.security;

import com.jorchdev.poketeams.pokegateway.client.Auth0Client;
import com.jorchdev.poketeams.pokegateway.dtos.responses.internal.TokenResponse;
import com.jorchdev.poketeams.pokegateway.repositories.SessionRepository;
import com.jorchdev.poketeams.pokegateway.repositories.TrainerRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@Component
public class SessionAuthenticationFilter extends OncePerRequestFilter {

    private final SessionRepository sessionRepository;
    private final TrainerRepository trainerRepository;
    private final Auth0Client auth0Client;

    public SessionAuthenticationFilter(
            SessionRepository sessionRepository,
            TrainerRepository trainerRepository,
            Auth0Client auth0Client)
    {
        this.sessionRepository = sessionRepository;
        this.trainerRepository = trainerRepository;
        this.auth0Client = auth0Client;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String sessionId = extractSessionCookie(request);

        if (sessionId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            sessionRepository.findById(sessionId).ifPresent(session -> {

                if (session.getSessionExpiresAt().isBefore(Instant.now())) {
                    sessionRepository.deleteById(sessionId);
                    return;
                }

                // access_token vencido pero la sesión sigue viva -> refrescar
                if (session.getAccessTokenExpiresAt().isBefore(Instant.now())) {
                    try {
                        TokenResponse refreshed = auth0Client.refreshAccessToken(session.getAuth0RefreshToken());
                        session.setAuth0AccessToken(refreshed.accessToken());
                        session.setAccessTokenExpiresAt(Instant.now().plusSeconds(refreshed.expiresIn()));
                        sessionRepository.save(session);
                    } catch (Exception ex) {
                        // el refresh token fue revocado o expiró -> sesión muerta
                        sessionRepository.deleteById(sessionId);
                        return;
                    }
                }

                trainerRepository.findById(session.getTrainerId()).ifPresent(trainer -> {
                    var authToken = new UsernamePasswordAuthenticationToken(
                            trainer, null, List.of());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                });
            });
        }

        filterChain.doFilter(request, response);
    }

    private String extractSessionCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        return Arrays.stream(request.getCookies())
                .filter(c -> "SESSION_ID".equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}