package com.jorchdev.poketeams.pokegateway.entities.internal.inputs;

import java.util.UUID;

public record SendMessageInput(UUID conversationId, String content) {
}
