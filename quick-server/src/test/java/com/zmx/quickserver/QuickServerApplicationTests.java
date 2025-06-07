package com.zmx.quickserver;

import com.zmx.quickserver.service.DishService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

@SpringBootTest
class QuickServerApplicationTests {


    @Test
    void contextLoads() {

    }




    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    void testRedis(){
        redisTemplate.opsForValue().set("city","北京");
        System.out.println(redisTemplate.opsForValue().get("city"));


        redisTemplate.opsForValue().set("code","1234",30, TimeUnit.SECONDS);
        redisTemplate.opsForValue().setIfAbsent("lock","11111");
        redisTemplate.opsForValue().setIfAbsent("lock","2222");

    }


    @Test
    void testHash(){
        redisTemplate.opsForHash().put("user:1","name","zhangsan");
        redisTemplate.opsForHash().put("user:1","age","18");
        redisTemplate.opsForHash().put("user:1","sex","男");
        redisTemplate.opsForHash().put("user:2","name","lisi");
        redisTemplate.opsForHash().put("user:2","age","19");
        redisTemplate.opsForHash().put("user:2","sex","女");

    }

}
