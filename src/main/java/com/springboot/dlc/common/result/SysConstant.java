package com.springboot.dlc.common.result;

/**
 * @Author: liujiebang
 * @Description: 系统公共常量
 * @Date: 2018/7/2 16:54
 **/
public class SysConstant {

    public static final String PROJECT_NAME = "springboot_";

    /**
     * 权限前缀
     */
    public static final String AUTHORITY = PROJECT_NAME + "authority_";

    public static final String TOKEN = "Authorization";

    public static final String USER = "user";

    public static final String USER_ID = "userId";

    public static final String MANAGER = "manager";

    public static final String MANAGER_ID = "managerId";

    /**
     * 用户登录过期时间
     */
    public static final long USER_AUTH_TIMEOUT = 604800000;

    /**
     * 管理员授权过期时间
     */
    public static final long ADMIN_AUTH_TIMEOUT = 259200000;
}
