package com.springboot.dlc.common.annotation.valid.impl;

import com.springboot.dlc.common.annotation.valid.URL;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;


/**
 * @auther: liujiebang
 * @Date: Create in 2018/11/26
 * @Description: 自定义URL校验实现
 **/
public class URLValidator implements ConstraintValidator<URL, String> {
    private Pattern pattern = Pattern.compile("http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?");

    @Override
    public void initialize(URL url) {

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return pattern.matcher(s).matches();
    }
}
