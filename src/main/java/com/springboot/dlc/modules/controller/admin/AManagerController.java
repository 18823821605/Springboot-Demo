package com.springboot.dlc.modules.controller.admin;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springboot.dlc.common.annotation.log.SysLog;
import com.springboot.dlc.modules.controller.base.BaseController;
import com.springboot.dlc.modules.entity.SysManager;
import com.springboot.dlc.common.enums.DictionaryEnum;
import com.springboot.dlc.modules.model.QSKey;
import com.springboot.dlc.modules.model.QPage;
import com.springboot.dlc.common.enums.ResultEnum;
import com.springboot.dlc.common.result.ResultView;
import com.springboot.dlc.common.result.SysConstant;
import com.springboot.dlc.modules.service.ISysManagerService;
import com.springboot.dlc.common.utils.IdentityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

/**
 * <p>
 * 管理员表 前端控制器
 * </p>
 *
 * @author liujiebang
 * @since 2018-10-02
 */
@RestController
@RequestMapping("/api/sys-manager/admin")
@Validated
@Slf4j
public class AManagerController extends BaseController {

    private final static Logger monitorLogger = LoggerFactory.getLogger("monitor");

    @Autowired
    private ISysManagerService iSysManagerService;


    /**
     * 当前管理员信息
     */
    @GetMapping("/get")
    public ResultView get(HttpServletRequest request) {
        String managerId = (String) request.getAttribute(SysConstant.MANAGER_ID);
        SysManager sysManager = (SysManager) redisService.getAuthorizedSubject(managerId);
        return ResultView.ok(sysManager);
    }


    /**
     * 后台登录
     *
     * @param userAccount
     * @param userPassword
     * @return
     */
    @SysLog(value = "登录")
    @PostMapping(value = "/login")
    public ResultView login(@NotBlank(message = "用户名不能为空") String userAccount,
                            @NotBlank(message = "密码不能为空")
                            @Length(min = 6, max = 20, message = "密码须在长度6-20位之间") String userPassword) {
        monitorLogger.info("自定义日志!");
        SysManager manager = iSysManagerService.login(userAccount, userPassword);
        String token = jwtToken(SysConstant.MANAGER_ID, manager.getManagerId(), manager, SysConstant.ADMIN_AUTH_TIMEOUT);
        return ResultView.ok(token);
    }


    /**
     * 修改密码
     *
     * @param passWord
     * @param request
     * @return
     */
    @SysLog(value = "修改密码")
    @PutMapping(value = "/updatePwd")
    public ResultView updatePwd(HttpServletRequest request, @NotBlank(message = "原密码不能为空") String oldPassWord,
                                @NotBlank(message = "新密码不能为空") @Length(min = 6, max = 20, message = "密码须在长度6-20位之间") String passWord) {
        String managerId = (String) request.getAttribute(SysConstant.MANAGER_ID);
        return iSysManagerService.updatePwd(managerId, oldPassWord, passWord);
    }


    /**
     * 退出登录
     *
     * @param request
     * @return
     */
    @SysLog(value = "退出登录")
    @GetMapping(value = "/logout")
    public ResultView logout(HttpServletRequest request) {
        String managerId = (String) request.getAttribute(SysConstant.MANAGER_ID);
        redisService.delAuthorizedSubject(managerId);
        return ResultView.ok();
    }


    /**
     * 设置管理员- 新增 or 修改
     *
     * @param sysManager 管理员对象
     * @return
     */
    @PostMapping("/set")
    public ResultView set(@Valid SysManager sysManager) {
        if (StringUtils.isBlank(sysManager.getManagerId())) {
            QueryWrapper<SysManager> qw = new QueryWrapper();
            qw.lambda().eq(SysManager::getUserAcount, sysManager.getUserAcount());
            if (iSysManagerService.count(qw) > 0) {
                return ResultView.error("账号已存在");
            }
            sysManager.setManagerId(IdentityUtil.identityId("MAN"))
                    .setCtime(new Date())
                    .setManagerType(DictionaryEnum.MANAGER_TYPE_GENERAL.getCode())
                    .setIsFlag(DictionaryEnum.IS_DEL_Y.getCode());
            return iSysManagerService.save(sysManager) ? ResultView.ok() : ResultView.error(ResultEnum.CODE_2);
        }
        return iSysManagerService.updateById(sysManager) ? ResultView.ok() : ResultView.error(ResultEnum.CODE_2);
    }


    /**
     * 获取管理员列表
     *
     * @param qPage       分页
     * @param userName    用户名称
     * @param managerType 管理员类型
     * @param startTime   开始时间
     * @param endTime     结束时间
     * @return
     */
    @GetMapping("/list")
    public ResultView list(QPage qPage,
                           @RequestParam(value = "userName", required = false) String userName,
                           @RequestParam(value = "managerType", required = false) Integer managerType,
                           @RequestParam(value = "startTime", required = false) Date startTime,
                           @RequestParam(value = "endTime", required = false) Date endTime) {
        Wrapper<SysManager> qw = new QueryWrapper<SysManager>().lambda()
                .like(SysManager::getUserName, userName)
                .eq(managerType != null, SysManager::getManagerType, managerType)
                .gt(startTime != null, SysManager::getCtime, startTime)
                .lt(endTime != null, SysManager::getCtime, endTime)
                .orderByAsc(SysManager::getManagerType).orderByDesc(SysManager::getCtime);
        IPage<SysManager> iPage = iSysManagerService.page(new Page(qPage.getOffset(), qPage.getLimit()), qw);

        return ResultView.ok(iPage);
    }


    /**
     * 启用禁用
     *
     * @param qKey 主键
     * @return
     */
    @PutMapping("/enable")
    public ResultView enable(@Valid QSKey qKey) {
        SysManager sysManager = iSysManagerService.getById(qKey.getKey());
        if (sysManager.getIsFlag() == DictionaryEnum.ISFLAG_Y.getCode()) {
            sysManager.setIsFlag(DictionaryEnum.ISFLAG_N.getCode());
        } else {
            sysManager.setIsFlag(DictionaryEnum.ISFLAG_Y.getCode());
        }
        return iSysManagerService.updateById(sysManager) ? ResultView.ok() : ResultView.error(ResultEnum.CODE_2);
    }


    /**
     * 删除管理员
     *
     * @param qKey 主键
     * @return
     */
    @DeleteMapping("/del")
    public ResultView del(@Valid QSKey qKey) {
        return iSysManagerService.delManager(qKey.getKey());
    }


    /**
     * 获取管理员的角色列表
     *
     * @param qKey 主键
     * @return
     */
    @GetMapping("/getRoleListByManager")
    public ResultView getRoleListByManager(@Valid QSKey qKey) {
        return ResultView.ok(iSysManagerService.getSysRoleListByManager(qKey.getKey()));
    }


    /**
     * 设置管理员的权限角色
     *
     * @param qKey    主键
     * @param roleIds
     * @return
     */
    @PutMapping("/setRoleByManager")
    public ResultView setRoleByManager(@Valid QSKey qKey, @NotEmpty(message = "角色集合不能为空") String... roleIds) {
        return iSysManagerService.setRoleByManager(qKey.getKey(), roleIds);
    }

}
