package com.jhinui.redis.server.dto;

import lombok.Getter;

import java.io.Serializable;
import java.util.List;

/**
 * @author jinhui
 * @date 2021/2/23 16:34
 */
@Getter
public class OrderDto implements Serializable {

    private String orderNum;

    private String status;

    private List<ItemDto> itemDtos;
}
