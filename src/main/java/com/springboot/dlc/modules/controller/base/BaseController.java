package com.springboot.dlc.modules.controller.base;

import com.springboot.dlc.common.redis.RedisService;
import com.springboot.dlc.mvc.resources.WebResource;
import com.springboot.dlc.common.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @auther: liujiebang
 * @Date: Create in 2018/10/16
 * @Description:
 **/
public class BaseController {

    @Autowired
    protected RedisService redisService;
    @Autowired
    protected WebResource webResource;

    /**
     * 生成token
     *
     * @param unique    id标识
     * @param uniqueId  用户编号-或管理员编号
     * @param obj       用户对象或管理员对象
     * @param TTLMillis token有效时间
     * @return
     */
    protected String jwtToken(String unique, String uniqueId, Object obj, long TTLMillis) {
        String jwtToken = JwtUtil.createJWT(unique,
                uniqueId,
                TTLMillis);
        redisService.setAuthorizedSubject(uniqueId, obj, TTLMillis / 1000);
        return jwtToken;
    }
}
