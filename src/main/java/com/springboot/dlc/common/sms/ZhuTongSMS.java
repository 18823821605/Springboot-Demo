package com.springboot.dlc.common.sms;

import com.springboot.dlc.common.enums.ResultEnum;
import com.springboot.dlc.common.exception.MyException;
import com.springboot.dlc.common.utils.DateUtils;
import com.springboot.dlc.common.utils.MD5Util;
import com.springboot.dlc.common.utils.RestTemplateUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * @auther: liujiebang
 * @Date: Create in 2019/2/16
 * @Description:
 **/
@Slf4j
@Component
@Data
@ConfigurationProperties(prefix = "zhu-tong-sms")
public class ZhuTongSMS {

    private static final String REQUEST_URL = "http://www.api.zthysms.com/sendSms.do";

    private String phoneBody;

    private int timeout;

    private String username;

    private String password;

    /**
     * 助通短信
     *
     * @param messageContent
     * @param phone
     * @return
     */
    public int sendSMS(String messageContent, String phone) {
        String dateTime = DateUtils.getCurrentDateTime(DateUtils.DATE_TIME_FORMAT_UNSIGNED);
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        params.add("password", MD5Util.MD5Encode(MD5Util.MD5Encode(password, "utf-8") + dateTime, "utf-8"));
        params.add("tkey", dateTime);
        params.add("mobile", phone);
        params.add("content", messageContent);
        try {
            String account = RestTemplateUtil.post(REQUEST_URL, params);
            log.info("获取短信短信平台返回信息:{}", account);
            if (account != null) {
                String[] strs = account.split(",");
                return Integer.valueOf(strs[0]);
            }
        } catch (Exception e) {
            throw new MyException(ResultEnum.CODE_1001);
        }
        return -1;
    }


    public String messageModel(String mobile_code, int modelType) {
        StringBuffer sbf = new StringBuffer();
        switch (modelType) {
            case 1:
                sbf.append(phoneBody + mobile_code + "(当前操作为注册,有效期为" + timeout + "分钟!)");
                break;
            case 2:
                sbf.append(phoneBody + mobile_code + "(当前操作为登陆,有效期为" + timeout + "分钟!)");
                break;
            case 3:
                sbf.append(phoneBody + mobile_code + "(当前操作为绑定手机,有效期为" + timeout + "分钟!)");
                break;
            case 4:
                sbf.append(phoneBody + mobile_code + "(当前操作为提现申请,有效期为" + timeout + "分钟!)");
                break;
            case 5:
                sbf.append(phoneBody + mobile_code + "(当前操作为修改手机号,有效期为" + timeout + "分钟!)");
                break;
            case 6:
                sbf.append(phoneBody + mobile_code + "(当前操为密码修改,有效期为" + timeout + "分钟!)");
                break;
            default:
                throw new MyException(ResultEnum.CODE_16);
        }
        return sbf.toString();
    }
}
