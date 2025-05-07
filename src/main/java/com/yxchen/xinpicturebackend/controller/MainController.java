package com.yxchen.xinpicturebackend.controller;

import com.yxchen.xinpicturebackend.common.BaseResponse;
import com.yxchen.xinpicturebackend.common.ResultUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 功能：健康检查
 * 作者：chen
 * 日期： 2025/5/8 2:30
 **/
@RestController
@RequestMapping("/")
public class MainController {

    /**
     * 健康检查
     */
    @GetMapping("/health")
    public BaseResponse<String> health() {
        return ResultUtils.success("ok");
    }
}
