package com.yxchen.xinpicturebackend.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 功能：添加用户
 * 作者：chen
 * 日期： 2025/5/8 18:36
 **/
@Data
public class UserAddRequest implements Serializable {

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 用户角色: user, admin
     */
    private String userRole;

    private static final long serialVersionUID = 1L;
}
