package com.springboot.dlc.common.exception;


import com.springboot.dlc.common.enums.ResultEnum;
import lombok.Getter;

/**
 * @Author: liujiebang
 * @Description: 支付异常
 * @Date: 2018/7/2 16:48
 **/
@Getter
public class PayException extends RuntimeException {

    private ResultEnum resultEnum;

    public PayException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.resultEnum = resultEnum;
    }

    public PayException(ResultEnum resultEnum, String message) {
        super(message);
        this.resultEnum = resultEnum;
    }
}
