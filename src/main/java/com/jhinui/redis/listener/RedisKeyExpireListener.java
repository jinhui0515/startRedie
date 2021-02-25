package com.jhinui.redis.listener;

import com.jhinui.redis.domain.Item;
import com.jhinui.redis.domain.Order;
import com.jhinui.redis.domain.repo.ItemRepo;
import com.jhinui.redis.domain.repo.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * 监听所有db的过期事件 "__keyevent@*__:expired"
 *
 * @author jinhui
 * @date 2021/2/22 11:19
 */
@Component
public class RedisKeyExpireListener extends KeyExpirationEventMessageListener {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private ItemRepo itemRepo;

    public RedisKeyExpireListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }


    @Transactional
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        Optional<Order> order = orderRepo.findByOrderNum(expiredKey);
        if (order.isPresent()) {
            List<Item> items = order.get().getItems();
            if (!order.get().getStatus().equals("已支付")) {
                order.get().getItems().clear();
                items.forEach(item -> itemRepo.delete(item));
                orderRepo.delete(order.get());
                System.out.println("订单号为" + expiredKey + "超时未支付，已取消");
            }
        }
    }
}
