package com.springboot.dlc.modules.controller.admin;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springboot.dlc.modules.entity.SysLog;
import com.springboot.dlc.modules.model.QPage;
import com.springboot.dlc.common.result.ResultView;
import com.springboot.dlc.modules.service.ISysLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * <p>
 * 系统日志表 前端控制器
 * </p>
 *
 * @author liujiebang
 * @since 2018-12-18
 */
@RestController
@RequestMapping("/api/sys-log/admin")
@Validated
@Slf4j
public class ALogController {

    @Autowired
    private ISysLogService iSysLogService;

    /**
     * 获取日志信息列表
     *
     * @param sysLog logType:【日志类型:1.业务日志 2.异常日志】
     * @param qPage  分页
     * @return
     */
    @GetMapping("/list")
    public ResultView list(SysLog sysLog, Date startTime, Date endTime, QPage qPage) {
        Wrapper<SysLog> qw = new QueryWrapper<SysLog>().lambda()
                .eq(SysLog::getLogType, sysLog.getLogType())
                .eq(StringUtils.isNotBlank(sysLog.getMethod()), SysLog::getMethod, sysLog.getMethod())
                .like(StringUtils.isNotBlank(sysLog.getUsername()), SysLog::getUsername, sysLog.getUsername())
                .like(StringUtils.isNotBlank(sysLog.getOperation()), SysLog::getOperation, sysLog.getOperation())
                .like(StringUtils.isNotBlank(sysLog.getUrl()), SysLog::getUrl, sysLog.getUrl())
                .like(StringUtils.isNotBlank(sysLog.getIp()), SysLog::getIp, sysLog.getIp())
                .gt(startTime != null, SysLog::getCtime, startTime)
                .lt(endTime != null, SysLog::getCtime, endTime)
                .orderByDesc(SysLog::getCtime);
        IPage<SysLog> iPage = iSysLogService.page(new Page(qPage.getOffset(), qPage.getLimit()), qw);
        return ResultView.ok(iPage);
    }

}
