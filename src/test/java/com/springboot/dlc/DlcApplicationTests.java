package com.springboot.dlc;

import com.springboot.dlc.common.redis.RedisService;
import com.springboot.dlc.config.RedisConfig;
import com.springboot.dlc.modules.entity.SysLog;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Iterator;
import java.util.Set;

import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DlcApplicationTests {

    @Autowired
    private RedisService redisService;

    @Test
    public void contextLoads() {

        SysLog sysLog = new SysLog();
        sysLog.setController("liujiebang");
        while (true) {
            redisService.sendMessage(RedisConfig.channle, sysLog);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}
