package com.codeLamb.mapper;

import com.codeLamb.my.mapper.MyMapper;
import com.codeLamb.pojo.Users;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UsersMapper extends MyMapper<Users> {

    Users searchByMobile(String mobile);
    Users searchByNickname(String nickName);
    Users searchByImoocNum(String imoocNum);
}