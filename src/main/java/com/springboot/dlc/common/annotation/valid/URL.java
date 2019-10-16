package com.springboot.dlc.common.annotation.valid;

import com.springboot.dlc.common.annotation.valid.impl.URLValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @Author liujiebang
 * @Descrition: 自定义URL
 * @Date: Create in 2018/11/26
 **/
@Constraint(validatedBy = URLValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface URL {

    String message() default "URL不合法";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
