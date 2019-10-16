package com.springboot.dlc.modules.aspect;


import com.springboot.dlc.common.utils.HttpContextUtils;
import com.springboot.dlc.common.utils.IdentityUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: liujiebang
 * @Description: 日志切面
 * @Date: 2018/6/27 15:03
 **/
@Aspect
@Component
@Slf4j
public class HttpAspect {

    @Pointcut("execution(public * com.springboot.dlc.modules.controller..*.*(..))")
    public void log() {

    }

    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        log.info("客户端IP = {}", IdentityUtil.getRemoteHost(request));
        log.info("请求地址 = {}", request.getRequestURL());
        log.info("请求方式 = {}", request.getMethod());
        log.info("请求控制器 = {}", joinPoint.getSignature().getDeclaringTypeName() + "," + joinPoint.getSignature().getName());
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] strings = methodSignature.getParameterNames();
        for (int i = 0; i < joinPoint.getArgs().length; i++) {
            log.info(strings[i] + " = " + joinPoint.getArgs()[i]);
        }
    }

    @Around("log()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {

        Object result = null;
        try {

        } catch (Throwable e) {

        }
        if (result == null) {
            return joinPoint.proceed();
        } else {
            return result;
        }
    }

    @AfterReturning(pointcut = "log()", returning = "object")
    public void doAfterReturing(Object object) {
        if (object != null) {
            log.info("返回结果 = {}", object.toString());
        }

    }
}
