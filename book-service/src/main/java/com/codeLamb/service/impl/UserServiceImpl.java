package com.codeLamb.service.impl;

import com.codeLamb.enums.Sex;
import com.codeLamb.enums.UserInfoModifyType;
import com.codeLamb.enums.YesOrNo;
import com.codeLamb.exceptions.GraceException;
import com.codeLamb.form.UpdateUserInfoForm;
import com.codeLamb.grace.result.ResponseStatusEnum;
import com.codeLamb.mapper.UsersMapper;
import com.codeLamb.pojo.Users;
import com.codeLamb.service.UserService;
import com.codeLamb.utils.DateUtil;
import com.codeLamb.utils.DesensitizationUtil;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author codeLamb
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UsersMapper usersMapper;

    @Resource
    private Sid sid;

    private static final String USER_FACE1 = "https://image.itbaima.net/images/36/image-2023052013548898.jpeg";

    @Override
    public Users queryMobileIsExist(String mobile) {
        return usersMapper.searchByMobile(mobile);

    }

    @Override
    @Transactional
    public Users createUser(String mobile) {

        // 获取全局唯一 id
        String userId = sid.nextShort();

        Users user = new Users();

        user.setId(userId);
        user.setMobile(mobile);
        user.setNickname("用户：" + DesensitizationUtil.commonDisplay(mobile));
        user.setImoocNum("用户：" + DesensitizationUtil.commonDisplay(mobile));
        user.setFace(USER_FACE1);

        user.setBirthday(DateUtil.stringToDate("1900-01-01"));
        user.setSex(Sex.secret.type);

        user.setCountry("中国");
        user.setProvince("");
        user.setCity("");
        user.setDistrict("");
        user.setDescription("这家伙很懒，什么都没留下~");
        user.setCanImoocNumBeUpdated(YesOrNo.YES.type);

        user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());

        usersMapper.insert(user);

        return user;
    }

    @Override
    public Users queryUserInfo(String userId) {
        return usersMapper.selectByPrimaryKey(userId);
    }

    @Override
    @Transactional
    public Users updateUserInfo(UpdateUserInfoForm form) {
        Users users = new Users();
        BeanUtils.copyProperties(form, users);

        int i = usersMapper.updateByPrimaryKeySelective(users);
        if (i != 1) {
            GraceException.display(ResponseStatusEnum.USER_UPDATE_ERROR);
        }
        return queryUserInfo(form.getId());

    }

    @Override
    @Transactional
    public Users updateUserInfo(UpdateUserInfoForm form, Integer type) {
        // 如果修改的是昵称需要先判断是不是重复的昵称
        if (type.equals(UserInfoModifyType.NICKNAME.type)) {
            Users users = usersMapper.searchByNickname(form.getNickname());
            if (users != null) {
                GraceException.display(ResponseStatusEnum.USER_INFO_UPDATED_NICKNAME_EXIST_ERROR);
            }
        }

        if (type.equals(UserInfoModifyType.IMOOCNUM.type)) {
            // 判断 can_imooc_num_be_updated 是否为 0
            Users usersById = usersMapper.selectByPrimaryKey(form.getId());
            if (usersById.getCanImoocNumBeUpdated().equals(YesOrNo.NO.type)) {
                GraceException.display(ResponseStatusEnum.USER_INFO_CANT_UPDATED_IMOOCNUM_ERROR);
            }
            // 判断是否可以修改
            Users users = usersMapper.searchByImoocNum(form.getImoocNum());
            if (users != null) {
                GraceException.display(ResponseStatusEnum.USER_INFO_UPDATED_IMOOCNUM_EXIST_ERROR);
            }

            // 将 can_imooc_num_be_updated 修改为零
            form.setCanImoocNumBeUpdated(YesOrNo.NO.type);
        }
        return updateUserInfo(form);
    }
}
