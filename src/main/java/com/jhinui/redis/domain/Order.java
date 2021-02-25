package com.jhinui.redis.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

/**
 * @author jinhui
 * @date 2021/2/23 14:53
 */
@Entity
@Table(name = "my_order")
@Getter
public class Order {

    @javax.persistence.Id
    @GeneratedValue(generator = "uuid")
    // 使用 hibernate UUIDHexGenerator 会生成一条警告，说生成的 uuid 不符合规范，推荐使用 UUIDGenerator，生成的字符串默认有 '-' 连接，导致最大长度长于 32，这里把 @column 去掉，使用默认 255 长度
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 36)
    private String id;

    private String orderNum;

    private String status;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @Setter
    private List<Item> items;

    public Order() {

    }

    public Order(String orderNum, String status) {
        this.orderNum = orderNum;
        this.status = status;
    }
}
