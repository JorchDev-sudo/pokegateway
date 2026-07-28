package com.jorchdev.poketeams.pokegateway.entities;

import com.jorchdev.poketeams.pokegateway.entities.internal.inputs.PokemonPositionInput;
import com.jorchdev.poketeams.pokegateway.exceptions.team.TeamException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Entity
@Getter
public class Team {

    @Id
    @GeneratedValue
    private UUID id;

    @Setter
    private String name;

    @Setter
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    @OneToMany(
            mappedBy = "team",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("position ASC")
    private final List<TeamPokemon> pokemons = new ArrayList<>();

    public void addPokemon(int pokemonId, String pokemonName) {
        if (pokemons.size() >= 6)
            throw new TeamException("Maximum 6 pokemons");

        pokemons.add(
                new TeamPokemon(
                        this,
                        pokemonId,
                        pokemonName,
                        pokemons.size()
                )
        );
    }

    public void removePokemon(UUID id) {
        pokemons.removeIf(tp -> tp.getId().equals(id));
        recalculatePositions();
    }

    public void reorderPokemons(List<PokemonPositionInput> newPositions) {
        validateNewPositions(newPositions);

        Map<Integer, Integer> desiredPositionByPokemonId = newPositions.stream()
                .collect(Collectors.toMap(PokemonPositionInput::pokemonId, PokemonPositionInput::position));

        pokemons.sort(Comparator.comparingInt(tp -> desiredPositionByPokemonId.get(tp.getPokemonId())));

        recalculatePositions();
    }

    private void validateNewPositions(List<PokemonPositionInput> newPositions) {
        if (newPositions.size() != pokemons.size()) {
            throw new TeamException("You must indicate the positions for all the pokemons in the team");
        }

        Set<Integer> currentIds = pokemons.stream()
                .map(TeamPokemon::getPokemonId)
                .collect(Collectors.toSet());

        Set<Integer> incomingIds = newPositions.stream()
                .map(PokemonPositionInput::pokemonId)
                .collect(Collectors.toSet());

        if (!currentIds.equals(incomingIds)) {
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

    public void recalculatePositions() {
        for (int i = 0; i < pokemons.size(); i++) {
            pokemons.get(i).setPosition(i);
        }
    }
}