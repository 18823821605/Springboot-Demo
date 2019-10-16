package com.springboot.dlc.modules.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author liujiebang
 * @since 2018-10-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysManager implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 管理员编号
     */
    @TableId("managerId")
    private String managerId;

    /**
     * 用户名称
     */
    @NotBlank(message = "名称不能为空")
    @TableField("userName")
    private String userName;

    /**
     * 用户账号
     */
    @NotBlank(message = "账号不能为空")
    @TableField("userAcount")
    private String userAcount;

    /**
     * 管理员权限 1超级管理员 2管理员
     */
    @TableField("managerType")
    private Integer managerType;

    /**
     * 用户密码
     */
    @NotBlank(message = "密码不能为空")
    @Length(min = 6, max = 20, message = "密码须在长度6-20位之间")
    @TableField("passWord")
    private String passWord;

    /**
     * 1:正常 2:禁用
     */
    @TableField("isFlag")
    private Integer isFlag;

    /**
     * 创建时间
     */
    @TableField("ctime")
    private Date ctime;


}
