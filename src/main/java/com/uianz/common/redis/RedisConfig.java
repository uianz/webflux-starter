package com.uianz.common.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author uianz
 * @date 2021/7/29
 */
@Configuration
public class RedisConfig {

    @Bean
    ReactiveStringRedisTemplate reactiveRedisTemplate(ReactiveRedisConnectionFactory factory,
                                                      ObjectMapper objectMapper) {
        return new ReactiveStringRedisTemplate(factory);
    }

    @Bean
    @Primary
    public ReactiveRedisOperations<String,Object> redisTemplate(ReactiveRedisConnectionFactory redisConnectionFactory,
                                                                ObjectMapper objectMapper){
        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
        var serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        serializer.setObjectMapper(objectMapper);
//        var keySerializer = new StringRedisSerializer();
        var context =
                RedisSerializationContext.<String, Object>newSerializationContext(new StringRedisSerializer())
                        .value(serializer)
                        .hashValue(serializer)
//                        .hashKey(keySerializer)
                        .build();
        return new ReactiveRedisTemplate<>(redisConnectionFactory, context);
    }

    @Bean
    public ReactiveValueOperations<String,Object> valueOperations(ReactiveRedisOperations<String,Object> redisOperations){
        return redisOperations.opsForValue();
    }

    @Bean
    public ReactiveListOperations<String,Object> listOperations(ReactiveRedisOperations<String,Object> redisOperations){
        return redisOperations.opsForList();
    }

    @Bean
    public ReactiveHashOperations<String,String,Object> hashOperations(ReactiveRedisOperations<String,Object> redisOperations){
        return redisOperations.opsForHash();
    }

    @Bean
    public ReactiveSetOperations<String,Object> setOperations(ReactiveRedisOperations<String,Object> redisOperations){
        return redisOperations.opsForSet();
    }

    @Bean
    public ReactiveZSetOperations<String, Object> zSetOperations(ReactiveRedisOperations<String,Object> redisOperations){
        return redisOperations.opsForZSet();
    }
}
