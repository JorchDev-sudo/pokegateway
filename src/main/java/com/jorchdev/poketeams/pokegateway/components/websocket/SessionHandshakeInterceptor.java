package com.jorchdev.poketeams.pokegateway.components.websocket;

import com.jorchdev.poketeams.pokegateway.components.InternalJwtIssuer;
import com.jorchdev.poketeams.pokegateway.components.session.SessionAuthenticator;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class SessionHandshakeInterceptor implements HandshakeInterceptor {

    private final SessionAuthenticator authenticator;
    private final InternalJwtIssuer issuer;

    public SessionHandshakeInterceptor(
            SessionAuthenticator authenticator,
            InternalJwtIssuer issuer
    ) {
        this.authenticator = authenticator;
        this.issuer = issuer;
    }

    @Override
    public boolean beforeHandshake(
            @NonNull ServerHttpRequest request,
            @NonNull ServerHttpResponse response,
            @NonNull WebSocketHandler wsHandler,
            @NonNull Map<String, Object> attributes
    ) throws Exception {

        if (!(request instanceof ServletServerHttpRequest servletRequest)) {
            return false;
        }

        Optional<UUID> trainerId = authenticator.authenticate(
                servletRequest.getServletRequest()
        );

        if (trainerId.isEmpty()) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }

        String jwt = issuer.issue(trainerId.get());

        attributes.put("trainerId", trainerId.get());
        attributes.put("accessToken", jwt);

        return true;
    }

    @Override
    public void afterHandshake(
            @NonNull ServerHttpRequest request,
            @NonNull ServerHttpResponse response,
            @NonNull WebSocketHandler wsHandler,
            Exception exception
    ) {
    }
}