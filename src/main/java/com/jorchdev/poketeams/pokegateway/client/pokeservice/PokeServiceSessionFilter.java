package com.jorchdev.poketeams.pokegateway.client.pokeservice;

import com.jorchdev.poketeams.pokegateway.components.InternalJwtIssuer;
import com.jorchdev.poketeams.pokegateway.components.session.HeaderOverrideRequest;
import com.jorchdev.poketeams.pokegateway.components.session.SessionAuthenticator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public class PokeServiceSessionFilter extends OncePerRequestFilter {
    private final SessionAuthenticator authenticator;
    private final InternalJwtIssuer issuer;

    public PokeServiceSessionFilter(SessionAuthenticator authenticator, InternalJwtIssuer issuer) {
        this.authenticator = authenticator;
        this.issuer = issuer;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getRequestURI().startsWith("/pokemon/");
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain) throws ServletException, IOException
    {
        Optional<UUID> trainerId = authenticator.authenticate(request);
        if (trainerId.isEmpty()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        String bearer = "Bearer " + issuer.issue(trainerId.get());
        chain.doFilter(new HeaderOverrideRequest(request, "Authorization", bearer), response);
    }
}