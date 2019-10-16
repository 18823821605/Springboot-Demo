package com.springboot.dlc.common.utils;


import org.apache.commons.lang3.time.FastDateFormat;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.util.Random;
import java.util.UUID;

public class IdentityUtil {

    /**
     * UUID
     *
     * @return
     */
    public static String uuid() {
        String uuid = String.valueOf(UUID.randomUUID()).replace("-", "");
        uuid = uuid.substring(7);
        return uuid;
    }

    /**
     * 生成唯一编号
     *
     * @param title
     * @return
     */
    public static String identityId(String title) {
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        //有可能是负数
        if (hashCodeV < 0) {
            hashCodeV = -hashCodeV;
        }
        String identityId = title + String.format("%015d", hashCodeV).substring(6) + (System.nanoTime() + "").substring(4, 10);
        return identityId;
    }

    /**
     * 生成支付流水号
     *
     * @return
     */
    public static String outTradeNo(String serviceNo) {
        String currentTime = FastDateFormat.getInstance("yyyyMMddHHmmssSSS").format(System.currentTimeMillis());
        String prefix = currentTime.substring(0, 8);
        String afterFix = currentTime.substring(8, currentTime.length());
        String outTradeNo = serviceNo + prefix + getRandomNum(7) + afterFix;
        return outTradeNo;
    }

    /**
     * 生成手机验证码
     *
     * @param length
     * @return
     */
    public static String getRandomNum(int length) {
        if (length == 0) length = 6;
        String str = "0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; ++i) {
            int number = random.nextInt(10);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 生成验证码
     *
     * @return
     */
    public static String verificationCode(int length) {
        if (length == 0) length = 6;
        String randomcode = "";
        // 用字符数组的方式随机
        String model = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        char[] m = model.toCharArray();
        for (int j = 0; j < length; j++) {
            char c = m[(int) (Math.random() * 36)];
            // 保证六位随机数之间没有重复的
            if (randomcode.contains(String.valueOf(c))) {
                j--;
                continue;
            }
            randomcode = randomcode + c;
        }
        return randomcode;
    }

    public static String getNonceStr() {
        Random random = new Random();
        return MD5Util.MD5Encode(String.valueOf(random.nextInt(10000)), "UTF-8");
    }


    /**
     * 获取本机IP
     *
     * @return
     */
    public static String getLocalhostIp() {
        String ip = "";
        try {
            ip = InetAddress.getLocalHost().toString();
            ip = ip.substring(ip.indexOf("/") + 1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ip;
    }

    /**
     * 获取客户端IP
     *
     * @param request
     * @return
     */
    public static String getRemoteHost(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
    }
}
