package com.jhinui.redis.server.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhinui.redis.domain.Item;
import com.jhinui.redis.domain.Order;
import com.jhinui.redis.domain.User;
import com.jhinui.redis.domain.repo.ItemRepo;
import com.jhinui.redis.domain.repo.OrderRepo;
import com.jhinui.redis.server.dto.ItemDto;
import com.jhinui.redis.server.dto.OrderDto;
import com.jhinui.redis.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author jinhui
 * @date 2021/2/4 16:58
 */
@Slf4j
@Service
public class RedisService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private ItemRepo itemRepo;

    @Transactional
    public void delKey(String key) {
        redisUtil.delKey(key);
    }

    @Transactional
    public boolean existKey(String key) {
        return redisUtil.existsKey(key);
    }

    @Transactional
    public void expire(String key, long time) {
        redisUtil.expire(key, time);
    }

    @Transactional
    public long getExpireTime(String key) {
        return redisUtil.getExpiredTime(key);
    }

    /****************************String*****************************/

    @Transactional
    public void set(String key, String id, String name, String userCode, String sex) {
        User user = new User(id, name, userCode, sex);
        redisUtil.set(key, user);
    }

    @Transactional
    public void setOrder(String key, OrderDto orderDto) {
        Order order = new Order(orderDto.getOrderNum(), orderDto.getStatus());
        List<Item> items = orderDto.getItemDtos().stream().map(itemDto -> {
            Item item = new Item(itemDto.getName(), itemDto.getNum());
            return item;
        }).collect(Collectors.toList());
        itemRepo.saveAll(items);
        order.setItems(items);
        orderRepo.save(order);
        redisUtil.set(key, order);
    }

    @Transactional
    public void set2(String key, int value) {
        redisUtil.set(key, value);
    }

    @Transactional
    public User get(String key) {
        Object o = redisUtil.get(key);
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonStr = mapper.writeValueAsString(o);
            System.out.println(jsonStr);
            return mapper.readValue(jsonStr, User.class);
        } catch (JsonProcessingException e) {
            if (log.isErrorEnabled()) {
                log.error("json转化为User异常" + e.getMessage());
            }
            return null;
        }
    }

    @Transactional
    public void setWithExpireTime(String key, int value, long time) {
        redisUtil.setWithExpiredTime(key, value, time);
    }

    @Transactional
    public long incrBy(String key, long increment) {
        return redisUtil.incr(key, increment);
    }

    @Transactional
    public long decrBy(String key, long decrement) {
        return redisUtil.decr(key, decrement);
    }

    /****************************Hash*****************************/

    @Transactional
    public void hset(String key, String field, Object value) {
        redisUtil.hset(key, field, value);
    }

    @Transactional
    public Object hget(String key, String field) {
        return redisUtil.hget(key, field);
    }

    @Transactional
    public void hmset(String key, User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("userCode", user.getUserCode());
        map.put("sex", user.getSex());
        map.put("age", user.getAge());
        redisUtil.hmset(key, map);
    }

    @Transactional
    public Map<Object, Object> hmget(String key) {
        return redisUtil.hmget(key);
    }

    @Transactional
    public void hsetWithExpireTime(String key, String field, Object value, long time) {
        redisUtil.hsetWithExpiredTime(key, field, value, time);
    }

    @Transactional
    public void hmsetWithExpireTime(String key, User user, long time) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", user.getName());
        map.put("userCode", user.getUserCode());
        map.put("sex", user.getSex());
        map.put("age", user.getAge());
        redisUtil.hmsetWithExpireTime(key, map, time);
    }

    @Transactional
    public boolean hasHashField(String key, String field) {
        return redisUtil.hasField(key, field);
    }

    @Transactional
    public void deleteHashFields(String key, String[] fields) {
        redisUtil.deleteHashItems(key, (Object) fields);
    }

    @Transactional
    public double hincr(String key, String field, double increment) {
        return redisUtil.hincrBy(key, field, increment);
    }

    @Transactional
    public double hdecr(String key, String field, double decrement) {
        return redisUtil.hdecrBy(key, field, decrement);
    }
}
