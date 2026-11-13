package com.jorchdev.poketeams.pokegateway.dtos.chat.requests;

import java.util.List;
import java.util.UUID;

public record CreateConversationRequest(List<UUID> participantIds) {
}
