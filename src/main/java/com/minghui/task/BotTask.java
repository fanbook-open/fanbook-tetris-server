package com.minghui.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.minghui.entity.TServerConfig;
import com.minghui.service.IServerConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
@EnableScheduling
@Slf4j
@Lazy(value = false)
public class BotTask {
    @Autowired
    IServerConfigService serverConfigService;

    @Async("executor2")
    @Scheduled(cron = "0 59 23 * * ?")
    public void tasks() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        String time = sdf1.format(new Date());
        log.debug("触发排行任务:" + sdf.format(new Date()));
        log.debug("执行{}号排行", time);
        System.out.println("触发排行任务:" + sdf.format(new Date()));
        QueryWrapper<TServerConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        queryWrapper.eq("type", 2);
        //获取启用规则的服务器列表
        List<TServerConfig> configList = serverConfigService.list(queryWrapper);
        for (TServerConfig tServerConfig : configList) {
            serverConfigService.executeRank(tServerConfig, time);
        }
    }
}
