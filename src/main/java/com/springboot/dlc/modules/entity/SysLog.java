package com.springboot.dlc.modules.entity;

import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import com.springboot.dlc.common.annotation.valid.Status;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统日志表
 * </p>
 *
 * @author liujiebang
 * @since 2018-12-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日志编号
     */
    @TableId(value = "logId", type = IdType.AUTO)
    private Long logId;

    /**
     * 日志类型
     */
    @Status
    @TableField("logType")
    private Integer logType;
    /**
     * 用户名
     */
    @TableField("username")
    private String username;

    /**
     * 用户操作
     */
    @TableField("operation")
    private String operation;

    /**
     * 请求方式
     */
    @TableField("method")
    private String method;

    /**
     * 请求方法
     */
    @TableField("controller")
    private String controller;

    /**
     * 请求路径
     */
    @TableField("url")
    private String url;

    /**
     * 请求参数
     */
    @TableField("params")
    private String params;

    /**
     * 执行时长
     */
    @TableField("time")
    private Long time;

    /**
     * IP地址
     */
    @TableField("ip")
    private String ip;

    /**
     * 异常内容
     */
    @TableField("errorMessage")
    private String errorMessage;

    /**
     * 创建时间
     */
    @TableField("ctime")
    private Date ctime;

    public SysLog() {
    }


}
