package com.springboot.dlc.modules.service;

import com.springboot.dlc.modules.entity.SysRoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色菜单表 服务类
 * </p>
 *
 * @author liujiebang
 * @since 2018-10-02
 */
public interface ISysRoleMenuService extends IService<SysRoleMenu> {

    /**
     * 根据角色或者菜单查询角色绑定该权限
     *      （用于判断该菜单是否可以删除）
     * @param roleId
     * @param menuId
     * @return
     */
    List<SysRoleMenu> roleMenuListByRoleIdAndMenuId(String roleId, String menuId);


}
