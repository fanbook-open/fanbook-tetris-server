package com.minghui.service;

import com.minghui.commons.dto.GameScoreDTO;
import com.minghui.entity.UserGameScore;

import java.util.List;
import java.util.Map;

public interface IGameScoreService {
    Map<String, Object> saveGameScore(GameScoreDTO gameScoreDTO);

    Map<String, Object> queryTotalScoreRank(GameScoreDTO gameScoreDTO);

    Map<String, Object> queryTodayScoreRank(GameScoreDTO gameScoreDTO);

    Map<String, UserGameScore> queryTotalAndTodayFirstScore(GameScoreDTO gameScoreDTO);
}
