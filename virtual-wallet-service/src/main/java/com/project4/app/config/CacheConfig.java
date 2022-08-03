package com.project4.app.config;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.project4.app.entity.VirtualWallet;

@Configuration
@ConditionalOnProperty(name = "cache.enabled", havingValue = "true")
public class CacheConfig extends CachingConfigurerSupport {
    public static final String WALLET_CACHE = "wallet-cache";

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>();
        redisCacheConfigurationMap.put(WALLET_CACHE, createConfig(1, ChronoUnit.MINUTES));

        return RedisCacheManager
                .builder(redisConnectionFactory)
                .withInitialCacheConfigurations(redisCacheConfigurationMap)
                .build();
    }

    private static RedisCacheConfiguration createConfig(long time, ChronoUnit temporalUnit) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.of(time, temporalUnit));
    }
    
    @Bean
    public ReactiveHashOperations<String, String, VirtualWallet> hashOperations(ReactiveRedisConnectionFactory redisConnectionFactory){
        var template = new ReactiveRedisTemplate<>(
                redisConnectionFactory,
                RedisSerializationContext.<String, VirtualWallet>newSerializationContext(new StringRedisSerializer())
                                         .hashKey(new GenericToStringSerializer<>(String.class))
                                         .hashValue(new Jackson2JsonRedisSerializer<>(VirtualWallet.class))
                                         .build()
        );
        return template.opsForHash();
    }
}
