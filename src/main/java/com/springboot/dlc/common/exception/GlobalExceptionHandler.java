package com.springboot.dlc.common.exception;


import com.alibaba.fastjson.JSONObject;
import com.springboot.dlc.common.enums.ResultEnum;
import com.springboot.dlc.common.result.ResultView;
import com.springboot.dlc.modules.service.ISysLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Iterator;
import java.util.Set;


/**
 * @Author: liujiebang
 * @Description: Controller异常捕获类
 * @Date: 2018/7/2 16:51
 **/
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @Autowired
    private ISysLogService iSysLogService;

    /**
     * 自定义全局异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = MyException.class)
    @ResponseBody
    public ResultView myExceptionHandler(MyException e) {
        ResultView resultView = ResultView.error(e.getResultEnum(), e.getMessage());
        return errorResponse(resultView);
    }

    /**
     * 授权异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = AuthException.class)
    @ResponseBody
    public ResultView authExceptionHandler(AuthException e) {
        RecordExceptionLog(e);
        ResultView resultView = ResultView.error(e.getResultEnum(), e.getMessage());
        return resultView;
    }

    /**
     * 支付异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = PayException.class)
    @ResponseBody
    public ResultView payExceptionHandler(PayException e) {
        RecordExceptionLog(e);
        ResultView resultView = ResultView.error(e.getResultEnum().getCode(), e.getMessage());
        return resultView;
    }

    /**
     * 获取bindingResult中的错误信息
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public ResultView bindExceptionHandler(BindException e) {
        RecordExceptionLog(e);
        FieldError fieldError = e.getFieldError();
        StringBuilder sb = new StringBuilder();
        String errorMsg = fieldError.getDefaultMessage();
        sb.append(ResultEnum.CODE_2.getMsg()).append(errorMsg);
        ResultView resultView = ResultView.error(ResultEnum.CODE_2.getCode(), sb.toString());
        return resultView;
    }


    /**
     * 请求参数类型不匹配
     *
     * @param e
     * @return
     */
    @ExceptionHandler({TypeMismatchException.class})
    @ResponseBody
    public ResultView typeMismatchExceptionHandler(TypeMismatchException e) {
        RecordExceptionLog(e);
        ResultView resultView = ResultView.error(ResultEnum.CODE_400.getCode(), "参数类型不匹配,参数" + e.getPropertyName() + "类型应该为" + e.getRequiredType());
        return resultView;
    }

    /**
     * 请求参数个数不匹配
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    @ResponseBody
    public ResultView missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException e) {
        RecordExceptionLog(e);
        ResultView resultView = ResultView.error(ResultEnum.CODE_400.getCode(), "缺少必要参数,参数名称为" + e.getParameterName());
        return resultView;
    }

    /**
     * 请求方式错误
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public ResultView httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException e) {
        RecordExceptionLog(e);
        ResultView resultView = ResultView.error(ResultEnum.CODE_405);
        return resultView;
    }

    /**
     * 返回解析错误
     *
     * @param e
     * @return
     */
    @ExceptionHandler({HttpMediaTypeNotAcceptableException.class})
    @ResponseBody
    public ResultView httpMediaTypeNotAcceptabledExceptionHandler(HttpMediaTypeNotAcceptableException e) {
        RecordExceptionLog(e);
        ResultView resultView = ResultView.error(ResultEnum.CODE_406);
        return resultView;
    }

    /**
     * Violation 参数校验异常
     *
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResultView constraintViolationExceptionHandler(ConstraintViolationException e) {
        RecordExceptionLog(e);
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        Iterator<ConstraintViolation<?>> iterator = constraintViolations.iterator();
        StringBuffer sb = new StringBuffer(ResultEnum.CODE_2.getMsg());
        while (iterator.hasNext()) {
            ConstraintViolation<?> cvl = iterator.next();
            sb.append(",");
            sb.append(cvl.getMessageTemplate());
            break;
        }
        ResultView resultView = ResultView.error(ResultEnum.CODE_2.getCode(), sb.toString());
        return resultView;
    }


    /**
     * 默认异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultView defaultErrorHandler(Exception e) {
        RecordExceptionLog(e);
        ResultView resultView = ResultView.error(ResultEnum.CODE_500);
        return resultView;
    }

    /**
     * 记录异常日志
     *
     * @param e
     */
    private void RecordExceptionLog(Exception e) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        iSysLogService.saveByError(request, e);
    }


    /**
     * 集成结果打印
     *
     * @param resultView
     * @return
     */
    private ResultView errorResponse(ResultView resultView) {
        String json = JSONObject.toJSONString(resultView);
        log.error("返回结果 = {}", json);
        return resultView;
    }
}
