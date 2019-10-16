package com.springboot.dlc.common.enums;

import lombok.Getter;

@Getter
public enum RedisEnum {

    LOG("LOG"),

    MANAGER("MANAGER"),

    ROLE("ROLE"),

    MENU("MENU");


    private String key;

    RedisEnum(String key) {
        this.key = key;
    }
}
