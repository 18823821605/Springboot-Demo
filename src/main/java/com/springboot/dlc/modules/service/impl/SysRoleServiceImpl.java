package com.springboot.dlc.modules.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.springboot.dlc.common.enums.RedisEnum;
import com.springboot.dlc.common.redis.RedisService;
import com.springboot.dlc.modules.entity.SysMenu;
import com.springboot.dlc.modules.entity.SysRole;
import com.springboot.dlc.common.exception.MyException;
import com.springboot.dlc.modules.mapper.SysRoleMapper;
import com.springboot.dlc.common.enums.ResultEnum;
import com.springboot.dlc.common.result.ResultView;
import com.springboot.dlc.modules.service.ISysManagerRoleService;
import com.springboot.dlc.modules.service.ISysMenuService;
import com.springboot.dlc.modules.service.ISysRoleMenuService;
import com.springboot.dlc.modules.service.ISysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springboot.dlc.common.utils.IdentityUtil;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author liujiebang
 * @since 2018-10-02
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Autowired
    private ISysRoleMenuService iSysRoleMenuService;

    @Autowired
    private ISysManagerRoleService iSysManagerRoleService;

    @Autowired
    private ISysMenuService iSysMenuService;

    @Autowired
    private RedisService redisService;

    @Transactional(rollbackFor = MyException.class)
    @Override
    public ResultView addRole(String roleName, String roleNote) {
        SysRole sysRole = new SysRole().setId(IdentityUtil.identityId("ROL")).setRoleName(roleName).setRoleNote(roleNote).setCtime(new Date());
        return save(sysRole) ? ResultView.ok() : ResultView.error(ResultEnum.CODE_2);
    }

    @Transactional(rollbackFor = MyException.class)
    @Override
    public ResultView delRole(String key) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("roleId", key);
        if (iSysManagerRoleService.count(queryWrapper) > 0) {
            return ResultView.error("还有管理员绑定该角色,请先解绑再删除该角色");
        }
        redisService.del(RedisEnum.MENU.getKey() + key);
        return removeById(key) && iSysRoleMenuService.remove(queryWrapper) ? ResultView.ok() : ResultView.error(ResultEnum.CODE_2);
    }

    @Override
    public ResultView getAuthorityByRole(String key) {
        List<SysMenu> sysMenuList = (List<SysMenu>) redisService.get(RedisEnum.MENU.getKey() + key);
        if (sysMenuList != null && sysMenuList.size() > 0) {
            return ResultView.ok(sysMenuList);
        }
        sysMenuList = iSysMenuService.findModelerByRoleId(key);
        if (sysMenuList != null && sysMenuList.size() > 0) {
            sysMenuList.forEach(value -> value.setSysMenuList(iSysMenuService.findRecursionById(value.getId(), key, null)));
        }
        redisService.set(RedisEnum.MENU.getKey() + key, sysMenuList);
        return ResultView.ok(sysMenuList);
    }

    @Override
    public List<SysRole> getRoleListByManager(String managerId) {
        return baseMapper.getRoleListByManager(managerId);
    }
}
