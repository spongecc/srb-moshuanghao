package com.mosh.srb.core;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author 莫双豪
 * @version 1.0
 * @date 2021/8/31
 */
@SpringBootTest
public class RedisTemplateTests {

    @Resource
    private RedisTemplate redisTemplate;

    @Test
    public void save(){
        redisTemplate.opsForValue().set("dict", "testDict", 5, TimeUnit.MINUTES);
    }

}
