package com.alea.challenge.services;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final WebClient webClient;
    private final Gson gson;

    @Autowired
    public ClientService(WebClient webClient, Gson gson) {
        this.webClient = webClient;
        this.gson = gson;
    }

    public String getHeaviestPokemons() {
        List<Pokemon> names = getAllPokemons();
        return "heaviest";
    }

    public String getHighestPokemons() {
        return "highest";
    }

    public String getMoreExperiencedPokemons() {
        return "moreExperienced";
    }

    public List<Pokemon> getAllPokemons() {
        List<String> names = getAllPokemonNames();
        List<Pokemon> pokemons = new ArrayList<>();
    
        for (String name : names) {
            String url = "/pokemon/" + name;
            
            Mono<byte[]> responseBody = webClient
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(byte[].class);

            String jsonResponse = responseBody.map(String::new).block();
    
            try {
                Pokemon pokemon = gson.fromJson(jsonResponse, Pokemon.class);
    
                pokemons.add(pokemon);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    
        return pokemons;
    }

    public List<String> getAllPokemonNames() {
        String jsonResponse = webClient
                .get().uri("/pokemon-species/?limit=-1")
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
                        if (element.isJsonObject() && element.getAsJsonObject().has("name")) {
                            pokemonNames.add(element.getAsJsonObject().get("name").getAsString());
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