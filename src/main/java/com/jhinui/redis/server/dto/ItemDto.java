package com.jhinui.redis.server.dto;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author jinhui
 * @date 2021/2/23 16:35
 */
@Getter
@Builder
public class ItemDto implements Serializable {
    private String name;

    private int num;

}
