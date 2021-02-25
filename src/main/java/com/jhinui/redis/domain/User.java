package com.jhinui.redis.domain;

import lombok.Generated;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author jinhui
 * @date 2021/1/28 17:01
 */
@Getter
public class User implements Serializable {

    @Generated
    private String id;

    private String name;

    private String userCode;

    private String sex;

    @Setter
    private int age;

    public User() {

    }

    public User(String id, String name, String userCode, String sex) {
        this.id = id;
        this.name = name;
        this.userCode = userCode;
        this.sex = sex;
    }
}
