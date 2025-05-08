package com.yxchen.xinpicturebackend.model.enums;

import cn.hutool.core.util.ObjectUtil;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 功能：用户角色枚举
 * 作者：chen
 * 日期： 2025/5/8 18:06
 **/
@Getter
public enum UserRoleEnum {

    USER("用户","user"),
    ADMIN("管理员","admin");

    public final String text;
    public final String value;
    public static final Map<String, UserRoleEnum>VALUE_ENUM_MAP = new HashMap<>();
    static {
        for (UserRoleEnum en : UserRoleEnum.values()) {
            VALUE_ENUM_MAP.put(en.value, en);
        }
    }
    UserRoleEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public static UserRoleEnum getEnumByValue(String value){
        if(ObjectUtil.isEmpty(value)){
            return null;
        }
        return VALUE_ENUM_MAP.get(value);
    }
}
