package com.springboot.dlc.common.annotation.valid;

import com.springboot.dlc.common.annotation.valid.impl.IDCradValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @Author liujiebang
 * @Descrition: 自定义身份证
 * @Date: Create in 2018/11/26
 **/
@Constraint(validatedBy = IDCradValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IDCrad {

    String message() default "身份证不合法";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
