package com.jorchdev.poketeams.pokegateway.dtos.chat.requests;

import java.util.UUID;

public record SendMessageRequest(UUID conversationId, String content) {
}
