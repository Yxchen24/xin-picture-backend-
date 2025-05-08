package com.yxchen.xinpicturebackend.service.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yxchen.xinpicturebackend.constant.UserConstant;
import com.yxchen.xinpicturebackend.exception.BusinessException;
import com.yxchen.xinpicturebackend.exception.ErrorCode;
import com.yxchen.xinpicturebackend.model.dto.user.UserQueryRequest;
import com.yxchen.xinpicturebackend.model.entity.User;
import com.yxchen.xinpicturebackend.model.enums.UserRoleEnum;
import com.yxchen.xinpicturebackend.model.vo.LoginUserVo;
import com.yxchen.xinpicturebackend.model.vo.UserVO;
import com.yxchen.xinpicturebackend.service.UserService;
import com.yxchen.xinpicturebackend.Mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author 34303
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2025-05-08 17:55:57
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    /**
     * 用户注册
     * @param userAccount 账号
     * @param userPassword 密码
     * @param checkPassword 校验密码
     * @return
     */
    public Long userRegister(String userAccount, String userPassword, String checkPassword){
        // 1.校验
        if(StrUtil.hasEmpty(userAccount, userPassword, checkPassword)){
            throw  new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if(userAccount.length() < 4){
            throw  new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if(userPassword.length() < 8 || checkPassword.length() < 8){
            throw  new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        if(!userPassword.equals(checkPassword)){
            throw  new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        // 2.检查是否重复注册
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long  count = this.baseMapper.selectCount(queryWrapper);
        if(count > 0){
            throw  new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
        }

        // 3.加密
        String encryptPassword = getEncryptPassword(userPassword);

        // 4.插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setUserRole(UserRoleEnum.USER.value);
        user.setUserName("无名");
        boolean saveResult = this.save(user);
        if(!saveResult){
            throw  new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败");
        }
        return user.getId();
    }

    /**
     * 获取加密密码
     * @param userPassword
     * @return
     */
    @Override
    public String getEncryptPassword(String userPassword){
        // 加盐，混淆密码
        final String salt = "chenyuxin-xin-picture";
        return DigestUtils.md5DigestAsHex((userPassword + salt).getBytes());

    }

    /**
     * 用户登录
     * @param userAccount
     * @param userPassword
     * @param request
     * @return
     */

    @Override
    public LoginUserVo userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1.校验
        if(StrUtil.hasEmpty(userAccount, userPassword)){
            throw  new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if(userAccount.length() < 4){
            throw  new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        // 2.获取加密后密码
        String encryptPassword = getEncryptPassword(userPassword);
        // 2.查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = this.baseMapper.selectOne(queryWrapper);
        // 用户不存在
        if(user == null){
            log.info("user login failed, userAccount cannot match userPassword");
            throw  new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或者密码错误");
        }

        // 4.记住登录状态
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, user);
        return this.getLoginUserVO(user);
    }

    /**
     * 获取已经脱敏登录用户
     * @param user
     * @return
     */
    @Override
    public LoginUserVo getLoginUserVO(User user) {
        if(user==null){
            return null;
        }
        LoginUserVo loginUserVO = new LoginUserVo();
        BeanUtils.copyProperties(user, loginUserVO);
        return loginUserVO;

    }

    /**
     * 获取登录用户
     * @param request
     * @return
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        Object attribute = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User currentUser = (User) attribute;
        if(currentUser == null){
            throw  new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        User loginUser = this.getById(currentUser.getId());
        if(loginUser == null){
            throw  new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return loginUser;

    }

    /**
     * 用户注销
     * @param request
     * @return
     */
    @Override
    public boolean userLogout(HttpServletRequest request) {
        Object attribute = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if(attribute == null){
            throw  new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE);
        return true;
    }


    /**
     * 获取脱敏用户信息
     * @param user
     * @return
     */
    @Override
    public UserVO getUserVO(User user) {
        if(user==null){
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }


    /**
     * 获取脱敏用户列表
     * @param userList
     * @return
     */
    @Override
    public List<UserVO> getUserVOList(List<User> userList) {
        if (userList == null){
            return new ArrayList<>();
        }
        return userList.stream()
                .map(this::getUserVO)
                .collect(Collectors.toList());
    }


    /**
     * 获取查询条件
     * @param userQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = userQueryRequest.getId();
        String userAccount = userQueryRequest.getUserAccount();
        String userName = userQueryRequest.getUserName();
        String userProfile = userQueryRequest.getUserProfile();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ObjUtil.isNotNull(id), "id", id);
        queryWrapper.eq(StrUtil.isNotBlank(userRole), "userRole", userRole);
        queryWrapper.like(StrUtil.isNotBlank(userAccount), "userAccount", userAccount);
        queryWrapper.like(StrUtil.isNotBlank(userName), "userName", userName);
        queryWrapper.like(StrUtil.isNotBlank(userProfile), "userProfile", userProfile);
        queryWrapper.orderBy(StrUtil.isNotEmpty(sortField), sortOrder.equals("ascend"), sortField);
        return queryWrapper;
    }



}




