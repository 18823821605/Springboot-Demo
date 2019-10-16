package com.springboot.dlc.modules.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.springboot.dlc.modules.entity.SysManager;
import com.springboot.dlc.modules.entity.SysManagerRole;
import com.springboot.dlc.modules.entity.SysRole;
import com.springboot.dlc.common.exception.MyException;
import com.springboot.dlc.modules.mapper.SysManagerMapper;
import com.springboot.dlc.common.redis.RedisService;
import com.springboot.dlc.common.enums.ResultEnum;
import com.springboot.dlc.common.result.ResultView;
import com.springboot.dlc.common.result.SysConstant;
import com.springboot.dlc.modules.service.ISysManagerRoleService;
import com.springboot.dlc.modules.service.ISysManagerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springboot.dlc.modules.service.ISysMenuService;
import com.springboot.dlc.modules.service.ISysRoleService;
import com.springboot.dlc.common.utils.DESCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author liujiebang
 * @since 2018-10-02
 */
@Service
public class SysManagerServiceImpl extends ServiceImpl<SysManagerMapper, SysManager> implements ISysManagerService {


    @Autowired
    private RedisService redisService;
    @Autowired
    private ISysRoleService iSysRoleService;
    @Autowired
    private ISysManagerRoleService iSysManagerRoleService;
    @Autowired
    private ISysMenuService iSysMenuService;

    @Override
    public SysManager login(String userAccount, String userPassword) {
        QueryWrapper<SysManager> qw = new QueryWrapper<>();
        qw.lambda().eq(SysManager::getUserAcount, userAccount.trim());
        SysManager manager = getOne(qw);
        if (manager == null) {
            throw new MyException(ResultEnum.CODE_14);
        }
        if (!DESCode.encode(userPassword.trim()).equals(manager.getPassWord())) {
            throw new MyException(ResultEnum.CODE_15);
        }
        //该管理员的权限集合
        List<String> authorityList = iSysMenuService.getAuthoritysByManager(manager.getManagerId());
        if (authorityList != null && authorityList.size() > 0) {
            redisService.set(SysConstant.AUTHORITY + manager.getManagerId(), authorityList);
        }
        return manager;
    }

    @Transactional(rollbackFor = MyException.class)
    @Override
    public ResultView updatePwd(String managerId, String oldPassWord, String passWord) {
        SysManager manager = getById(managerId);
        if (!DESCode.encode(oldPassWord).equals(manager.getPassWord())) {
            return ResultView.error(ResultEnum.CODE_15);
        }
        SysManager updateManager = new SysManager()
                .setManagerId(managerId)
                .setPassWord(DESCode.encode(passWord));
        if (updateById(updateManager)) {
            redisService.delAuthorizedSubject(managerId);
            return ResultView.ok();
        }
        return ResultView.error(ResultEnum.CODE_2);
    }

    @Override
    public List<SysRole> getSysRoleListByManager(String key) {
        return iSysRoleService.getRoleListByManager(key);
    }

    @Transactional(rollbackFor = MyException.class)
    @Override
    public ResultView setRoleByManager(String managerId, String[] roleIds) {
        Wrapper<SysManagerRole> qw = new QueryWrapper<SysManagerRole>().lambda().eq(SysManagerRole::getManagerId, managerId);
        if (iSysManagerRoleService.remove(qw) && iSysManagerRoleService.setRoleByManager(managerId, roleIds) > 0) {
            return ResultView.ok();
        }
        return ResultView.error(ResultEnum.CODE_2);
    }

    @Transactional(rollbackFor = MyException.class)
    @Override
    public ResultView delManager(String key) {
        Wrapper<SysManagerRole> qw = new QueryWrapper<SysManagerRole>().lambda().eq(SysManagerRole::getManagerId, key);
        return removeById(key) && iSysManagerRoleService.remove(qw) ? ResultView.ok() : ResultView.error(ResultEnum.CODE_2);
    }
}
