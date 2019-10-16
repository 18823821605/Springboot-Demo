package com.springboot.dlc.modules.controller.admin;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springboot.dlc.modules.entity.SysRole;
import com.springboot.dlc.modules.model.QSKey;
import com.springboot.dlc.modules.model.QPage;
import com.springboot.dlc.common.enums.ResultEnum;
import com.springboot.dlc.common.result.ResultView;
import com.springboot.dlc.modules.service.ISysMenuService;
import com.springboot.dlc.modules.service.ISysRoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author liujiebang
 * @since 2018-10-02
 */
@RestController
@RequestMapping("/api/sys-role/admin")
@Validated
public class ARoleController {

    @Autowired
    private ISysRoleService iSysRoleService;

    @Autowired
    private ISysMenuService iSysMenuService;

    /**
     * 获取角色信息
     *
     * @param qKey 主键
     * @return
     */
    @GetMapping("/get")
    public ResultView get(@Valid QSKey qKey) {
        return ResultView.ok(iSysRoleService.getById(qKey.getKey()));
    }


    /**
     * 获取角色列表
     *
     * @param qPage 分页
     * @return
     */
    @GetMapping("/list")
    public ResultView list(@Valid QPage qPage) {
        QueryWrapper<SysRole> qw = new QueryWrapper<>();
        qw.lambda().orderByDesc(SysRole::getCtime);
        IPage<SysRole> iPage = iSysRoleService.page(new Page(qPage.getOffset(), qPage.getLimit()), qw);
        return ResultView.ok(iPage);
    }


    /**
     * 设置角色- 新增 or 修改角色
     *
     * @param roleName 角色名称
     * @param roleNote 备注
     */
    @PostMapping("/set")
    public ResultView set(String id,
                          @NotBlank(message = "角色名称不能为空") String roleName,
                          @NotBlank(message = "角色备注信息不能为空") String roleNote) {
        if (StringUtils.isNotBlank(id)) {
            SysRole role = new SysRole().setId(id).setRoleName(roleName).setRoleNote(roleNote);
            return iSysRoleService.updateById(role) ? ResultView.ok() : ResultView.error(ResultEnum.CODE_2);
        }
        return iSysRoleService.addRole(roleName, roleNote);
    }


    /**
     * 删除角色
     *
     * @param qKey 主键
     */
    @DeleteMapping("/del")
    public ResultView del(@Valid QSKey qKey) {
        return iSysRoleService.delRole(qKey.getKey());
    }


    /**
     * 获取角色的权限菜单列表
     *
     * @param qKey 主键
     */
    @GetMapping("/getAuthorityByRole")
    public ResultView getAuthorityByRole(@Valid QSKey qKey) {
        return iSysRoleService.getAuthorityByRole(qKey.getKey());
    }


    /**
     * 设置角色权限
     *
     * @param qKey  角色id
     * @param menus 菜单id
     * @Description 设置角色权限
     */
    @PutMapping("/setAuthorityByRole")
    public ResultView setAuthorityByRole(@Valid QSKey qKey, @NotEmpty(message = "菜单编号不能为空") String... menus) {
        return iSysMenuService.setAuthorityByRole(qKey.getKey(), menus);
    }
}
