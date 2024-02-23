package com.alea.challenge.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.alea.challenge.model.Pokemon;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import reactor.core.publisher.Mono;

@Service
public class ClientService {
    private static final String CATEGORY_WEIGHT = "weight";
    private static final String CATEGORY_HEIGHT = "height";
    private static final String CATEGORY_BASE_EXP = "base_experience";
    private static String MESSAGE_TEMPLATE = "The top pokemons with highest value in %s are:\n%s";
    
    private final WebClient webClient;
    private final Gson gson;
    private final CacheManager cacheManager;

    @Autowired
    public ClientService(WebClient webClient, Gson gson, CacheManager cacheManager) {
        this.webClient = webClient;
        this.gson = gson;
        this.cacheManager = cacheManager;
    }

    public String getHeaviestPokemons() {
        try {
            return BuildResponseString(CATEGORY_WEIGHT, getFilteredPokemon(CATEGORY_WEIGHT));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "error: " + e.getMessage();
        }
    }

    public String getHighestPokemons() {
        try {
            return BuildResponseString(CATEGORY_HEIGHT, getFilteredPokemon(CATEGORY_HEIGHT));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "error";

        }
    }

    public String getMoreExperiencedPokemons() {
        try {
            return BuildResponseString(CATEGORY_BASE_EXP, getFilteredPokemon(CATEGORY_BASE_EXP));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "error";
        }
    }

    private String BuildResponseString(String category, List<Pokemon> pokemons) {
        String pokemonDetails = pokemons.stream()
            .map(pokemon -> {
                String additionalInfo;
                switch (category) {
                    case "base_experience":
                        additionalInfo = ": " + pokemon.getBase_experience();
                        break;
                    case "height":
                        additionalInfo = ": " + pokemon.getHeight();
                        break;
                    case "weight":
                        additionalInfo = ": " + pokemon.getWeight();
                        break;
                    default:
                        additionalInfo = "";
                }
                return pokemon.getName() + additionalInfo;
            })
            .collect(Collectors.joining("\n"));

        return String.format(MESSAGE_TEMPLATE, category, pokemonDetails);
    }

    private List<Pokemon> getFilteredPokemon(String category) throws Exception {
        Cache cache = cacheManager.getCache("filteredPokemon");
        Cache.ValueWrapper valueWrapper = cache.get(category);

        if (valueWrapper != null) {
            return (List<Pokemon>) valueWrapper.get();
        } else {
            List<Pokemon> pokemons = getAllPokemons();
            List<Pokemon> topWeight = new ArrayList<Pokemon>();
            List<Pokemon> topHeight = new ArrayList<Pokemon>();
            List<Pokemon> topExperience = new ArrayList<Pokemon>();

            for (Pokemon pokemon : pokemons) {
                updateTopList(topHeight, pokemon, Pokemon.heightComparator);
                updateTopList(topWeight, pokemon, Pokemon.weightComparator);
                updateTopList(topExperience, pokemon, Pokemon.experienceComparator);
            }

            cache.put(CATEGORY_BASE_EXP, topExperience);
            cache.put(CATEGORY_HEIGHT, topHeight);
            cache.put(CATEGORY_WEIGHT, topWeight);

            switch (category) {
                case CATEGORY_BASE_EXP: return topExperience;
                case CATEGORY_HEIGHT: return topHeight;
                case CATEGORY_WEIGHT: return topWeight;
                default: throw new Exception("Invalid category");
            }
        }
    }

    private void updateTopList(List<Pokemon> topList, Pokemon pokemon, Comparator<Pokemon> comparator) {
        if (topList.size() < 5) {
            topList.add(pokemon);
        } else {
            System.out.println("Comparing: " + pokemon.buildStatsString() + " with " + topList.get(4).buildStatsString() + " => " + comparator.compare(pokemon, topList.get(4)));
            if (comparator.compare(pokemon, topList.get(4)) > 0) {
                int insertIndex = 0;
                while (insertIndex < 5 && comparator.compare(pokemon, topList.get(insertIndex)) > 0) {
                    insertIndex++;
                }
                topList.add(insertIndex, pokemon);
                topList.remove(5);
            }
        }
    }

    private List<Pokemon> getAllPokemons() {
        List<String> names = getAllPokemonURLs();
        List<Pokemon> pokemons = new ArrayList<>();
        
        System.out.println("Fetching all pokemons, this may take a while");
        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i);
            double progress = ((double) i / names.size() * 100);
            String formattedProgress = String.format("%.2f", progress);
            System.out.print("\rProgress: " + formattedProgress + "%");
            
            Mono<byte[]> responseBody = webClient
                .get()
                .uri(name)
                .retrieve()
                .bodyToMono(byte[].class);

            String jsonResponse = responseBody.map(String::new).block();
    
            try {
                Pokemon pokemon = gson.fromJson(jsonResponse, Pokemon.class);
                pokemons.add(pokemon);
                //String jsonOutput = gson.toJson(pokemon);
                //System.out.println("\njson -> ======\n" + jsonResponse + "\n=========\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("\rProgress: 100%");
    
        return pokemons;
    }

    private List<String> getAllPokemonURLs() {
        String jsonResponse = webClient
                .get().uri("/pokemon/?limit=20")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        List<String> pokemonNames = new ArrayList<>();

        try {
            JsonElement jsonElement = gson.fromJson(jsonResponse, JsonElement.class);

            if (jsonElement != null && jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                if (jsonObject.has("results") && jsonObject.get("results").isJsonArray()) {
                    JsonArray resultsArray = jsonObject.getAsJsonArray("results");
                    for (JsonElement element : resultsArray) {
                        if (element.isJsonObject() && element.getAsJsonObject().has("url")) {
                            pokemonNames.add(element.getAsJsonObject().get("url").getAsString());
                        }
                    }
                } else {
                    System.err.println("Unexpected JSON structure");
                }
            } else {
                System.err.println("Unexpected JSON structure");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pokemonNames;
    }
}