package com.springboot.dlc.modules.controller.admin;


import com.springboot.dlc.common.enums.RedisEnum;
import com.springboot.dlc.common.redis.RedisService;
import com.springboot.dlc.modules.entity.SysMenu;
import com.springboot.dlc.common.enums.DictionaryEnum;
import com.springboot.dlc.modules.model.QSKey;
import com.springboot.dlc.common.enums.ResultEnum;
import com.springboot.dlc.common.result.ResultView;
import com.springboot.dlc.common.result.SysConstant;
import com.springboot.dlc.modules.service.ISysMenuService;

import java.util.List;

import com.springboot.dlc.common.utils.IdentityUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author liujiebang
 * @since 2018-10-02
 */
@RestController
@RequestMapping("/api/sys-menu/admin")
@Validated
public class AMenuController {

    @Autowired
    private ISysMenuService iSysMenuService;

    @Autowired
    private RedisService redisService;

    /**
     * 获取权限菜单列表
     *
     * @param managerId 管理员id
     */
    @GetMapping("/list")
    public ResultView getAuthorityMenuList(String managerId, HttpServletRequest request) {
        if (StringUtils.isBlank(managerId)) {
            managerId = (String) request.getAttribute(SysConstant.MANAGER_ID);
        }
        return iSysMenuService.getAuthorityMenuList(managerId);
    }


    /**
     * 设置权限 - 新增 or 修改
     *
     * @param menu 菜单对象
     */
    @PostMapping("/set")
    public ResultView set(@Valid SysMenu menu) {
        if (menu.getInterfaceType() != DictionaryEnum.JURISDICTION_MODULAR.getCode()) {
            if (StringUtils.isEmpty(menu.getFid())) {
                return ResultView.error("父id不能为空");
            }
            if (StringUtils.isEmpty(menu.getInterfaceUrl())) {
                return ResultView.error("接口地址不能为空");
            }
        }
        if (StringUtils.isNotBlank(menu.getId())) {
            if (menu.getId().equals(menu.getFid())) {
                return ResultView.error("无法将修改的权限重新绑定给自己");
            }
            return iSysMenuService.putMenu(menu);
        }
        menu.setId(IdentityUtil.identityId("MEN"));
        redisService.del(RedisEnum.MENU.getKey() + "id");
        return iSysMenuService.save(menu) ? ResultView.ok() : ResultView.error(ResultEnum.CODE_2);
    }


    /**
     * 获取菜单详情
     *
     * @param qKey 主键
     */
    @GetMapping("/getById")
    public ResultView getById(@Valid QSKey qKey) {
        return ResultView.ok(iSysMenuService.getById(qKey.getKey()));
    }


    /**
     * 获取模块或者菜单
     *
     * @param fid 父id
     */
    @GetMapping("/getByFid")
    public ResultView getByFid(String fid) {
        List<SysMenu> sysMenuList = iSysMenuService.getMenuByFid(fid);
        return ResultView.ok(sysMenuList);
    }


    /**
     * 删除权限菜单
     *
     * @param qKey 主键
     * @return
     */
    @DeleteMapping("/del")
    public ResultView del(@Valid QSKey qKey) {
        return iSysMenuService.delMenu(qKey.getKey());
    }
}
