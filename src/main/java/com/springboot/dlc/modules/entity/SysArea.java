package com.springboot.dlc.modules.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 地区
 * </p>
 *
 * @author liujiebang
 * @since 2019-03-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysArea implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("areaId")
    private Long areaId;

    /**
     * 地区名称
     */
    private String name;

    /**
     * 父节点地区id
     */
    private Long fid;

    /**
     * 排序字段
     */
    private Integer sort;

    /**
     * 经度
     */
    @TableField("longTude")
    private BigDecimal longTude;

    /**
     * 纬度
     */
    @TableField("latiTude")
    private BigDecimal latiTude;

    @TableField("pinYin")
    private String pinYin;

    private String first;

    @TableField(value = "sub", exist = false)
    private List<SysArea> sub;
}
