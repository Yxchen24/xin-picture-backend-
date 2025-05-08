package com.yxchen.xinpicturebackend.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 功能：
 * 作者：chen
 * 日期： 2025/5/8 18:36
 **/
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 确认密码
     */
    private String checkPassword;
}
