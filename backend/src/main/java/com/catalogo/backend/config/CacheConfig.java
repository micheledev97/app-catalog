package com.catalogo.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;

/**
 * Configurazione della cache applicativa basata su Redis.
 *
 * Espone un {@link CacheManager} che usa Redis come store,
 * così da far funzionare le annotazioni di Spring Cache
 * (@Cacheable, @CachePut, @CacheEvict) sull'intera applicazione.
 */
@Configuration
public class CacheConfig {

    /**
     * Tempo di vita (TTL) predefinito delle voci di cache, in secondi.
     * Valore letto da "app.cache.ttl-seconds" (application.yml/properties).
     * Se la proprietà non è definita, usa il default 120 secondi.
     */
    @Value("${app.cache.ttl-seconds:120}")
    private long ttl;

    /**
     * Crea e registra un {@link CacheManager} che salva le cache in Redis.
     *
     * @param factory la {@link RedisConnectionFactory} che incapsula i dettagli
     *                della connessione a Redis (host, porta, password, ecc.)
     * @return un {@link RedisCacheManager} configurato con il TTL predefinito.
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        // Configurazione di base per le cache Redis:
        // - imposta un TTL uniforme per tutte le entry (chiavi) di cache
        //   create tramite le annotazioni di Spring Cache.
        RedisCacheConfiguration config = RedisCacheConfiguration
                .defaultCacheConfig()
                // Ogni elemento inserito in cache scadrà dopo 'ttl' secondi
                .entryTtl(Duration.ofSeconds(ttl));

        // Costruisce un RedisCacheManager usando la connection factory
        // e applicando la configurazione di default definita sopra.
        // Questo manager verrà usato automaticamente da Spring
        // quando incontra annotazioni come @Cacheable.
        return RedisCacheManager
                .builder(factory)
                .cacheDefaults(config)
                .build();
    }
}
