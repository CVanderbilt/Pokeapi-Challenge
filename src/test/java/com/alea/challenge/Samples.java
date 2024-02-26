package com.alea.challenge;

import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import com.alea.challenge.model.Pokemon;

public class Samples {
    public static final String POKEMON_URL1 = "https://pokeapi.co/api/v2/pokemon/1/";
    public static final String POKEMON_URL2 = "https://pokeapi.co/api/v2/pokemon/2/";
    public static final String POKEMON_URL3 = "https://pokeapi.co/api/v2/pokemon/3/";
    public static final String POKEMON_URL4 = "https://pokeapi.co/api/v2/pokemon/4/";
    public static final String POKEMON_URL5 = "https://pokeapi.co/api/v2/pokemon/5/";
    public static final String POKEMON_URL6 = "https://pokeapi.co/api/v2/pokemon/6/";
    public static final String POKEMON_URL7 = "https://pokeapi.co/api/v2/pokemon/7/";
    public static final String POKEMON_URL8 = "https://pokeapi.co/api/v2/pokemon/8/";
    public static final String POKEMON_URL9 = "https://pokeapi.co/api/v2/pokemon/9/";
    public static final String POKEMON_URL10 = "https://pokeapi.co/api/v2/pokemon/10/";

    
    public static final Pokemon POKEMON1 = new Pokemon(POKEMON_URL1, "pokemon1", 1, 5, 10);
    public static final Pokemon POKEMON2 = new Pokemon(POKEMON_URL2, "pokemon2", 2, 6, 9);
    public static final Pokemon POKEMON3 = new Pokemon(POKEMON_URL3, "pokemon3", 3, 7, 8);
    public static final Pokemon POKEMON4 = new Pokemon(POKEMON_URL4, "pokemon4", 4, 8, 7);
    public static final Pokemon POKEMON5 = new Pokemon(POKEMON_URL5, "pokemon5", 5, 9, 6);
    public static final Pokemon POKEMON6 = new Pokemon(POKEMON_URL6, "pokemon6", 6, 10, 5);
    public static final Pokemon POKEMON7 = new Pokemon(POKEMON_URL7, "pokemon7", 7, 1, 4);
    public static final Pokemon POKEMON8 = new Pokemon(POKEMON_URL8, "pokemon8", 8, 2, 3);
    public static final Pokemon POKEMON9 = new Pokemon(POKEMON_URL9, "pokemon9", 8, 3, 2);
    public static final Pokemon POKEMON10 = new Pokemon(POKEMON_URL10, "pokemon10", 10, 4, 1);
    //List<Pokemon> pokemonList;
    public static final List<Pokemon> LIST_OF_POKEMONS =
        List.of(POKEMON1, POKEMON2, POKEMON3, POKEMON4, POKEMON5, POKEMON6, POKEMON7, POKEMON8, POKEMON9, POKEMON10);
    public static final List<Pokemon> LIST_OF_HEAVY_POKEMONS = List.of(POKEMON1, POKEMON2, POKEMON3, POKEMON4, POKEMON5);
    public static final List<Pokemon> LIST_OF_HEIGHT_POKEMONS = List.of(POKEMON6, POKEMON5, POKEMON4, POKEMON3, POKEMON2);
    public static final List<Pokemon> LIST_OF_EXP_POKEMONS = List.of(POKEMON10, POKEMON9, POKEMON8, POKEMON7, POKEMON6);

    public static TreeSet<Pokemon> buildTreeSet(Comparator<Pokemon> comparator, List<Pokemon> pokemonList) {
        TreeSet<Pokemon> treeSet = new TreeSet<>(comparator);
        treeSet.addAll(pokemonList);
        return treeSet;
    }
    
    public static final String CATEGORY_WEIGHT = "weight";
    public static final String CATEGORY_HEIGHT = "height";
    public static final String CATEGORY_BASE_EXP = "base_experience";
    
    public static String MESSAGE_TEMPLATE = "The top pokemons with highest value in %s are:\n"
        + "%s: %s\n"
        + "%s: %s\n"
        + "%s: %s\n"
        + "%s: %s\n"
        + "%s: %s";

    public static final String WEIGHT_MESSAGE = String.format(MESSAGE_TEMPLATE, CATEGORY_WEIGHT,
        POKEMON1.getName(), POKEMON1.getWeight(),
        POKEMON2.getName(), POKEMON2.getWeight(),
        POKEMON3.getName(), POKEMON3.getWeight(),
        POKEMON4.getName(), POKEMON4.getWeight(),
        POKEMON5.getName(), POKEMON5.getWeight());

    public static final String HEIGHT_MESSAGE = String.format(MESSAGE_TEMPLATE, CATEGORY_HEIGHT,
        POKEMON6.getName(), POKEMON6.getHeight(),
        POKEMON5.getName(), POKEMON5.getHeight(),
        POKEMON4.getName(), POKEMON4.getHeight(),
        POKEMON3.getName(), POKEMON3.getHeight(),
        POKEMON2.getName(), POKEMON2.getHeight());

    public static final String EXP_MESSAGE = String.format(MESSAGE_TEMPLATE, CATEGORY_BASE_EXP,
        POKEMON10.getName(), POKEMON10.getBase_experience(),
        POKEMON9.getName(), POKEMON9.getBase_experience(),
        POKEMON8.getName(), POKEMON8.getBase_experience(),
        POKEMON7.getName(), POKEMON7.getBase_experience(),
        POKEMON6.getName(), POKEMON6.getBase_experience());

    //public static final String POKEAPI_POKEMON_LIST_JSON = String.format("{\"count\":1302,\"next\":\"https://pokeapi.co/api/v2/pokemon/?offset=20&limit=20\",\"previous\":null,\"results\":[{\"name\":\"bulbasaur\",\"url\":\"%s\"},{\"name\":\"ivysaur\",\"url\":\"%s\"},{\"name\":\"venusaur\",\"url\":\"%s\"},{\"name\":\"charmander\",\"url\":\"%s\"},{\"name\":\"charmeleon\",\"url\":\"%s\"}, {\"name\":\"charmeleon\",\"url\":\"%s\"}]}",

    public static String buildPokeApiPokemonListString() {
        String template = "{\"count\":1302,\"next\":\"https://pokeapi.co/api/v2/pokemon/?offset=20&limit=20\",\"previous\":null,\"results\":[%s]}";
        String listElementTemplate = "{\"name\":\"%s\",\"url\":\"%s\"}";
        String listStr = "";
        for (int i = 0; i < LIST_OF_POKEMONS.size(); i++) {
            Pokemon p = LIST_OF_POKEMONS.get(i);
            String listElement = String.format(listElementTemplate, p.getName(), p.getUrl());
            if (i < LIST_OF_POKEMONS.size() - 1) {
                listElement += ",";
            }
            listStr += listElement;
        }
        return String.format(template, listStr);
    }
}
