package com.springboot.dlc.modules.service;

import com.springboot.dlc.modules.entity.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springboot.dlc.common.result.ResultView;

import java.util.List;


/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author liujiebang
 * @since 2018-10-02
 */
public interface ISysMenuService extends IService<SysMenu> {

    /**
     * 设置角色的权限
     *
     * @param key
     * @param menus
     * @return
     */
    ResultView setAuthorityByRole(String key, String[] menus);

    /**
     * 获取权限菜单列表
     *
     * @param managerId
     * @return
     */
    ResultView getAuthorityMenuList(String managerId);

    /**
     * 删除权限菜单
     *
     * @param key
     * @return
     */
    ResultView delMenu(String key);

    /**
     * 根据父ID查询指定（菜单或按钮）列表
     *
     * @param key
     * @return
     */
    List<SysMenu> getMenuByFid(String key);

    /**
     * 修改权限信息
     *
     * @param menu
     * @return
     */
    ResultView putMenu(SysMenu menu);

    /**
     * 根据角色编号查询权限(模块)
     *
     * @param roleId
     * @return
     */
    List<SysMenu> findModelerByRoleId(String roleId);


    /**
     * 根据fid查询模块下的所有菜单和按钮
     *
     * @param fid
     * @param roleId
     * @param managerId
     * @return
     */
    List<SysMenu> findRecursionById(String fid, String roleId, String managerId);

    /**
     * 获取管理员的接口权限集合
     *
     * @param managerId
     * @return
     */
    List<String> getAuthoritysByManager(String managerId);
}
