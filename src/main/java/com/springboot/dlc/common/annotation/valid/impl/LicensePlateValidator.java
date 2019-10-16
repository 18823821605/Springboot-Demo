package com.springboot.dlc.common.annotation.valid.impl;

import com.springboot.dlc.common.annotation.valid.LicensePlate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * @auther: liujiebang
 * @Date: Create in 2018/11/26
 * @Description: 自定义车牌号码校验实现
 **/
public class LicensePlateValidator implements ConstraintValidator<LicensePlate, String> {

    private Pattern pattern = Pattern.compile("^([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[a-zA-Z](([DF]((?![IO])[a-zA-Z0-9](?![IO]))[0-9]{4})|([0-9]{5}[DF]))|[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1})$");

    @Override
    public void initialize(LicensePlate licensePlate) {

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return pattern.matcher(s).matches();
    }
}
