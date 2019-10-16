package com.springboot.dlc.modules.controller.system;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.springboot.dlc.common.annotation.valid.Phone;
import com.springboot.dlc.common.annotation.valid.Status;
import com.springboot.dlc.common.sms.ZhuTongSMS;
import com.springboot.dlc.modules.controller.base.BaseController;
import com.springboot.dlc.common.enums.ResultEnum;
import com.springboot.dlc.common.utils.*;
import com.springboot.dlc.common.result.ResultView;
import com.springboot.dlc.modules.entity.SysManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


/**
 * @Author: liujiebang
 * @Description:
 * @Date: 2018/7/2 16:48
 **/
@Slf4j
@RestController
@RequestMapping("/api/sms")
@Validated
public class SendController extends BaseController {

    @Autowired
    private ZhuTongSMS zhuTongSMS;

    /**
     * 发送手机短信
     *
     * @param phone
     * @param type
     * @return
     */
    @GetMapping("/send")
    public ResultView getPhoneCode(@Phone String phone,
                                   @Status(value = "^[1-6]$", isNotBlank = false) String type) {
        String phoneCode = IdentityUtil.getRandomNum(6);
        String messageModel = zhuTongSMS.messageModel(phoneCode, Integer.valueOf(type));
        if (zhuTongSMS.sendSMS(messageModel, phone) == 1) {
            redisService.setAuthorizedSubject(phone, phoneCode, 180);
            log.info("给" + phone + "手机号码发送验证码----->" + phoneCode);
            return ResultView.ok();
        } else {
            return ResultView.error(ResultEnum.CODE_17);
        }
    }

}
