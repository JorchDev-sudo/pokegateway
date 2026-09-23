package com.jorchdev.poketeams.pokegateway.controllers;

import com.jorchdev.poketeams.pokegateway.services.auth.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Value("${auth0.domain}")
    private String domain;

    @Value("${auth0.client-id}")
    private String clientId;

    @Value("${auth0.redirect-uri}")
    private String redirectUri;

    @Value("${auth0.audience}")
    private String audience;

    @Value("${auth0.frontend-success-redirect}")
    private String frontendRedirect;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public void login(HttpServletResponse response) throws IOException {
        // "state" evita CSRF sobre el propio flujo de login: un atacante
        // no puede forzar a un usuario a completar un callback que
        // nosotros no iniciamos, porque no puede adivinar este valor.
        String state = UUID.randomUUID().toString();

        ResponseCookie stateCookie = ResponseCookie.from("OAUTH_STATE", state)
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")
                .maxAge(300) // 5 min, tiempo de sobra para completar el login
                .path("/auth")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, stateCookie.toString());

        String authorizeUrl = UriComponentsBuilder
                .fromUriString("https://" + domain + "/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("scope", "openid profile email")
                .queryParam("audience", audience)
                .queryParam("state", state)
                .build()
                .toUriString();

        response.sendRedirect(authorizeUrl);
    }

    @GetMapping("/callback")
    public void callback(
            @RequestParam("code") String code,
            @RequestParam("state") String state,
            @CookieValue(name = "OAUTH_STATE", required = false) String expectedState,
            HttpServletResponse response) throws IOException {

        if (expectedState == null || !expectedState.equals(state)) {
            response.sendError(HttpStatus.BAD_REQUEST.value(), "Invalid state parameter");
            return;
        }

        String sessionId = authService.handleCallback(code, redirectUri);

        clearCookie(response, "OAUTH_STATE", "/auth");

        ResponseCookie sessionCookie = ResponseCookie.from("SESSION_ID", sessionId)
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")
                .maxAge(Duration.ofDays(7))
                .path("/")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, sessionCookie.toString());

        response.sendRedirect(frontendRedirect);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(name = "SESSION_ID", required = false) String sessionId,
            HttpServletResponse response) {

        if (sessionId != null) {
            authService.logout(sessionId);
        }
        clearCookie(response, "SESSION_ID", "/");
        return ResponseEntity.noContent().build();
    }

    private void clearCookie(HttpServletResponse response, String name, String path) {
        ResponseCookie expired = ResponseCookie.from(name, "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")
                .maxAge(0)
                .path(path)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, expired.toString());
    }
}
