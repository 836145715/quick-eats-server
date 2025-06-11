package com.zmx.quickserver;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.cache.RedisCacheManager;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 缓存配置测试类
 */
@SpringBootTest
@Slf4j
public class CacheConfigTest {

    @Autowired
    private CacheManager cacheManager;

    /**
     * 测试缓存管理器是否正确配置
     */
    @Test
    public void testCacheManagerConfiguration() {
        log.info("=== 开始测试缓存管理器配置 ===");
        
        // 验证缓存管理器不为空
        assertNotNull(cacheManager, "缓存管理器不应该为空");
        
        // 验证是Redis缓存管理器
        assertTrue(cacheManager instanceof RedisCacheManager, 
                "缓存管理器应该是RedisCacheManager类型");
        
        log.info("✅ 缓存管理器类型: {}", cacheManager.getClass().getSimpleName());
        
        // 验证预定义的缓存名称
        var cacheNames = cacheManager.getCacheNames();
        log.info("当前缓存名称: {}", cacheNames);
        
        // 测试获取缓存实例
        var dishMobileCache = cacheManager.getCache("dishMobile");
        assertNotNull(dishMobileCache, "dishMobile缓存应该可以创建");
        log.info("✅ dishMobile缓存创建成功");
        
        var dishFlavorCache = cacheManager.getCache("dishFlavor");
        assertNotNull(dishFlavorCache, "dishFlavor缓存应该可以创建");
        log.info("✅ dishFlavor缓存创建成功");
        
        var categoryCache = cacheManager.getCache("category");
        assertNotNull(categoryCache, "category缓存应该可以创建");
        log.info("✅ category缓存创建成功");
        
        var setmealCache = cacheManager.getCache("setmeal");
        assertNotNull(setmealCache, "setmeal缓存应该可以创建");
        log.info("✅ setmeal缓存创建成功");
        
        log.info("=== 缓存管理器配置测试完成 ===");
    }

    /**
     * 测试缓存基本操作
     */
    @Test
    public void testCacheBasicOperations() {
        log.info("=== 开始测试缓存基本操作 ===");
        
        var cache = cacheManager.getCache("dishMobile");
        assertNotNull(cache, "缓存实例不应该为空");
        
        // 测试缓存存储和获取
        String testKey = "test-key";
        String testValue = "test-value";
        
        // 存储数据
        cache.put(testKey, testValue);
        log.info("数据已存储到缓存");
        
        // 获取数据
        var cachedValue = cache.get(testKey);
        assertNotNull(cachedValue, "缓存值不应该为空");
        assertEquals(testValue, cachedValue.get(), "缓存值应该与存储值相同");
        log.info("✅ 缓存存储和获取测试成功");
        
        // 测试缓存清除
        cache.evict(testKey);
        var evictedValue = cache.get(testKey);
        assertNull(evictedValue, "清除后缓存值应该为空");
        log.info("✅ 缓存清除测试成功");
        
        log.info("=== 缓存基本操作测试完成 ===");
    }
}
