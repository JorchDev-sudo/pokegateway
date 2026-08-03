package com.jorchdev.poketeams.pokegateway.entities;

import com.jorchdev.poketeams.pokegateway.entities.internal.inputs.PokemonPositionInput;
import com.jorchdev.poketeams.pokegateway.exceptions.team.TeamException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    public void addPokemon (int pokemonId, String pokemonName) {
        if (pokemons.size() >= 6)
            throw new TeamException("Maximum 6 pokemons");

        pokemons.add(
                new Pokemon(
                        this,
                        pokemonId,
                        pokemonName,
                        pokemons.size()
                )
        );
    }

    public void removePokemon (UUID id) {
        pokemons.removeIf(tp -> tp.getId().equals(id));
        recalculatePositions();
    }

    public void movePokemons (List<PokemonPositionInput> newPositions) {
        validateNewPositions(newPositions);

        Map<UUID, Integer> desiredPositionById = newPositions.stream()
                .collect(Collectors.toMap(PokemonPositionInput::id, PokemonPositionInput::position));

        pokemons.sort(Comparator.comparingInt(tp -> desiredPositionById.get(tp.getId())));

        recalculatePositions();
    }

    private void validateNewPositions (List<PokemonPositionInput> newPositions) {
        if (newPositions.size() != pokemons.size()) {
            throw new TeamException("You must indicate the positions for all the pokemons in the team");
        }

        Set<UUID> currentIds = pokemons.stream()
                .map(Pokemon::getId)
                .collect(Collectors.toSet());

        Set<UUID> incomingIds = newPositions.stream()
                .map(PokemonPositionInput::id)
                .collect(Collectors.toSet());

        if (!currentIds.equals(incomingIds)) {
            newPositions.forEach(tp -> {
                log.error(
                        "Pokemon position inputs: {} {}",
                        tp.id(), tp.position()
                );
            });

            throw new TeamException("The positions must refer exactly to the Pokemon currently on the team, without duplicates.");
        }

        Set<Integer> incomingPositions = newPositions.stream()
                .map(PokemonPositionInput::position)
                .collect(Collectors.toSet());

        Set<Integer> expectedRange = IntStream.range(0, pokemons.size())
                .boxed()
                .collect(Collectors.toSet());

        if (!incomingPositions.equals(expectedRange)) {
            throw new TeamException("The positions must be a contiguous range from 0 to N-1, with no duplicates or gaps.");
        }
    }

    public void recalculatePositions () {
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