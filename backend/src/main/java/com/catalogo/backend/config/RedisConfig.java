package com.catalogo.backend.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory cf) {
        RedisTemplate<String, Object> t = new RedisTemplate<>();
        t.setConnectionFactory(cf);
        // JSON per valori
        var valueSer = new Jackson2JsonRedisSerializer<>(Object.class);
        t.setValueSerializer(valueSer);
        t.setHashValueSerializer(valueSer);
        // String per chiavi
        var keySer = new StringRedisSerializer();
        t.setKeySerializer(keySer);
        t.setHashKeySerializer(keySer);
        t.afterPropertiesSet();
        return t;
    }
}
