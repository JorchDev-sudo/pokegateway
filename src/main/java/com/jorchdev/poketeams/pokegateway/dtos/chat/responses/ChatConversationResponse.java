package com.jorchdev.poketeams.pokegateway.dtos.chat.responses;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChatConversationResponse(
        UUID id,
        List<UUID> participantIds,
        Instant createdAt,
        Instant updatedAt,
        ChatMessageResponse lastMessage
) {
}
