package com.minghui.entity;

import lombok.Data;

@Data
public class ServerConfig {
    Integer id;
    Long guildId;
    //类型 1 挑战分数 2每日排名
    //目标分数
    Integer target;
    //开始排名
    Integer begin;
    //结束排名
    Integer end;
    //奖励积分
    Integer rewards;
}
