package com.springboot.dlc.modules.service.impl;

import com.springboot.dlc.modules.entity.SysManagerRole;
import com.springboot.dlc.modules.mapper.SysManagerRoleMapper;
import com.springboot.dlc.modules.service.ISysManagerRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 管理员角色表 服务实现类
 * </p>
 *
 * @author liujiebang
 * @since 2018-10-02
 */
@Service
public class  SysManagerRoleServiceImpl extends ServiceImpl<SysManagerRoleMapper, SysManagerRole> implements ISysManagerRoleService {

    @Override
    public int setRoleByManager(String managerId, String[] roleIds) {
        return baseMapper.setRoleByManager(managerId,roleIds);
    }
}
