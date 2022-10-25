package com.minghui.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.minghui.commons.dto.GameScoreDTO;
import com.minghui.entity.TServerConfig;
import com.minghui.entity.UserGameScore;

public interface IServerConfigService extends IService<TServerConfig> {
    void executeRank(TServerConfig tServerConfig, String time);

    void executeRankToDay(Integer day);

    void executeScore(GameScoreDTO gameScoreDTO);
}
