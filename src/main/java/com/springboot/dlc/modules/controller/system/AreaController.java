package com.springboot.dlc.modules.controller.system;


import com.alibaba.fastjson.JSONObject;
import com.springboot.dlc.common.result.ResultView;
import com.springboot.dlc.modules.entity.SysArea;
import com.springboot.dlc.modules.service.ISysAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 地区 前端控制器
 * </p>
 *
 * @author liujiebang
 * @since 2019-03-28
 */
@RestController
@RequestMapping("/sys-area")
public class AreaController {

    @Autowired
    private ISysAreaService iSysAreaService;

    @GetMapping("/recursion")
    public Object list() {
        List<SysArea> list = iSysAreaService.recursion(0L);
        return list;
    }
}
