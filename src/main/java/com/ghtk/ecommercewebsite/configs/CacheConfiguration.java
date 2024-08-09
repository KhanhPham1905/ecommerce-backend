//package com.ghtk.ecommercewebsite.configs;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.module.SimpleModule;
//import lombok.RequiredArgsConstructor;
//import org.springframework.cache.CacheManager;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.cache.interceptor.KeyGenerator;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.redis.cache.RedisCacheConfiguration;
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.serializer.RedisSerializationContext;
//import org.springframework.data.redis.serializer.RedisSerializer;
//import org.springframework.data.redis.serializer.SerializationException;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//import java.io.IOException;
//import java.time.Duration;
//
//@Configuration
//@EnableCaching
//@RequiredArgsConstructor
//public class CacheConfiguration {
//
////    private final PageJsonSerializer pageJsonSerializer;
////    private final PageJsonDeserializer pageJsonDeserializer;
//
//    @Bean
//    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
////        RedisSerializer<Object> valueSerializer = new RedisSerializer<>() {
////            private final ObjectMapper objectMapper = new ObjectMapper()
////                    .registerModule(new SimpleModule()
////                            .addSerializer(Page.class, pageJsonSerializer)
////                            .addDeserializer(Page.class,pageJsonDeserializer));
////
////            @Override
////            public byte[] serialize(Object value) throws SerializationException {
////                try {
////                    return objectMapper.writeValueAsBytes(value);
////                } catch (JsonProcessingException e) {
////                    throw new RuntimeException(e);
////                }
////            }
////
////            @Override
////            public Object deserialize(byte[] bytes) throws SerializationException {
////                try {
////                    return objectMapper.readValue(bytes, Object.class);
////                } catch (IOException e) {
////                    throw new RuntimeException(e);
////                }
////            }
////        };
//
//        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(Duration.ofHours(1))
//                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()));
//        // Redis cant deal with Page with RedisSerializer.json(), so we need to create valueSerializer
//        return RedisCacheManager.builder(redisConnectionFactory)
//                .cacheDefaults(redisCacheConfiguration)
//                .build();
//    }
//
//    @Bean
//    public KeyGenerator keyGenerator() {
//        return (target, method, params) -> {
//            StringBuilder sb = new StringBuilder();
//            sb.append(target.getClass().getSimpleName());
//            sb.append(".").append(method.getName()).append("[");
//            for (Object param : params) {
//                sb.append(param.toString());
//            }
//            sb.append("]");
//            return sb.toString();
//        };
//    }
//
//}
