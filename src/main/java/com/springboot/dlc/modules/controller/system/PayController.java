package com.springboot.dlc.modules.controller.system;


import com.alipay.api.internal.util.AlipaySignature;
import com.github.liujiebang.pay.ali.config.AliConfig;
import com.github.liujiebang.pay.ali.config.AliPayConfig;
import com.github.liujiebang.pay.utils.IdentityUtil;
import com.github.liujiebang.pay.utils.XMLUtil;
import com.github.liujiebang.pay.wx.config.WxConfig;
import com.github.liujiebang.pay.wx.entity.WxRequest;
import com.github.liujiebang.pay.wx.service.WxAuthService;
import com.github.liujiebang.pay.wx.service.WxPayService;
import com.github.liujiebang.pay.wx.service.impl.WxPayServiceImpl;
import com.springboot.dlc.common.result.ResultView;
import com.springboot.dlc.common.result.SysConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @Author: liujiebang
 * @Description: 支付
 * @Date: 2018/7/2 16:30
 **/
@Slf4j
@RestController
@RequestMapping("/api")
public class PayController {

    @Autowired
    private WxConfig wxConfig;

    @Autowired
    private AliPayConfig aliPayConfig;



    /**
     * 支付
     *
     * @param outTradeNo 支付流水号
     * @return
     */
    @PostMapping(value = "/pay/front")
    public ResultView pay(HttpServletRequest request, String outTradeNo, String payType) {
        String userId = (String) request.getAttribute(SysConstant.USER_ID);
        WxPayService wxPayService = new WxPayServiceImpl();
        Map map = null;
        return ResultView.ok(map);
    }


    /**
     * 支付宝异步回调方法
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/ali/notify")
    public String zfbNotify(HttpServletRequest request) {
        try {
            Map<String, String> map = XMLUtil.aliPayNotify(request);
            String status = "trade_status";
            if (AliConfig.PayStatus.TRADE_SUCCESS.equals(map.get(status))) {
                //验签
                boolean flag = AlipaySignature.rsaCheckV1(map, aliPayConfig.getPublicKey(), aliPayConfig.getCharset(), aliPayConfig.getSignType());
                if (flag) {
                    String outTradeNo = map.get("out_trade_no");
                    System.out.println("支付宝回调订单号－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－" + outTradeNo);
                    return "success";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("---------------------------回调通知异常！！！-------------------------------");
            return "fail";
        }
        return "fail";
    }

    /**
     * 微信支付异步回调方法
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/wcPay/notify")
    public String wcPayNotify(HttpServletRequest request) {
        try {
            Map<String, String> map = XMLUtil.wxPayNotify(request);
            if (WxRequest.Status.SUCCESS.equals(map.get(WxRequest.evokeTransfers.RETURN_CODE))
                    && WxRequest.Status.SUCCESS.equals(map.get(WxRequest.evokeTransfers.RESULT_CODE))) {
                //验签
                if (IdentityUtil.inspectionSign(map, wxConfig.getSpMchKey())) {
                    String outTradeNo = map.get("out_trade_no");
                    System.out.println("微信回调订单号－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－" + outTradeNo);
                    return XMLUtil.setWechatXml(WxRequest.Status.SUCCESS, "OK");
                } else {
                    return XMLUtil.setWechatXml(WxRequest.Status.FAIL, "验签失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("---------------------------回调通知异常！！！-------------------------------");
            return XMLUtil.setWechatXml(WxRequest.Status.FAIL, "回调通知异常");
        }
        return XMLUtil.setWechatXml(WxRequest.Status.FAIL, "订单支付失败");
    }


    /**
     * 支付回调通用业务逻辑
     *
     * @param outTradeNo 支付流水号
     * @param payType    支付类型
     */
    private void pay(String outTradeNo, int payType) {
        String sub = outTradeNo.substring(0, 3);
    }

}
