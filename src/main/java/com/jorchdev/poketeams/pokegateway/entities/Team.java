package com.jorchdev.poketeams.pokegateway.entities;

import com.jorchdev.poketeams.pokegateway.entities.internal.inputs.PokemonSyncInput;
import com.jorchdev.poketeams.pokegateway.exceptions.team.TeamException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Entity
@Getter
public class Team {
    @Id
    @GeneratedValue
    private UUID id;

    @Setter
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    @OneToMany(
            mappedBy = "team",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("position ASC")
    private List<Pokemon> pokemons = new ArrayList<>();

    //Add pokemons to this Team
    private Pokemon addPokemon(int pokemonId, String pokemonName, String nickname) {
        Pokemon p = new Pokemon(this, pokemonId, pokemonName, nickname == null ? pokemonName : nickname, pokemons.size());

        this.pokemons.add(p);
        return p;
    }

    //Accept a preconstructed Team and Sync this Team to the incoming synced Team
    public void syncPokemons(List<PokemonSyncInput> syncedTeam) {
        validateSyncTeam(syncedTeam);

        Set<UUID> idsInRequest = syncedTeam.stream()
                .map(input -> input.id)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        this.pokemons.removeIf(p -> !idsInRequest.contains(p.getId()));

        List<Pokemon> ordered = syncedTeam.stream()
                .sorted(Comparator.comparingInt(input -> input.position))
                .map(this::resolvePokemon)
                .toList();

        this.pokemons.clear();
        this.pokemons.addAll(ordered);

        recalculatePositions();
    }

    //Resolves the changes in a existing Pokemon
    private Pokemon resolvePokemon(PokemonSyncInput input) {
        if (input.id == null) {
            return addPokemon(input.pokemonId, input.pokemonName, input.nickname);
        }

        Pokemon existing = this.pokemons.stream()
                .filter(p -> p.getId().equals(input.id))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Pokemon does'nt belong to this team: " + input.id));

        existing.setNickname(input.nickname != null ? input.nickname : existing.getPokemonName());
        return existing;
    }

    //Validates the incoming synced Team
    private void validateSyncTeam(List<PokemonSyncInput> syncedTeam) {
        if (syncedTeam.size() > 6) {
            throw new TeamException("Maximum 6 pokemons");
        }

        Set<Integer> positions = syncedTeam.stream().map(i -> i.position).collect(Collectors.toSet());
        if (positions.size() != syncedTeam.size()) {
            throw new TeamException("There's duplicated positions in the request");
        }

        Set<UUID> existingIds = this.pokemons.stream().map(Pokemon::getId).collect(Collectors.toSet());
        for (PokemonSyncInput input : syncedTeam) {
            if (input.id != null && !existingIds.contains(input.id)) {
                throw new IllegalArgumentException("Pokemon not found: " + input.id);
            }
            if (input.id == null && (input.pokemonId == 0)) {
                throw new IllegalArgumentException("The incoming pokemon must have a pokemon id");
            }
            if (input.id == null && (input.pokemonName == null || input.pokemonName.isBlank())) {
                throw new IllegalArgumentException("The incoming pokemon must have a pokemon name");
            }
        }
    }

    //Recalculates the pokemons positions
    private void recalculatePositions() {
        for (int i = 0; i < pokemons.size(); i++) {
            pokemons.get(i).setPosition(i);
        }
    }

    public Team() {}

    public Team (String name, Trainer trainer){
        this.name = name;
        this.trainer = trainer;
    }

    public Team (String name, Trainer trainer, List<Pokemon> pokemons){
        this.name = name;
        this.trainer = trainer;
        this.pokemons = pokemons;
    }
}