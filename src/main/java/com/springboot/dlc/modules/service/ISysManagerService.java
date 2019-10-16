package com.springboot.dlc.modules.service;

import com.springboot.dlc.modules.entity.SysManager;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springboot.dlc.modules.entity.SysRole;
import com.springboot.dlc.common.result.ResultView;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author liujiebang
 * @since 2018-10-02
 */
public interface ISysManagerService extends IService<SysManager> {

    /**
     * 管理员登录
     *
     * @param userAccount  账号
     * @param userPassword 密码
     * @return
     */
    SysManager login(String userAccount, String userPassword);

    /**
     * 修改密码
     *
     * @param managerId
     * @param oldPassWord 原密码
     * @param passWord    新密码
     * @return
     */
    ResultView updatePwd(String managerId, String oldPassWord, String passWord);

    /**
     * 获取管理员的角色列表
     * @param key
     * @return
     */
    List<SysRole> getSysRoleListByManager(String key);

    /**
     * 修改管理员的权限角色
     * @param key
     * @param roleIds
     * @return
     */
    ResultView setRoleByManager(String key, String[] roleIds);

    /**
     *  删除管理员
     * @param key
     * @return
     */
    ResultView delManager(String key);
}
