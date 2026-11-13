package com.jorchdev.poketeams.pokegateway.client.pokeservice;

import com.jorchdev.poketeams.pokegateway.dtos.responses.internal.ApiPokemonResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class PokemonServiceClient {
    private final WebClient webClient;

    public PokemonServiceClient(@Qualifier("pokemonServiceWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public ApiPokemonResponse getPokemonById(int id) {
        return webClient.get()
                .uri("/api/pokemon/{id}", id)
                .retrieve()
                .onStatus(status -> status.value() == 404,
                        r -> Mono.error(new EntityNotFoundException("Pokemon " + id + " not found")))
                .onStatus(HttpStatusCode::isError,
                        r -> Mono.error(new IllegalStateException(
                                "Pokemon service error: " + r.statusCode())))
                .bodyToMono(ApiPokemonResponse.class)
                .block(Duration.ofSeconds(5));
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
                .onStatus(status -> status.value() == 404,
                        r -> Mono.error(new EntityNotFoundException("Pokemon " + name + " not found")))
                .onStatus(HttpStatusCode::isError,
                        r -> Mono.error(new IllegalStateException(
                                "Pokemon service error: " + r.statusCode())))
                .bodyToMono(ApiPokemonResponse.class)
                .block(Duration.ofSeconds(5));
    }
}
