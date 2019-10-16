package com.springboot.dlc.modules.service;

import com.springboot.dlc.modules.entity.SysArea;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 地区 服务类
 * </p>
 *
 * @author liujiebang
 * @since 2019-03-28
 */
public interface ISysAreaService extends IService<SysArea> {

    List<SysArea> recursion(Long fid);


    List<SysArea> getByFid(Long fid);
}
