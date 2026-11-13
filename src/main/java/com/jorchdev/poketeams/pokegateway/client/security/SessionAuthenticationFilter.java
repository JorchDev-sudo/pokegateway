package com.jorchdev.poketeams.pokegateway.client.security;

import com.jorchdev.poketeams.pokegateway.components.session.SessionAuthenticator;
import com.jorchdev.poketeams.pokegateway.repositories.TrainerRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class SessionAuthenticationFilter extends OncePerRequestFilter {

    private final SessionAuthenticator authenticator;
    private final TrainerRepository trainerRepository;

    public SessionAuthenticationFilter(SessionAuthenticator authenticator,
                                       TrainerRepository trainerRepository) {
        this.authenticator = authenticator;
        this.trainerRepository = trainerRepository;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain) throws ServletException, IOException {

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            authenticator.authenticate(request)
                    .flatMap(trainerRepository::findById)
                    .ifPresent(trainer -> {
                        var auth = new UsernamePasswordAuthenticationToken(trainer, null, List.of());
                        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    });
        }

        chain.doFilter(request, response);
    }
}