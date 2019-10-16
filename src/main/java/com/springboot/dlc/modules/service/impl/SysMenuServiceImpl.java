package com.springboot.dlc.modules.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.springboot.dlc.common.enums.RedisEnum;
import com.springboot.dlc.common.redis.RedisService;
import com.springboot.dlc.modules.entity.SysMenu;
import com.springboot.dlc.modules.entity.SysRoleMenu;
import com.springboot.dlc.common.enums.DictionaryEnum;
import com.springboot.dlc.common.exception.MyException;
import com.springboot.dlc.modules.mapper.SysMenuMapper;
import com.springboot.dlc.common.enums.ResultEnum;
import com.springboot.dlc.common.result.ResultView;
import com.springboot.dlc.modules.service.ISysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springboot.dlc.modules.service.ISysRoleMenuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author liujiebang
 * @since 2018-10-02
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    @Autowired
    private ISysRoleMenuService iSysRoleMenuService;

    @Autowired
    private RedisService redisService;

    @Transactional(rollbackFor = MyException.class)
    @Override
    public ResultView setAuthorityByRole(String roleId, String[] menus) {
        return (baseMapper.delAuthorityById(roleId, null) >= 0 && baseMapper.setAuthorityByRole(roleId, menus) >= 0)
                ? ResultView.ok() : ResultView.error(ResultEnum.CODE_2);
    }

    @Override
    public ResultView getAuthorityMenuList(String managerId) {
        //获取所有模块
        List<SysMenu> menuList = baseMapper.getMenuListByCondition(null, null, managerId);
        //拿到所有列表和按钮
        if (menuList != null && menuList.size() > 0) {
            menuList.forEach(value -> value.setSysMenuList(findRecursionById(value.getId(), null, managerId)));
        }
        return ResultView.ok(menuList);
    }


    @Transactional(rollbackFor = MyException.class)
    @Override
    public ResultView delMenu(String key) {
        //如果有角色绑定该权限-不允许删除
        List<SysRoleMenu> sysRoleMenus = iSysRoleMenuService.roleMenuListByRoleIdAndMenuId(null, key);
        if (sysRoleMenus != null && sysRoleMenus.size() > 0) {
            return ResultView.error(ResultEnum.CODE_23);
        }
        //如果当前权限下有子权限-不允许删除
        checkMenuLowerLevel(key);
        redisService.del(RedisEnum.MENU.getKey() + key);
        return removeById(key) ? ResultView.ok() : ResultView.error(ResultEnum.CODE_2);
    }

    @Override
    public List<SysMenu> getMenuByFid(String key) {
        QueryWrapper<SysMenu> qw = new QueryWrapper();
        if (StringUtils.isBlank(key)) {
            qw.lambda().eq(SysMenu::getInterfaceType, DictionaryEnum.JURISDICTION_MODULAR.getCode());
        } else {
            qw.lambda().eq(SysMenu::getFid, key);
        }
        qw.lambda().orderByAsc(SysMenu::getOrderBy);
        return list(qw);
    }

    @Transactional(rollbackFor = MyException.class)
    @Override
    public ResultView putMenu(SysMenu menu) {
        SysMenu sysMenu = getById(menu.getId());
        //需要判断当前权限是否在降低权限操作
        if (sysMenu.getInterfaceType() < menu.getInterfaceType()) {
            checkMenuLowerLevel(menu.getId());
        }
        return updateById(menu) ? ResultView.ok() : ResultView.error(ResultEnum.CODE_2);
    }

    @Override
    public List<SysMenu> findModelerByRoleId(String roleId) {
        return baseMapper.findModelerByRoleId(roleId);
    }

    @Override
    public List<SysMenu> findRecursionById(String fid, String roleId, String managerId) {
        List<SysMenu> sysMenuList = baseMapper.getMenuListByCondition(fid, roleId, managerId);
        if (fid != null && sysMenuList != null && sysMenuList.size() > 0) {
            sysMenuList.forEach(value -> value.setSysMenuList(findRecursionById(value.getId(), roleId, managerId)));
        }
        return sysMenuList;
    }


    @Override
    public List<String> getAuthoritysByManager(String managerId) {
        List<String> sysAuthorityByManager = baseMapper.getAuthoritysByManager(managerId);
        return sysAuthorityByManager;
    }

    /**
     * 检查下级
     *
     * @param key
     * @return
     */
    private void checkMenuLowerLevel(String key) {
        List<SysMenu> list = getMenuByFid(key);
        //当前权限有子权限，不允许修改
        if (list != null && list.size() > 0) {
            throw new MyException(ResultEnum.CODE_22);
        }
    }
}
