package com.springboot.dlc.common.exception;

import com.springboot.dlc.common.enums.ResultEnum;
import com.springboot.dlc.common.result.ResultView;
import com.springboot.dlc.common.utils.IdentityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @auther: liujiebang
 * @Date: Create in 2019/1/22
 * @Description:
 **/
@Slf4j
@RestController
public class ErController implements ErrorController{

    private static final String ERROR_PATH = "/error";

    @RequestMapping(value = ERROR_PATH)
    public ResultView handleError(HttpServletResponse response, HttpServletRequest request) {
        String ip = IdentityUtil.getRemoteHost(request);
        log.info("请求错误:{}", response.getStatus());
        log.info("请求IP是:{}", ip);
        if (response.getStatus() == ResultEnum.CODE_401.getCode()) {
            return ResultView.error(ResultEnum.CODE_401);
        } else if (response.getStatus() == ResultEnum.CODE_403.getCode()) {
            return ResultView.error(ResultEnum.CODE_403);
        } else if (response.getStatus() == ResultEnum.CODE_404.getCode()) {
            return ResultView.error(ResultEnum.CODE_404);
        } else if (response.getStatus() == ResultEnum.CODE_405.getCode()) {
            return ResultView.error(ResultEnum.CODE_405);
        } else if (response.getStatus() == ResultEnum.CODE_406.getCode()) {
            return ResultView.error(ResultEnum.CODE_406);
        }else if (response.getStatus() == ResultEnum.CODE_415.getCode()) {
            return ResultView.error(ResultEnum.CODE_415);
        }
        return ResultView.error(ResultEnum.CODE_9999);
    }


    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}
