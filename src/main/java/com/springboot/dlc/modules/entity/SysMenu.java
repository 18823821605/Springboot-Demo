package com.springboot.dlc.modules.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 菜单表
 * </p>
 *
 * @author liujiebang
 * @since 2018-10-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 接口名称
     */
    @NotBlank(message = "权限名称名称不能为空")
    @TableField("interfaceName")
    private String interfaceName;

    /**
     * 接口地址
     */
    @TableField(value = "interfaceUrl", strategy = FieldStrategy.IGNORED)
    private String interfaceUrl;

    @TableField("pageUrl")
    private String pageUrl;

    /**
     * 父id
     */
    @TableField(value = "fid", strategy = FieldStrategy.IGNORED)
    private String fid;

    /**
     * 接口类型(1:模块 2:菜单 3:按钮)
     */
    @Max(value = 3,message = "类型最大值为3")
    @Min(value = 1,message = "类型最小值为1")
    @NotNull(message = "接口类型不能为空")
    @TableField("interfaceType")
    private Integer interfaceType;

    /**
     * 排序
     */
    @NotNull(message = "排序不能为空")
    @TableField("orderBy")
    private Integer orderBy;

    /**
     * 1:有效  2:无效
     */
    @TableField("isFlag")
    private Integer isFlag;

    @TableField(value = "sysMenuList", exist = false)
    private List<SysMenu> sysMenuList;
}
