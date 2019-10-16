package com.springboot.dlc.modules.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.springboot.dlc.modules.entity.SysArea;
import com.springboot.dlc.modules.mapper.SysAreaMapper;
import com.springboot.dlc.modules.service.ISysAreaService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 地区 服务实现类
 * </p>
 *
 * @author liujiebang
 * @since 2019-03-28
 */
@Service
public class SysAreaServiceImpl extends ServiceImpl<SysAreaMapper, SysArea> implements ISysAreaService {

    @Override
    public List<SysArea> recursion(Long fid) {
        List<SysArea> list = getByFid(fid);
        if (!list.isEmpty()) {
            list.forEach(value -> value.setSub(recursion(value.getAreaId())));
        }
        return list;
    }

    @Override
    public List<SysArea> getByFid(Long fid) {
        QueryWrapper<SysArea> qw = new QueryWrapper<>();
        qw.lambda().eq(SysArea::getFid, fid);
        qw.lambda().orderByAsc(SysArea::getSort);
        return list(qw);
    }
}
