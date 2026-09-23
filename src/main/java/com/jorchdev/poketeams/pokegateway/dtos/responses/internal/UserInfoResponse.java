package com.jorchdev.poketeams.pokegateway.dtos.responses.internal;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserInfoResponse(
        String sub,
        String email,
        String name,
        @JsonProperty("email_verified") boolean emailVerified
) {}