package com.yxchen.xinpicturebackend.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 功能：通用的删除类
 * 作者：chen
 * 日期： 2025/5/8 2:22
 **/
@Data
public class DeleteRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}

