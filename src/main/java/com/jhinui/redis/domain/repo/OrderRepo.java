package com.jhinui.redis.domain.repo;

import com.jhinui.redis.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

/**
 * @author jinhui
 * @date 2021/2/23 16:18
 */
public interface OrderRepo extends JpaRepository<Order, String>, QuerydslPredicateExecutor<Order> {

    Optional<Order> findByOrderNum(String orderNum);
}
