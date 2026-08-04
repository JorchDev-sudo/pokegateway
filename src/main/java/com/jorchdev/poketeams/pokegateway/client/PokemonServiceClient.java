package com.jorchdev.poketeams.pokegateway.client;

import com.jorchdev.poketeams.pokegateway.dtos.responses.internal.ApiPokemonResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class PokemonServiceClient {
    private final WebClient webClient;

    public PokemonServiceClient(@Qualifier("pokemonServiceWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public ApiPokemonResponse getPokemonById(int pokemonId) {
        return webClient
                .get()
                .uri("/api/pokemon/{id}", pokemonId)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new EntityNotFoundException())
                )
                .bodyToMono(ApiPokemonResponse.class)
                .block();
    }

    public ApiPokemonResponse getPokemonByName(String name){
        System.out.println(name);

        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/pokemon/search")
                        .queryParam("name", name)
                        .build())
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new EntityNotFoundException())
                )
                .bodyToMono(ApiPokemonResponse.class)
                .block();
    }
}
