package com.minghui.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.minghui.commons.constant.FanbookPostConstant;
import com.minghui.commons.dto.GameScoreDTO;
import com.minghui.commons.utils.HttpClientUtil;
import com.minghui.commons.utils.SendMessageUtil;
import com.minghui.dao.GameScoreMapper;
import com.minghui.dao.UserOauthMapper;
import com.minghui.entity.UserGameScore;
import com.minghui.entity.UserOauth;
import com.minghui.service.IGameScoreService;
import com.minghui.service.IUserOauthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author 明辉
 * @Date 2021-06-11
 */
@Service
public class GameScoreServiceImpl implements IGameScoreService {

    @Autowired
    private GameScoreMapper gameScoreMapper;

    @Autowired
    private IUserOauthService userOauthService;

    @Autowired
    private UserOauthMapper userOauthMapper;

    /**
     * 保存用户游戏得分
     * @return int 存储结果
     * @param gameScoreDTO
     */
    @Override
    @Transactional
    public Map<String, Object> saveGameScore(GameScoreDTO gameScoreDTO) {
        //查询用户是否存在

        QueryWrapper<UserOauth> wrapper = new QueryWrapper<>();
        wrapper.eq("USER_ID",gameScoreDTO.getUserId())
                .eq("GAME_ID",gameScoreDTO.getGameId());
        UserOauth userOauth = userOauthMapper.selectOne(wrapper);
        if (userOauth != null) {
            //判断是否是历史最高分
            UserGameScore userGameScore2 = gameScoreMapper.queryTotalFirstScore(
                    gameScoreDTO.getServerId(),
                    gameScoreDTO.getGameId());
            if (userGameScore2 != null && userGameScore2.getScore() < gameScoreDTO.getScore()) {
                //查询到历史最高分记录，并比历史最高分还高
                String json = SendMessageUtil.sendMessage2(userOauth.getNickName(),userOauth.getAvatar(), gameScoreDTO);
                HttpClientUtil.sendMsgCardPost(FanbookPostConstant.SEND_MSG_CARD_URL, json);
            } else if (userGameScore2 == null) {
                //查询不到历史最高分记录，自己就是历史最高分记录
                String json = SendMessageUtil.sendMessage2(userOauth.getNickName(),userOauth.getAvatar(), gameScoreDTO);
                HttpClientUtil.sendMsgCardPost(FanbookPostConstant.SEND_MSG_CARD_URL, json);
            } else {
                //判断是否为今日最高分
                UserGameScore userGameScore3 = gameScoreMapper.queryTodayFirstScore(
                        gameScoreDTO.getServerId(),
                        gameScoreDTO.getGameId());
                if (userGameScore3 != null && userGameScore3.getScore() < gameScoreDTO.getScore()) {
                    //查询到今日最高分记录，并比今日最高分还高
                    String json = SendMessageUtil.sendMessage3(userOauth.getNickName(),userOauth.getAvatar(), gameScoreDTO);
                    HttpClientUtil.sendMsgCardPost(FanbookPostConstant.SEND_MSG_CARD_URL, json);
                } else if (userGameScore3 == null){
                    //查询不到今日最高分记录，自己就是今日最高分
                    String json = SendMessageUtil.sendMessage3(userOauth.getNickName(),userOauth.getAvatar(), gameScoreDTO);
                    HttpClientUtil.sendMsgCardPost(FanbookPostConstant.SEND_MSG_CARD_URL, json);
                }
            }

            UserGameScore userGameScore = new UserGameScore()
                    .setUserId(gameScoreDTO.getUserId())
                    .setGameId(gameScoreDTO.getGameId())
                    .setScore(gameScoreDTO.getScore())
                    .setServerId(gameScoreDTO.getServerId())
                    .setUpdateDatetime(new Date());
            int result = gameScoreMapper.insert(userGameScore);
            if (result > 0) {
                Map<String,Object> map = new HashMap<>();
                //查询今日最高得分
                Long maxScore = gameScoreMapper.queryTodayMaxScore(gameScoreDTO.getServerId(),gameScoreDTO.getGameId());
                //查询我的今日排名
                UserGameScore userGameScore1 = gameScoreMapper.queryMyTodayRankScore(
                        gameScoreDTO.getServerId(),gameScoreDTO.getGameId(),gameScoreDTO.getUserId());

                //查询我的历史得分数据及排名
                UserGameScore myMaxScore = gameScoreMapper.queryMyTotalRankScore(
                        gameScoreDTO.getServerId(),gameScoreDTO.getGameId(),gameScoreDTO.getUserId());

                map.put("currentScore",gameScoreDTO.getScore());
                map.put("todayMaxScore",maxScore);
                map.put("myMaxScore",myMaxScore.getScore());

                map.put("rank",userGameScore1.getRank());
                return map;
            }
        }
        return null;
    }

    /**
     * 查询历史得分总榜
     * @param gameScoreDTO
     * @return Map<String, Object>
     */
    @Override
    public Map<String, Object> queryTotalScoreRank(GameScoreDTO gameScoreDTO) {
        if (gameScoreDTO.getCurrentPage() == null) {
            gameScoreDTO.setCurrentPage(1);
        }
        if (gameScoreDTO.getPageSize() == null) {
            gameScoreDTO.setPageSize(20);
        }

        //开启分页
        PageHelper.startPage(gameScoreDTO.getCurrentPage(),gameScoreDTO.getPageSize());
        //查询需要分页的数据
        List<UserGameScore> list = gameScoreMapper.queryTotalScoreRank(gameScoreDTO.getServerId(),gameScoreDTO.getGameId());
        HashMap<String, Object> map = new HashMap<>();
        if (list != null) {
            PageInfo<UserGameScore> pageInfo = new PageInfo<>(list);
            //查询用户的nickname和avatar
            if (pageInfo.getList() != null) {
                for (UserGameScore userGameScore : pageInfo.getList()) {
                    UserOauth userOauth = userOauthService.queryUserInfo(userGameScore.getUserId());
                    if (userOauth != null) {
                        userGameScore.setNickName(userOauth.getNickName());
                        userGameScore.setAvatar(userOauth.getAvatar());
                        userGameScore.setGender(userOauth.getGender());
                    }
                }
            }
            //查询的历史得分数据及排名
            UserGameScore userGameScore = gameScoreMapper.queryMyTotalRankScore(
                    gameScoreDTO.getServerId(),gameScoreDTO.getGameId(),gameScoreDTO.getUserId());
            if (userGameScore != null) {
                UserOauth userOauth = userOauthService.queryUserInfo(gameScoreDTO.getUserId());
                if (userOauth != null) {
                    userGameScore.setNickName(userOauth.getNickName());
                    userGameScore.setAvatar(userOauth.getAvatar());
                    userGameScore.setGender(userOauth.getGender());
                }
            }
            //封装数据
            map.put("currentPage", pageInfo.getPageNum());
            map.put("pageSize", pageInfo.getPageSize());
            map.put("totalSize", pageInfo.getTotal());
            map.put("totalPages", pageInfo.getPages());
            map.put("list", pageInfo.getList());
            map.put("myScore",userGameScore);
        }
        return map;
    }

    /**
     * 查询今日得分总榜
     * @param gameScoreDTO
     * @return Map<String, Object>
     */
    @Override
    public Map<String, Object> queryTodayScoreRank(GameScoreDTO gameScoreDTO) {
        if (gameScoreDTO.getCurrentPage() == null) {
            gameScoreDTO.setCurrentPage(1);
        }
        if (gameScoreDTO.getPageSize() == null) {
            gameScoreDTO.setPageSize(20);
        }

        //开启分页
        PageHelper.startPage(gameScoreDTO.getCurrentPage(),gameScoreDTO.getPageSize());
        //查询需要分页的数据
        List<UserGameScore> list = gameScoreMapper.queryTodayScoreRank(gameScoreDTO.getServerId(),gameScoreDTO.getGameId());
        HashMap<String, Object> map = new HashMap<>();
        if (list != null) {
            PageInfo<UserGameScore> pageInfo = new PageInfo<>(list);
            //查询用户的nickname和avatar
            if (pageInfo.getList() != null) {
                for (UserGameScore userGameScore : pageInfo.getList()) {
                    UserOauth userOauth = userOauthService.queryUserInfo(userGameScore.getUserId());
                    if (userOauth != null) {
                        userGameScore.setNickName(userOauth.getNickName());
                        userGameScore.setAvatar(userOauth.getAvatar());
                        userGameScore.setGender(userOauth.getGender());
                    }
                }
            }
            //查询的历史得分数据及排名
            UserGameScore userGameScore = gameScoreMapper.queryMyTodayRankScore(
                    gameScoreDTO.getServerId(),gameScoreDTO.getGameId(),gameScoreDTO.getUserId());
            if (userGameScore != null) {
                UserOauth userOauth = userOauthService.queryUserInfo(gameScoreDTO.getUserId());
                if (userOauth != null) {
                    userGameScore.setNickName(userOauth.getNickName());
                    userGameScore.setAvatar(userOauth.getAvatar());
                    userGameScore.setGender(userOauth.getGender());
                }
            }

            //封装数据
            map.put("currentPage", pageInfo.getPageNum());
            map.put("pageSize", pageInfo.getPageSize());
            map.put("totalSize", pageInfo.getTotal());
            map.put("totalPages", pageInfo.getPages());
            map.put("list", pageInfo.getList());
            map.put("myScore",userGameScore);
        }
        return map;
    }

    /**
     * 查询历史和今日得分第一名
     * @param gameScoreDTO
     * @return Map<String, UserGameScore>
     */
    @Override
    public Map<String, UserGameScore> queryTotalAndTodayFirstScore(GameScoreDTO gameScoreDTO) {
        //查询历史得分第一名
        UserGameScore userGameScore1 = gameScoreMapper.queryTotalFirstScore(gameScoreDTO.getServerId(),gameScoreDTO.getGameId());
        if (userGameScore1 != null) {
            UserOauth userOauth = userOauthService.queryUserInfo(userGameScore1.getUserId());
            if (userOauth != null) {
                userGameScore1.setNickName(userOauth.getNickName());
                userGameScore1.setAvatar(userOauth.getAvatar());
            }
        }
        //查询今日得分第一名
        UserGameScore userGameScore2 = gameScoreMapper.queryTodayFirstScore(gameScoreDTO.getServerId(),gameScoreDTO.getGameId());
        if (userGameScore2 != null) {
            UserOauth userOauth = userOauthService.queryUserInfo(userGameScore2.getUserId());
            if (userOauth != null) {
                userGameScore2.setNickName(userOauth.getNickName());
                userGameScore2.setAvatar(userOauth.getAvatar());
            }
        }
        Map<String,UserGameScore> map = new HashMap<>();
        map.put("total",userGameScore1);
        map.put("today",userGameScore2);
        return map;
    }
}
