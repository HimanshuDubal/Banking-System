package com.banking.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.banking.dto.AccountResponse;
import com.banking.dto.FixedDepositResponse;
import com.banking.dto.LoanResponse;
import com.banking.dto.PendingTransferOtp;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
@EnableCaching
public class RedisCacheConfig {

    private ObjectMapper baseMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    @SuppressWarnings("deprecation")
    private <T> Jackson2JsonRedisSerializer<T> typedSerializer(Class<T> type) {
        Jackson2JsonRedisSerializer<T> serializer = new Jackson2JsonRedisSerializer<>(type);
        serializer.setObjectMapper(baseMapper());
        return serializer;
    }

    @SuppressWarnings("deprecation")
    private <T> Jackson2JsonRedisSerializer<T> typedListSerializer(Class<?> elementType) {
        ObjectMapper mapper = baseMapper();
        CollectionType listType = mapper.getTypeFactory().constructCollectionType(List.class, elementType);
        Jackson2JsonRedisSerializer<T> serializer = new Jackson2JsonRedisSerializer<>(listType);
        serializer.setObjectMapper(mapper);
        return serializer;
    }
    
    @Bean
    public RedisTemplate<String, PendingTransferOtp> otpRedisTemplate(RedisConnectionFactory connectionFactory) {
    	RedisTemplate<String, PendingTransferOtp> template = new RedisTemplate<>();
    	template.setConnectionFactory(connectionFactory);
    	template.setKeySerializer(new StringRedisSerializer());
    	template.setValueSerializer(typedSerializer(PendingTransferOtp.class));
    	template.afterPropertiesSet();
    	return template;
    }

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(5))
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()));

        Map<String, RedisCacheConfiguration> perCacheConfig = new HashMap<>();


        perCacheConfig.put("accounts", defaultConfig
                .entryTtl(Duration.ofMinutes(2))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(typedSerializer(AccountResponse.class))));


        perCacheConfig.put("loans", defaultConfig
                .entryTtl(Duration.ofMinutes(2))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(typedSerializer(LoanResponse.class))));


        perCacheConfig.put("loansByUser", defaultConfig
                .entryTtl(Duration.ofMinutes(2))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(typedListSerializer(LoanResponse.class))));

        perCacheConfig.put("transactions", defaultConfig.entryTtl(Duration.ofMinutes(2)));

        perCacheConfig.put("fixedDeposits", defaultConfig
                .entryTtl(Duration.ofMinutes(2))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(typedSerializer(FixedDepositResponse.class))));
 
        perCacheConfig.put("fixedDepositsByUser", defaultConfig
                .entryTtl(Duration.ofMinutes(2))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(typedListSerializer(FixedDepositResponse.class))));

        perCacheConfig.put("userDetails", defaultConfig
                .entryTtl(Duration.ofMinutes(10))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new JdkSerializationRedisSerializer())));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(perCacheConfig)
                .build();
    }
}