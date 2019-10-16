package com.springboot.dlc.modules.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.springboot.dlc.modules.entity.SysRoleMenu;
import com.springboot.dlc.modules.mapper.SysRoleMenuMapper;
import com.springboot.dlc.modules.service.ISysRoleMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 角色菜单表 服务实现类
 * </p>
 *
 * @author liujiebang
 * @since 2018-10-02
 */
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements ISysRoleMenuService {


    @Override
    public List<SysRoleMenu> roleMenuListByRoleIdAndMenuId(String roleId, String menuId) {
        Wrapper<SysRoleMenu> qw = new QueryWrapper<SysRoleMenu>().lambda()
                .eq(StringUtils.isNotBlank(roleId), SysRoleMenu::getRoleId, roleId)
                .eq(StringUtils.isNotBlank(menuId), SysRoleMenu::getMenuId, menuId);
        return list(qw);
    }
}
