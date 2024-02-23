package com.alea.challenge.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.web.reactive.function.client.WebClient;
import com.alea.challenge.Samples;

import com.google.gson.Gson;

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
    public void getHeaviestPokemons_happyPath() {
        //Cache.Entry <Object, Object> entry = Mockito.mock(Cache.Entry.class);
        Cache.ValueWrapper entry = Mockito.mock(Cache.ValueWrapper.class);
        Cache cache = Mockito.mock(Cache.class);
        Mockito.when(cacheManager.getCache(anyString())).thenReturn(cache);
        Mockito.when(cache.get(anyString())).thenReturn(entry);
        Mockito.when(entry.get()).thenReturn(Samples.LIST_OF_POKEMONS);

        String result = toTest.getHeaviestPokemons();

        String expectedResult = Samples.WEIGHT_MESSAGE; // Replace with your expected result
        assertEquals(expectedResult, result);
    }
}
