package com.minghui.task;

import com.minghui.service.IMessageCardService;
import com.minghui.service.IServerConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时器
 *
 * @Author 明辉
 * @Date 2021-07-01
 */
@Component
@EnableScheduling
public class BootMsgTask {
    @Autowired
    private IMessageCardService messageCardService;


    //每隔5秒执行一次
    @Async("executor1")
    @Scheduled(fixedRate = 4000)
    public void testTasks() {
        messageCardService.getBootMsgCard();
    }



}
