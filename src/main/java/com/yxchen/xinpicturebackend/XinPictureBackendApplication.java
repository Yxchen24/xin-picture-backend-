package com.yxchen.xinpicturebackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@MapperScan("com.yxchen.xinpicturebackend.Mapper")
@EnableAspectJAutoProxy(exposeProxy = true)
public class XinPictureBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(XinPictureBackendApplication.class, args);
    }

}
