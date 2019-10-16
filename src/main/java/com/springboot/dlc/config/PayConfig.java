package com.springboot.dlc.config;


import com.github.liujiebang.pay.ali.config.AliPayConfig;
import com.github.liujiebang.pay.ali.service.AliAuthService;
import com.github.liujiebang.pay.ali.service.AliPayService;
import com.github.liujiebang.pay.ali.service.impl.AliAuthServiceImpl;
import com.github.liujiebang.pay.ali.service.impl.AliPayServiceImpl;
import com.github.liujiebang.pay.wx.config.WxConfig;
import com.github.liujiebang.pay.wx.service.WxAuthService;
import com.github.liujiebang.pay.wx.service.WxPayService;
import com.github.liujiebang.pay.wx.service.impl.WxAuthServiceImpl;
import com.github.liujiebang.pay.wx.service.impl.WxPayServiceImpl;
import com.springboot.dlc.common.sms.AliSMS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: liujiebang
 * @Description: 微信支付授权, 支付宝支付配置类
 * @Date: 2018/6/27 15:05
 **/
@Configuration
@Slf4j
public class PayConfig {

    @Bean
    public AliPayService aliPayService(AliPayConfig aliPayConfig) {
        AliPayService aliPayService = new AliPayServiceImpl();
        aliPayService.setAliPayConfigStorage(aliPayConfig);
        return aliPayService;
    }

    @Bean
    public AliAuthService aliAuthService(AliPayConfig aliPayConfig) {
        AliAuthService aliAuthService = new AliAuthServiceImpl();
        aliAuthService.setAliPayConfigStorage(aliPayConfig);
        return aliAuthService;
    }

    @Bean
    public WxPayService wxPayService(WxConfig wxConfig) {
        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setWxConfigStorage(wxConfig);
        return wxPayService;
    }

    @Bean
    public WxAuthService wxAuthService(WxConfig wxConfig) {
        WxAuthService wxAuthService = new WxAuthServiceImpl();
        wxAuthService.setWxConfigStorage(wxConfig);
        return wxAuthService;
    }

    @ConfigurationProperties(prefix = "ali-pay")
    @Bean
    public AliPayConfig aliPayConfig() {
        return new AliPayConfig();
    }

    @ConfigurationProperties(prefix = "we-chat")
    @Bean
    public WxConfig wxConfig() {
        return new WxConfig();
    }

}
