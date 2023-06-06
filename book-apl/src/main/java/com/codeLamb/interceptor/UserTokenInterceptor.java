package com.codeLamb.interceptor;

import com.codeLamb.base.BaseController;
import com.codeLamb.exceptions.GraceException;
import com.codeLamb.grace.result.ResponseStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author codeLamb
 */
@Slf4j
public class UserTokenInterceptor extends BaseController implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {

        String userId = request.getHeader("headerUserId");
        String userToken = request.getHeader("headerUserToken");
        // 判断 userId 和 userToken 是否为空，为空的话重新登录
        if (StringUtils.isNoneBlank(userId) && StringUtils.isNoneBlank(userId)) {
            // 从 redis 中取出 token
            String redisToken = redisOperator.get(REDIS_USER_TOKEN + ":" + userId);
            if (StringUtils.isBlank(redisToken)) {
                GraceException.display(ResponseStatusEnum.UN_LOGIN);
                return false;
            } else {
                // 防止在多个手机中登录
                if (!userToken.equalsIgnoreCase(redisToken)) {
                    GraceException.display(ResponseStatusEnum.UN_LOGIN);
                    return false;
                }
            }
        }else {
            GraceException.display(ResponseStatusEnum.UN_LOGIN);
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
