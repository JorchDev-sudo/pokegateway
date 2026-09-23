package com.jorchdev.poketeams.pokegateway.client;

import com.jorchdev.poketeams.pokegateway.dtos.responses.internal.TokenResponse;
import com.jorchdev.poketeams.pokegateway.dtos.responses.internal.UserInfoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class Auth0Client {

    private final WebClient webClient;
    private final String clientId;
    private final String clientSecret;

    public Auth0Client(
            @Value("${OAUTH0_DOMAIN}") String domain,
            @Value("${OAUTH0_CLIENT_ID}") String clientId,
            @Value("${OAUTH0_CLIENT_SECRET}") String clientSecret) {

        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.webClient = WebClient.builder()
                .baseUrl("https://" + domain)
                .build();
    }

    // Paso del Authorization Code Flow: intercambia el "code" que
    // Auth0 te manda en el callback por los tokens reales
    public TokenResponse exchangeCodeForTokens(String code, String redirectUri) {
        return webClient.post()
                .uri("/oauth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of(
                        "grant_type", "authorization_code",
                        "client_id", clientId,
                        "client_secret", clientSecret,
                        "code", code,
                        "redirect_uri", redirectUri
                ))
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .block();
    }

    // Para renovar el access_token cuando expira, usando el refresh_token
    public TokenResponse refreshAccessToken(String refreshToken) {
        return webClient.post()
                .uri("/oauth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of(
                        "grant_type", "refresh_token",
                        "client_id", clientId,
                        "client_secret", clientSecret,
                        "refresh_token", refreshToken
                ))
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .block();
    }

    // Para el logout: revoca el refresh_token
    public void revokeRefreshToken(String refreshToken) {
        webClient.post()
                .uri("/oauth/revoke")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of(
                        "client_id", clientId,
                        "client_secret", clientSecret,
                        "token", refreshToken
                ))
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public UserInfoResponse getUserInfo(String accessToken) {
        return webClient.get()
                .uri("/userinfo")
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(UserInfoResponse.class)
                .block();
    }
}
