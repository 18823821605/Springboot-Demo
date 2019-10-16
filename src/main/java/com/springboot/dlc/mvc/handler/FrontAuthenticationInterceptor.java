package com.springboot.dlc.mvc.handler;

import com.alibaba.druid.util.StringUtils;
import com.springboot.dlc.common.exception.AuthException;
import com.springboot.dlc.common.enums.ResultEnum;
import com.springboot.dlc.common.redis.RedisService;
import com.springboot.dlc.common.result.SysConstant;
import com.springboot.dlc.common.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: liujiebang
 * @Description: 前端认证
 * @Date: 2018/7/2 16:50
 **/
@Slf4j
public class FrontAuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisService redisService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader(SysConstant.TOKEN);
        if (StringUtils.isEmpty(token)) {
            throw new AuthException(ResultEnum.CODE_3);
        } else {
            try {
                String userId = JwtUtil.getUniqueId(token, SysConstant.USER_ID);
                redisService.getAuthorizedSubject(userId);
                request.setAttribute(SysConstant.USER_ID, userId);
            } catch (NullPointerException e) {
                throw new AuthException(ResultEnum.CODE_403);
            } catch (ClassCastException e) {
                throw new AuthException(ResultEnum.CODE_403);
//            } catch (AuthException e) {
//                throw new AuthException(e.getResultEnum());
            } catch (Exception e) {
                throw new AuthException(ResultEnum.CODE_403);
            }
        }

        return true;
    }

}
