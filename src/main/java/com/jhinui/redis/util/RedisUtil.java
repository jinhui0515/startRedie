package com.jhinui.redis.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Redis 基本操作工具类
 *
 * @author jinhui
 * @date 2021/2/1 16:02
 */
@Component
@Slf4j
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * Redis设置 Kye 过期时间
     *
     * @param key  键
     * @param time 过期时间，单位转化为秒
     */
    public void expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("failed to set expiredTime");
            }
        }
    }

    /**
     * 获取 key 的到期时间
     *
     * @param key 键 key
     * @return 到期时间
     */
    public Long getExpiredTime(String key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * 判断 key 是否存在
     *
     * @param key 键key
     * @return true or false
     */
    public boolean existsKey(String key) {
        try {
            if (StringUtils.hasText(key)) {
                /**
                 * hasKey方法返回一个 Boolean ,
                 * 但 redisTemplate.hasKey(key)将返回值转换为 boolean,
                 * 即取消了装箱
                 * 若 hasKey() 返回值为null，则会报错
                 * 因此用以下方式避免报错
                 * 以上expire()方法同理
                 */
                return Boolean.TRUE.equals(redisTemplate.hasKey(key));
            }
            return true;
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("failed to determine if there is a key. ");
            }
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param keys 将要被删除缓存的key
     */
    public void delKey(String... keys) {
        if (keys != null && keys.length > 0) {
            if (keys.length == 1) {
                redisTemplate.delete(keys[0]);
            } else {
                List<String> keyList = Arrays.asList(keys);
                redisTemplate.delete(keyList);
            }
        }
    }

    /************************String类型的操作*******************************/

    /**
     * 获取缓存
     *
     * @param key 键
     * @return 缓存中 key 的 value
     */
    public Object get(String key) {
        if (StringUtils.hasText(key)) {
            return redisTemplate.opsForValue().get(key);
        }
        return null;
    }

    /**
     * 加入缓存
     *
     * @param key   键 key
     * @param value 值 value
     */
    public void set(String key, Object value) {
        if (StringUtils.hasText(key) && value != null) {
            redisTemplate.opsForValue().set(key, value);
        }
    }

    /**
     * 加入缓存并加上生存时间
     *
     * @param key   键 key
     * @param value 值 value
     * @param time  生存时间
     */
    public void setWithExpiredTime(String key, Object value, long time) {
        if (time > 0) {
            redisTemplate.opsForValue().set(key, value, time);
        } else {
            set(key, value);
        }
    }

    /**
     * 递增
     *
     * @param key       键key
     * @param increment 增量
     * @return 加上增量之后的值
     */
    public Long incr(String key, long increment) {
        if (increment < 0) {
            throw new RuntimeException("increment must gt 0");
        }
        return redisTemplate.opsForValue().increment(key, increment);
    }

    /**
     * 递减
     *
     * @param key       键 key
     * @param decrement 减量
     * @return 减去减量之后的值
     */
    public Long decr(String key, long decrement) {
        if (decrement < 0) {
            throw new RuntimeException("decrement must gt 0");
        } else {
            return redisTemplate.opsForValue().decrement(key, decrement);
        }
    }

    /*****************************Hash类型的操作*****************************/

    /**
     * HashGet
     *
     * @param key   键 key
     * @param field 域 field
     * @return 哈希表 key 中域 field 对应的值 value
     */
    public Object hget(String key, String field) {
        if (StringUtils.hasText(key) && StringUtils.hasText(field)) {
            return redisTemplate.opsForHash().get(key, field);
        }
        return null;
    }

    /**
     * 获取哈希表 key 的所有 field-value 对
     *
     * @param key 哈希表名称key
     * @return 哈希表 key 的所有 field-value 对
     * {
     * "sex": "男",
     * "userCode": "87009666",
     * "age": 20,
     * "name": "jhinua"
     * }
     */
    public Map<Object, Object> hmget(String key) {
        if (StringUtils.hasText(key)) {
            return redisTemplate.opsForHash().entries(key);
        }
        return null;
    }

    /**
     * hash 批量加入 field-value 对
     *
     * @param key 键 key
     * @param map field-value 对
     */
    public void hmset(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("hash批量加入filed-value对失败");
            }
        }
    }

    /**
     * hash批量加入带有生存时间的field-value对
     *
     * @param key  键 key
     * @param map  field-value对
     * @param time 生存时间time
     */
    public void hmsetWithExpireTime(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("hash批量加入带有生存时间的field-value对失败");
            }
        }
    }

    /**
     * hash加入数据，如果不存在将被创建
     *
     * @param key   键key
     * @param field 域field
     * @param value 值value
     */
    public void hset(String key, String field, Object value) {
        try {
            redisTemplate.opsForHash().put(key, field, value);
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("hash加入一条数据失败");
            }
        }
    }

    /**
     * hash加入一条数据，并设置生存时间，不存在将创建
     *
     * @param key   键 key
     * @param field 域 field
     * @param value 值 value
     * @param time  生存时间 time
     */
    public void hsetWithExpiredTime(String key, String field, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, field, value);
            if (time > 0) {
                expire(key, time);
            }
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("hash加入一条带有生存时间的数据失败");
            }
        }
    }

    /**
     * 判断hashKey中是否拥有field
     *
     * @param key   键 key
     * @param field 域 field
     * @return hashKey中是否拥有field
     */
    public boolean hasField(String key, String field) {
        try {
            return Boolean.TRUE.equals(redisTemplate.opsForHash().hasKey(key, field));
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("判断hashKey中是否拥有field失败");
            }
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key      键 key
     * @param hashKeys 要删除的值
     */
    public void deleteHashItems(String key, Object... hashKeys) {
        try {
            redisTemplate.opsForHash().delete(key, hashKeys);
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("删除hash表中的值失败");
            }
        }
    }

    /**
     * hash 递增
     *
     * @param key       键 key
     * @param field     域 field
     * @param increment 增量（大于0）
     * @return 加上增量之后的值
     */
    public double hincrBy(String key, String field, double increment) {
        try {
            return redisTemplate.opsForHash().increment(key, field, increment);
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("hash递增失败");
            }
            return 0;
        }
    }

    /**
     * hash 递减
     *
     * @param key       键 key
     * @param field     域 field
     * @param decrement 减量 decrement （小于0）
     * @return 减去减量之后的值
     */
    public double hdecrBy(String key, String field, double decrement) {
        try {
            return redisTemplate.opsForHash().increment(key, field, decrement);
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("hash递减失败");
            }
            return 0;
        }
    }

    /*****************************Set********************************/

}
