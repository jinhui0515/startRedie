package com.jhinui.redis.server.controller;

import com.jhinui.redis.domain.User;
import com.jhinui.redis.server.dto.OrderDto;
import com.jhinui.redis.server.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Redis：String 与 Hash 基本操作
 *
 * @author jinhui
 * @date 2021/2/4 16:57
 */
@RestController
@RequestMapping("/redis")
public class RedisController {

    @Autowired
    private RedisService redisService;

    @DeleteMapping("/{key}")
    public void delKey(@PathVariable(name = "key") String key) {
        redisService.delKey(key);
    }

    @GetMapping("/{key}")
    public boolean existKey(@PathVariable(name = "key") String key) {
        return redisService.existKey(key);
    }

    @PostMapping("/{key}")
    public void expire(@PathVariable(name = "key") String key,
                       @RequestParam(name = "expireTime") long time) {
        redisService.expire(key, time);
    }

    @GetMapping("/{key}/expire")
    public long getExpireTime(@PathVariable(name = "key") String key) {
        return redisService.getExpireTime(key);
    }

    /*******************************String*************************/

    /**
     * String加入缓存
     *
     * @param key  键
     * @param user 值
     */
    @PostMapping("/str/{key}")
    public void set(@PathVariable(name = "key") String key, @RequestBody User user) {
        redisService.set(key, user.getId(), user.getName(), user.getUserCode(), user.getSex());
    }

    @PostMapping("/str/{key}/order")
    public void setOrder(@PathVariable(name = "key") String key,
                         @RequestBody OrderDto orderDto) {
        redisService.setOrder(key, orderDto);
    }

    @PostMapping("/str/{key}/v2")
    public void set2(@PathVariable(name = "key") String key,
                     @RequestParam(name = "value") int value) {
        redisService.set2(key, value);
    }

    /**
     * String获取缓存
     *
     * @param key 键
     */
    @GetMapping("/str/{key}")
    public User get(@PathVariable(name = "key") String key) {
        return redisService.get(key);
    }

    /**
     * 测试时有些问题，redis入库是Binary,生存时间未生效
     * String加入缓存，并设置生存时间
     *
     * @param key   键
     * @param value 值
     * @param time  生存时间
     */
    @PostMapping("/str/{key}/expire")
    public void setWithExpireTime(@PathVariable(name = "key") String key,
                                  @RequestParam(name = "value") int value,
                                  @RequestParam(name = "expireTime") long time) {
        redisService.setWithExpireTime(key, value, time);
    }

    /**
     * String自增
     *
     * @param key       键
     * @param increment 增量（大于0 ）
     * @return 加上增量之后的值
     */
    @PostMapping("/str/{key}/incr")
    public long incrby(@PathVariable(name = "key") String key,
                       @RequestParam(name = "increment") long increment) {
        return redisService.incrBy(key, increment);
    }

    /**
     * string 自减
     *
     * @param key       键
     * @param decrement 减量（小于0 ）
     * @return 减去减量之后的值
     */
    @PostMapping("/str/{key}/decr")
    public long decr(@PathVariable(name = "key") String key,
                     @RequestParam(name = "decrement") long decrement) {
        return redisService.decrBy(key, decrement);
    }

    /*****************************Hash*******************************/

    @PostMapping("/hash/{key}")
    public void hset(@PathVariable(name = "key") String key,
                     @RequestParam(name = "field") String field,
                     @RequestParam(name = "value") Object value) {
        redisService.hset(key, field, value);
    }

    @GetMapping("/hash/{key}")
    public Object hget(@PathVariable(name = "key") String key,
                       @RequestParam(name = "field") String field) {
        return redisService.hget(key, field);
    }

    @PostMapping("/hash/{key}/hm")
    public void hmset(@PathVariable(name = "key") String key,
                      @RequestBody User user) {
        redisService.hmset(key, user);
    }

    @GetMapping("/hash/{key}/hm")
    public Map<Object, Object> hmget(@PathVariable(name = "key") String key) {
        return redisService.hmget(key);
    }

    @PostMapping("/hash/{key}/expire")
    public void hsetWithExpireTime(@PathVariable(name = "key") String key,
                                   @RequestParam(name = "field") String field,
                                   @RequestParam(name = "value") Object value,
                                   @RequestParam(name = "expireTime") long time) {
        redisService.hsetWithExpireTime(key, field, value, time);
    }

    @PostMapping("/hash/{key}/hm/expire")
    public void hmsetWithExpireTime(@PathVariable(name = "key") String key,
                                    @RequestBody User user,
                                    @RequestParam(name = "expireTime") long time) {
        redisService.hmsetWithExpireTime(key, user, time);
    }

    @GetMapping("/hash/{key}/field")
    public boolean hasHashField(@PathVariable(name = "key") String key,
                                @RequestParam(name = "field") String field) {
        return redisService.hasHashField(key, field);
    }

    @DeleteMapping("/hash/{key}")
    public void deleteHashFields(@PathVariable(name = "key") String key,
                                 @RequestParam(name = "fields") String[] fieds) {
        redisService.deleteHashFields(key, fieds);
    }

    @GetMapping("/hash/{key}/incr")
    public double hincr(@PathVariable(name = "key") String key,
                        @RequestParam(name = "field") String field,
                        @RequestParam(name = "increment") double increment) {
        return redisService.hincr(key, field, increment);
    }

    @GetMapping("/hash/{key}/decr")
    public double hdecr(@PathVariable(name = "key") String key,
                        @RequestParam(name = "field") String field,
                        @RequestParam(name = "decrement") double decrement) {
        return redisService.hdecr(key, field, decrement);
    }
}
