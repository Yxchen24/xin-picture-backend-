package com.yxchen.xinpicturebackend.common;

import com.yxchen.xinpicturebackend.exception.ErrorCode;
import lombok.Data;

import java.io.Serializable;

/**
 * 功能：全局响应封装类
 * 作者：chen
 * 日期： 2025/5/8 1:39
 **/
@Data
public class BaseResponse<T> implements Serializable {
    public int code ;
    public T data ;
    public String message ;


    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public BaseResponse(int code ,T data){
        this(code ,data, "");
    }

    public BaseResponse(ErrorCode errorCode){
        this(errorCode.getCode(),null, errorCode.getMessage());
    }
}
