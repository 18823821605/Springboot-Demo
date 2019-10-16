package com.springboot.dlc.modules.service;

import com.springboot.dlc.modules.entity.SysManagerRole;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 管理员角色表 服务类
 * </p>
 *
 * @author liujiebang
 * @since 2018-10-02
 */
public interface ISysManagerRoleService extends IService<SysManagerRole> {

    /**
     * 设置管理员的权限
     *
     * @param managerId
     * @param roleIds
     * @return
     */
    int setRoleByManager(String managerId, String[] roleIds);
}
