package com.springboot.dlc.common.annotation.valid;

import com.springboot.dlc.common.annotation.valid.impl.StatusValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @Author liujiebang
 * @Descrition: 自定义状态值
 * @Date: Create in 2018/11/26
 **/
@Constraint(validatedBy = StatusValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Status {

    String message() default "参数类型不合法";

    String value() default "^[1-2]$";

    boolean isNotBlank() default true;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
