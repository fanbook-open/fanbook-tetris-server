package com.minghui.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.minghui.entity.UserGameScore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GameScoreMapper extends BaseMapper<UserGameScore> {
    UserGameScore queryMaxScoreByUserId(@Param("userId") String userId, @Param("serverId") String serverId, @Param("gameId") String gameId);

    List<UserGameScore> queryTodayScoreRank(@Param("serverId") String serverId, @Param("gameId") String gameId);

    UserGameScore queryTotalFirstScore(@Param("serverId") String serverId, @Param("gameId") String gameId);

    UserGameScore queryTodayFirstScore(@Param("serverId") String serverId, @Param("gameId") String gameId);

    List<UserGameScore> queryTotalScoreRank(@Param("serverId") String serverId, @Param("gameId") String gameId);

    UserGameScore queryMyTotalRankScore(@Param("serverId") String serverId, @Param("gameId") String gameId, @Param("userId") String userId);

    UserGameScore queryMyTodayRankScore(@Param("serverId") String serverId, @Param("gameId") String gameId, @Param("userId") String userId);

    List<UserGameScore> queryTodayRankScore(@Param("serverId") String serverId, @Param("rank") Integer rank, @Param("time") String time);

    List<UserGameScore> queryTodayRankScoreToDay(@Param("serverId") String serverId, @Param("rank") Integer rank, @Param("day") Integer day);


    Long queryTodayMaxScore(@Param("serverId") String serverId, @Param("gameId") String gameId);
}
