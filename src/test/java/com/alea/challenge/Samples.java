package com.alea.challenge;

import java.util.List;

import com.alea.challenge.model.Pokemon;

public class Samples {
    public static final String POKEMON_URL = "https://pokeapi.co/api/v2/pokemon/2/";
    
    public static final Pokemon POKEMON1 = new Pokemon(POKEMON_URL, "pokemon1", 1, 5, 10);
    public static final Pokemon POKEMON2 = new Pokemon(POKEMON_URL, "pokemon2", 2, 6, 9);
    public static final Pokemon POKEMON3 = new Pokemon(POKEMON_URL, "pokemon3", 3, 7, 8);
    public static final Pokemon POKEMON4 = new Pokemon(POKEMON_URL, "pokemon4", 4, 8, 7);
    public static final Pokemon POKEMON5 = new Pokemon(POKEMON_URL, "pokemon5", 5, 9, 6);
    public static final Pokemon POKEMON6 = new Pokemon(POKEMON_URL, "pokemon6", 6, 10, 5);
    public static final Pokemon POKEMON7 = new Pokemon(POKEMON_URL, "pokemon7", 7, 1, 4);
    public static final Pokemon POKEMON8 = new Pokemon(POKEMON_URL, "pokemon8", 8, 2, 3);
    public static final Pokemon POKEMON9 = new Pokemon(POKEMON_URL, "pokemon9", 8, 3, 2);
    public static final Pokemon POKEMON10 = new Pokemon(POKEMON_URL, "pokemon10", 10, 4, 1);
    //List<Pokemon> pokemonList;
    public static final List<Pokemon> LIST_OF_POKEMONS =
        List.of(POKEMON1, POKEMON2, POKEMON3, POKEMON4, POKEMON5, POKEMON6, POKEMON7, POKEMON8, POKEMON9, POKEMON10);
    
    public static final String CATEGORY_WEIGHT = "weight";
    public static final String CATEGORY_HEIGHT = "height";
    public static final String CATEGORY_BASE_EXP = "base_experience";
    
    private static String MESSAGE_TEMPLATE = "The top pokemons with highest value in %s are:\n"
    + "%s: %s\n"
    + "%s: %s\n"
    + "%s: %s\n"
    + "%s: %s\n"
    + "%s: %s\n";

    public static final String WEIGHT_MESSAGE = String.format(MESSAGE_TEMPLATE, CATEGORY_WEIGHT,
    POKEMON1.getName(), POKEMON1.getWeight(),
    POKEMON2.getName(), POKEMON2.getWeight(),
    POKEMON3.getName(), POKEMON3.getWeight(),
    POKEMON4.getName(), POKEMON4.getWeight(),
    POKEMON5.getName(), POKEMON5.getWeight());
/*

The top pokemons with highest value in weight are:
pokemon1: 10
pokemon2: 9
pokemon3: 8
pokemon4: 7
pokemon5: 6
pokemon6: 5
pokemon7: 4
pokemon8: 3
pokemon9: 2
pokemon10: 1


 */
}
