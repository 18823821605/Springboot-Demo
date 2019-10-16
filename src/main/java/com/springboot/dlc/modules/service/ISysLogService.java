package com.springboot.dlc.modules.service;

import com.springboot.dlc.modules.entity.SysLog;
import com.baomidou.mybatisplus.extension.service.IService;
import org.aspectj.lang.ProceedingJoinPoint;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 系统日志表 服务类
 * </p>
 *
 * @author liujiebang
 * @since 2018-12-18
 */
public interface ISysLogService extends IService<SysLog> {



    /**
     * 保存系统日志
     *
     * @param request
     * @param sysLogA
     * @param point
     */
    void saveBySystem(HttpServletRequest request, com.springboot.dlc.common.annotation.log.SysLog sysLogA, ProceedingJoinPoint point, long time) throws Throwable;

    /**
     * 保存异常日志
     *
     * @param request
     */
    void saveByError(HttpServletRequest request, Exception e);
}
