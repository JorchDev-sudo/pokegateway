package com.jorchdev.poketeams.pokegateway.dtos.chat.responses;

import java.time.Instant;
import java.util.UUID;

public record ChatMessageResponse(
        UUID id,
        UUID conversationId,
        UUID senderId,
        String content,
        Instant createdAt
) {
}
