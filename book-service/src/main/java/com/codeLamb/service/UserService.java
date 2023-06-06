package com.codeLamb.service;

import com.codeLamb.form.UpdateUserInfoForm;
import com.codeLamb.pojo.Users;

/**
 * @author codeLamb
 */
public interface UserService {

    /**
     * 判断用户是否存在，存在返回用户信息
     */
    Users queryMobileIsExist(String mobile);

    /**
     * 新增用户
     */
    Users createUser(String mobile);

    /**
     * 查询用户信息
     */
    Users queryUserInfo(String userId);

    /**
     * 修改用户信息
     */
    Users updateUserInfo(UpdateUserInfoForm form);

    Users updateUserInfo(UpdateUserInfoForm form, Integer type);
}
