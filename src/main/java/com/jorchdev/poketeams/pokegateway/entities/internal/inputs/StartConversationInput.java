package com.jorchdev.poketeams.pokegateway.entities.internal.inputs;

import java.util.List;
import java.util.UUID;

public record StartConversationInput(List<UUID> participantIds) {
}
