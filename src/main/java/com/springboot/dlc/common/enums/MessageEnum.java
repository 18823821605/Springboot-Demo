package com.springboot.dlc.common.enums;


import lombok.Getter;

/**
 * @Author liujiebang
 * @Descrition: 消息模板
 * @Date: Create in 2018/11/15
 **/
@Getter
public enum MessageEnum {

    /**
     * 退押金
     */
    RETURN_DEPOSIT_MESSAGE(1, "尊敬的用户,您的押金已退回", "尊敬的用户,您于%s发起押金退款已处理成功,可能会有3-5分钟的延迟,请注意查看微信退款通知!"),
    /**
     * 缴纳押金
     */
    PAY_DEPOSIT_MESSAGE(2, "尊敬的用户,您的押金已缴纳成功", "尊敬的用户,您于%s缴纳押金成功,您可以使用陪护床，让您的生活更便捷!"),
    /**
     * 待支付订单
     */
    NOTPAY_ORDER_MESSAGE(3, "尊敬的用户,您有一笔订单待支付", "尊敬的用户,您于%s结束陪护床使用,请及时支付!"),
    /**
     * 订单超时提示
     */
    TIMEOUT_ORDER_MESSAGE(4, "尊敬的用户,您有一笔订单快到期", "尊敬的用户,您有一笔订单还有%s分钟到期,请及时关锁或续费,订单超时将会以每小时%s元产生超时费用!");

    private int code;

    private String title;

    private String context;

    MessageEnum(int code, String title, String context) {
        this.code = code;
        this.title = title;
        this.context = context;
    }

}
