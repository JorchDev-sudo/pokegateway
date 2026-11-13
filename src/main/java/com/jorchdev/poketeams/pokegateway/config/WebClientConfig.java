package com.jorchdev.poketeams.pokegateway.config;

import com.jorchdev.poketeams.pokegateway.components.InternalJwtIssuer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${pokemon.service.base-url}")
    private String serviceBaseUrl;

    @Value("${chat.service.base-url:http://localhost:8082}")
    private String chatBaseUrl;

    @Bean
    public WebClient pokemonServiceWebClient(InternalJwtIssuer issuer) {
        return WebClient.builder()
                .baseUrl(serviceBaseUrl)
                .filter((request, next) -> {
                    ClientRequest authed = ClientRequest.from(request)
                            .headers(headers ->
                                    headers.setBearerAuth(issuer.issueService()))
                            .build();

                    return next.exchange(authed);
                })
                .build();
    }
}
