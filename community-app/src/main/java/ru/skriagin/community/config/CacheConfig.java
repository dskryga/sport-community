package ru.skriagin.community.config;

import org.locationtech.jts.geom.Point;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import tools.jackson.databind.module.SimpleModule;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisValueSerializer()));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(cacheConfiguration)
                .build();
    }

    private RedisSerializer<Object> redisValueSerializer() {
        SimpleModule pointModule = new SimpleModule();
        pointModule.addSerializer(Point.class, new PointJsonSerializer());
        pointModule.addDeserializer(Point.class, new PointJsonDeserializer());

        return GenericJacksonJsonRedisSerializer.builder()
                .enableDefaultTyping(BasicPolymorphicTypeValidator.builder().allowIfBaseType(Object.class).build())
                .customize(mapperBuilder -> mapperBuilder
                        .findAndAddModules()
                        .addModule(pointModule)
                        // User (cached as-is for the auth lookup) implements UserDetails, whose derived getters
                        // (getAuthorities/isAccountNonExpired/...) look like bean properties to Jackson but have
                        // no matching field/setter - ignore them on read instead of failing to deserialize.
                        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES))
                .build();
    }
}
