package com.yxchen.xinpicturebackend.aop;

import com.yxchen.xinpicturebackend.annotation.AuthCheck;
import com.yxchen.xinpicturebackend.exception.BusinessException;
import com.yxchen.xinpicturebackend.exception.ErrorCode;
import com.yxchen.xinpicturebackend.exception.ThrowUtils;
import com.yxchen.xinpicturebackend.model.entity.User;
import com.yxchen.xinpicturebackend.model.enums.UserRoleEnum;
import com.yxchen.xinpicturebackend.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 功能：
 * 作者：chen
 * 日期： 2025/5/8 23:54
 **/
@Aspect
@Component
public class AuthInterceptor {

    @Resource
    private UserService userService;


    /**
     * 权限校验
     * @param joinPoint
     * @param authCheck
     * @return
     * @throws Throwable
     */
    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        // 1.获取参数（需要的权限）
        String mustRole= authCheck.mustRole();
        UserRoleEnum mustRoleEnum = UserRoleEnum.getEnumByValue(mustRole);

        if (mustRoleEnum==null){
            return joinPoint.proceed();
        }
        // 2.获取当前用户登录信息
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        // 当前用户没有权限抛异常
        UserRoleEnum userRole = UserRoleEnum.getEnumByValue(userService.getLoginUser(request).getUserRole());
        if(userRole==null){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 3.要求必须有管理员权限但是用户没有管理员权限抛异常
        if(UserRoleEnum.ADMIN.equals(mustRoleEnum)&&!UserRoleEnum.ADMIN.equals(userRole)){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 4.通过校验,放行
        return joinPoint.proceed();
    }
}
