package com.zmx.quickserver;

import com.zmx.common.response.Result;
import com.zmx.quickpojo.vo.DishMobileRspVO;
import com.zmx.quickserver.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * 缓存功能测试类
 */
@SpringBootTest
@Slf4j
public class CacheTest {

    @Autowired
    private DishService dishService;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 测试菜品移动端列表缓存
     */
    @Test
    public void testDishMobileCache() {
        log.info("=== 开始测试菜品移动端列表缓存 ===");
        
        // 第一次调用 - 应该从数据库查询
        long startTime1 = System.currentTimeMillis();
        Result<List<DishMobileRspVO>> result1 = dishService.listMobile();
        long endTime1 = System.currentTimeMillis();
        log.info("第一次调用耗时: {}ms, 数据条数: {}", 
                endTime1 - startTime1, 
                result1.getData() != null ? result1.getData().size() : 0);

        // 第二次调用 - 应该从缓存获取
        long startTime2 = System.currentTimeMillis();
        Result<List<DishMobileRspVO>> result2 = dishService.listMobile();
        long endTime2 = System.currentTimeMillis();
        log.info("第二次调用耗时: {}ms, 数据条数: {}", 
                endTime2 - startTime2, 
                result2.getData() != null ? result2.getData().size() : 0);

        // 验证缓存效果
        if (endTime2 - startTime2 < endTime1 - startTime1) {
            log.info("✅ 缓存生效！第二次调用明显更快");
        } else {
            log.warn("⚠️ 缓存可能未生效，请检查配置");
        }
        
        log.info("=== 菜品移动端列表缓存测试完成 ===");
    }

    /**
     * 测试缓存管理器
     */
    @Test
    public void testCacheManager() {
        log.info("=== 开始测试缓存管理器 ===");
        
        log.info("缓存管理器类型: {}", cacheManager.getClass().getName());
        log.info("可用缓存名称: {}", cacheManager.getCacheNames());
        
        // 检查dishMobile缓存
        var dishMobileCache = cacheManager.getCache("dishMobile");
        if (dishMobileCache != null) {
            log.info("✅ dishMobile缓存已创建");
            log.info("缓存实现类: {}", dishMobileCache.getClass().getName());
        } else {
            log.warn("⚠️ dishMobile缓存未找到");
        }
        
        log.info("=== 缓存管理器测试完成 ===");
    }

    /**
     * 测试Redis连接
     */
    @Test
    public void testRedisConnection() {
        log.info("=== 开始测试Redis连接 ===");
        
        try {
            // 测试基本操作
            String testKey = "cache:test:key";
            String testValue = "cache test value";
            
            redisTemplate.opsForValue().set(testKey, testValue);
            Object result = redisTemplate.opsForValue().get(testKey);
            
            if (testValue.equals(result)) {
                log.info("✅ Redis连接正常");
            } else {
                log.warn("⚠️ Redis数据不一致");
            }
            
            // 清理测试数据
            redisTemplate.delete(testKey);
            
        } catch (Exception e) {
            log.error("❌ Redis连接异常: {}", e.getMessage());
        }
        
        log.info("=== Redis连接测试完成 ===");
    }

    /**
     * 测试缓存清除
     */
    @Test
    public void testCacheEvict() {
        log.info("=== 开始测试缓存清除 ===");
        
        // 先调用一次，让数据进入缓存
        dishService.listMobile();
        log.info("数据已缓存");
        
        // 检查缓存是否存在
        var cache = cacheManager.getCache("dishMobile");
        if (cache != null) {
            var cachedValue = cache.get("list");
            if (cachedValue != null) {
                log.info("✅ 缓存数据存在");
                
                // 手动清除缓存
                cache.evict("list");
                log.info("缓存已手动清除");
                
                // 再次检查
                var cachedValueAfterEvict = cache.get("list");
                if (cachedValueAfterEvict == null) {
                    log.info("✅ 缓存清除成功");
                } else {
                    log.warn("⚠️ 缓存清除失败");
                }
            } else {
                log.warn("⚠️ 缓存数据不存在");
            }
        }
        
        log.info("=== 缓存清除测试完成 ===");
    }
}
