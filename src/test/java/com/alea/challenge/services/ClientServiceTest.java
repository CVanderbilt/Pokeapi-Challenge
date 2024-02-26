package com.alea.challenge.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

import com.alea.challenge.Samples;
import com.alea.challenge.model.Pokemon;
import com.google.gson.Gson;

import reactor.core.publisher.Mono;

@SpringBootTest
public class ClientServiceTest {

    @Mock
    private WebClient webClient;

    private Gson gson = new Gson();

    @Mock
    private CacheManager cacheManager;

    private ClientService toTest;

    @BeforeEach
    public void setUp() {
        toTest = new ClientService(webClient, gson, cacheManager);
    }

    @Test
    public void getHeaviestPokemons_cached() {
        Cache.ValueWrapper entry = Mockito.mock(Cache.ValueWrapper.class);
        Cache cache = Mockito.mock(Cache.class);
        Mockito.when(cacheManager.getCache(anyString())).thenReturn(cache);
        Mockito.when(cache.get(anyString())).thenReturn(entry);
        Mockito.when(entry.get()).thenReturn(Samples.buildTreeSet(Pokemon.weightComparator, Samples.LIST_OF_HEAVY_POKEMONS));

        String result = toTest.getHeaviestPokemons();

        String expectedResult = Samples.WEIGHT_MESSAGE;
        assertEquals(expectedResult, result);
    }

    @Test
    public void getHighestPokemons_cached() {
        Cache.ValueWrapper entry = Mockito.mock(Cache.ValueWrapper.class);
        Cache cache = Mockito.mock(Cache.class);
        Mockito.when(cacheManager.getCache(anyString())).thenReturn(cache);
        Mockito.when(cache.get(anyString())).thenReturn(entry);
        Mockito.when(entry.get()).thenReturn(Samples.buildTreeSet(Pokemon.heightComparator, Samples.LIST_OF_HEIGHT_POKEMONS));

        String result = toTest.getHighestPokemons();

        String expectedResult = Samples.HEIGHT_MESSAGE;
        assertEquals(expectedResult, result);
    }

    @Test
    public void getMoreExperiencedPokemons_cached() {
        Cache.ValueWrapper entry = Mockito.mock(Cache.ValueWrapper.class);
        Cache cache = Mockito.mock(Cache.class);
        Mockito.when(cacheManager.getCache(anyString())).thenReturn(cache);
        Mockito.when(cache.get(anyString())).thenReturn(entry);
        Mockito.when(entry.get()).thenReturn(Samples.buildTreeSet(Pokemon.experienceComparator, Samples.LIST_OF_EXP_POKEMONS));

        String result = toTest.getMoreExperiencedPokemons();

        String expectedResult = Samples.EXP_MESSAGE;
        assertEquals(expectedResult, result);
    }

    @Test
    public void getHeaviestPokemons_noCache() {
        Gson gson = new Gson();
        Cache cache = Mockito.mock(Cache.class);
        Mockito.when(cacheManager.getCache(anyString())).thenReturn(cache);
        Mockito.when(cache.get(anyString())).thenReturn(null);

        RequestHeadersUriSpec requestHeadersUriSpecMock = Mockito.mock(RequestHeadersUriSpec.class);
        RequestHeadersSpec requestHeadersSpecMock = Mockito.mock(RequestHeadersSpec.class);
        ResponseSpec responseSpecMock = Mockito.mock(ResponseSpec.class);

        Mockito.when(webClient.get()).thenReturn(requestHeadersUriSpecMock);
        Mockito.when(requestHeadersUriSpecMock.uri("/pokemon/?limit=20")).thenReturn(requestHeadersSpecMock);
        Mockito.when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
        Mockito.when(responseSpecMock.bodyToMono(String.class)).thenReturn(Mono.just(Samples.buildPokeApiPokemonListString()));
        //Mockito.when(responseSpecMock.bodyToMono(byte[].class)).thenReturn(Mono.just(gson.toJson(Samples.POKEMON10).getBytes()));
        
        // Individual pokemon requests mocks
        int amount = Samples.LIST_OF_POKEMONS.size();
        List<RequestHeadersSpec> requestHeadersSpecMocks = new ArrayList<>();//(Collections.nCopies(amount, Mockito.mock(RequestHeadersSpec.class)));
        List<ResponseSpec> responseSpecMocks = new ArrayList<>();//(Collections.nCopies(amount, Mockito.mock(ResponseSpec.class)));

        for (int i = 0; i < amount; i++) {
            Pokemon pokemon = Samples.LIST_OF_POKEMONS.get(i);
            RequestHeadersSpec requestHeadersSpecMockPokemon1 = Mockito.mock(RequestHeadersSpec.class);
            ResponseSpec responseSpecMockPokemon1 = Mockito.mock(ResponseSpec.class);
            
            requestHeadersSpecMocks.add(requestHeadersSpecMockPokemon1);
            responseSpecMocks.add(responseSpecMockPokemon1);
            
            Mockito.when(requestHeadersUriSpecMock.uri(pokemon.getUrl())).thenReturn(requestHeadersSpecMocks.get(i));
            Mockito.when(requestHeadersSpecMocks.get(i).retrieve()).thenReturn(responseSpecMocks.get(i));
            Mockito.when(responseSpecMocks.get(i).bodyToMono(byte[].class)).thenReturn(Mono.just(gson.toJson(pokemon).getBytes()));
        }
        
        String result = toTest.getHeaviestPokemons();

        assertEquals(Samples.WEIGHT_MESSAGE, result);
    }

    @Test
    public void getHighestPokemons_noCache() {
        Gson gson = new Gson();
        Cache cache = Mockito.mock(Cache.class);
        Mockito.when(cacheManager.getCache(anyString())).thenReturn(cache);
        Mockito.when(cache.get(anyString())).thenReturn(null);

        RequestHeadersUriSpec requestHeadersUriSpecMock = Mockito.mock(RequestHeadersUriSpec.class);
        RequestHeadersSpec requestHeadersSpecMock = Mockito.mock(RequestHeadersSpec.class);
        ResponseSpec responseSpecMock = Mockito.mock(ResponseSpec.class);

        Mockito.when(webClient.get()).thenReturn(requestHeadersUriSpecMock);
        Mockito.when(requestHeadersUriSpecMock.uri("/pokemon/?limit=20")).thenReturn(requestHeadersSpecMock);
        Mockito.when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
        Mockito.when(responseSpecMock.bodyToMono(String.class)).thenReturn(Mono.just(Samples.buildPokeApiPokemonListString()));
        //Mockito.when(responseSpecMock.bodyToMono(byte[].class)).thenReturn(Mono.just(gson.toJson(Samples.POKEMON10).getBytes()));
        
        // Individual pokemon requests mocks
        int amount = Samples.LIST_OF_POKEMONS.size();
        List<RequestHeadersSpec> requestHeadersSpecMocks = new ArrayList<>();//(Collections.nCopies(amount, Mockito.mock(RequestHeadersSpec.class)));
        List<ResponseSpec> responseSpecMocks = new ArrayList<>();//(Collections.nCopies(amount, Mockito.mock(ResponseSpec.class)));

        for (int i = 0; i < amount; i++) {
            Pokemon pokemon = Samples.LIST_OF_POKEMONS.get(i);
            RequestHeadersSpec requestHeadersSpecMockPokemon1 = Mockito.mock(RequestHeadersSpec.class);
            ResponseSpec responseSpecMockPokemon1 = Mockito.mock(ResponseSpec.class);
            
            requestHeadersSpecMocks.add(requestHeadersSpecMockPokemon1);
            responseSpecMocks.add(responseSpecMockPokemon1);
            
            Mockito.when(requestHeadersUriSpecMock.uri(pokemon.getUrl())).thenReturn(requestHeadersSpecMocks.get(i));
            Mockito.when(requestHeadersSpecMocks.get(i).retrieve()).thenReturn(responseSpecMocks.get(i));
            Mockito.when(responseSpecMocks.get(i).bodyToMono(byte[].class)).thenReturn(Mono.just(gson.toJson(pokemon).getBytes()));
        }
        
        String result = toTest.getHighestPokemons();

        assertEquals(Samples.HEIGHT_MESSAGE, result);
    }

    @Test
    public void getMoreExperienced_noCache() {
        Gson gson = new Gson();
        Cache cache = Mockito.mock(Cache.class);
        Mockito.when(cacheManager.getCache(anyString())).thenReturn(cache);
        Mockito.when(cache.get(anyString())).thenReturn(null);

        RequestHeadersUriSpec requestHeadersUriSpecMock = Mockito.mock(RequestHeadersUriSpec.class);
        RequestHeadersSpec requestHeadersSpecMock = Mockito.mock(RequestHeadersSpec.class);
        ResponseSpec responseSpecMock = Mockito.mock(ResponseSpec.class);

        Mockito.when(webClient.get()).thenReturn(requestHeadersUriSpecMock);
        Mockito.when(requestHeadersUriSpecMock.uri("/pokemon/?limit=20")).thenReturn(requestHeadersSpecMock);
        Mockito.when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
        Mockito.when(responseSpecMock.bodyToMono(String.class)).thenReturn(Mono.just(Samples.buildPokeApiPokemonListString()));
        //Mockito.when(responseSpecMock.bodyToMono(byte[].class)).thenReturn(Mono.just(gson.toJson(Samples.POKEMON10).getBytes()));
        
        // Individual pokemon requests mocks
        int amount = Samples.LIST_OF_POKEMONS.size();
        List<RequestHeadersSpec> requestHeadersSpecMocks = new ArrayList<>();//(Collections.nCopies(amount, Mockito.mock(RequestHeadersSpec.class)));
        List<ResponseSpec> responseSpecMocks = new ArrayList<>();//(Collections.nCopies(amount, Mockito.mock(ResponseSpec.class)));

        for (int i = 0; i < amount; i++) {
            Pokemon pokemon = Samples.LIST_OF_POKEMONS.get(i);
            RequestHeadersSpec requestHeadersSpecMockPokemon1 = Mockito.mock(RequestHeadersSpec.class);
            ResponseSpec responseSpecMockPokemon1 = Mockito.mock(ResponseSpec.class);
            
            requestHeadersSpecMocks.add(requestHeadersSpecMockPokemon1);
            responseSpecMocks.add(responseSpecMockPokemon1);
            
            Mockito.when(requestHeadersUriSpecMock.uri(pokemon.getUrl())).thenReturn(requestHeadersSpecMocks.get(i));
            Mockito.when(requestHeadersSpecMocks.get(i).retrieve()).thenReturn(responseSpecMocks.get(i));
            Mockito.when(responseSpecMocks.get(i).bodyToMono(byte[].class)).thenReturn(Mono.just(gson.toJson(pokemon).getBytes()));
        }
        
        String result = toTest.getMoreExperiencedPokemons();

        assertEquals(Samples.EXP_MESSAGE, result);
    }
}
