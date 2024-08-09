//package com.ghtk.ecommercewebsite.configs;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.cache.RedisCacheConfiguration;
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.RedisSerializationContext;
//import org.springframework.data.redis.serializer.RedisSerializer;
//
//import java.time.Duration;
//
///**
// * Redis configuration
// * JacksonConfig, PageDeserializer, PageSerializer
// * PageJsonDeserializer, PageJsonSerializer
// * UserSerializer
// * were removed
// */
//@Configuration
//@RequiredArgsConstructor
//public class RedisConfig {
//
//    @Bean
//    public LettuceConnectionFactory redisConnectionFactory() {
//        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration("localhost", 6379);
//
//        return new LettuceConnectionFactory(configuration);
//    }
//
//    @Bean
//    public RedisCacheManager cacheManager() {
//        RedisCacheConfiguration cacheConfig = myDefaultCacheConfig(Duration.ofMinutes(10)).disableCachingNullValues();
//
//        return RedisCacheManager.builder(redisConnectionFactory())
//                .cacheDefaults(cacheConfig)
//                .withCacheConfiguration("sellers", myDefaultCacheConfig(Duration.ofMinutes(5)))
//                .withCacheConfiguration("admin", myDefaultCacheConfig(Duration.ofMinutes(5)))
//                .build();
//    }
//
//    private RedisCacheConfiguration myDefaultCacheConfig(Duration duration) {
//        return RedisCacheConfiguration
//                .defaultCacheConfig()
//                .entryTtl(duration)
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
//    }
//
////    private final ObjectMapper objectMapper;
////
////    @Bean
////    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory connectionFactory) {
////        RedisTemplate<String, Object> template = new RedisTemplate<>();
////        template.setConnectionFactory(connectionFactory);
////        RedisSerializer<Object> serializer = new GenericJackson2JsonRedisSerializer(objectMapper);
////        template.setDefaultSerializer(serializer);
////        return template;
////    }
//}
