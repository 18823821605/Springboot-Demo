package com.springboot.dlc.common.exception;

import com.springboot.dlc.common.enums.ResultEnum;
import lombok.Getter;

/**
 * @Author: liujiebang
 * @Description: 授权异常
 * @Date: 2018/7/2 16:48
 **/
@Getter
public class AuthException extends Exception {

    private ResultEnum resultEnum;

    public AuthException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.resultEnum = resultEnum;
    }

    public AuthException(ResultEnum resultEnum, String message) {
        super(message);
        this.resultEnum = resultEnum;
    }
}
