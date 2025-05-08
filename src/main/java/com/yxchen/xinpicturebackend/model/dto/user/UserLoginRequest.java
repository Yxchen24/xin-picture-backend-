package com.yxchen.xinpicturebackend.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 功能：登录
 * 作者：chen
 * 日期： 2025/5/8 18:36
 **/
@Data
public class UserLoginRequest implements Serializable {


    private static final long serialVersionUID = -782092607884109970L;
    /**
     * 账号
     */
    private String userAccount;

    /**
     * 密码
     */
    private String userPassword;

}
