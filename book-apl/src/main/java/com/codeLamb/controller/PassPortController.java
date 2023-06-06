package com.codeLamb.controller;

import com.codeLamb.VO.UserVO;
import com.codeLamb.base.BaseController;
import com.codeLamb.exceptions.GraceException;
import com.codeLamb.exceptions.GraceExceptionHandler;
import com.codeLamb.form.UserLoginRegistForm;
import com.codeLamb.grace.result.GraceJSONResult;
import com.codeLamb.grace.result.ResponseStatusEnum;
import com.codeLamb.mapper.UsersMapper;
import com.codeLamb.pojo.Users;
import com.codeLamb.service.UserService;
import com.codeLamb.utils.IPUtil;
import com.codeLamb.utils.SMSUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author codeLamb
 */
@RestController
@Slf4j
@Api(tags = "PassPortController 短信发送接口")
@RequestMapping("passport")
public class PassPortController extends BaseController {

    @Resource
    private SMSUtils smsUtils;

    @Resource
    private UserService userService;

    @PostMapping("getSMSCode")
    public GraceJSONResult getSmsCode(@RequestParam String mobile,
                             HttpServletRequest request) throws Exception {
        // 判断手机号是否为空,为空什么也不返回
        if (StringUtils.isBlank(mobile)) {
            return GraceJSONResult.ok();
        }

        // 获取用户 ip
        String userIp = IPUtil.getRequestIp(request);
        // 将用户 ip 地址存入 redis 中用于判断
        redisOperator.setnx60s(MOBILE_SMSCODE + ":" + userIp, userIp);
        // 生成随机六位的验证码
        String code = (int) ((Math.random() * 9 + 1) * 100000) + "";

        // 发送验证码
        smsUtils.sendSMS(mobile, code);
        log.info(code);

        // 将验证码放入 redis 中用于之后的验证
        redisOperator.set(MOBILE_SMSCODE + ":" + mobile, code);

        return GraceJSONResult.ok("");
    }

    @PostMapping("login")
    public GraceJSONResult login(@RequestBody @Valid UserLoginRegistForm form,
                                      // BindingResult bindingResult,// 对代码有侵入性
                                      HttpServletRequest request) throws Exception {

        String smsCode = form.getSmsCode();
        String mobile = form.getMobile();
        // 1.验证验证码是否正确
        String code = redisOperator.get(MOBILE_SMSCODE + ":" + mobile);
        if (StringUtils.isBlank(code) || !smsCode.equalsIgnoreCase(code)){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.SMS_CODE_ERROR);
        }

        // 2.判断数据库中是否有用户
        Users user = userService.queryMobileIsExist(mobile);
        if (user == null) {
            user = userService.createUser(mobile);
        }

        // 3. 生成 token
        String uToken = UUID.randomUUID().toString();

        // 4. 将 token 保存到 redis 中
        redisOperator.set(REDIS_USER_TOKEN + ":" + user.getId(), uToken);

        // 5.删除用户保存在 redis 中的验证码
        redisOperator.del(MOBILE_SMSCODE + ":" + mobile);

        // 6. 将 token 封装到 user 中返回给前端
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        userVO.setUserToken(uToken);


        return GraceJSONResult.ok(userVO);
    }

    @PostMapping("logout")
    public GraceJSONResult logout(@RequestParam String userId) {
        redisOperator.del(REDIS_USER_TOKEN + ":" + userId);
        return GraceJSONResult.ok();
    }

}
