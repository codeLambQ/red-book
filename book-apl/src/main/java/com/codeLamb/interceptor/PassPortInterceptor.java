package com.codeLamb.interceptor;

import com.codeLamb.base.BaseController;
import com.codeLamb.exceptions.GraceException;
import com.codeLamb.grace.result.ResponseStatusEnum;
import com.codeLamb.utils.IPUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author codeLamb
 */
@Slf4j
public class PassPortInterceptor extends BaseController implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {

        // 获取用户 ip 地址
        String userIp = IPUtil.getRequestIp(request);

        // 判断是不是第一次发送验证码
        boolean isExist = redisOperator.keyIsExist(MOBILE_SMSCODE + ":" + userIp);
        // 不是第一次发送，拦截
        if (isExist) {
            GraceException.display(ResponseStatusEnum.SMS_NEED_WAIT_ERROR);
            log.info("短信发送频率太快");
            return false;
        }

        /**
         * 第一次发送直接返回
         */
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
