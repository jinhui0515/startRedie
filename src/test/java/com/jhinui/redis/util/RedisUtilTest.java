package com.jhinui.redis.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author jinhui
 * @date 2021/2/7 9:49
 */
class RedisUtilTest {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void set() {
        redisTemplate.opsForValue().set("strTest", "Jhinui");
    }

    @Test
    void setWithExpiredTime() {
        redisUtil.setWithExpiredTime("numTest", 123, 60);
    }
}