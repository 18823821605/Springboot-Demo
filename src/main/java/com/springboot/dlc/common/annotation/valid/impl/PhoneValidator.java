package com.springboot.dlc.common.annotation.valid.impl;

import com.springboot.dlc.common.annotation.valid.Phone;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * @auther: liujiebang
 * @Date: Create in 2018/11/24
 * @Description: 手机号码校验实现
 **/
public class PhoneValidator implements ConstraintValidator<Phone, String> {

    private Pattern pattern = Pattern.compile("((^(13|14|15|17|18|19)[0-9]{9}$)|(^0[1,2]{1}\\d{1}-?\\d{8}$)|(^0[3-9] {1}\\d{2}-?\\d{7,8}$)|(^0[1,2]{1}\\d{1}-?\\d{8}-(\\d{1,4})$)|(^0[3-9]{1}\\d{2}-? \\d{7,8}-(\\d{1,4})$))");

    @Override
    public void initialize(Phone phone) {

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return pattern.matcher(s).matches();
    }
}
