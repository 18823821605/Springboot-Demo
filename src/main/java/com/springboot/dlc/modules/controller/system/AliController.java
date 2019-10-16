package com.springboot.dlc.modules.controller.system;

import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.github.liujiebang.pay.ali.entity.AliRequest;
import com.github.liujiebang.pay.ali.service.AliAuthService;
import com.springboot.dlc.modules.controller.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @auther: liujiebang
 * @Date: Create in 2018/12/11
 * @Description:
 **/
@Controller
@RequestMapping("/api/ali")
public class AliController extends BaseController {

    @Autowired
    private AliAuthService aliAuthService;

    /**
     * 支付宝授权
     *
     * @param resultUrl
     */
    @GetMapping("/publicAppAuthorize")
    public String publicAppAuthorize(String resultUrl) {
        String redirectUrl = webResource.getProjectPath() + "api/ali/publicOauthToken";
        String url = aliAuthService.publicAppAuthorize(redirectUrl, AliRequest.ScopeType.AUTH_USER, resultUrl);
        return "redirect:" + url;
    }

    /**
     * 支付宝获取AccessToken
     *
     * @param appId      支付宝公众号会带入
     * @param scope      支付宝公众号会带入
     * @param authCode
     * @param errorScope 支付宝公众号会带入
     * @param state
     */
    @GetMapping("/publicOauthToken")
    public String publicAppAuthorize(@RequestParam(value = "app_id", required = false) String appId,
                                     @RequestParam(value = "scope", required = false) String scope,
                                     @RequestParam("auth_code") String authCode,
                                     @RequestParam(value = "error_scope", required = false) String errorScope,
                                     @RequestParam(value = "state", required = false) String state) {
        //获取AccessToken和支付宝唯一编号userId
        AlipaySystemOauthTokenResponse alipaySystemOauthTokenResponse = aliAuthService.systemOauthToken(authCode, null);
        //拉取支付宝用户信息
        AlipayUserInfoShareResponse userInfoShare = aliAuthService.getUserInfoShare(alipaySystemOauthTokenResponse.getAccessToken());
        //自定义业务逻辑
        return "redirect:" + state;
    }
}
