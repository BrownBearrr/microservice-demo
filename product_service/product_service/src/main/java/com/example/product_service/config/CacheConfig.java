package com.example.product_service.config;


import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager ;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {
    @Bean
    public CacheManager cacheManager () {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager() ;
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(20, TimeUnit.SECONDS) // sau 20s kể từ lần ghi cache cuối cùng, cache sẽ bị xóa
                .maximumSize(1000) // giới hạn số lượng cache tối đa là 1000, nếu vượt quá sẽ xóa cache cũ nhất
        ) ;
        return cacheManager ;
    }

    @Bean
    @Primary
    public CacheManager redisCacheManager (RedisConnectionFactory connectionFactory) {

        var config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(60)) // sau 20s kể từ lần ghi cache cuối cùng, cache sẽ bị xóa
                .disableCachingNullValues()
                ; // không cache giá trị null

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .build()
                ;
    }
}
