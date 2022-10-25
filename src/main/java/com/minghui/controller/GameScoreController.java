package com.minghui.controller;

import com.minghui.commons.dto.GameScoreDTO;
import com.minghui.commons.enums.ResponseEnum;
import com.minghui.commons.utils.Md5Util;
import com.minghui.commons.vo.ResponseResult;
import com.minghui.entity.UserGameScore;
import com.minghui.service.IGameScoreService;
import com.minghui.service.IServerConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author 明辉
 * @Date 2021-06-11
 */
@Slf4j
@RestController
@RequestMapping("/score")
public class GameScoreController {

    @Autowired
    private IGameScoreService gameScoreService;

    @Autowired
    IServerConfigService serverConfigService;

    /**
     * 保存用户游戏得分
     *
     * @param gameScoreDTO
     * @return ResponseResult
     */
    @PostMapping("/saveGameScore")
    public ResponseResult saveGameScore(@RequestBody GameScoreDTO gameScoreDTO) {

        String md5key = "chatId:" + gameScoreDTO.getChatId()
                + ",gameId:" + gameScoreDTO.getGameId()
                + ",userId:" + gameScoreDTO.getUserId()
                + ",score:" + gameScoreDTO.getScore()
                + ",serverId:" + gameScoreDTO.getServerId();
        // 前端请求参数校验
        if (!Md5Util.checkParameterMd5(md5key, gameScoreDTO.getMd5())) {
            return new ResponseResult(
                    ResponseEnum.SAVE_GAME_SCORE_FAIL.getCode(),
                    ResponseEnum.SAVE_GAME_SCORE_FAIL.getMsg(),
                    null);
        }

        Map<String, Object> map = gameScoreService.saveGameScore(gameScoreDTO);

        //执行添加积分
        serverConfigService.executeScore(gameScoreDTO);
        if (map != null) {
            return new ResponseResult(
                    ResponseEnum.SUCCESS.getCode(),
                    ResponseEnum.SUCCESS.getMsg(),
                    map);
        }
        return new ResponseResult(
                ResponseEnum.SAVE_GAME_SCORE_FAIL.getCode(),
                ResponseEnum.SAVE_GAME_SCORE_FAIL.getMsg(),
                null);

    }

    /**
     * 查询历史得分总榜
     *
     * @param gameScoreDTO
     * @return ResponseResult
     */
    @PostMapping("/queryTotalScoreRank")
    public ResponseResult queryTotalScoreRank(@RequestBody GameScoreDTO gameScoreDTO) {
        Map<String, Object> map = gameScoreService.queryTotalScoreRank(gameScoreDTO);
        if (map != null) {
            return new ResponseResult(
                    ResponseEnum.SUCCESS.getCode(),
                    ResponseEnum.SUCCESS.getMsg(),
                    map);
        }
        return new ResponseResult(
                ResponseEnum.QUERY_TOTAL_SCORE_RANK_FAIL.getCode(),
                ResponseEnum.QUERY_TOTAL_SCORE_RANK_FAIL.getMsg(),
                null);
    }

    /**
     * 查询今日得分总榜
     *
     * @param gameScoreDTO
     * @return ResponseResult
     */
    @PostMapping("/queryTodayScoreRank")
    public ResponseResult queryTodayScoreRank(@RequestBody GameScoreDTO gameScoreDTO) {
        Map<String, Object> map = gameScoreService.queryTodayScoreRank(gameScoreDTO);
        if (map != null) {
            return new ResponseResult(
                    ResponseEnum.SUCCESS.getCode(),
                    ResponseEnum.SUCCESS.getMsg(),
                    map);
        }
        return new ResponseResult(
                ResponseEnum.QUERY_TODAY_SCORE_RANK_FAIL.getCode(),
                ResponseEnum.QUERY_TODAY_SCORE_RANK_FAIL.getMsg(),
                null);
    }

    /**
     * 查询历史和今日得分第一名
     *
     * @param gameScoreDTO
     * @return ResponseResult
     */
    @PostMapping("/queryTotalAndTodayFirstScore")
    public ResponseResult queryTotalAndTodayFirstScore(@RequestBody GameScoreDTO gameScoreDTO) {
        Map<String, UserGameScore> map = gameScoreService.queryTotalAndTodayFirstScore(gameScoreDTO);
        if (map != null) {
            return new ResponseResult(
                    ResponseEnum.SUCCESS.getCode(),
                    ResponseEnum.SUCCESS.getMsg(),
                    map);
        }
        return new ResponseResult(
                ResponseEnum.QUERY_TOTAL_AND_TODAY_FIRST_SCORE_FAIL.getCode(),
                ResponseEnum.QUERY_TOTAL_AND_TODAY_FIRST_SCORE_FAIL.getMsg(),
                null);
    }
}
