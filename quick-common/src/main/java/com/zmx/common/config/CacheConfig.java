package com.zmx.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Spring Cache配置类
 * 使用Redis作为缓存存储
 */
@Configuration
@EnableCaching
@Slf4j
public class CacheConfig {

    /**
     * 配置缓存管理器
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        log.info("正在创建 Spring Cache 缓存管理器...");

        // 使用GenericJackson2JsonRedisSerializer，这是Spring Boot 3.0推荐的方式
        // 它不需要手动配置ObjectMapper，内部会自动处理类型信息
        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer();

        // 配置序列化（解决乱码的问题）
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(30)) // 默认缓存时间30分钟
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jsonSerializer))
                .disableCachingNullValues(); // 不缓存空值

        // 设置不同缓存的过期时间
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

        // 菜品移动端列表缓存 - 30分钟
        cacheConfigurations.put("dishMobile", config.entryTtl(Duration.ofMinutes(30)));

        // 菜品口味缓存 - 1小时
        cacheConfigurations.put("dishFlavor", config.entryTtl(Duration.ofHours(1)));

        // 分类缓存 - 2小时（分类变化较少）
        cacheConfigurations.put("category", config.entryTtl(Duration.ofHours(2)));

        // 套餐缓存 - 30分钟
        cacheConfigurations.put("setmeal", config.entryTtl(Duration.ofMinutes(30)));

        return RedisCacheManager.builder(factory)
                .cacheDefaults(config)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }
}
