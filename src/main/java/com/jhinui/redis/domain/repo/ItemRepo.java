package com.jhinui.redis.domain.repo;

import com.jhinui.redis.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

/**
 * @author jinhui
 * @date 2021/2/23 16:20
 */
public interface ItemRepo extends JpaRepository<Item, String>, QuerydslPredicateExecutor<Item> {
}
